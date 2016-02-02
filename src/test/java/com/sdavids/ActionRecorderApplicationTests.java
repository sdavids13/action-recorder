package com.sdavids;

import com.sdavids.domain.Action;
import com.sdavids.domain.ObjectType;
import com.sdavids.domain.Verb;
import com.sdavids.service.ActionsRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.transaction.Transactional;
import java.util.Arrays;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WithMockUser("tester")
@Transactional
@ActiveProfiles("unit-test")
@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ActionRecorderApplication.class)
public class ActionRecorderApplicationTests {

	@Autowired
	private ActionsRepository actionsRepository;

	@Autowired
	private WebApplicationContext context;

	private MockMvc mvc;

	@Before
	public void setUp() {
		mvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();
	}

	@Test
	public void createAction() throws Exception {
		String uri = "http://google.com";
		long oldActionCount = actionsRepository.count();

		mvc.perform(post("/").param("verb", Verb.PLAY.name())
				.param("objectType", ObjectType.BOOKMARK.name()).param("objectUri", uri))
			.andDo(print())
			.andExpect(status().isCreated())
			.andExpect(jsonPath("$.user").value("tester"))
			.andExpect(jsonPath("$.verb").value("PLAY"))
			.andExpect(jsonPath("$.objectType").value("BOOKMARK"))
			.andExpect(jsonPath("$.objectUri").value(uri))
			.andExpect(jsonPath("$.id").isNumber());

		assertThat("Action was not created", actionsRepository.count(), is(oldActionCount + 1));
	}

	@Test
	public void getUserActionsForObjectUri() throws Exception {
		String uri = "https://foo.com";
		actionsRepository.save(Arrays.asList(
				new Action(Verb.SAVE, ObjectType.BOOKMARK, uri), new Action(Verb.PLAY, ObjectType.BOOKMARK, uri),
				new Action(Verb.SAVE, ObjectType.BOOKMARK, "https://address.com")));

		mvc.perform(get("/").param("objectUri", uri))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$").value(hasSize(2)))
			.andExpect(jsonPath("$[0].user").value("tester"))
			.andExpect(jsonPath("$[0].verb").value("SAVE"))
			.andExpect(jsonPath("$[0].objectType").value("BOOKMARK"))
			.andExpect(jsonPath("$[0].objectUri").value(uri))
			.andExpect(jsonPath("$[1].user").value("tester"))
			.andExpect(jsonPath("$[1].verb").value("PLAY"))
			.andExpect(jsonPath("$[1].objectType").value("BOOKMARK"))
			.andExpect(jsonPath("$[1].objectUri").value(uri));
	}

	@Test
	public void getPageableUserAction() throws Exception {
		actionsRepository.save(Arrays.asList(
				new Action(Verb.SAVE, ObjectType.BOOKMARK, "https://foo.com"),
				new Action(Verb.PLAY, ObjectType.BOOKMARK, "https://foo2.com"),
				new Action(Verb.SAVE, ObjectType.BOOKMARK, "https://foo3.com")));

		mvc.perform(get("/").param("page", "0").param("size", "2").param("sort", "verb,objectUri"))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$._embedded.actionList").value(hasSize(2)))
			.andExpect(jsonPath("$._embedded.actionList.[0].verb").value("PLAY"))
			.andExpect(jsonPath("$._embedded.actionList.[0].objectUri").value("https://foo2.com"))
			.andExpect(jsonPath("$._embedded.actionList.[1].verb").value("SAVE"))
			.andExpect(jsonPath("$._embedded.actionList.[1].objectUri").value("https://foo.com"))
			.andExpect(jsonPath("$._links.first.href").value("http://localhost?page=0&size=2&sort=verb,objectUri,asc"))
			.andExpect(jsonPath("$._links.next.href").value("http://localhost?page=1&size=2&sort=verb,objectUri,asc"))
			.andExpect(jsonPath("$._links.prev.href").doesNotExist());

		mvc.perform(get("/").param("page", "1").param("size", "2").param("sort", "verb,objectUri"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$._embedded.actionList").value(hasSize(1)))
			.andExpect(jsonPath("$._embedded.actionList.[0].verb").value("SAVE"))
			.andExpect(jsonPath("$._embedded.actionList.[0].objectUri").value("https://foo3.com"))
			.andExpect(jsonPath("$._links.first.href").value("http://localhost?page=0&size=2&sort=verb,objectUri,asc"))
			.andExpect(jsonPath("$._links.prev.href").value("http://localhost?page=0&size=2&sort=verb,objectUri,asc"))
			.andExpect(jsonPath("$._links.next.href").doesNotExist());
	}

	@Test
	public void deleteAction() throws Exception {
		Action action = actionsRepository.saveAndFlush(new Action(Verb.SAVE, ObjectType.BOOKMARK, "https://foo.com"));
		long oldActionCount = actionsRepository.count();

		mvc.perform(delete("/{id}", action.getId())).andExpect(status().isOk());

		assertThat("Action was not deleted", actionsRepository.count(), is(oldActionCount - 1));
	}

	@Test
	public void doNotDeleteOtherUserAction() throws Exception {
		Action action = actionsRepository.saveAndFlush(new Action(Verb.SAVE, ObjectType.BOOKMARK, "https://foo.com"));
		long oldActionCount = actionsRepository.count();

		mvc.perform(delete("/{id}", action.getId()).with(user("user"))).andExpect(status().isOk());

		assertThat("Action should not have been deleted by a user who didn't create the action.",
				actionsRepository.count(), is(oldActionCount));
	}

	@Test
	public void badCreateParams() throws Exception {
		mvc.perform(post("/")).andExpect(status().isBadRequest());
	}

}

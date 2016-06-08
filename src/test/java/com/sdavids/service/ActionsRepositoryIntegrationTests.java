package com.sdavids.service;

import com.sdavids.ActionRecorderApplication;
import com.sdavids.domain.Action;
import com.sdavids.domain.ObjectType;
import com.sdavids.domain.Verb;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.transaction.Transactional;

import java.math.BigDecimal;
import java.util.Date;

import static org.hamcrest.Matchers.closeTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * Integration tests for {@link ActionsRepository}.
 */
@Transactional
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(ActionRecorderApplication.class)
public class ActionsRepositoryIntegrationTests {

    @Autowired
    ActionsRepository repository;

    @Test
    public void findsFirstPageOfActions() {
        Page<Action> actions = repository.findAll(new PageRequest(0, 10));
        assertThat(actions.getTotalElements(), is(6L));
    }

    @Test
    public void findByUserAndObjectUri() {
        Page<Action> actions = repository.findByUserAndObjectUri("user", "http://google.com", new PageRequest(0, 5));
        assertThat(actions.getTotalElements(), is(2L));
    }

    @Test
    @WithMockUser("tester")
    public void insert() {
        long count = repository.count();
        Action result = repository.saveAndFlush(new Action(Verb.SAVE, ObjectType.BOOKMARK, "http://foo.bar"));
        assertThat(repository.count(), is(count + 1));

        assertThat(result.getUser(), is("tester"));
        assertThat(BigDecimal.valueOf(result.getCreateDate().getTime()), closeTo(BigDecimal.valueOf(new Date().getTime()), BigDecimal.valueOf(100)));
    }

}

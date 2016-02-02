package com.sdavids.web;

import com.sdavids.domain.Action;
import com.sdavids.domain.ObjectType;
import com.sdavids.domain.Verb;
import com.sdavids.service.ActionsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedResources;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ActionsController {

	@Autowired
	private ActionsRepository actionsRepository;

	@ResponseStatus(value = HttpStatus.CREATED)
	@RequestMapping(path = "/", method = RequestMethod.POST)
	public Action newAction(@RequestParam("verb") Verb verb, @RequestParam("objectType") ObjectType objectType, @RequestParam("objectUri") String objectUri) {
		return actionsRepository.saveAndFlush(new Action(verb, objectType, objectUri));
	}

	@RequestMapping(path = "/{id}", method = RequestMethod.DELETE)
	public void deleteAction(@PathVariable("id") Long id, Authentication authentication) {
		actionsRepository.delete(id, authentication.getName());
	}

	@RequestMapping(path = "/", method = RequestMethod.GET)
	public PagedResources<Action> getActions(Authentication authentication, Pageable pageable, PagedResourcesAssembler assembler) {
		return assembler.toResource(actionsRepository.findByUser(authentication.getName(), pageable));
	}

	@RequestMapping(path = "/", method = RequestMethod.GET, params = "objectUri")
	public List<Action> getActionsByObjectUri(@RequestParam("objectUri") String objectUri, Authentication authentication) {
		return actionsRepository.findByUserAndObjectUri(authentication.getName(), objectUri);
	}

}

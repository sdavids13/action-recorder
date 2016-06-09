package com.sdavids.web;

import com.sdavids.domain.Action;
import com.sdavids.domain.ObjectType;
import com.sdavids.domain.Verb;
import com.sdavids.service.ActionsRepository;
import com.sdavids.web.hateos.ActionResourceAssembler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.ExposesResourceFor;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@ExposesResourceFor(Action.class)
@RestController
public class ActionsController {

    @Autowired
    private ActionsRepository actionsRepository;

    @Autowired
    private ActionResourceAssembler resourceAssembler;

    @ResponseStatus(value = HttpStatus.CREATED)
    @RequestMapping(path = "/", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Resource<Action> newAction(@RequestParam("verb") Verb verb, @RequestParam("objectType") ObjectType objectType, @RequestParam("objectUri") String objectUri) {
        return resourceAssembler.toResource(actionsRepository.saveAndFlush(new Action(verb, objectType, objectUri)));
    }

    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    @RequestMapping(path = "/{id}", method = RequestMethod.DELETE)
    public void deleteAction(@PathVariable("id") Long id, Authentication authentication) {
        actionsRepository.delete(id, authentication.getName());
    }

    @RequestMapping(path = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Resource<Action> getAction(@PathVariable("id") Long id, Authentication authentication) {
        return resourceAssembler.toResource(actionsRepository.findOne(id, authentication.getName()));
    }

    @RequestMapping(path = "/", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public PagedResources<Action> getActions(Authentication authentication, Pageable pageable, PagedResourcesAssembler pageAssembler) {
        return pageAssembler.toResource(actionsRepository.findByUser(authentication.getName(), pageable), resourceAssembler);
    }

    @RequestMapping(path = "/", method = RequestMethod.GET, params = "objectUri", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public PagedResources<Action> getActionsByObjectUri(@RequestParam("objectUri") String objectUri, Authentication authentication, Pageable pageable, PagedResourcesAssembler pageAssembler) {
        return pageAssembler.toResource(actionsRepository.findByUserAndObjectUri(authentication.getName(), objectUri, pageable), resourceAssembler);
    }

}

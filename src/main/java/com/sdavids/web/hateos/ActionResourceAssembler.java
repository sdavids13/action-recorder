package com.sdavids.web.hateos;

import com.sdavids.domain.Action;
import com.sdavids.web.ActionsController;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceAssembler;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

public class ActionResourceAssembler implements ResourceAssembler<Action, Resource<Action>> {

    @Override
    public Resource<Action> toResource(Action action) {
        return new Resource<>(action, linkTo(ActionsController.class).slash(action).withSelfRel());
    }
}

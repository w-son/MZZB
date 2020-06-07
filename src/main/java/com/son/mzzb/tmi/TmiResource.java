package com.son.mzzb.tmi;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

public class TmiResource extends Resource<Tmi> {

    public TmiResource(Tmi tmi, Link... links) {
        super(tmi, links);
        add(linkTo(TmiController.class).slash(tmi.getId()).withRel("self"));
    }

}

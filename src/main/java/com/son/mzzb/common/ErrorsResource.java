package com.son.mzzb.common;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import org.springframework.validation.Errors;

public class ErrorsResource extends Resource<Errors> {

    public ErrorsResource(Errors errors, Link... links) {
        super(errors, links);
    }

}

package com.son.mzzb.matzip;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

public class MatzipResource extends Resource<Matzip> {

    /*
     ResponseEntity의 body에 Resource형 클래스를 사용하면
     - JsonUnwrapped를 사용하여 id값에 접근하지 못하는 경우를 방지
     - _links 들을 추가하고 관리하기에 용이
     */
    public MatzipResource(Matzip matzip, Link... links) {
        super(matzip, links);
        add(linkTo(MatzipController.class).slash(matzip.getId()).withSelfRel());
    }

}

package me.hol22mol22.demorestapi.common;

import me.hol22mol22.demorestapi.index.IndexController;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.validation.Errors;

import java.util.Arrays;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

// 에러 발생시 index 경로를 추가하는 에러 리소스 생성
public class ErrorsResource extends EntityModel<Errors> {
    public ErrorsResource(Errors content, Link... links){
        super(content, Arrays.asList(links));
        add(linkTo(methodOn(IndexController.class).index()).withRel("index"));
    }
}

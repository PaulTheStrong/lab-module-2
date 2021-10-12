package com.epam.esm.hateoas.processor;

import com.epam.esm.controller.UserController;
import com.epam.esm.hateoas.model.UserModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.RepresentationModelProcessor;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class UserModelProcessor implements RepresentationModelProcessor<UserModel> {
    @Override
    public UserModel process(UserModel model) {
        int userId = model.getId();
        Link ordersLink = linkTo(methodOn(UserController.class).getUserOrders(1, userId)).withRel("orders");
        model.add(ordersLink);
        return model;
    }
}

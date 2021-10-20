package com.epam.esm.hateoas.processor;

import com.epam.esm.controller.UserController;
import com.epam.esm.data.PageInfo;
import com.epam.esm.hateoas.model.UserModel;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.RepresentationModelProcessor;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class UserModelProcessor implements RepresentationModelProcessor<UserModel>, CollectionModelProcessor<UserModel> {
    @Override
    public UserModel process(UserModel model) {
        int userId = model.getId();
        Link ordersLink = linkTo(methodOn(UserController.class).getUserOrders(userId, 1, 10)).withRel("orders");
        model.add(ordersLink);
        return model;
    }

    @Override
    public CollectionModel<UserModel> process(CollectionModel<UserModel> collectionModel, PageInfo currentPage) {
        PageInfo first = currentPage.getFirst();
        PageInfo last = currentPage.getLast();
        PageInfo next = currentPage.getNextOrLast();
        PageInfo prev = currentPage.getPreviousOrFirst();
        int pageSize = currentPage.getPageSize();
        Link linkFirst = linkTo(getAllUsersMethod(first, pageSize)).withRel("first");
        Link linkLast = linkTo(getAllUsersMethod(last, pageSize)).withRel("last");
        Link linkPrev = linkTo(getAllUsersMethod(prev, pageSize)).withRel("prev");
        Link linkNext = linkTo(getAllUsersMethod(next, pageSize)).withRel("next");
        Link linkSelf = linkTo(getAllUsersMethod(currentPage, pageSize)).withSelfRel();
        return collectionModel.add(linkFirst, linkLast, linkSelf, linkPrev, linkNext);
    }

    private CollectionModel<UserModel> getAllUsersMethod(PageInfo first, int pageSize) {
        return methodOn(UserController.class).getAllUsers(first.getCurrentPage(), pageSize);
    }
}

package com.epam.esm.hateoas.processor;

import com.epam.esm.controller.GiftCertificateController;
import com.epam.esm.controller.UserController;
import com.epam.esm.data.PageInfo;
import com.epam.esm.hateoas.model.OrderModel;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.RepresentationModelProcessor;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class OrderModelProcessor implements RepresentationModelProcessor<OrderModel>, UserOrderCollectionModelProcessor {

    @Override
    public OrderModel process(OrderModel model) {
        int userId = model.getUserId();
        Link userLink = linkTo(methodOn(UserController.class).getUserById(userId)).withRel("user");
        model.add(userLink);

        int certificateId = model.getCertificateId();
        Link certificateLink = linkTo(methodOn(GiftCertificateController.class).getById(certificateId)).withRel("certificate");
        model.add(certificateLink);
        return model;
    }

    @Override
    public CollectionModel<OrderModel> process(CollectionModel<OrderModel> collectionModel, PageInfo currentPage, int userId) {
        PageInfo first = currentPage.getFirst();
        PageInfo last = currentPage.getLast();
        PageInfo next = currentPage.getNextOrLast();
        PageInfo prev = currentPage.getPreviousOrFirst();
        int pageSize = currentPage.getPageSize();
        Link linkFirst = linkTo(userOrdersMethod(userId, first, pageSize)).withRel("first");
        Link linkLast = linkTo(userOrdersMethod(userId, last, pageSize)).withRel("last");
        Link linkPrev = linkTo(userOrdersMethod(userId, prev, pageSize)).withRel("prev");
        Link linkNext = linkTo(userOrdersMethod(userId, next, pageSize)).withRel("next");
        Link linkSelf = linkTo(userOrdersMethod(userId, first, pageSize)).withSelfRel();
        return collectionModel.add(linkFirst, linkLast, linkSelf, linkPrev, linkNext);
    }

    private CollectionModel<OrderModel> userOrdersMethod(int userId, PageInfo first, int pageSize) {
        return methodOn(UserController.class).getUserOrders(userId, first.getCurrentPage(), pageSize);
    }
}

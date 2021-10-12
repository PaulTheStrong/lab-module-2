package com.epam.esm.hateoas.processor;

import com.epam.esm.controller.GiftCertificateController;
import com.epam.esm.controller.TagController;
import com.epam.esm.controller.UserController;
import com.epam.esm.hateoas.model.OrderModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.mediatype.Affordances;
import org.springframework.hateoas.server.RepresentationModelProcessor;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class OrderModelProcessor implements RepresentationModelProcessor<OrderModel> {

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

}

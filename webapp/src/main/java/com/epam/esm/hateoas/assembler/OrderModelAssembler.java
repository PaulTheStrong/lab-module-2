package com.epam.esm.hateoas.assembler;

import com.epam.esm.controller.UserController;
import com.epam.esm.data.OrderDto;
import com.epam.esm.hateoas.model.OrderModel;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class OrderModelAssembler extends RepresentationModelAssemblerSupport<OrderDto, OrderModel> {

    public OrderModelAssembler() {
        super(UserController.class, OrderModel.class);
    }

    @Override
    public OrderModel toModel(OrderDto entity) {
        OrderModel model = new OrderModel();

        int userId = entity.getUserId();
        Link selfLink = linkTo(methodOn(UserController.class).getUserOrder(userId, entity.getId())).withSelfRel();
        model.add(selfLink);

        model.setId(entity.getId());
        model.setCertificateId(entity.getCertificateId());
        model.setPurchaseTimestamp(entity.getTimestamp());
        model.setUserId(userId);
        model.setTotalPrice(entity.getPrice());
        return model;
    }

    public CollectionModel<OrderModel> toCollectionModel(Iterable<? extends OrderDto> entities) {
        List<OrderModel> orderModels = new ArrayList<>();
        for (OrderDto order : entities) {
            OrderModel orderModel = toModel(order);
            orderModels.add(orderModel);
        }
        return CollectionModel.of(orderModels);
    }
}

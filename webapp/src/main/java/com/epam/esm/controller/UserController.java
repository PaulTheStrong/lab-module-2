package com.epam.esm.controller;

import com.epam.esm.data.OrderDto;
import com.epam.esm.entities.Order;
import com.epam.esm.entities.User;
import com.epam.esm.hateoas.assembler.OrderModelAssembler;
import com.epam.esm.hateoas.assembler.UserModelAssembler;
import com.epam.esm.hateoas.model.OrderModel;
import com.epam.esm.hateoas.model.UserModel;
import com.epam.esm.hateoas.processor.OrderModelProcessor;
import com.epam.esm.hateoas.processor.UserModelProcessor;
import com.epam.esm.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RequestMapping("/users")
@RestController
public class UserController {

    private static final int DEFAULT_PAGE_SIZE = 10;
    private final UserService userService;
    private final UserModelAssembler userModelAssembler;
    private final OrderModelAssembler orderModelAssembler;
    private final UserModelProcessor userModelProcessor;
    private final OrderModelProcessor orderModelProcessor;

    @Autowired
    public UserController(UserService userService, UserModelAssembler userModelAssembler, OrderModelAssembler orderModelAssembler, UserModelProcessor userModelProcessor, OrderModelProcessor orderModelProcessor) {
        this.userService = userService;
        this.userModelAssembler = userModelAssembler;
        this.orderModelAssembler = orderModelAssembler;
        this.userModelProcessor = userModelProcessor;
        this.orderModelProcessor = orderModelProcessor;
    }

    @GetMapping
    public CollectionModel<UserModel> getAllUsers(@RequestParam(defaultValue = "1") int page) {
        List<User> users = userService.getUsers(page, DEFAULT_PAGE_SIZE);
        return userModelAssembler.toCollectionModel(users);
    }

    @GetMapping("/{id}")
    public UserModel getUserById(@PathVariable int id) {
        User user = userService.getUserById(id);
        UserModel userModel = userModelAssembler.toModel(user);
        return userModelProcessor.process(userModel);
    }

    @GetMapping("/{id}/orders")
    public CollectionModel<OrderModel> getUserOrders(@RequestParam(defaultValue = "1") Integer page, @PathVariable int id) {
        List<OrderDto> userOrders = userService.getUserOrders(id, page, DEFAULT_PAGE_SIZE);
        return orderModelAssembler.toCollectionModel(userOrders);
    }

    @GetMapping("/{userId}/orders/{orderNumber}")
    public OrderModel getUserOrder(@PathVariable int userId, @PathVariable int orderNumber) {
        OrderDto userOrder = userService.getUserOrder(userId, orderNumber);
        OrderModel orderModel = orderModelAssembler.toModel(userOrder);
        return  orderModelProcessor.process(orderModel);
    }

    @PostMapping("/{userId}/purchase/{certificateId}")
    public OrderModel purchaseCertificate(@PathVariable int userId, @PathVariable int certificateId) {
        OrderDto order = userService.purchaseCertificate(userId, certificateId);
        OrderModel orderModel = orderModelAssembler.toModel(order);
        return orderModelProcessor.process(orderModel);
    }
}

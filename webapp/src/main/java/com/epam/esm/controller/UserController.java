package com.epam.esm.controller;

import com.epam.esm.entities.Order;
import com.epam.esm.entities.User;
import com.epam.esm.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
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

    public static final int DEFAULT_PAGE_SIZE = 10;
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<User> getAllUsers(@RequestParam Optional<Integer> page) {
        int pageNumber = page.orElse(1);
        return userService.getUsers(pageNumber, DEFAULT_PAGE_SIZE);
    }

    @GetMapping("/{id}/orders")
    public List<Order> getUserOrders(@RequestParam Optional<Integer> page, @PathVariable int id) {
        int pageNumber = page.orElse(1);
        return userService.getUserOrders(id, pageNumber, DEFAULT_PAGE_SIZE);
    }

    @GetMapping("/{userId}/orders/{orderNumber}")
    public Order getUserOrder(@PathVariable int userId, @PathVariable int orderNumber) {
        return userService.getUserOrder(userId, orderNumber);
    }

    @PostMapping("/{userId}/purchase/{certificateId}")
    public Order purchaseCertificate(@PathVariable int userId, @PathVariable int certificateId) {
        return userService.purchaseCertificate(userId, certificateId);
    }
}

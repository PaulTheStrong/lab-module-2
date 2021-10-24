package com.epam.esm.controller;

import com.epam.esm.data.OrderDto;
import com.epam.esm.data.PageInfo;
import com.epam.esm.entities.GiftCertificate;
import com.epam.esm.entities.Order;
import com.epam.esm.entities.User;
import com.epam.esm.hateoas.assembler.OrderModelAssembler;
import com.epam.esm.hateoas.assembler.UserModelAssembler;
import com.epam.esm.hateoas.model.OrderModel;
import com.epam.esm.hateoas.model.UserModel;
import com.epam.esm.hateoas.processor.OrderModelProcessor;
import com.epam.esm.hateoas.processor.UserModelProcessor;
import com.epam.esm.service.PurchaseService;
import com.epam.esm.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Min;
import java.util.List;

import static com.epam.esm.exception.ExceptionCodes.PAGE_MUST_BE_POSITIVE;
import static com.epam.esm.exception.ExceptionCodes.PAGE_SIZE_MUST_BE_POSITIVE;

@RequestMapping("/users")
@RestController
public class UserController {

    private static final String DEFAULT_PAGE_SIZE = "10";
    private static final String START_PAGE = "1";
    private final UserService userService;
    private final UserModelAssembler userModelAssembler;
    private final OrderModelAssembler orderModelAssembler;
    private final UserModelProcessor userModelProcessor;
    private final OrderModelProcessor orderModelProcessor;
    private final PurchaseService purchaseService;

    @Autowired
    public UserController(UserService userService, UserModelAssembler userModelAssembler, OrderModelAssembler orderModelAssembler, UserModelProcessor userModelProcessor, OrderModelProcessor orderModelProcessor, PurchaseService purchaseService) {
        this.userService = userService;
        this.userModelAssembler = userModelAssembler;
        this.orderModelAssembler = orderModelAssembler;
        this.userModelProcessor = userModelProcessor;
        this.orderModelProcessor = orderModelProcessor;
        this.purchaseService = purchaseService;
    }

    /**
     * Retrieves {@link User} entities from database in pageable format
     * and creates {@link CollectionModel} of this entities.
     * @param page the number of the page
     * @return {@link CollectionModel} of {@link UserModel} entities from database starting from
     * (pageNumber - 1) * pageSize
     */
    @GetMapping
    public CollectionModel<UserModel> getAllUsers(
            @RequestParam(defaultValue = START_PAGE) @Min(value = 1, message = PAGE_MUST_BE_POSITIVE) int page,
            @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) @Min(value = 1, message = PAGE_SIZE_MUST_BE_POSITIVE) int pageSize) {
        List<User> users = userService.getUsers(page, pageSize);
        PageInfo pageInfo = userService.userPageInfo(page, pageSize);
        CollectionModel<UserModel> collectionModel = userModelAssembler.toCollectionModel(users);
        return userModelProcessor.process(collectionModel, pageInfo);
    }

    /**
     * Get {@link User} from database and transfer
     * it to {@link UserModel}
     * @param id {@link User}'s id
     * @return {@link UserModel} with specified id
     */
    @GetMapping("/{id}")
    public UserModel getUserById(@PathVariable int id) {
        User user = userService.getUserById(id);
        UserModel userModel = userModelAssembler.toModel(user);
        return userModelProcessor.process(userModel);
    }
    /**
     * Retrieves {@link User}'s {@link OrderDto} entities from database in pageable format
     * and creates {@link OrderModel}s of it.
     * @param id {@link User}'s id.
     * @param page the number of the page
     * @return {@link CollectionModel} of pageSize
     * {@link OrderModel} entities from database starting from
     * (pageNumber - 1) * pageSize
     */
    @GetMapping("/{id}/orders")
    public CollectionModel<OrderModel> getUserOrders(
            @PathVariable int id,
            @RequestParam(defaultValue = START_PAGE) @Min(value = 1, message = PAGE_MUST_BE_POSITIVE) int page,
            @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) @Min(value = 1, message = PAGE_SIZE_MUST_BE_POSITIVE) int size) {
        List<OrderDto> userOrders = userService.getUserOrders(id, page, size);
        PageInfo pageInfo = userService.userOrdersPageInfo(id, page, size);
        CollectionModel<OrderModel> orderModels = orderModelAssembler.toCollectionModel(userOrders);
        return orderModelProcessor.process(orderModels, pageInfo, id);
    }

    /**
     * Retrieves {@link User}'s {@link Order} from database and
     * creates {@link UserModel} of it.
     * @param userId {@link User}'s id in database
     * @param orderId {@link Order}'s id in database
     * @return {@link OrderModel} associated with {@link User} with specified id.
     */
    @GetMapping("/{userId}/orders/{orderId}")
    public OrderModel getUserOrder(@PathVariable int userId, @PathVariable int orderId) {
        OrderDto userOrder = userService.getUserOrder(userId, orderId);
        OrderModel orderModel = orderModelAssembler.toModel(userOrder);
        return  orderModelProcessor.process(orderModel);
    }

    /**
     * Performs purchase operation on {@link User} entity:
     * Creates new {@link Order} and subtracts {@link User}'s balance
     * by {@link GiftCertificate} price and create {@link OrderModel}.
     * @param userId {@link User}'s id in database
     * @param certificateId {@link GiftCertificate}'s id in database
     * @return newly created {@link OrderModel}
     */
    @PutMapping("/{userId}/purchase/{certificateId}")
    public OrderModel purchaseCertificate(@PathVariable int userId, @PathVariable int certificateId) {
        OrderDto order = purchaseService.purchaseCertificate(userId, certificateId);
        OrderModel orderModel = orderModelAssembler.toModel(order);
        return orderModelProcessor.process(orderModel);
    }
}

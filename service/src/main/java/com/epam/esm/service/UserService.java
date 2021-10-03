package com.epam.esm.service;

import com.epam.esm.entities.GiftCertificate;
import com.epam.esm.entities.Order;
import com.epam.esm.entities.User;
import com.epam.esm.exception.ServiceException;
import com.epam.esm.repository.api.GiftCertificateRepository;
import com.epam.esm.repository.api.OrderRepository;
import com.epam.esm.repository.api.UserOrderUtil;
import com.epam.esm.repository.api.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Transactional
@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserOrderUtil userOrderUtil;
    private final OrderRepository orderRepository;
    private final GiftCertificateRepository giftCertificateRepository;

    @Autowired
    public UserService(UserRepository userRepository, UserOrderUtil userOrderUtil, OrderRepository orderRepository, GiftCertificateRepository giftCertificateRepository) {
        this.userRepository = userRepository;
        this.userOrderUtil = userOrderUtil;
        this.orderRepository = orderRepository;
        this.giftCertificateRepository = giftCertificateRepository;
    }

    public List<User> getUsers(int pageNumber, int pageSize) {
        return userRepository.findAll(pageNumber, pageSize);
    }

    public List<Order> getUserOrders(int userId, int pageNumber, int pageSize) {
        Optional<User> user = userRepository.findById(userId);
        if (!user.isPresent()) {
            throw new ServiceException("User not found", userId);
        }
        return userOrderUtil.getUserOrders(userId, pageNumber, pageSize);
    }

    public Order getUserOrder(int userId, int orderNumber) {
        Optional<User> user = userRepository.findById(userId);
        if (!user.isPresent()) {
            throw new ServiceException("User not found", userId);
        }
        List<Order> orders = user.get().getOrders();
        orderNumber -= 1;
        if (orderNumber < 0 || orders.size() < orderNumber) {
            throw new ServiceException("Order doesn't exist", orderNumber);
        }
        return orders.get(orderNumber);
    }

    public Order purchaseCertificate(int userId, int certificateId) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (!userOptional.isPresent()) {
            throw new ServiceException("User not found", userId);
        }
        Optional<GiftCertificate> certificateOptional = giftCertificateRepository.findById(certificateId);
        if (!certificateOptional.isPresent()) {
            throw new ServiceException("Certificate not found", userId);
        }
        GiftCertificate certificate = certificateOptional.get();
        User user = userOptional.get();
        BigDecimal balance = user.getBalance();
        BigDecimal price = certificate.getPrice();
        if (balance.compareTo(price) < 0) {
            throw new ServiceException("Not enough money");
        }
        BigDecimal newBalance = balance.subtract(price);
        user.setBalance(newBalance);
        LocalDateTime now = LocalDateTime.now();
        Order order = new Order(price, now, user, certificate);
        Optional<Order> savedOrder = orderRepository.save(order);
        if (!savedOrder.isPresent()) {
            throw new ServiceException("Unable to save order");
        }
        return order;
    }

}

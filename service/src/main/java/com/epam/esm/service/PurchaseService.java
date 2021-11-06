package com.epam.esm.service;

import com.epam.esm.data.OrderDto;
import com.epam.esm.entities.GiftCertificate;
import com.epam.esm.entities.Order;
import com.epam.esm.entities.User;
import com.epam.esm.exception.ServiceException;
import com.epam.esm.repository.api.GiftCertificateRepository;
import com.epam.esm.repository.api.OrderRepository;
import com.epam.esm.repository.api.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static com.epam.esm.exception.ExceptionCodes.*;

@Service
@Transactional
public class PurchaseService {

    private final UserRepository userRepository;
    private final GiftCertificateRepository giftCertificateRepository;
    private final OrderRepository orderRepository;

    @Autowired
    public PurchaseService(UserRepository userRepository, GiftCertificateRepository giftCertificateRepository, OrderRepository orderRepository) {
        this.userRepository = userRepository;
        this.giftCertificateRepository = giftCertificateRepository;
        this.orderRepository = orderRepository;
    }

    /**
     * Performs purchase operation on {@link User} entity:
     * Creates new {@link Order} and subtracts {@link User}'s balance
     * by {@link GiftCertificate} price.
     * @param userId {@link User}'s id in database
     * @param certificateId {@link GiftCertificate}'s id in database
     * @return newly create {@link Order}
     * @throws ServiceException if {@link User} doesn't exist, {@link GiftCertificate} doesn't exist or
     * {@link User} doesn't have enough money on balance.
     */
    public OrderDto purchaseCertificate(int userId, int certificateId) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (!userOptional.isPresent()) {
            throw new ServiceException(USER_NOT_FOUND, userId);
        }
        Optional<GiftCertificate> certificateOptional = giftCertificateRepository.findById(certificateId);
        if (!certificateOptional.isPresent()) {
            throw new ServiceException(CERTIFICATE_NOT_FOUND, certificateId);
        }
        GiftCertificate certificate = certificateOptional.get();
        User user = userOptional.get();
        BigDecimal balance = user.getBalance();
        BigDecimal price = certificate.getPrice();
        if (balance.compareTo(price) < 0) {
            throw new ServiceException(NOT_ENOUGH_MONEY);
        }
        BigDecimal newBalance = balance.subtract(price);
        user.setBalance(newBalance);
        LocalDateTime now = LocalDateTime.now();
        Order order = new Order(null, price, now, user, certificate);
        Order savedOrder = orderRepository.save(order);
        return new OrderDto(savedOrder);
    }

}

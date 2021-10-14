package com.epam.esm.service;

import com.epam.esm.data.OrderDto;
import com.epam.esm.entities.GiftCertificate;
import com.epam.esm.entities.Order;
import com.epam.esm.entities.Tag;
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
import java.util.stream.Collectors;

import static com.epam.esm.exception.ExceptionCodes.CERTIFICATE_NOT_FOUND;
import static com.epam.esm.exception.ExceptionCodes.NOT_ENOUGH_MONEY;
import static com.epam.esm.exception.ExceptionCodes.ORDER_NOT_FOUND;
import static com.epam.esm.exception.ExceptionCodes.TAG_NOT_FOUND;
import static com.epam.esm.exception.ExceptionCodes.UNABLE_TO_SAVE_ORDER;
import static com.epam.esm.exception.ExceptionCodes.USER_DOESNT_HAVE_THIS_ORDER;
import static com.epam.esm.exception.ExceptionCodes.USER_NOT_FOUND;

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

    /**
     * Retrieves {@link User} entities from database in pageable format.
     * @param pageNumber the number of the page
     * @param pageSize the size of the single page
     * @return {@link List} of pageSize {@link User} entities from database starting from
     * (pageNumber - 1) * pageSize
     */
    public List<User> getUsers(int pageNumber, int pageSize) {
        return userRepository.findAll(pageNumber, pageSize);
    }

    /**
     * Retrieves {@link User}'s {@link OrderDto} entities from database in pageable format.
     * @param userId {@link User}'s id.
     * @param pageNumber the number of the page
     * @param pageSize the size of the single page
     * @return {@link List} of pageSize {@link OrderDto} entities from database starting from
     * (pageNumber - 1) * pageSize
     * @throws ServiceException if user doesn't exist.
     */
    public List<OrderDto> getUserOrders(int userId, int pageNumber, int pageSize) {
        Optional<User> user = userRepository.findById(userId);
        if (!user.isPresent()) {
            throw new ServiceException(USER_NOT_FOUND, userId);
        }
        return userOrderUtil.getUserOrders(userId, pageNumber, pageSize)
                .stream()
                .map(OrderDto::new)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves {@link User}'s {@link Order} from database
     * @param userId {@link User}'s id in database
     * @param orderId {@link Order}'s id in database
     * @return {@link OrderDto} associated with {@link User}
     * @throws ServiceException if {@link User} not found or {@link Order} not found.
     */
    public OrderDto getUserOrder(int userId, int orderId) {
        Optional<User> user = userRepository.findById(userId);
        if (!user.isPresent()) {
            throw new ServiceException(USER_NOT_FOUND, userId);
        }
        List<Order> orders = user.get().getOrders();
        Optional<Order> orderOptional = orders.stream()
                .filter(order -> order.getId() == orderId)
                .findFirst();
        if (!orderOptional.isPresent()) {
            throw new ServiceException(USER_DOESNT_HAVE_THIS_ORDER);
        }
        return new OrderDto(orderOptional.get());
    }

    /**
     * Get {@link User} from database
     * @param userId {@link User}'s id
     * @return {@link User} with specified id
     * @throws ServiceException if {@link User} not found
     */
    public User getUserById(int userId) {
        Optional<User> user = userRepository.findById(userId);
        if (!user.isPresent()) {
            throw new ServiceException(USER_NOT_FOUND);
        }
        return user.get();
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
            throw new ServiceException(CERTIFICATE_NOT_FOUND, userId);
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
        Order order = new Order(price, now, user, certificate);
        Optional<Order> savedOrder = orderRepository.save(order);
        if (!savedOrder.isPresent()) {
            throw new ServiceException(UNABLE_TO_SAVE_ORDER);
        }
        return new OrderDto(order);
    }

    public Tag getMostUsedTagOfUserWithHighestCostOfAllOrders() {
        Optional<Tag> tag = userRepository.findMostUsedTagOfUserWithHighestCostOfAllOrders();
        if (!tag.isPresent()) {
            throw new ServiceException("No orders in database");
        }
        return tag.get();
    }
}

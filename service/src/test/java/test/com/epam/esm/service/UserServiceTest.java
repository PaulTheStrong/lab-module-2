package test.com.epam.esm.service;

import com.epam.esm.data.OrderDto;
import com.epam.esm.data.PageInfo;
import com.epam.esm.entities.GiftCertificate;
import com.epam.esm.entities.Order;
import com.epam.esm.entities.Tag;
import com.epam.esm.entities.User;
import com.epam.esm.exception.ServiceException;
import com.epam.esm.repository.api.OrderRepository;
import com.epam.esm.repository.api.UserOrderUtil;
import com.epam.esm.repository.api.UserRepository;
import com.epam.esm.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
public class UserServiceTest {

    private static final Optional<User> EMPTY_USER = Optional.empty();
    @Mock
    private OrderRepository orderRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserOrderUtil userOrderUtil;

    @InjectMocks
    private UserService userService;

    private static final List<Order> FIRST_USER_ORDERS = new ArrayList<>(Arrays.asList(
            new Order(1, new BigDecimal(120), LocalDateTime.now(), null, new GiftCertificate()),
            new Order(2, new BigDecimal(130), LocalDateTime.now(), null, new GiftCertificate())
    ));

    private static final List<Order> SECOND_USER_ORDERS = new ArrayList<>(Arrays.asList(
            new Order(3, new BigDecimal(120), LocalDateTime.now(), null, new GiftCertificate()),
            new Order(4, new BigDecimal(130), LocalDateTime.now(), null, new GiftCertificate())
    ));

    private static final List<User> USERS = new ArrayList<>(Arrays.asList(
            new User(1, "user1", new BigDecimal(100), FIRST_USER_ORDERS),
            new User(2, "user2", new BigDecimal(100), SECOND_USER_ORDERS)
    ));

    @BeforeEach
    public void init() {
        FIRST_USER_ORDERS.forEach(o -> o.setUser(USERS.get(0)));
        SECOND_USER_ORDERS.forEach(o -> o.setUser(USERS.get(1)));
    }

    @Test
    public void testGetUserByIdShouldReturnUserWhenUserExists() {
        User expectedUser = USERS.get(0);
        when(userRepository.findById(1)).thenReturn(Optional.of(expectedUser));

        User user = userService.getUserById(1);

        assertEquals(expectedUser, user);

    }

    @Test
    public void testGetUserByIdShouldThrowExceptionWhenUserNotExist() {
        when(userRepository.findById(1)).thenReturn(Optional.empty());
        assertThrows(ServiceException.class, () -> userService.getUserById(1));
    }

    @Test
    public void testGetUsersShouldReturnAllUsers() {
        when(userRepository.findAll(1, 10)).thenReturn(USERS);

        List<User> users = userService.getUsers(1, 10);

        assertEquals(USERS, users);
    }

    @Test
    public void testGetUserOrdersShouldReturnUserOrdersWhenUserExists() {
        when(userRepository.findById(1)).thenReturn(Optional.of(USERS.get(0)));
        when(userOrderUtil.getUserOrders(1, 1, 10)).thenReturn(FIRST_USER_ORDERS);

        List<OrderDto> actual = userService.getUserOrders(1, 1, 10);
        assertAll(
            () -> assertEquals(FIRST_USER_ORDERS.get(0).getId(), actual.get(0).getId()),
            () -> assertEquals(FIRST_USER_ORDERS.get(1).getId(), actual.get(1).getId()),
            () -> assertEquals(FIRST_USER_ORDERS.get(0).getTotalPrice(), actual.get(0).getPrice()),
            () -> assertEquals(FIRST_USER_ORDERS.get(1).getTotalPrice(), actual.get(1).getPrice())
        );
    }

    @Test
    public void testGetUserOrdersShouldThrowServiceExceptionWhenUserNotExists() {
        when(userRepository.findById(1)).thenReturn(EMPTY_USER);
        assertThrows(ServiceException.class, () -> userService.getUserOrders(1, 1, 10));
    }

    @Test
    public void testGetUserOrderShouldReturnUserOrderWhenOrderExistsForUser() {
        when(userRepository.findById(1)).thenReturn(Optional.of(USERS.get(0)));
        Order expected = FIRST_USER_ORDERS.get(0);
        when(orderRepository.findById(1)).thenReturn(Optional.of(expected));
        OrderDto actual = userService.getUserOrder(1, 1);

        assertAll(
                () -> assertEquals(expected.getId(), actual.getId()),
                () -> assertEquals(expected.getTotalPrice(), actual.getPrice()),
                () -> assertEquals(expected.getUser().getId(), actual.getUserId())
        );
    }

    @Test
    public void testGetUserOrderShouldThrowExceptionWhenUserNotExist() {
        when(userRepository.findById(1)).thenReturn(EMPTY_USER);
        assertThrows(ServiceException.class, () -> userService.getUserOrder(1, 1));
    }

    @Test
    public void testGetUserUserOrderShouldThrowExceptionWhenOrderNotAttachedToUser() {
        when(userRepository.findById(1)).thenReturn(Optional.of(USERS.get(0)));

        assertThrows(ServiceException.class, () -> userService.getUserOrder(1, 4));
    }

    @Test
    public void testGetMostUsedTagShouldReturnTagWhenRepositoryFound() {
        Tag expected = new Tag(1, "hello");
        when(userRepository.findMostUsedTagOfUserWithHighestCostOfAllOrders()).thenReturn(Optional.of(expected));
        assertEquals(expected.getId(), 1);
        assertEquals(expected.getName(), "hello");
    }

    @Test
    public void testGetMostUsedTagShouldThrowExceptionWhenTagNotFound() {
        when(userRepository.findMostUsedTagOfUserWithHighestCostOfAllOrders()).thenReturn(Optional.empty());
        assertThrows(ServiceException.class, () -> userService.getMostUsedTagOfUserWithHighestCostOfAllOrders());
    }

    @Test
    public void testUserPageInfoShouldReturnPageInfoWhenPositiveParametersPassed() {
        when(userRepository.countAll()).thenReturn(30);
        PageInfo expected = new PageInfo(20, 2, 30);
        PageInfo actual = userService.userPageInfo(2, 20);
        assertEquals(expected.getPageSize(), actual.getPageSize());
        assertEquals(expected.getCurrentPage(), actual.getCurrentPage());
        assertEquals(expected.getEntityCount(), actual.getEntityCount());
    }

    @Test
    public void testUserOrderPageInfoShouldReturnPageInfoWhenPositiveParametersPassed() {
        when(userOrderUtil.countUserOrders(1)).thenReturn(2);
        PageInfo expected = new PageInfo(1, 1, 2);
        PageInfo actual = userService.userOrdersPageInfo(1, 1, 1);
        assertEquals(expected.getPageSize(), actual.getPageSize());
        assertEquals(expected.getCurrentPage(), actual.getCurrentPage());
        assertEquals(expected.getEntityCount(), actual.getEntityCount());
    }
}

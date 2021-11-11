package com.epam.esm.service;

import com.epam.esm.data.OrderDto;
import com.epam.esm.data.PageInfo;
import com.epam.esm.entities.Order;
import com.epam.esm.entities.Role;
import com.epam.esm.entities.Tag;
import com.epam.esm.entities.User;
import com.epam.esm.exception.ServiceException;
import com.epam.esm.repository.api.OrderRepository;
import com.epam.esm.repository.api.RoleRepository;
import com.epam.esm.repository.api.TagRepository;
import com.epam.esm.repository.api.UserRepository;
import com.epam.esm.security.ApplicationSecurityUserDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.epam.esm.exception.ExceptionCodes.*;

@Transactional
@Service
@RequiredArgsConstructor
@Slf4j
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final PasswordEncoder passwordEncoder;
    private final TagRepository tagRepository;
    private final RoleRepository roleRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> userOptional = userRepository.findByUsername(username);
        if (!userOptional.isPresent()) {
            throw new UsernameNotFoundException("User with username " + username + " not found");
        }
        User user = userOptional.get();
        return new ApplicationSecurityUserDetails(user);
    }

    public User getUserByUsername(String username) {
        Optional<User> userOptional = userRepository.findByUsername(username);
        if (!userOptional.isPresent()) {
            throw new ServiceException(USER_NOT_FOUND);
        }
        return userOptional.get();
    }

    /**
     * Retrieves {@link User} entities from database in pageable format.
     * @param pageNumber the number of the page
     * @param pageSize the size of the single page
     * @return {@link List} of pageSize {@link User} entities from database starting from
     * (pageNumber - 1) * pageSize
     */
    public List<User> getUsers(int pageNumber, int pageSize) {
        return userRepository.findAll(PageRequest.of(pageNumber - 1, pageSize)).getContent();
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
        return orderRepository.getUserOrders(userId, PageRequest.of(pageNumber - 1, pageSize))
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
            throw new ServiceException(USER_NOT_FOUND, userId);
        }
        return user.get();
    }

    public Tag getMostUsedTagOfUserWithHighestCostOfAllOrders() {
        Optional<Tag> tag = tagRepository.findMostUsedTagOfUserWithHighestCostOfAllOrders();
        if (!tag.isPresent()) {
            throw new ServiceException("No orders in database");
        }
        return tag.get();
    }

    public PageInfo userPageInfo(int pageNumber, int pageSize) {
        int usersCount = (int)userRepository.count();
        return new PageInfo(pageSize, pageNumber, usersCount);
    }

    public PageInfo userOrdersPageInfo(int userId, int pageNumber, int pageSize) {
        int userOrdersCount  = orderRepository.countUserOrders(userId);
        return new PageInfo(pageSize, pageNumber, userOrdersCount);
    }

    public User register(User user) {
        Optional<User> userOptional = userRepository.findByUsername(user.getUsername());
        if (userOptional.isPresent()) {
            throw new ServiceException(USER_WITH_SUCH_USERNAME_EXISTS);
        }
        user.setBalance(BigDecimal.ZERO);
        user.setOrders(null);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        Optional<Role> role = roleRepository.findRoleByName(ApplicationSecurityUserDetails.USER);
        user.setRole(role.get());
        log.info("User {} has been registered", user.getUsername());
        return userRepository.save(user);
    }
}

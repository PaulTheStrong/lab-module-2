package com.epam.esm.repository.api;

import com.epam.esm.entities.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

@EnableJpaAuditing
public interface OrderRepository extends PagingAndSortingRepository<Order, Integer> {

    @Query(
        value = "SELECT orders FROM User user JOIN user.orders orders WHERE user.id = :userId",
        countQuery = "SELECT count(o) FROM TheOrder o where o.user.id = :id")
    Page<Order> getUserOrders(@Param("userId") int userId, Pageable pageable);

    @Query("SELECT count(o) FROM TheOrder o where o.user.id = :userId")
    Integer countUserOrders(@Param("userId") int userId);

}

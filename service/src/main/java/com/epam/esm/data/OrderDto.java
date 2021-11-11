package com.epam.esm.data;

import com.epam.esm.entities.GiftCertificate;
import com.epam.esm.entities.Order;
import lombok.Data;
import org.springframework.hateoas.EntityModel;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class OrderDto {

    private final Integer id;
    private final BigDecimal price;
    private final Integer userId;
    private final Integer certificateId;
    private final LocalDateTime timestamp;

    public OrderDto(Order order) {
        this.id = order.getId();
        this.price = order.getTotalPrice();
        this.userId = order.getUser().getId();
        GiftCertificate giftCertificate = order.getGiftCertificate();
        this.certificateId = giftCertificate.getId();
        this.timestamp = order.getTimestamp();
    }



}

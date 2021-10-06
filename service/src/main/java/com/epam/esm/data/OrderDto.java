package com.epam.esm.data;

import com.epam.esm.entities.Order;
import lombok.Data;

import java.math.BigDecimal;
import java.time.ZoneOffset;

@Data
public class OrderDto {

    private final int id;
    private final BigDecimal price;
    private final int userId;
    private final int certificateId;
    private final long timestamp;

    public OrderDto(Order order) {
        this.id = order.getId();
        this.price = order.getTotalPrice();
        this.userId = order.getUser().getId();
        this.certificateId = order.getGiftCertificate().getId();
        this.timestamp = order.getTimestamp().toInstant(ZoneOffset.UTC).toEpochMilli();
    }

}

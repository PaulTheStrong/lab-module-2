package com.epam.esm.entities;

import lombok.Data;
import lombok.NonNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class GiftCertificate implements Identifiable {

    private int id;
    private String name;
    private String description;
    private BigDecimal price;
    private Double duration;
    private LocalDateTime createDate;
    private LocalDateTime lastUpdateDate;

}

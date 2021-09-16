package com.epam.esm.entities;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Data
public class GiftCertificate extends Identifiable {

    private String name;
    private String description;
    private BigDecimal price;
    private Double duration;
    private LocalDateTime createDate;
    private LocalDateTime lastUpdateDate;

}

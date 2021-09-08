package com.epam.esm.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

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

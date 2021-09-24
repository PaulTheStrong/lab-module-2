package com.epam.esm.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GiftCertificate extends Identifiable {

    private String name;

    private String description;

    private BigDecimal price;

    private Double duration;
    private LocalDateTime createDate;
    private LocalDateTime lastUpdateDate;

    public GiftCertificate(int id, String name, String description, BigDecimal price,
                           Double duration, LocalDateTime createDate, LocalDateTime lastUpdateDate) {
        this(name, description, price, duration, createDate, lastUpdateDate);
        setId(id);
    }

}

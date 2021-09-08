package com.epam.esm.data;

import com.epam.esm.entities.Tag;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class GiftCertificateDto {

    private int id;
    private String name;
    private String description;
    private BigDecimal price;
    private Double duration;
    private String createDate;
    private String lastUpdateDate;

    private List<Tag> tags;

}

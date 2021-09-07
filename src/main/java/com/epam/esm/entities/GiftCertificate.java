package com.epam.esm.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.Date;

@Data
@AllArgsConstructor
@ToString
//@JsonAutoDetect(getterVisibility = JsonAutoDetect.Visibility.PUBLIC_ONLY,
//                setterVisibility = JsonAutoDetect.Visibility.PUBLIC_ONLY)
@NoArgsConstructor
public class GiftCertificate implements Identifiable {

    private int id;
    private String name;
    private String description;
    private BigDecimal price;
    private Double duration;
    private Date createDate;
    private Date lastUpdateDate;

}

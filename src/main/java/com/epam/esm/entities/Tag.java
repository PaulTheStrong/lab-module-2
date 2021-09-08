package com.epam.esm.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

@Data
public class Tag implements Identifiable {

    int id;
    private String name;

}

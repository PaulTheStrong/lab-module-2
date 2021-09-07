package com.epam.esm.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

@Data
@AllArgsConstructor
@ToString
public class Tag implements Identifiable {
    private int id;
    private String name;
}

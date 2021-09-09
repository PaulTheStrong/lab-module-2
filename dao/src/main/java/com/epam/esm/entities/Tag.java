package com.epam.esm.entities;

import lombok.Data;

@Data
public class Tag implements Identifiable {

    int id;
    private String name;

}

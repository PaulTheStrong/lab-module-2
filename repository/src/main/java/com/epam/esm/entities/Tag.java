package com.epam.esm.entities;

import lombok.Data;
import lombok.NonNull;

@Data
public class Tag implements Identifiable {

    int id;
    @NonNull
    private String name;

}

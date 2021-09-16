package com.epam.esm.entities;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;

@EqualsAndHashCode(callSuper = true)
@Data
public class Tag extends Identifiable {

    @NonNull
    private String name;

}

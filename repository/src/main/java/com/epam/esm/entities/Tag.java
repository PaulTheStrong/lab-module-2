package com.epam.esm.entities;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class Tag extends Identifiable {

    @NotNull(message = "40008")
    @Size(min = 1, max = 20, message = "40011")
    private String name;

    public Tag(String name) {
        this(0, name);
    }

    public Tag(int id, String name) {
        super(id);
        this.name = name;
    }

}

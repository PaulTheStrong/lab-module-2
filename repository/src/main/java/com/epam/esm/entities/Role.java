package com.epam.esm.entities;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "role")
public class Role extends Identifiable {

    public static final String USER = "ROLE_USER";
    public static final String ADMIN = "ROLE_ADMIN";

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

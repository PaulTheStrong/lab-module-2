package com.epam.esm.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.Hibernate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name="user")
@JsonIgnoreProperties("orders")
public class User extends Identifiable{

    @Column(name="username")
    @Size(max=20, min=4)
    @NotNull
    private String username;

    @Column(name="balance")
    @Min(0)
    @NotNull
    private BigDecimal balance;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    private List<Order> orders;

    public User() {}

    public User(int id, String username, BigDecimal balance, List<Order> orders) {
        super(id);
        this.username = username;
        this.balance = balance;
        this.orders = orders;
    }

    public User(String username, BigDecimal balance, List<Order> orders) {
        this.username = username;
        this.balance = balance;
        this.orders = orders;
    }

    public User(String username, BigDecimal balance) {
        this(username, balance, new ArrayList<>());
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        User user = (User) o;
        return Objects.equals(getId(), user.getId());
    }

    @Override
    public int hashCode() {
        return 0;
    }
}
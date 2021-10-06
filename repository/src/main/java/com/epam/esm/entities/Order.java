package com.epam.esm.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Table(name = "`order`")
@Entity(name="TheOrder")
public class Order extends Identifiable {

    @Column(name="total_price")
    @NotNull
    private BigDecimal totalPrice;

    @Column(name="buy_date")
    private LocalDateTime timestamp;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name="certificate_id")
    private GiftCertificate giftCertificate;

    public Order() {}

    public Order(Integer id, BigDecimal totalPrice, LocalDateTime timestamp, User user, GiftCertificate giftCertificate) {
        super(id);
        this.totalPrice = totalPrice;
        this.timestamp = timestamp;
        this.user = user;
        this.giftCertificate = giftCertificate;
    }

    public Order(BigDecimal totalPrice, LocalDateTime timestamp, User user, GiftCertificate giftCertificate) {
        this.totalPrice = totalPrice;
        this.timestamp = timestamp;
        this.user = user;
        this.giftCertificate = giftCertificate;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public GiftCertificate getGiftCertificate() {
        return giftCertificate;
    }

    public void setGiftCertificate(GiftCertificate giftCertificate) {
        this.giftCertificate = giftCertificate;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
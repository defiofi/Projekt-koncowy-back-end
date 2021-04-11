package com.kodilla.finalproject.domain;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "CURRENCIES")
public class Currency {

    private Long currencyID;

    private String currencyName;

    private String currencyCode;

    private BigDecimal account;

    private User user;

    public Currency(){}

    public Currency( String currencyName, String currencyCode, BigDecimal account, User user) {
        this.currencyName = currencyName;
        this.currencyCode = currencyCode;
        this.account = account;
        this.user = user;
    }

    public Currency(Long currencyID, String currencyName, String currencyCode, BigDecimal account, User user) {
        this.currencyID = currencyID;
        this.currencyName = currencyName;
        this.currencyCode = currencyCode;
        this.account = account;
        this.user = user;
    }
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column
    public Long getCurrencyID() {
        return currencyID;
    }
    @Column
    public String getCurrencyName() {
        return currencyName;
    }
    @Column
    public String getCurrencyCode() {
        return currencyCode;
    }
    @Column
    public BigDecimal getAccount() {
        return account;
    }

    @ManyToOne(//cascade = {CascadeType.PERSIST },
            targetEntity = User.class
    )
    public User getUser() { return user; }

    private void setCurrencyID(Long currencyID) { this.currencyID = currencyID; }

    private void setCurrencyName(String currencyName) {
        this.currencyName = currencyName;
    }

    private void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    private void setAccount(BigDecimal account) {
        this.account = account;
    }

    private void setUser(User user) { this.user = user; }
}

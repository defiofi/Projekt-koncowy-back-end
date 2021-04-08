package com.kodilla.finalproject.domain;

import java.math.BigDecimal;

public class CurrencyDTO {
    private String currencyName;
    private String currencyCode;
    private BigDecimal account;

    public CurrencyDTO(){}

    public CurrencyDTO(String currencyName, String currencyCode, BigDecimal account) {
        this.currencyName = currencyName;
        this.currencyCode = currencyCode;
        this.account = account;
    }

    public String getCurrencyName() {
        return currencyName;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public BigDecimal getAccount() {
        return account;
    }
}

package com.kodilla.finalproject.domain;

import java.math.BigDecimal;

public class CurrencyDTO {
    private String currencyCode;
    private BigDecimal account;

    public CurrencyDTO(String currencyCode, BigDecimal account) {
        this.currencyCode = currencyCode;
        this.account = account;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public BigDecimal getAccount() {
        return account;
    }
}

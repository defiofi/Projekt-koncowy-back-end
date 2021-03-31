package com.kodilla.finalproject.domain;

import java.util.List;


public class AccountDTO {

    private List<CurrencyDTO> purchasedCurrencies;

    public AccountDTO(List<CurrencyDTO> purchasedCurrencies) {

        this.purchasedCurrencies = purchasedCurrencies;
    }

    public List<CurrencyDTO> getPurchasedCurrencies() {
        return purchasedCurrencies;
    }
}

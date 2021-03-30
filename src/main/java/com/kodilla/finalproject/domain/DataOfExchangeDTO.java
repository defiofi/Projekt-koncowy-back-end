package com.kodilla.finalproject.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DataOfExchangeDTO {
    @JsonProperty("tradingDate")
    Date tradingDate;
    @JsonProperty("rates")
    RateOfExchangeDTO[] rates;

    public DataOfExchangeDTO(){
    }
    public DataOfExchangeDTO(Date tradingDate , RateOfExchangeDTO[] rates) {
        this.tradingDate = tradingDate;
        for(int i = 0 ; i< rates.length ; i++) {
            this.rates[i] = new RateOfExchangeDTO(
                    rates[i].getCurrencyName(), rates[i].getCurrencyCode(), rates[i].getBid(), rates[i].getAsk()
            );
        }
    }

    public Date getTradingDate() {
        return tradingDate;
    }
    public List<RateOfExchangeDTO> getRates(){
       return  Arrays.stream(rates).collect(Collectors.toList());
    }
}

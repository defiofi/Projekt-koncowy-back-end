package com.kodilla.finalproject.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DataOfExchangeDTO {
    @JsonProperty("tradingDate")
    private LocalDate tradingDate;
    @JsonProperty("rates")
    private RateOfExchangeDTO[] rates;

    public DataOfExchangeDTO(){
    }
    public DataOfExchangeDTO(LocalDate tradingDate , RateOfExchangeDTO[] rates) {
        this.tradingDate = tradingDate;
        this.rates = new RateOfExchangeDTO[(int)Arrays.stream(rates).count()];
        for(int i = 0 ; i< Arrays.stream(rates).count() ; i++) {
            this.rates[i] = new RateOfExchangeDTO(
                    rates[i].getCurrencyName(), rates[i].getCurrencyCode(), rates[i].getBid(), rates[i].getAsk()
            );
        }
    }

    public  LocalDate getTradingDate() {
        return tradingDate;
    }
    public List<RateOfExchangeDTO> getRates(){
       return  Arrays.stream(rates).collect(Collectors.toList());
    }
}

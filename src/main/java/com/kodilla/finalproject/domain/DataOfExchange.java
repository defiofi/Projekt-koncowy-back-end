package com.kodilla.finalproject.domain;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "DATAOFEXCHANGES")
public class DataOfExchange {

    private Long dataID;
    private LocalDate tradingDate;
    private List<RateOfExchange> rateOfExchangeList;

    public DataOfExchange(){}

    public DataOfExchange(LocalDate tradingDate, List<RateOfExchange> rateOfExchangeList){
        this.tradingDate = tradingDate;
        this.rateOfExchangeList = rateOfExchangeList;
    }

    public DataOfExchange(Long dataID, LocalDate tradingDate, List<RateOfExchange> rateOfExchangeList) {
        this.dataID = dataID;
        this.tradingDate = tradingDate;
        this.rateOfExchangeList = rateOfExchangeList;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column
    public Long getDataID() {
        return dataID;
    }
    @Column
    public LocalDate getTradingDate() {
        return tradingDate;
    }
    @OneToMany(
            targetEntity = RateOfExchange.class,
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY
    )
    public List<RateOfExchange> getRateOfExchange() {
        return rateOfExchangeList;
    }

    private void setDataID(Long dataID) {
        this.dataID = dataID;
    }

    private void setTradingDate(LocalDate tradingDate) {
        this.tradingDate = tradingDate;
    }

    public void setRateOfExchange(List<RateOfExchange> rateOfExchangeList) {
        this.rateOfExchangeList = rateOfExchangeList;
    }
}


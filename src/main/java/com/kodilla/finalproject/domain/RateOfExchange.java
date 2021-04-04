package com.kodilla.finalproject.domain;

import javax.persistence.*;

@Entity
@Table(name = "RATESOFEXCHANGE")
public class RateOfExchange {
    private Long rateID;
    private String currencyName;
    private String currencyCode;
    private Double bid;
    private Double ask;
    private DataOfExchange data;

    public RateOfExchange(){}

    public RateOfExchange( String currencyName, String currencyCode, Double bid, Double ask, DataOfExchange data) {
        this.currencyName = currencyName;
        this.currencyCode = currencyCode;
        this.bid = bid;
        this.ask = ask;
        this.data = data;
    }

    public RateOfExchange(Long rateID, String currencyName, String currencyCode, Double bid, Double ask, DataOfExchange data) {
        this.rateID = rateID;
        this.currencyName = currencyName;
        this.currencyCode = currencyCode;
        this.bid = bid;
        this.ask = ask;
        this.data = data;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column
    public Long getRateID() {
        return rateID;
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
    public Double getBid() {
        return bid;
    }
    @Column
    public Double getAsk() {
        return ask;
    }
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "DATA_ID")
    public DataOfExchange getData() {
        return data;
    }

    private void setRateID(Long rateID) {
        this.rateID = rateID;
    }

    private void setCurrencyName(String currencyName) {
        this.currencyName = currencyName;
    }

    private void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    private void setBid(Double bid) {
        this.bid = bid;
    }

    private void setAsk(Double ask) {
        this.ask = ask;
    }

    public void setData(DataOfExchange data) {
        this.data = data;
    }
}


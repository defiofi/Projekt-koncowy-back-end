package com.kodilla.finalproject.domain;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "USERS")
public class User {

    private Long userID;
    private String userName;
    private List<Currency> currencyList;

    public User(){}

    public User(String userName, List<Currency> currencyList){
        this.userName = userName;
        this.currencyList = currencyList;
    }
    public User(Long userID, String userName, List<Currency> currencyList) {
        this.userID = userID;
        this.userName = userName;
        this.currencyList = currencyList;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column
    public Long getUserID() {
        return userID;
    }
    @Column
    public String getUserName() {
        return userName;
    }

    @OneToMany(
            targetEntity = Currency.class,
            mappedBy = "user",
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY
    )
    public List<Currency> getCurrency() { return currencyList; }

    private void setUserID(Long userID) {
        this.userID = userID;
    }

    private void setUserName(String userName) {
        this.userName = userName;
    }

    public void setCurrency(List<Currency> currencyList) {
        this.currencyList = currencyList;
    }
}

package com.kodilla.finalproject.domain;

public class UserDTO {
    private Long userID;
    private String name;

    public UserDTO(Long userID, String name) {
        this.userID = userID;
        this.name = name;
    }
    public UserDTO(String name){
        this.name = name;
        this.userID = null;
    }

    public Long getUserID() {
        return userID;
    }

    public String getName() {
        return name;
    }
}

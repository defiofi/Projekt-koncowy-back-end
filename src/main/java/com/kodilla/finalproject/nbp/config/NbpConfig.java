package com.kodilla.finalproject.nbp.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class NbpConfig {
    @Value("${nbp.api.endpoint}")
    private String NbpApiEndPoint;

    public String getNbpApiEndPoint() {
        return NbpApiEndPoint;
    }
}

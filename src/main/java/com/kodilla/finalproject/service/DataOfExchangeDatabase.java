package com.kodilla.finalproject.service;

import com.kodilla.finalproject.domain.DataOfExchange;
import com.kodilla.finalproject.repository.DataOFExchangeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DataOfExchangeDatabase {
    @Autowired
    DataOFExchangeRepository dataOFExchangeRepository;

    public DataOfExchangeDatabase(DataOFExchangeRepository dataOFExchangeRepository){
        this.dataOFExchangeRepository = dataOFExchangeRepository;
    }
    public DataOfExchange save(DataOfExchange dataOfExchange){
       return dataOFExchangeRepository.save(dataOfExchange);
    }
    public List<DataOfExchange> findAll(){
        return dataOFExchangeRepository.findAll();
    }
}

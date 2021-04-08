package com.kodilla.finalproject.service;

import com.kodilla.finalproject.domain.Currency;
import com.kodilla.finalproject.repository.CurrencyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CurrencyDatabase {
    @Autowired
    private final CurrencyRepository currencyRepository;

    public CurrencyDatabase(CurrencyRepository currencyRepository){
        this.currencyRepository = currencyRepository;
    }
    public Currency save(final Currency currency){
        return currencyRepository.save(currency);
    }
    public void delete(final Currency currency){
        currencyRepository.delete(currency);
    }
    public void deleteByID(Long id){currencyRepository.deleteById(id);}
}

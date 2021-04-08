package com.kodilla.finalproject.domain;

import com.kodilla.finalproject.repository.CurrencyRepository;
import com.kodilla.finalproject.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class CurrencyTestSuite {
    @Autowired
    private CurrencyRepository currencyRepository;
    @Autowired
    private UserRepository userRepository;

    @Test
    public void TestCurrencyCreate() {
        //Give
        Currency currency = new Currency("funt szterling", "GBP", new BigDecimal(0.0), null);
        //When
        currencyRepository.save(currency);
        Long IdCur = currency.getCurrencyID();

        //Then
        assertTrue(currencyRepository.findById(IdCur).isPresent());
        //CleanUp
        currencyRepository.deleteById(IdCur);
    }
    @Test
    public void TestCurrencyRead(){
        //Give
        Currency currency = new Currency("funt szterling", "GBP", new BigDecimal(0.0), null);
        //When
        currencyRepository.save(currency);
        Long IdCur = currency.getCurrencyID();
        List<Currency> currencyList = new ArrayList<>();
        currencyList.add(currency);
        User user = new User("Test", currencyList);
        currencyRepository.save(new Currency(IdCur,"funt szterling","GBP", new BigDecimal(0.0), user ));
        //Then
        assertEquals(IdCur, currencyRepository.findById(IdCur).get().getCurrencyID());
        assertEquals("funt szterling", currencyRepository.findById(IdCur).get().getCurrencyName());
        assertEquals("GBP", currencyRepository.findById(IdCur).get().getCurrencyCode());
        //CleanUp
        currencyRepository.deleteById(IdCur);
    }
    @Test
    public void TestUserModification(){
        //Give
        Currency currency = new Currency("funt szterling", "GBP", new BigDecimal(0.0), null);
        //When
        currencyRepository.save(currency);
        Long IdCur = currency.getCurrencyID();
        //Then
        assertEquals(IdCur, currencyRepository.findById(IdCur).get().getCurrencyID());
        assertEquals("funt szterling", currencyRepository.findById(IdCur).get().getCurrencyName());
        assertEquals("GBP", currencyRepository.findById(IdCur).get().getCurrencyCode());
        currencyRepository.save(new Currency(IdCur, "korona czeska", "CZK", new BigDecimal(0.0), null));
        assertEquals(IdCur, currencyRepository.findById(IdCur).get().getCurrencyID());
        assertEquals("korona czeska", currencyRepository.findById(IdCur).get().getCurrencyName());
        assertEquals("CZK", currencyRepository.findById(IdCur).get().getCurrencyCode());
        //CleanUp
        currencyRepository.deleteById(IdCur);
    }
    @Test
    public void TestCurrencyDelete(){
        //Give
        Currency currency = new Currency("funt szterling", "GBP", new BigDecimal(0.0), null);
        //When
        currencyRepository.save(currency);
        Long IdCur = currency.getCurrencyID();
        List<Currency> currencyList = new ArrayList<>();
        currencyList.add(currency);
        User user = new User("Test", currencyList);
        Currency savedCurrency = currencyRepository.save(new Currency(IdCur,"funt szterling","GBP", new BigDecimal(0.0), user ));
        Long userID = savedCurrency.getUser().getUserID();
        assertTrue(currencyRepository.findById(IdCur).isPresent());
        assertEquals(IdCur, currencyRepository.findById(IdCur).get().getCurrencyID());
        assertEquals("funt szterling", currencyRepository.findById(IdCur).get().getCurrencyName());
        assertEquals("GBP", currencyRepository.findById(IdCur).get().getCurrencyCode());
        currencyRepository.delete(savedCurrency);
        assertFalse(currencyRepository.existsById(IdCur));
        assertTrue(userRepository.findById(userID).isPresent());
    }
}

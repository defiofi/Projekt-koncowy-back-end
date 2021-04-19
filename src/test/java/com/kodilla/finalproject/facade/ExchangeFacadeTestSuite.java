package com.kodilla.finalproject.facade;

import com.kodilla.finalproject.controller.UserNotFoundException;
import com.kodilla.finalproject.domain.Currency;
import com.kodilla.finalproject.domain.CurrencyDTO;
import com.kodilla.finalproject.domain.RateOfExchangeDTO;
import com.kodilla.finalproject.domain.User;
import com.kodilla.finalproject.repository.CurrencyRepository;
import com.kodilla.finalproject.service.CurrencyDatabase;
import com.kodilla.finalproject.service.UserDatabase;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class ExchangeFacadeTestSuite {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserFacadeTestSuite.class);
    @Autowired
    private ExchangeFacade exchangeFacade;
    @Autowired
    private CurrencyDatabase currencyDatabase;
    @Autowired
    private UserDatabase userDatabase;
    @Autowired
    private CurrencyRepository currencyRepository;

    @Test
    public void TestExchangeFacadeGetCurrency() {
        //GIVE
        User user1 = new User("GetCurrency", null);
        userDatabase.save(user1);
        Long userID = user1.getUserID();
        Currency newCurrency1 = currencyDatabase.save(new Currency(
                "nowy złoty polski",
                "PLN",
                new BigDecimal(0.0),
                user1));
        Long cID1 = newCurrency1.getCurrencyID();
        Currency newCurrency2 = currencyDatabase.save(new Currency(
                "dolar amerykański",
                "USD",
                new BigDecimal(0.0),
                user1));
        Long cID2 = newCurrency2.getCurrencyID();
        //WHEN
        List<CurrencyDTO> facadeCurrencyList = exchangeFacade.getCurrency(userID);
        //THEN
        assertTrue( facadeCurrencyList.size()>1);
        assertEquals("nowy złoty polski", facadeCurrencyList.get(0).getCurrencyName());
        assertEquals("PLN", facadeCurrencyList.get(0).getCurrencyCode());
        assertEquals("dolar amerykański", facadeCurrencyList.get(1).getCurrencyName());
        assertEquals("USD", facadeCurrencyList.get(1).getCurrencyCode());
        //CLEAN
        currencyDatabase.deleteByID(cID1);
        currencyDatabase.deleteByID(cID2);
        userDatabase.deleteUser(userID);
    }
    @Test
    public void TestExchangeFacadeNewCurrency() {
        //GIVE
        User user1 = new User("NewCurrency", null);
        userDatabase.save(user1);
        Long userID = user1.getUserID();
        //WHEN
        CurrencyDTO facadeCurrency = new CurrencyDTO();
        try {
            facadeCurrency = exchangeFacade.NewCurrency(userID, "PLN");
        } catch (UserNotFoundException e) {
            LOGGER.error("UserNotFoundException");
        }
        //THEN
        assertEquals("nowy złoty polski", facadeCurrency.getCurrencyName());
        assertEquals("PLN", facadeCurrency.getCurrencyCode());
        //CLEAN
        exchangeFacade.DeleteCurrency(userID, "PLN");
        userDatabase.deleteUser(userID);
    }
    @Test
    public void TestExchangeFacadeDeleteCurrency() {
        //GIVE
        User user1 = new User("DeleteCurrency", null);
        userDatabase.save(user1);
        Long userID = user1.getUserID();
        Currency newCurrency1 = currencyDatabase.save(new Currency(
                "nowy złoty polski",
                "PLN",
                new BigDecimal(0.0),
                user1));
        Long currencyID = newCurrency1.getCurrencyID();
        //WHEN
        assertEquals("nowy złoty polski", newCurrency1.getCurrencyName());
        assertEquals("PLN", newCurrency1.getCurrencyCode());
        exchangeFacade.DeleteCurrency(userID, "PLN");
        //THEN
        assertFalse(currencyRepository.findById(currencyID).isPresent());
        //CLEAN
        userDatabase.deleteUser(userID);
    }
    @Test
    public void TestExchangeFacadeTopUpAccount() {
        //GIVE
        User user1 = new User("TopUpAccount", null);
        userDatabase.save(user1);
        Long userID = user1.getUserID();
        Currency newCurrency1 = currencyDatabase.save(new Currency(
               "SDR (MFW)",
                "XDR",
                new BigDecimal(0.0),
                user1));
        //WHEN
        CurrencyDTO facadeCurrency = new CurrencyDTO();
        try {
            facadeCurrency = exchangeFacade.topUpAccount(userID, "XDR", 1000.0);
        }catch(UserNotFoundException e){
            LOGGER.error("UserNotFoundException");
        }
        //THEN
        assertTrue( facadeCurrency.getAccount().compareTo(new BigDecimal(1000))==0);
        //CLEAN
        currencyDatabase.delete(newCurrency1);
        userDatabase.deleteUser(userID);
    }
    @Test
    public void TestExchangeFacadePayOutAccount() {
        //GIVE
        User user1 = new User("PayOutAccount", null);
        userDatabase.save(user1);
        Long userID = user1.getUserID();
        Currency newCurrency1 = currencyDatabase.save(new Currency(
                "euro",
                "EUR",
                new BigDecimal(1000.0),
                user1));
        //WHEN
        CurrencyDTO facadeCurrency = new CurrencyDTO();
        try {
            facadeCurrency = exchangeFacade.topUpAccount(userID, "EUR", 1000.0);
        } catch (UserNotFoundException e) {
            LOGGER.error("UserNotFoundException");
        }
        //THEN
        assertTrue(facadeCurrency.getAccount().compareTo(new BigDecimal(0.00000001)) > 0);
        //CLEAN
        currencyDatabase.delete(newCurrency1);
        userDatabase.deleteUser(userID);
    }
    @Test
    public void TestExchangeFacadeGetActualRates() {
        //GIVE
        //WHEN
        List<RateOfExchangeDTO> dtoList = exchangeFacade.getActualRates();
        //THEN
        assertTrue(dtoList.size()>10);
    }
    @Test
    public void TestExchangeFacadeBuyCurrency() {
        //GIVE
        User user1 = new User("BuyCurrency", null);
        userDatabase.save(user1);
        Long userID = user1.getUserID();
        Currency newCurrency1 = currencyDatabase.save(new Currency(
                "nowy złoty polski",
                "PLN",
                new BigDecimal(1000.0),
                user1));
        Currency newCurrency2 = currencyDatabase.save(new Currency(
                "korona czeska",
                "CZK",
                new BigDecimal(0.0),
                user1));
        //WHEN
        List<CurrencyDTO> list = new ArrayList<>();
        try{
            list = exchangeFacade.buyCurrency(userID, "CZK", 100.0);
        }catch(UserNotFoundException e){
            LOGGER.error("UserNotFoundException");
        }
        assertTrue(list.size() == 2);
        //CLEAN
        currencyDatabase.delete(newCurrency1);
        currencyDatabase.delete(newCurrency2);
        userDatabase.deleteUser(userID);
    }
    @Test
    public void TestExchangeFacadeSellCurrency() {
        //GIVE
        User user1 = new User("SellCurrency", null);
        userDatabase.save(user1);
        Long userID = user1.getUserID();
        Currency newCurrency1 = currencyDatabase.save(new Currency(
                "nowy złoty polski",
                "PLN",
                new BigDecimal(1000.0),
                user1));
        Currency newCurrency2 = currencyDatabase.save(new Currency(
                "korona duńska",
                "DKK",
                new BigDecimal(1000.0),
                user1));
        //WHEN
        List<CurrencyDTO> list = new ArrayList<>();
        try{
            list = exchangeFacade.sellCurrency(userID, "DKK", 100.0);
        }catch(UserNotFoundException e){
            LOGGER.error("UserNotFoundException");
        }
        //THEN
        assertTrue(list.size() == 2);
        //CLEAN
        currencyDatabase.delete(newCurrency1);
        currencyDatabase.delete(newCurrency2);
        userDatabase.deleteUser(userID);
    }
}

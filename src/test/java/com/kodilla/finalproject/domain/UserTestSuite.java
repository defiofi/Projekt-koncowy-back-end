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
import static org.junit.jupiter.api.Assertions.assertFalse;

@SpringBootTest
public class UserTestSuite {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CurrencyRepository currencyRepository;

    @Test
    public void TestUserCreate(){
        //Give
        User user = new User("Artur", null);
        //When
        User savedUser = userRepository.save(user);
        Long id = user.getUserID();
        //Then
        assertTrue(userRepository.findById(id).isPresent());
        //CleanUp
        userRepository.deleteById(id);
    }
    @Test
    public void TestUserRead(){
        //Give
        User user = new User("Artur", null);
        List<Currency> currencyList = new ArrayList<>();
        //When
        User savedUser = userRepository.save(user);
        Long id = user.getUserID();
        currencyList.add( new Currency("dolar amerykański","USD",new BigDecimal(0),user));
        userRepository.save(new User(id , "Artur", currencyList));
        //Then
        assertEquals(id, userRepository.findById(id).get().getUserID());
        assertEquals("Artur",userRepository.findById(id).get().getUserName());
        //CleanUp
        userRepository.deleteById(id);
    }
    @Test
    public void TestUserModification(){
        //Give
        User user = new User("Artur", null);

        //When
        userRepository.save(user);
        Long id = user.getUserID();

        //Then
        assertEquals(id, userRepository.findById(id).get().getUserID());
        assertEquals("Artur",userRepository.findById(id).get().getUserName());
        userRepository.save(new User(id , "Marcin", null));
        assertEquals(id, userRepository.findById(id).get().getUserID());
        assertEquals("Marcin",userRepository.findById(id).get().getUserName());

        //CleanUp
        userRepository.deleteById(id);
    }
    @Test
    public void TestUserDelete(){
        //Give
        User user = new User("Artur", null);
        List<Currency> currencyList = new ArrayList<>();

        //When
        userRepository.save(user);
        Long id = user.getUserID();
        Currency newCurrency = new Currency("dolar amerykański","USD",new BigDecimal(0),user);
        currencyRepository.save(newCurrency);
        Long curID = newCurrency.getCurrencyID();
        currencyList.add(newCurrency);
        userRepository.save(new User(id , "Artur", currencyList));

        //Then
        assertEquals(id, userRepository.findById(id).get().getUserID());
        assertEquals("Artur",userRepository.findById(id).get().getUserName());
        assertEquals(currencyList.size(), userRepository.findById(id).get().getCurrency().size());
        userRepository.deleteById(id);
        assertFalse(userRepository.findById(id).isPresent());
        assertFalse(currencyRepository.findById(curID).isPresent());
    }
}

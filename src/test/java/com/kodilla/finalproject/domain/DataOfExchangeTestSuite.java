package com.kodilla.finalproject.domain;

import com.kodilla.finalproject.repository.DataOFExchangeRepository;
import com.kodilla.finalproject.repository.RateOfExchangeRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertFalse;

@SpringBootTest
public class DataOfExchangeTestSuite {

    @Autowired
    DataOFExchangeRepository dataOFExchangeRepository;
    @Autowired
    RateOfExchangeRepository rateOfExchangeRepository;

    @Test
    public void TestDataOfExchangeCreate(){
        //Give
        DataOfExchange newDataOfExchange = dataOFExchangeRepository.save(
                new DataOfExchange( LocalDate.now(), null)
        );
        List<RateOfExchange> rateOfExchangeList = new ArrayList<>();
        rateOfExchangeList.add(
                new RateOfExchange("dolar amerykański","USD",3.1234,3.5678, newDataOfExchange)
        );
        Long Id = newDataOfExchange.getDataID();
        //When
        dataOFExchangeRepository.save(
                new DataOfExchange(newDataOfExchange.getDataID(), newDataOfExchange.getTradingDate(), rateOfExchangeList )
        );
        //Then
        assertTrue(dataOFExchangeRepository.findById(Id).isPresent());
        //Clean
        dataOFExchangeRepository.delete(dataOFExchangeRepository.findById(Id).get());
    }
    @Test
    public void TestDataOfExchangeRead(){
        //Give
        LocalDate date = LocalDate.now();
        DataOfExchange newDataOfExchange = dataOFExchangeRepository.save(new DataOfExchange( date, null));
        List<RateOfExchange> rateOfExchangeList = new ArrayList<>();
        rateOfExchangeList.add(new RateOfExchange("dolar amerykański","USD",3.1234,3.5678, newDataOfExchange));
        Long Id = newDataOfExchange.getDataID();
        //When
        dataOFExchangeRepository.save(new DataOfExchange(newDataOfExchange.getDataID(), newDataOfExchange.getTradingDate(), rateOfExchangeList ));
        //Then
        assertEquals(Id , dataOFExchangeRepository.findById(Id).get().getDataID());
        assertEquals(date, dataOFExchangeRepository.findById(Id).get().getTradingDate());
        assertEquals(1 , dataOFExchangeRepository.findById(Id).get().getRateOfExchange().size());
        //Clean
        dataOFExchangeRepository.delete(dataOFExchangeRepository.findById(Id).get());
    }
    @Test
    public void TestDataOfExchangeModification(){
        //Give
        LocalDate date = LocalDate.parse("2021-03-20");
        LocalDate newdate = LocalDate.parse("2021-04-06");
        DataOfExchange newDataOfExchange = dataOFExchangeRepository.save(new DataOfExchange( date, null));
        List<RateOfExchange> rateOfExchangeList = new ArrayList<>();
        rateOfExchangeList.add(new RateOfExchange("dolar amerykański","USD",3.1234,3.5678, newDataOfExchange));
        Long Id = newDataOfExchange.getDataID();
        //When
        dataOFExchangeRepository.save(new DataOfExchange(newDataOfExchange.getDataID(), newDataOfExchange.getTradingDate(), rateOfExchangeList ));
        //Then
        assertEquals(Id , dataOFExchangeRepository.findById(Id).get().getDataID());
        assertEquals(date, dataOFExchangeRepository.findById(Id).get().getTradingDate());
        assertEquals(1 , dataOFExchangeRepository.findById(Id).get().getRateOfExchange().size());
        dataOFExchangeRepository.save(new DataOfExchange(newDataOfExchange.getDataID(), newdate, null ));
        assertEquals(Id , dataOFExchangeRepository.findById(Id).get().getDataID());
        assertEquals(newdate, dataOFExchangeRepository.findById(Id).get().getTradingDate());
        //Clean
        dataOFExchangeRepository.delete(dataOFExchangeRepository.findById(Id).get());
    }
    @Test
    public void TestDataOfExchangeDelete(){
        //Give
        LocalDate date = LocalDate.now();
        DataOfExchange newDataOfExchange = dataOFExchangeRepository.save(new DataOfExchange( date, null));
        List<RateOfExchange> rateOfExchangeList = new ArrayList<>();
        rateOfExchangeList.add(new RateOfExchange("dolar amerykański","USD",3.1234,3.5678, newDataOfExchange));
        Long Id = newDataOfExchange.getDataID();
        //When
        dataOFExchangeRepository.save(new DataOfExchange(newDataOfExchange.getDataID(), newDataOfExchange.getTradingDate(), rateOfExchangeList ));
        Long IdRate = dataOFExchangeRepository.findById(Id).get().getRateOfExchange().get(0).getRateID();
        //Then
        assertTrue(dataOFExchangeRepository.findById(Id).isPresent());
        dataOFExchangeRepository.delete(dataOFExchangeRepository.findById(Id).get());
        assertFalse(dataOFExchangeRepository.findById(Id).isPresent());
        assertFalse(rateOfExchangeRepository.findById(IdRate).isPresent());
    }
}

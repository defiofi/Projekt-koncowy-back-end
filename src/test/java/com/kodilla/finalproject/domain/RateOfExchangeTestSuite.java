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

@SpringBootTest
public class RateOfExchangeTestSuite {
    @Autowired
    DataOFExchangeRepository dataOFExchangeRepository;
    @Autowired
    RateOfExchangeRepository rateOfExchangeRepository;

    @Test
    public void TestRateOfExchangeCreate(){
        //Give
        RateOfExchange newRateOfExchange = rateOfExchangeRepository.save(
                new RateOfExchange("dolar amerykański","USD",6.1234, 6.5678, null)
        );

        //When
        Long rateId = newRateOfExchange.getRateID();

        //Then
        assertTrue(rateOfExchangeRepository.findById(rateId).isPresent());

        //Clean
        rateOfExchangeRepository.delete(newRateOfExchange);
    }
    @Test
    public void TestRateOfExchangeRead(){
        //Give
        RateOfExchange newRateOfExchange = rateOfExchangeRepository.save(
                new RateOfExchange("dolar amerykański","USD",1.1234, 1.5678, null)
        );
        //When
        Long rateId = newRateOfExchange.getRateID();
        rateOfExchangeRepository.save(
                new RateOfExchange(rateId,"dolar amerykański","USD",7.1234, 7.5678, null));
        //Then
        assertEquals(rateId, rateOfExchangeRepository.findById(rateId).get().getRateID() );
        assertEquals("dolar amerykański", rateOfExchangeRepository.findById(rateId).get().getCurrencyName());
        assertEquals("USD", rateOfExchangeRepository.findById(rateId).get().getCurrencyCode());
        assertEquals(7.1234, rateOfExchangeRepository.findById(rateId).get().getBid());
        assertEquals(7.5678, rateOfExchangeRepository.findById(rateId).get().getAsk());
        //Clean
        rateOfExchangeRepository.delete(rateOfExchangeRepository.findById(rateId).get());
    }
    @Test
    public void TestRateOfExchangeModification(){
        //Give
        RateOfExchange newRateOfExchange = rateOfExchangeRepository.save(
                new RateOfExchange("dolar amerykański","USD",8.1234, 8.5678, null)
        );
        //When
        Long rateId = newRateOfExchange.getRateID();
        //Then
        assertEquals(rateId, rateOfExchangeRepository.findById(rateId).get().getRateID() );
        assertEquals("dolar amerykański", rateOfExchangeRepository.findById(rateId).get().getCurrencyName());
        assertEquals("USD", rateOfExchangeRepository.findById(rateId).get().getCurrencyCode());
        assertEquals(8.1234, rateOfExchangeRepository.findById(rateId).get().getBid());
        assertEquals(8.5678, rateOfExchangeRepository.findById(rateId).get().getAsk());
        rateOfExchangeRepository.save(
                new RateOfExchange(rateId,"dolar australijski","AUD",2.1234, 2.5678, null)
        );
        assertEquals(rateId, rateOfExchangeRepository.findById(rateId).get().getRateID() );
        assertEquals("dolar australijski", rateOfExchangeRepository.findById(rateId).get().getCurrencyName());
        assertEquals("AUD", rateOfExchangeRepository.findById(rateId).get().getCurrencyCode());
        assertEquals(2.1234, rateOfExchangeRepository.findById(rateId).get().getBid());
        assertEquals(2.5678, rateOfExchangeRepository.findById(rateId).get().getAsk());
        //Clean
        rateOfExchangeRepository.delete(rateOfExchangeRepository.findById(rateId).get());
    }
    @Test
    public void TestRateOfExchangeDelete(){
        //Give
        RateOfExchange newRateOfExchange = rateOfExchangeRepository.save(
                new RateOfExchange("dolar amerykański","USD",9.1234, 9.5678, null)
        );
        //When
        Long rateId = newRateOfExchange.getRateID();
        List<RateOfExchange> rateOfExchangeList = new ArrayList<>();
        rateOfExchangeList.add(newRateOfExchange);
        DataOfExchange dataOfExchange = new DataOfExchange( LocalDate.now(), rateOfExchangeList);
        rateOfExchangeRepository.save(
                new RateOfExchange(rateId,"dolar amerykański","USD",9.1234, 9.5678, dataOfExchange));
        //Then
        Long dataId = rateOfExchangeRepository.findById(rateId).get().getData().getDataID();
        assertTrue(rateOfExchangeRepository.findById(rateId).isPresent());
        rateOfExchangeRepository.delete(rateOfExchangeRepository.findById(rateId).get());
        assertFalse(rateOfExchangeRepository.findById(rateId).isPresent());
        assertTrue(dataOFExchangeRepository.findById(dataId).isPresent());
        //CLEAN
        dataOFExchangeRepository.delete(dataOFExchangeRepository.findById(dataId).get());
    }
}

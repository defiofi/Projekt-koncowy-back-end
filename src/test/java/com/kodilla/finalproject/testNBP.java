package com.kodilla.finalproject;

import com.kodilla.finalproject.domain.DataOfExchangeDTO;
import com.kodilla.finalproject.nbp.client.NbpClient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class testNBP {
    @Autowired
    private NbpClient nbpClient;
    @Test
    public void testGetActualNBP(){

        //GIVE & WHEN
        DataOfExchangeDTO answer = nbpClient.getActualNbpCurrency();
        //THEN
        assertTrue(answer.getRates().size()>0);
        assertEquals("USD", answer.getRates().get(0).getCurrencyCode());
        assertEquals("dolar amerykański" , answer.getRates().get(0).getCurrencyName());
        assertTrue(answer.getRates().get(0).getBid()> 0.0);
        assertTrue(answer.getRates().get(0).getAsk()> 0.0);
    }

    @Test
    public void testGetNBPByDate(){
        //GIVE
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusDays(28);
        //WHEN
        List<DataOfExchangeDTO> answer = nbpClient.getNbpCurrencyByDate(startDate , endDate);
        assertTrue(answer.size()>= 19);

    }
}

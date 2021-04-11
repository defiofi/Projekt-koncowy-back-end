package com.kodilla.finalproject.mappers;

import com.kodilla.finalproject.domain.DataOfExchange;
import com.kodilla.finalproject.domain.DataOfExchangeDTO;
import com.kodilla.finalproject.domain.RateOfExchange;
import com.kodilla.finalproject.domain.RateOfExchangeDTO;
import com.kodilla.finalproject.mapper.DataOfExchangeMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class DataOfExchangeMapperTestSuite {
    @Autowired
    private DataOfExchangeMapper dataOfExchangeMapper;

    @Test
    public void TestDataOfExchangeMapperMapToDataOfExchange() {
        //GIVE
        RateOfExchangeDTO[] rateOfExchangeDTOS = new RateOfExchangeDTO[2];
        rateOfExchangeDTOS[0] = new RateOfExchangeDTO("dolar amerykański","USD",3.1234,3.4567);
        rateOfExchangeDTOS[1] = new RateOfExchangeDTO("dolar australijski","AUD",2.1234,2.5678);
        LocalDate tradingDate = LocalDate.parse("2021-04-10");
        DataOfExchangeDTO dataOfExchangeDTO = new DataOfExchangeDTO( tradingDate, rateOfExchangeDTOS);
        //WHEN
        DataOfExchange mappedDataOfExchange = dataOfExchangeMapper.maptoDataOfExchange(dataOfExchangeDTO, 1L);
        //THEN
        assertEquals(tradingDate, mappedDataOfExchange.getTradingDate());
        assertEquals(2, mappedDataOfExchange.getRateOfExchange().size());
        assertEquals("dolar amerykański", mappedDataOfExchange.getRateOfExchange().get(0).getCurrencyName());
        assertEquals("dolar australijski", mappedDataOfExchange.getRateOfExchange().get(1).getCurrencyName());
        assertEquals("USD", mappedDataOfExchange.getRateOfExchange().get(0).getCurrencyCode());
        assertEquals("AUD", mappedDataOfExchange.getRateOfExchange().get(1).getCurrencyCode());
        assertEquals(3.1234, mappedDataOfExchange.getRateOfExchange().get(0).getBid());
        assertEquals(2.1234, mappedDataOfExchange.getRateOfExchange().get(1).getBid());
        assertEquals(3.4567, mappedDataOfExchange.getRateOfExchange().get(0).getAsk());
        assertEquals(2.5678, mappedDataOfExchange.getRateOfExchange().get(1).getAsk());
    }
    @Test
    public void TestDataOfExchangeMapperMapToListRateOfExchange() {
        //GIVE
        List<RateOfExchangeDTO> rateOfExchangeDTOS = new ArrayList<>();
        rateOfExchangeDTOS.add(new RateOfExchangeDTO("dolar amerykański","USD",3.1234,3.4567));
        rateOfExchangeDTOS.add(new RateOfExchangeDTO("dolar australijski","AUD",2.1234,2.5678));
        //WHEN
        List<RateOfExchange> mappedlistRateOfExchange = dataOfExchangeMapper.maptoListRateOfExchange(rateOfExchangeDTOS, 1L);
        //THEN
        assertEquals(2, mappedlistRateOfExchange.size());
        assertEquals("dolar amerykański", mappedlistRateOfExchange.get(0).getCurrencyName());
        assertEquals("dolar australijski", mappedlistRateOfExchange.get(1).getCurrencyName());
        assertEquals("USD", mappedlistRateOfExchange.get(0).getCurrencyCode());
        assertEquals("AUD", mappedlistRateOfExchange.get(1).getCurrencyCode());
        assertEquals(3.1234, mappedlistRateOfExchange.get(0).getBid());
        assertEquals(2.1234, mappedlistRateOfExchange.get(1).getBid());
        assertEquals(3.4567, mappedlistRateOfExchange.get(0).getAsk());
        assertEquals(2.5678, mappedlistRateOfExchange.get(1).getAsk());
    }
    @Test
    public void TestDataOfExchangeMapperMapToDataOfExchangeDTO() {
        //GIVE
        List<RateOfExchange> rateOfExchangeList = new ArrayList<>();
        rateOfExchangeList.add(new RateOfExchange("dolar amerykański", "USD", 3.1234, 3.4567, null));
        rateOfExchangeList.add(new RateOfExchange("dolar australijski", "AUD", 2.1234, 2.4567, null));
        LocalDate tradingDate = LocalDate.parse("2021-04-10");
        DataOfExchange dataOfExchange = new DataOfExchange(tradingDate, rateOfExchangeList);
        //WHEN
        DataOfExchangeDTO mappedDataOfExchangeDTO = dataOfExchangeMapper.maptoDataOfExchangeDTO(dataOfExchange);
        //THEN
        assertEquals(2, mappedDataOfExchangeDTO.getRates().size());
        assertEquals(tradingDate, mappedDataOfExchangeDTO.getTradingDate());
        assertEquals("dolar amerykański", mappedDataOfExchangeDTO.getRates().get(0).getCurrencyName());
        assertEquals("dolar australijski", mappedDataOfExchangeDTO.getRates().get(1).getCurrencyName());
        assertEquals("USD", mappedDataOfExchangeDTO.getRates().get(0).getCurrencyCode());
        assertEquals("AUD", mappedDataOfExchangeDTO.getRates().get(1).getCurrencyCode());
        assertEquals(3.1234, mappedDataOfExchangeDTO.getRates().get(0).getBid());
        assertEquals(2.1234, mappedDataOfExchangeDTO.getRates().get(1).getBid());
        assertEquals(3.4567, mappedDataOfExchangeDTO.getRates().get(0).getAsk());
        assertEquals(2.4567, mappedDataOfExchangeDTO.getRates().get(1).getAsk());
    }
    @Test
    public void TestDataOfExchangeMapperMapToRateOfExchangeDTO() {
        //GIVE
        RateOfExchange rateOfExchange = new RateOfExchange("dolar amerykański", "USD", 3.1234, 3.4567, null);
        //WHEN
        RateOfExchangeDTO mappedRateOfExchangeDTO = dataOfExchangeMapper.maptoRateOfExchangeDTO(rateOfExchange);
        //THEN
        assertEquals("dolar amerykański", mappedRateOfExchangeDTO.getCurrencyName());
        assertEquals("USD", mappedRateOfExchangeDTO.getCurrencyCode());
        assertEquals(3.1234, mappedRateOfExchangeDTO.getBid());
        assertEquals(3.4567, mappedRateOfExchangeDTO.getAsk());
    }
    @Test
    public void TestDataOfExchangeMapperMapToListRateOfExchangeDTO() {
        //GIVE
        List<RateOfExchange> rateOfExchangeList = new ArrayList<>();
        rateOfExchangeList.add(new RateOfExchange("dolar amerykański", "USD", 3.1234, 3.4567, null));
        rateOfExchangeList.add(new RateOfExchange("dolar australijski", "AUD", 2.1234, 2.4567, null));
        //WHEN
        List<RateOfExchangeDTO> mappedRateOfExchangeDTOList = dataOfExchangeMapper.maptoListRateOfExchangeDTO(rateOfExchangeList);
        //THEN
        assertEquals(2, mappedRateOfExchangeDTOList.size());
        assertEquals("dolar amerykański", mappedRateOfExchangeDTOList.get(0).getCurrencyName());
        assertEquals("dolar australijski", mappedRateOfExchangeDTOList.get(1).getCurrencyName());
        assertEquals("USD", mappedRateOfExchangeDTOList.get(0).getCurrencyCode());
        assertEquals("AUD", mappedRateOfExchangeDTOList.get(1).getCurrencyCode());
        assertEquals(3.1234, mappedRateOfExchangeDTOList.get(0).getBid());
        assertEquals(2.1234, mappedRateOfExchangeDTOList.get(1).getBid());
        assertEquals(3.4567, mappedRateOfExchangeDTOList.get(0).getAsk());
        assertEquals(2.4567, mappedRateOfExchangeDTOList.get(1).getAsk());
    }
}

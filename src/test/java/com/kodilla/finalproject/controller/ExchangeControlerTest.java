package com.kodilla.finalproject.controller;

import com.kodilla.finalproject.domain.*;
import com.kodilla.finalproject.facade.ExchangeFacade;
import com.kodilla.finalproject.mapper.CurrencyMapper;
import com.kodilla.finalproject.mapper.DataOfExchangeMapper;
import com.kodilla.finalproject.nbp.client.NbpClient;
import com.kodilla.finalproject.nbp.config.NbpConfig;
import com.kodilla.finalproject.repository.CurrencyRepository;
import com.kodilla.finalproject.service.CurrencyDatabase;
import com.kodilla.finalproject.service.DataOfExchangeDatabase;
import com.kodilla.finalproject.service.UserDatabase;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringJUnitWebConfig
@WebMvcTest(ExchangeControler.class)
class ExchangeControlerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private ExchangeFacade exchangeFacade;
    @MockBean
    private  UserDatabase userDatabase;
    @MockBean
    private CurrencyMapper currencyMapper;
    @MockBean
    private  DataOfExchangeMapper dataOfExchangeMapper;
    @MockBean
    private CurrencyDatabase currencyDatabase;
    @MockBean
    private DataOfExchangeDatabase dataOfExchangeDatabase;
    @MockBean
    private CurrencyRepository currencyRepository;
    @MockBean
    private NbpClient nbpClient;
    @MockBean
    private NbpConfig nbpConfig;
    @MockBean
    RestTemplateBuilder restTemplateBuilder;

    @Test
    public void ExchangeControllerGetCurrency() throws Exception {
        //GIVEN
        List<CurrencyDTO> currencyDTOList = new ArrayList<>();
        currencyDTOList.add(new CurrencyDTO("nowy złoty polski","PLN", new BigDecimal(0.0)));
        when(exchangeFacade.getCurrency(any())).thenReturn(currencyDTOList);

        //WHEN and THEN
        mockMvc.perform(MockMvcRequestBuilders
                .get("/project/currency/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(1)));
    }

    @Test
    public void ExchangeControllerNewCurrency() throws Exception {
        //GIVEN

        when(exchangeFacade.NewCurrency(any(), any())).thenReturn(new CurrencyDTO("dolar amerykański","USD", new BigDecimal(0.0)));

        //WHEN and THEN
        mockMvc.perform(MockMvcRequestBuilders
                .post("/project/currency?userId=1&currency_Code=USD")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$.currencyName", Matchers.is("dolar amerykański")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.currencyCode", Matchers.is("USD")));
    }
    @Test
    public void ExchangeControllerDeleteCurrency() throws Exception {
        //GIVEN

        //WHEN and THEN
        mockMvc.perform(MockMvcRequestBuilders
                .delete("/project/currency?userId=1&currency_Code=PLN")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is(200));
    }
    @Test
    public void ExchangeControllerTopUpAccount() throws Exception {
        //GIVEN
        when(exchangeFacade.topUpAccount(any(), any(), any())).thenReturn(new CurrencyDTO("dolar amerykański","USD", new BigDecimal(1000.0)));
        //WHEN and THEN
        mockMvc.perform(MockMvcRequestBuilders
                .put("/project/currency/topUp?userId=1&currency_Code=USD&value=1000")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$.currencyName", Matchers.is("dolar amerykański")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.currencyCode", Matchers.is("USD")));
    }
    @Test
    public void ExchangeControllerPayOutAccount() throws Exception {
        //GIVEN
        when(exchangeFacade.payOutAccount(any(), any(), any())).thenReturn(new CurrencyDTO("dolar amerykański", "USD", new BigDecimal(0.0)));
        //WHEN and THEN
        mockMvc.perform(MockMvcRequestBuilders
                .put("/project/currency/payOut?userId=1&currency_Code=USD&value=1000")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$.currencyName", Matchers.is("dolar amerykański")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.currencyCode", Matchers.is("USD")));;
    }
    @Test
    public void ExchangeControllerGetActualRates() throws Exception {
        //GIVEN
        List<RateOfExchangeDTO> rate = new ArrayList<>();
        rate.add( new RateOfExchangeDTO("dolar amerykański", "USD", 3.1234, 3.5678));
        rate.add( new RateOfExchangeDTO("dolar australijski", "AUD", 2.1234, 2.5678));
        when(exchangeFacade.getActualRates()).thenReturn(rate);

        //WHEN and THEN
        mockMvc.perform(MockMvcRequestBuilders
                .get("/project/exchange")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].currency", Matchers.is("dolar amerykański")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[1].currency", Matchers.is("dolar australijski")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].code", Matchers.is("USD")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[1].code", Matchers.is("AUD")));
    }
    @Test
    public void ExchangeControllerBuyCurrency() throws Exception {
        //GIVEN
        List<CurrencyDTO> currencyDTOList = new ArrayList<>();
        currencyDTOList.add(new CurrencyDTO("nowy złoty polski","PLN", new BigDecimal(1000.0)));
        currencyDTOList.add(new CurrencyDTO("dolar amerykański","USD", new BigDecimal(1000.0)));
        when(exchangeFacade.buyCurrency(any(), any(), any())).thenReturn(currencyDTOList);

        //WHEN and THEN
        mockMvc.perform(MockMvcRequestBuilders
                .put("/project/exchange/buy?userId=1&currency_Code=USD&value=1000")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].currencyName", Matchers.is("nowy złoty polski")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[1].currencyName", Matchers.is("dolar amerykański")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].currencyCode", Matchers.is("PLN")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[1].currencyCode", Matchers.is("USD")));
    }
    @Test
    public void ExchangeControllerSellCurrency() throws Exception {
        //GIVEN
        List<CurrencyDTO> currencyDTOList = new ArrayList<>();
        currencyDTOList.add(new CurrencyDTO("nowy złoty polski", "PLN", new BigDecimal(5000.0)));
        currencyDTOList.add(new CurrencyDTO("dolar amerykański", "USD", new BigDecimal(0.0)));
        when(exchangeFacade.sellCurrency(any(), any(), any())).thenReturn(currencyDTOList);

        //WHEN and THEN
        mockMvc.perform(MockMvcRequestBuilders
                .put("/project/exchange/sell?userId=1&currency_Code=USD&value=1000")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].currencyName", Matchers.is("nowy złoty polski")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[1].currencyName", Matchers.is("dolar amerykański")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].currencyCode", Matchers.is("PLN")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[1].currencyCode", Matchers.is("USD")));
    }
}

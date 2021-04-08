package com.kodilla.finalproject.controller;

import com.kodilla.finalproject.domain.*;
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
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringJUnitWebConfig
@WebMvcTest(ExchangeControler.class)
class ExchangeControlerTest {
    @Autowired
    private MockMvc mockMvc;
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
    public void ExchangeControllerGetCurrencyUserExist() throws Exception {
        //GIVEN
        List<Currency> currencyList = new ArrayList<>();
        Optional<User> searchedUser = Optional.ofNullable(new User(1L, "Mateusz", currencyList));
        currencyList.add(new Currency("nowy złoty polski","PLN", new BigDecimal(0.0), searchedUser.get()));
        when(userDatabase.findUser(any())).thenReturn(searchedUser);
        List<CurrencyDTO> currencyDTOList = new ArrayList<>();
        currencyDTOList.add(new CurrencyDTO("nowy złoty polski","PLN", new BigDecimal(0.0)));
        when(currencyMapper.maptoListCurrencyDTO(any())).thenReturn(currencyDTOList);

        //WHEN and THEN
        mockMvc.perform(MockMvcRequestBuilders
                .get("/project/currency/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(1)));
    }
    @Test
    public void ExchangeControllerGetCurrencyUserNotExist() throws Exception {
        //GIVEN
        Optional<User> searchedUser = Optional.ofNullable(null);
        when(userDatabase.findUser(any())).thenReturn(searchedUser);
        List<CurrencyDTO> currencyDTOList = new ArrayList<>();
        currencyDTOList.add(new CurrencyDTO("nowy złoty polski", "PLN", new BigDecimal(0.0)));
        when(currencyMapper.maptoListCurrencyDTO(any())).thenReturn(currencyDTOList);

        //WHEN and THEN
        mockMvc.perform(MockMvcRequestBuilders
                .get("/project/currency/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(0)));
    }
    @Test
    public void ExchangeControllerNewCurrency() throws Exception {
        //GIVEN
        List<Currency> currencyList = new ArrayList<>();
        Optional<User> searchedUser = Optional.ofNullable(new User(1L, "Mateusz", currencyList));
        currencyList.add(new Currency("nowy złoty polski", "PLN", new BigDecimal(0.0), searchedUser.get()));
        when(userDatabase.findUser(any())).thenReturn(searchedUser);
        List<CurrencyDTO> currencyDTOList = new ArrayList<>();
        currencyDTOList.add(new CurrencyDTO("nowy złoty polski", "PLN", new BigDecimal(0.0)));
        when(currencyMapper.maptoListCurrencyDTO(any())).thenReturn(currencyDTOList);
        when(currencyDatabase.save(any())).thenReturn(new Currency("dolar amerykański", "USD", new BigDecimal(0.0), searchedUser.get()));
        when(currencyMapper.mapTocurrencyName(any())).thenReturn("dolar amerykański");
        when(currencyMapper.maptoCurrencyDTO(any())).thenReturn(new CurrencyDTO("dolar amerykański","USD", new BigDecimal(0.0)));

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
        List<Currency> currencyList = new ArrayList<>();
        Optional<User> searchedUser = Optional.ofNullable(new User(1L, "Mateusz", currencyList));
        currencyList.add(new Currency("nowy złoty polski", "PLN", new BigDecimal(0.0), searchedUser.get()));
        when(userDatabase.findUser(any())).thenReturn(searchedUser);
        List<CurrencyDTO> currencyDTOList = new ArrayList<>();
        currencyDTOList.add(new CurrencyDTO("nowy złoty polski", "PLN", new BigDecimal(0.0)));

        //WHEN and THEN
        mockMvc.perform(MockMvcRequestBuilders
                .delete("/project/currency?userId=1&currency_Code=PLN")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is(200));
    }
    @Test
    public void ExchangeControllerTopUpAccount() throws Exception {
        //GIVEN
        List<Currency> currencyList = new ArrayList<>();
        Optional<User> searchedUser = Optional.ofNullable(new User(1L, "Mateusz", currencyList));
        currencyList.add(new Currency("dolar amerykański", "USD", new BigDecimal(0.0), searchedUser.get()));
        when(userDatabase.findUser(any())).thenReturn(searchedUser);
        when(currencyDatabase.save(any())).thenReturn(new Currency("dolar amerykański", "USD", new BigDecimal(1000.0), searchedUser.get()));
        when(currencyMapper.maptoCurrencyDTO(any())).thenReturn(new CurrencyDTO("dolar amerykański","USD", new BigDecimal(1000.0)));

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
        List<Currency> currencyList = new ArrayList<>();
        Optional<User> searchedUser = Optional.ofNullable(new User(1L, "Mateusz", currencyList));
        currencyList.add(new Currency("dolar amerykański", "USD", new BigDecimal(1000.0), searchedUser.get()));
        when(userDatabase.findUser(any())).thenReturn(searchedUser);
        when(currencyDatabase.save(any())).thenReturn(new Currency("dolar amerykański", "USD", new BigDecimal(0.0), searchedUser.get()));
        when(currencyMapper.maptoCurrencyDTO(any())).thenReturn(new CurrencyDTO("dolar amerykański", "USD", new BigDecimal(0.0)));

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
        List<RateOfExchangeDTO> dtoList = new ArrayList<>();
        dtoList.add(new RateOfExchangeDTO("dolar amerykański", "USD", 3.1234, 3.5678));
        dtoList.add(new RateOfExchangeDTO("dolar australijski", "AUD", 2.1234, 2.5678));
        when(dataOfExchangeMapper.maptoListRateOfExchangeDTO(any())).thenReturn(dtoList);
        List<RateOfExchange> rList = new ArrayList<>();
        DataOfExchange data1 = new DataOfExchange(LocalDate.parse("2021-04-08"),rList );
        rList.add(new RateOfExchange("dolar amerykański", "USD", 3.1234, 3.5678, data1));
        rList.add(new RateOfExchange("dolar australijski", "AUD", 2.1234, 2.5678, data1));
        List<DataOfExchange> dList = new ArrayList<>();
        dList.add(new DataOfExchange(LocalDate.parse("2021-04-08"), rList));
        when(dataOfExchangeDatabase.findAll()).thenReturn(dList);
        when(dataOfExchangeDatabase.save(any())).thenReturn(data1);
        when(dataOfExchangeMapper.maptoDataOfExchange(any(), any())).thenReturn(data1);
        RateOfExchangeDTO[] rate = new RateOfExchangeDTO[2];
        rate[0] = new RateOfExchangeDTO("dolar amerykański", "USD", 3.1234, 3.5678);
        rate[1] = new RateOfExchangeDTO("dolar australijski", "AUD", 2.1234, 2.5678);
        /*when(nbpClient.getActualNbpCurrency()).thenReturn(new DataOfExchangeDTO(LocalDate.parse("2021-04-08"),rate ));

        //WHEN and THEN
        mockMvc.perform(MockMvcRequestBuilders
                .get("/project/exchange")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is(200));*/
    }
}

package com.kodilla.finalproject.mappers;

import com.kodilla.finalproject.domain.Currency;
import com.kodilla.finalproject.domain.CurrencyDTO;
import com.kodilla.finalproject.domain.User;
import com.kodilla.finalproject.mapper.CurrencyMapper;
import com.kodilla.finalproject.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class CurrencyMapperTestSuite {
    @Autowired
    private CurrencyMapper currencyMapper;
    @Autowired
    private UserRepository userRepository;

    @Test
    public void TestCurrencyMapperMaptoCurrency(){
        //GIVE
        CurrencyDTO currencyDTO = new CurrencyDTO("nowy złoty polski","PLN", new BigDecimal(0.0));
        List<Currency> currencyList = new ArrayList<>();
        User user = new User("Marcin", currencyList );
        userRepository.save(user);
        Long ID = user.getUserID();
        //WHEN
        Currency mappedCurrency = currencyMapper.maptoCurrency(currencyDTO , ID);
        //THEN
        assertEquals("nowy złoty polski", mappedCurrency.getCurrencyName());
        assertEquals("PLN", mappedCurrency.getCurrencyCode());
        assertEquals(ID, mappedCurrency.getUser().getUserID());
        //CLEAN
        userRepository.deleteById(ID);
    }
    @Test
    public void TestCurrencyMapperMaptoCurrencyDTO(){
        //GIVE
        Currency currency = new Currency("dolar amerykański", "USD", new BigDecimal(0.0) , null);
        //WHEN
        CurrencyDTO mappedCurrencyDTO = currencyMapper.maptoCurrencyDTO(currency);
        //THEN
        assertEquals("dolar amerykański", mappedCurrencyDTO.getCurrencyName());
        assertEquals("USD", mappedCurrencyDTO.getCurrencyCode());
    }
    @Test
    public void TestCurrencyMapperMaptoListCurrencyDTO() {
        //GIVE
        List<Currency> currencyList = new ArrayList<>();
        currencyList.add(new Currency("dolar australijski", "AUD", new BigDecimal(0.0), null));
        currencyList.add(new Currency("dolar kanadyjski", "CAD", new BigDecimal(0.0), null));

        //WHEN
        List<CurrencyDTO> mappedListCurrencyDTO = currencyMapper.maptoListCurrencyDTO(currencyList);

        //THEN
        assertEquals(2, mappedListCurrencyDTO.size());
        assertEquals("AUD", mappedListCurrencyDTO.get(0).getCurrencyCode());
        assertEquals("dolar australijski", mappedListCurrencyDTO.get(0).getCurrencyName());
        assertEquals("CAD", mappedListCurrencyDTO.get(1).getCurrencyCode());
        assertEquals("dolar kanadyjski", mappedListCurrencyDTO.get(1).getCurrencyName());
    }
    @Test
    public void TestCurrencyMapperMapToCurrencyName() {
        //GIVE
        String[] currencyCodes = new String[15];
        currencyCodes[0] = "USD";
        currencyCodes[1] = "AUD";
        currencyCodes[2] = "CAD";
        currencyCodes[3] = "EUR";
        currencyCodes[4] = "HUF";
        currencyCodes[5] = "CHF";
        currencyCodes[6] = "GBP";
        currencyCodes[7] = "JPY";
        currencyCodes[8] = "CZK";
        currencyCodes[9] = "DKK";
        currencyCodes[10] = "NOK";
        currencyCodes[11] = "SEK";
        currencyCodes[12] = "XDR";
        currencyCodes[13] = "PLN";
        currencyCodes[14] = "ABC";

        //WHEN
        String[] currencyNames = new String[15];
        for(int i=0; i<15; i++){
            currencyNames[i] = currencyMapper.mapTocurrencyName(currencyCodes[i]);
        }
        //THEN
        assertEquals("dolar amerykański", currencyNames[0]);
        assertEquals("dolar australijski", currencyNames[1]);
        assertEquals("dolar kanadyjski", currencyNames[2]);
        assertEquals("euro", currencyNames[3]);
        assertEquals("forint (Węgry)", currencyNames[4]);
        assertEquals("frank szwajcarski", currencyNames[5]);
        assertEquals("funt szterling", currencyNames[6]);
        assertEquals("jen (Japonia)", currencyNames[7]);
        assertEquals("korona czeska", currencyNames[8]);
        assertEquals("korona duńska", currencyNames[9]);
        assertEquals("korona norweska", currencyNames[10]);
        assertEquals("korona szwedzka", currencyNames[11]);
        assertEquals("SDR (MFW)", currencyNames[12]);
        assertEquals("nowy złoty polski", currencyNames[13]);
        assertEquals("", currencyNames[14]);
    }
}

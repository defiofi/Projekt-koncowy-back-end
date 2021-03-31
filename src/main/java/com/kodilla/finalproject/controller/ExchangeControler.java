package com.kodilla.finalproject.controller;

import com.kodilla.finalproject.domain.CurrencyDTO;
import com.kodilla.finalproject.domain.RateOfExchangeDTO;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/project")
public class ExchangeControler {
    @RequestMapping(method = RequestMethod.GET, value = "/currency/{userID}")
    public List<CurrencyDTO> getCurrency(@PathVariable Long userID) throws CurrencyNotFoundException {
        // DO NAPISANIA WLASCIWA METODA !!
        List<CurrencyDTO> currencyDTOList = new ArrayList<>();
        currencyDTOList.add(new CurrencyDTO("PLN", new BigDecimal(1000.0)));
        return currencyDTOList;
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/currency/topUp")
    public List<CurrencyDTO> topUpAccount(@RequestParam Long userId, @RequestParam String currencyCode, @RequestParam Double value) {
        // DO NAPISANIA WLASCIWA METODA !!
        List<CurrencyDTO> currencyDTOList = new ArrayList<>();
        currencyDTOList.add(new CurrencyDTO(currencyCode, new BigDecimal(value)));
        return currencyDTOList;
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/currency/payOut")
    public List<CurrencyDTO> payOutAccount(@RequestParam Long userId, @RequestParam String currencyCode, @RequestParam Double value) {
        // DO NAPISANIA WLASCIWA METODA !!
        List<CurrencyDTO> currencyDTOList = new ArrayList<>();
        currencyDTOList.add(new CurrencyDTO(currencyCode, new BigDecimal(0.0)));
        return currencyDTOList;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/exchange")
    public List<RateOfExchangeDTO> getActualRates() {
        // DO NAPISANIA WLASCIWA METODA !!
        List<RateOfExchangeDTO> rateList = new ArrayList<>();
        rateList.add(new RateOfExchangeDTO("dolar amerykański", "USD", 3.1415, 3.4567));
        return rateList;
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/exchange/buy")
    public List<CurrencyDTO> buyCurrency(@RequestParam Long userId, @RequestParam String currencyCode, @RequestParam Double value) {
        // DO NAPISANIA WLASCIWA METODA !!
        List<CurrencyDTO> currencyDTOList = new ArrayList<>();
        currencyDTOList.add(new CurrencyDTO("PLN", new BigDecimal(1000.0)
                .subtract(new BigDecimal(value).multiply(new BigDecimal(3.0)))));
        currencyDTOList.add(new CurrencyDTO(currencyCode, new BigDecimal(value)));
        return currencyDTOList;
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/exchange/sell")
    public List<CurrencyDTO> sellCurrency(@RequestParam Long userId, @RequestParam String currencyCode, @RequestParam Double value) {
        // DO NAPISANIA WLASCIWA METODA !!
        List<CurrencyDTO> currencyDTOList = new ArrayList<>();
        currencyDTOList.add(new CurrencyDTO("PLN", new BigDecimal(1000.0)
                .add(new BigDecimal(value).multiply(new BigDecimal(0.33)))));
        currencyDTOList.add(new CurrencyDTO(currencyCode, new BigDecimal(0.0)));
        return currencyDTOList;
    }
}

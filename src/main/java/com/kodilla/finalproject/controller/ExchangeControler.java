package com.kodilla.finalproject.controller;

import com.kodilla.finalproject.domain.*;
import com.kodilla.finalproject.facade.ExchangeFacade;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/project")
public class ExchangeControler {

    private final ExchangeFacade exchangeFacade;

    public ExchangeControler(ExchangeFacade exchangeFacade){
        this.exchangeFacade = exchangeFacade;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/currency/{userId}")
    public List<CurrencyDTO> getCurrency(@PathVariable Long userId) {
        return exchangeFacade.getCurrency(userId);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/currency")
    public CurrencyDTO NewCurrency(
            @RequestParam Long userId,
            @RequestParam String currency_Code) throws UserNotFoundException {
        return exchangeFacade.NewCurrency(userId, currency_Code);
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/currency")
    public void DeleteCurrency(
            @RequestParam Long userId,
            @RequestParam String currency_Code) {
        exchangeFacade.DeleteCurrency(userId, currency_Code);
    }
    @RequestMapping(method = RequestMethod.PUT, value = "/currency/topUp")
    public CurrencyDTO topUpAccount(
            @RequestParam Long userId,
            @RequestParam String currency_Code,
            @RequestParam Double value) throws UserNotFoundException {
        return exchangeFacade.topUpAccount(userId, currency_Code, value);
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/currency/payOut")
    public CurrencyDTO payOutAccount(
            @RequestParam Long userId,
            @RequestParam String currency_Code,
            @RequestParam Double value) throws UserNotFoundException{
        return exchangeFacade.payOutAccount(userId, currency_Code, value);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/exchange")
    public List<RateOfExchangeDTO> getActualRates() {
        return exchangeFacade.getActualRates();
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/exchange/buy")
    public List<CurrencyDTO> buyCurrency(
            @RequestParam Long userId,
            @RequestParam String currency_Code,
            @RequestParam Double value) throws UserNotFoundException{
        return exchangeFacade.buyCurrency(userId, currency_Code, value);
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/exchange/sell")
    public List<CurrencyDTO> sellCurrency(
            @RequestParam Long userId,
            @RequestParam String currency_Code,
            @RequestParam Double value) throws UserNotFoundException {
        return exchangeFacade.sellCurrency(userId, currency_Code, value);
    }
}

package com.kodilla.finalproject.controller;

import com.kodilla.finalproject.domain.*;
import com.kodilla.finalproject.mapper.CurrencyMapper;
import com.kodilla.finalproject.service.CurrencyDatabase;
import com.kodilla.finalproject.service.UserDatabase;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/project")
public class ExchangeControler {

    private final UserDatabase userDatabase;
    private final CurrencyMapper currencyMapper;
    private final CurrencyDatabase currencyDatabase;

    public ExchangeControler(UserDatabase userDatabase, CurrencyMapper currencyMapper, CurrencyDatabase currencyDatabase){
        this.userDatabase = userDatabase;
        this.currencyMapper = currencyMapper;
        this.currencyDatabase = currencyDatabase;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/currency/{userId}")
    public List<CurrencyDTO> getCurrency(@PathVariable Long userId) {
        if(userDatabase.findUser(userId).isPresent()){
            return currencyMapper.maptoListCurrencyDTO(userDatabase.findUser(userId).get().getCurrency());
        }
            List<CurrencyDTO> list = new ArrayList<>();
            return list;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/currency")
    public CurrencyDTO NewCurrency(
            @RequestParam Long userId,
            @RequestParam String currency_Code) throws UserNotFoundException {
        Optional<User> searchedUser = userDatabase.findUser(userId);
        if(searchedUser.isPresent()){
            List<Currency> searchedCurrencyList = searchedUser.get().getCurrency().stream()
                    .filter(c -> c.getCurrencyCode().equals(currency_Code))
                    .collect(Collectors.toList());
            if(searchedCurrencyList.size() == 0){
                Currency newCurrency = currencyDatabase.save(new Currency(
                        currencyMapper.mapTocurrencyName(currency_Code),
                        currency_Code ,
                        new BigDecimal(0.0),
                        searchedUser.get()));
                return currencyMapper.maptoCurrencyDTO(newCurrency);
            } else {
                return currencyMapper.maptoCurrencyDTO(searchedCurrencyList.get(0));
            }
        } else{ throw new UserNotFoundException();}
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/currency/topUp")
    public CurrencyDTO topUpAccount(
            @RequestParam Long userId,
            @RequestParam String currency_Code,
            @RequestParam Double value) throws UserNotFoundException {

        Optional<User> searchedUser = userDatabase.findUser(userId);
        if(searchedUser.isPresent()){
            List<Currency> searchedCurrencyList = searchedUser.get().getCurrency().stream()
                    .filter(c -> c.getCurrencyCode().equals(currency_Code))
                    .collect(Collectors.toList());
            if(searchedCurrencyList.size() == 0){
                return currencyMapper.maptoCurrencyDTO(new Currency(null,null,null,null));
            } else {
                Currency topUpCurrency = currencyDatabase.save(new Currency(
                        searchedCurrencyList.get(0).getCurrencyID(),
                        searchedCurrencyList.get(0).getCurrencyName(),
                        searchedCurrencyList.get(0).getCurrencyCode(),
                        new BigDecimal(value).add(searchedCurrencyList.get(0).getAccount()),
                        searchedCurrencyList.get(0).getUser()));
                return currencyMapper.maptoCurrencyDTO(topUpCurrency);
            }
        } else{ throw new UserNotFoundException();}
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/currency/payOut")
    public CurrencyDTO payOutAccount(
            @RequestParam Long userId,
            @RequestParam String currency_Code,
            @RequestParam Double value) throws UserNotFoundException{
        Optional<User> searchedUser = userDatabase.findUser(userId);
        if(searchedUser.isPresent()){
            List<Currency> searchedCurrencyList = searchedUser.get().getCurrency().stream()
                    .filter(c -> c.getCurrencyCode().equals(currency_Code))
                    .collect(Collectors.toList());
            if(searchedCurrencyList.size() == 0){
                return currencyMapper.maptoCurrencyDTO(new Currency(null,null,null,null));
            } else if(searchedCurrencyList.get(0).getAccount().compareTo( new BigDecimal(value))>=0) {
                Currency payOutCurrency = currencyDatabase.save(new Currency(
                        searchedCurrencyList.get(0).getCurrencyID(),
                        searchedCurrencyList.get(0).getCurrencyName(),
                        searchedCurrencyList.get(0).getCurrencyCode(),
                        searchedCurrencyList.get(0).getAccount().subtract(new BigDecimal(value)),
                        searchedCurrencyList.get(0).getUser()));
                return currencyMapper.maptoCurrencyDTO(payOutCurrency);
            } else {
                return currencyMapper.maptoCurrencyDTO(new Currency(null,null,null,null));
            }
        } else{ throw new UserNotFoundException();}
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
        currencyDTOList.add(new CurrencyDTO("","PLN", new BigDecimal(1000.0)
                .subtract(new BigDecimal(value).multiply(new BigDecimal(3.0)))));
        currencyDTOList.add(new CurrencyDTO("", currencyCode, new BigDecimal(value)));
        return currencyDTOList;
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/exchange/sell")
    public List<CurrencyDTO> sellCurrency(@RequestParam Long userId, @RequestParam String currencyCode, @RequestParam Double value) {
        // DO NAPISANIA WLASCIWA METODA !!
        List<CurrencyDTO> currencyDTOList = new ArrayList<>();
        currencyDTOList.add(new CurrencyDTO("","PLN", new BigDecimal(1000.0)
                .add(new BigDecimal(value).multiply(new BigDecimal(0.33)))));
        currencyDTOList.add(new CurrencyDTO("", currencyCode, new BigDecimal(0.0)));
        return currencyDTOList;
    }
}

package com.kodilla.finalproject.controller;

import com.kodilla.finalproject.domain.*;
import com.kodilla.finalproject.mapper.CurrencyMapper;
import com.kodilla.finalproject.mapper.DataOfExchangeMapper;
import com.kodilla.finalproject.nbp.client.NbpClient;
import com.kodilla.finalproject.repository.CurrencyRepository;
import com.kodilla.finalproject.service.CurrencyDatabase;
import com.kodilla.finalproject.service.DataOfExchangeDatabase;
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
    private final DataOfExchangeMapper dataOfExchangeMapper;
    private final CurrencyDatabase currencyDatabase;
    private final DataOfExchangeDatabase dataOfExchangeDatabase;
    private CurrencyRepository currencyRepository;
    private NbpClient nbpClient;

    public ExchangeControler(UserDatabase userDatabase,
                             CurrencyMapper currencyMapper,
                             DataOfExchangeMapper dataOfExchangeMapper,
                             CurrencyDatabase currencyDatabase,
                             DataOfExchangeDatabase dataOfExchangeDatabase,
                             NbpClient nbpClient,
                             CurrencyRepository currencyRepository){
        this.userDatabase = userDatabase;
        this.currencyMapper = currencyMapper;
        this.dataOfExchangeMapper = dataOfExchangeMapper;
        this.currencyDatabase = currencyDatabase;
        this.dataOfExchangeDatabase = dataOfExchangeDatabase;
        this.nbpClient = nbpClient;
        this.currencyRepository = currencyRepository;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/currency/{userId}")
    public List<CurrencyDTO> getCurrency(@PathVariable Long userId) {
        if(userDatabase.findUser(userId).isPresent()){
            return currencyMapper.maptoListCurrencyDTO(userDatabase.findUser(userId).get().getCurrency());
        }else {
            return new ArrayList<>();
        }
    }

    @RequestMapping(method = RequestMethod.POST, value = "/currency")
    public CurrencyDTO NewCurrency(
            @RequestParam Long userId,
            @RequestParam String currency_Code) throws UserNotFoundException {
        Optional<User> searchedUser = userDatabase.findUser(userId);
        if(searchedUser.isPresent()){
            Currency searchedCurrency = searchCurrency(searchedUser.orElse(new User()), currency_Code);
            if(searchedCurrency.getCurrencyID()!=null){
                return currencyMapper.maptoCurrencyDTO(searchedCurrency);
            }else{
                Currency newCurrency = currencyDatabase.save(new Currency(
                        currencyMapper.mapTocurrencyName(currency_Code),
                        currency_Code ,
                        new BigDecimal(0.0),
                        searchedUser.get()));

                return currencyMapper.maptoCurrencyDTO(newCurrency);
            }
        } else{ throw new UserNotFoundException();}
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/currency")
    public void DeleteCurrency(
            @RequestParam Long userId,
            @RequestParam String currency_Code) throws UserNotFoundException {
        Optional<User> searchedUser = userDatabase.findUser(userId);
        Currency searchedCurrency = searchCurrency(searchedUser.orElse(new User()), currency_Code);
        currencyRepository.deleteById(searchedCurrency.getCurrencyID());
    }
    @RequestMapping(method = RequestMethod.PUT, value = "/currency/topUp")
    public CurrencyDTO topUpAccount(
            @RequestParam Long userId,
            @RequestParam String currency_Code,
            @RequestParam Double value) throws UserNotFoundException {

        Optional<User> searchedUser = userDatabase.findUser(userId);
        if(searchedUser.isPresent()){
            Currency searchedCurrency = searchCurrency(searchedUser.orElse(new User()), currency_Code);
            if(searchedCurrency.getCurrencyID()!=null){
                Currency topUpCurrency = currencyDatabase.save(new Currency(
                        searchedCurrency.getCurrencyID(),
                        searchedCurrency.getCurrencyName(),
                        searchedCurrency.getCurrencyCode(),
                        new BigDecimal(value).add(searchedCurrency.getAccount()),
                        searchedCurrency.getUser()));
                return currencyMapper.maptoCurrencyDTO(topUpCurrency);
            } else {return currencyMapper.maptoCurrencyDTO(new Currency(null,null,null,null));}
        } else{ throw new UserNotFoundException();}
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/currency/payOut")
    public CurrencyDTO payOutAccount(
            @RequestParam Long userId,
            @RequestParam String currency_Code,
            @RequestParam Double value) throws UserNotFoundException{
        Optional<User> searchedUser = userDatabase.findUser(userId);
        if(searchedUser.isPresent()){
            Currency searchedCurrency = searchCurrency(searchedUser.orElse(new User()), currency_Code);
            if(searchedCurrency.getCurrencyID()!=null){
                Currency payOutCurrency = currencyDatabase.save(new Currency(
                        searchedCurrency.getCurrencyID(),
                        searchedCurrency.getCurrencyName(),
                        searchedCurrency.getCurrencyCode(),
                        searchedCurrency.getAccount().subtract(new BigDecimal(value)),
                        searchedCurrency.getUser()));
                return currencyMapper.maptoCurrencyDTO(payOutCurrency);
            } else {return currencyMapper.maptoCurrencyDTO(new Currency(null,null,null,null));}
        } else{ throw new UserNotFoundException();}
    }

    @RequestMapping(method = RequestMethod.GET, value = "/exchange")
    public List<RateOfExchangeDTO> getActualRates() {
        DataOfExchange rates = checkRates();
            return dataOfExchangeMapper.maptoListRateOfExchangeDTO(rates.getRateOfExchange());
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/exchange/buy")
    public List<CurrencyDTO> buyCurrency(@RequestParam Long userId, @RequestParam String currency_Code, @RequestParam Double value) throws UserNotFoundException{
        /** Opis działania metody:
         * -Sprawdzenie czy kursy walut są aktualne, jak nie to ich pobranie, znalezienie odpowiedniego kursu wymiany waluty.
         * -Odnalezienie danych użytkownika ( odnalezienie konta złotówkowego i kupowanej waluty).
         * -sprawdzenie czy użytkownik może zakupic walutę w żądanej ilości, jeżeli tak to wykonanie transakcji*/
        DataOfExchange rates = checkRates();
        RateOfExchange currencyRate = searchRate(rates, currency_Code);
        Optional<User> searchedUser = userDatabase.findUser(userId);
        if(searchedUser.isEmpty()){
            throw new UserNotFoundException();
        }
        Currency userPLN = searchCurrency(searchedUser.orElse(new User()), "PLN");
        Currency boughtCurrency = searchCurrency(searchedUser.orElse(new User()), currency_Code);
        if(userPLN.getAccount().subtract(new BigDecimal(currencyRate.getAsk()).multiply(new BigDecimal(value))).compareTo(new BigDecimal(0.0))<0){
            return currencyMapper.maptoListCurrencyDTO(searchedUser.get().getCurrency());
        } else{
            currencyDatabase.save(new Currency(boughtCurrency.getCurrencyID(),
                    boughtCurrency.getCurrencyName(),
                    boughtCurrency.getCurrencyCode(),
                    new BigDecimal(value).add(boughtCurrency.getAccount()),
                    boughtCurrency.getUser()));
            currencyDatabase.save(new Currency(userPLN.getCurrencyID(),
                    userPLN.getCurrencyName(),
                    userPLN.getCurrencyCode(),
                    userPLN.getAccount().subtract(new BigDecimal(currencyRate.getAsk()).multiply(new BigDecimal(value))),
                    userPLN.getUser()));
            return currencyMapper.maptoListCurrencyDTO(searchedUser.get().getCurrency());
        }
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/exchange/sell")
    public List<CurrencyDTO> sellCurrency(@RequestParam Long userId, @RequestParam String currency_Code, @RequestParam Double value) throws UserNotFoundException{
        /** Opis działania metody:
         * -Sprawdzenie czy kursy walut są aktualne, jak nie to ich pobranie, znalezienie odpowiedniego kursu wymiany waluty.
         * -Odnalezienie danych użytkownika ( odnalezienie konta złotówkowego i sprzedawanej waluty).
         * -sprawdzenie czy użytkownik może sprzedać walutę w żądanej ilości, jeżeli tak to wykonanie transakcji*/
        DataOfExchange rates = checkRates();
        RateOfExchange currencyRate = searchRate(rates, currency_Code);
        Optional<User> searchedUser = userDatabase.findUser(userId);
        if(searchedUser.isEmpty()) {
            throw new UserNotFoundException();
        }
        Currency userPLN = searchCurrency(searchedUser.orElse(new User()), "PLN");
        Currency soldCurrency = searchCurrency(searchedUser.orElse(new User()), currency_Code);
        if(soldCurrency.getAccount().subtract(new BigDecimal(value)).compareTo(new BigDecimal(0.0))<0){
            return currencyMapper.maptoListCurrencyDTO(searchedUser.get().getCurrency());
        }else{
            currencyDatabase.save(new Currency(soldCurrency.getCurrencyID(),
                    soldCurrency.getCurrencyName(),
                    soldCurrency.getCurrencyCode(),
                    soldCurrency.getAccount().subtract(new BigDecimal(value)),
                    soldCurrency.getUser()));
            currencyDatabase.save(new Currency(userPLN.getCurrencyID(),
                    userPLN.getCurrencyName(),
                    userPLN.getCurrencyCode(),
                    userPLN.getAccount().add(new BigDecimal(currencyRate.getBid()).multiply(new BigDecimal(value))),
                    userPLN.getUser()));
            return currencyMapper.maptoListCurrencyDTO(searchedUser.get().getCurrency());
        }
    }
    private DataOfExchange checkRates(){
        List<DataOfExchange> ratesList = dataOfExchangeDatabase.findAll().stream()
                .filter(d -> d.getTradingDate().compareTo(nbpClient.getActualNbpCurrency().getTradingDate())==0)
                .collect(Collectors.toList());
        if(ratesList.size()==0){
            DataOfExchange dataOfExchange = dataOfExchangeDatabase.save(new DataOfExchange(nbpClient.getActualNbpCurrency().getTradingDate(), null));
            DataOfExchange newDataOfExchange = dataOfExchangeMapper.maptoDataOfExchange(nbpClient.getActualNbpCurrency(), dataOfExchange.getDataID());
            ratesList.set(0, dataOfExchangeDatabase.save(newDataOfExchange));
        }
        return ratesList.get(0);
    }
    private RateOfExchange searchRate(DataOfExchange dataOfExchange, String currency_Code){
        List<RateOfExchange> list = dataOfExchange.getRateOfExchange().stream()
                .filter(r -> r.getCurrencyCode().equals(currency_Code))
                .collect(Collectors.toList());
        if(list.size()>0) {
            return list.get(0);
        } else {
            return new RateOfExchange();
        }
    }
    private Currency searchCurrency(User user, String currency_Code){
        List<Currency> list = user.getCurrency().stream()
                .filter(c -> c.getCurrencyCode().equals(currency_Code))
                .collect(Collectors.toList());
        if(list.size()>0) {
            return list.get(0);
        } else {
            return new Currency();
        }
    }
}

package com.kodilla.finalproject.facade;

import com.kodilla.finalproject.controller.UserNotFoundException;
import com.kodilla.finalproject.domain.*;
import com.kodilla.finalproject.mapper.CurrencyMapper;
import com.kodilla.finalproject.mapper.DataOfExchangeMapper;
import com.kodilla.finalproject.nbp.client.NbpClient;
import com.kodilla.finalproject.repository.CurrencyRepository;
import com.kodilla.finalproject.service.CurrencyDatabase;
import com.kodilla.finalproject.service.DataOfExchangeDatabase;
import com.kodilla.finalproject.service.UserDatabase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class ExchangeFacade {
    @Autowired
    private UserDatabase userDatabase;
    @Autowired
    private CurrencyMapper currencyMapper;
    @Autowired
    private CurrencyDatabase currencyDatabase;
    @Autowired
    private CurrencyRepository currencyRepository;
    @Autowired
    private DataOfExchangeMapper dataOfExchangeMapper;
    @Autowired
    private DataOfExchangeDatabase dataOfExchangeDatabase;
    @Autowired
    private NbpClient nbpClient;

    public List<CurrencyDTO> getCurrency(Long userId){
        if(userDatabase.findUser(userId).isPresent()){
            return currencyMapper.maptoListCurrencyDTO(userDatabase.findUser(userId).get().getCurrency());
        }else {
            return new ArrayList<>();
        }
    }
    public CurrencyDTO NewCurrency(Long userId, String currency_Code) throws UserNotFoundException{
        Optional<User> searchedUser = userDatabase.findUser(userId);
        if(searchedUser.isPresent()){
            Currency searchedCurrency = searchCurrency(searchedUser.get(), currency_Code);
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
    public void DeleteCurrency(Long userId, String currency_Code){
        Optional<User> searchedUser = userDatabase.findUser(userId);
        Currency searchedCurrency = searchCurrency(searchedUser.orElse(new User()), currency_Code);
        currencyRepository.delete(searchedCurrency);
    }
    public CurrencyDTO topUpAccount(Long userId, String currency_Code, Double value) throws UserNotFoundException{
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
    public CurrencyDTO payOutAccount(Long userId, String currency_Code, Double value) throws UserNotFoundException{
        Optional<User> searchedUser = userDatabase.findUser(userId);
        if(searchedUser.isPresent()){
            Currency searchedCurrency = searchCurrency(searchedUser.orElse(new User()), currency_Code);
            if(searchedCurrency.getCurrencyID()!=null && searchedCurrency.getAccount().compareTo(BigDecimal.valueOf(value))>=0){

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
    public List<RateOfExchangeDTO> getActualRates(){
        DataOfExchange rates = checkRates();
        return dataOfExchangeMapper.maptoListRateOfExchangeDTO(rates.getRateOfExchange());
    }
    public List<CurrencyDTO> buyCurrency(Long userId, String currency_Code, Double value) throws UserNotFoundException{
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
        if(userPLN.getAccount().subtract(new BigDecimal(currencyRate.getAsk()).multiply(new BigDecimal(value))).compareTo(new BigDecimal(0.0))<=0){
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
    public List<CurrencyDTO> sellCurrency(Long userId, String currency_Code, Double value) throws UserNotFoundException{
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
        if(soldCurrency.getAccount().subtract(new BigDecimal(value)).compareTo(new BigDecimal(0.0))<=0){
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
    public DataOfExchange checkRates(){
        List<DataOfExchange> ratesList = dataOfExchangeDatabase.findAll().stream()
                .filter(d -> d.getTradingDate().compareTo(nbpClient.getActualNbpCurrency().getTradingDate())==0)
                .collect(Collectors.toList());
        if(ratesList.size()==0){
            List<RateOfExchange> rateOfExchangeList = new ArrayList<>();
            DataOfExchange dataOfExchange = dataOfExchangeDatabase.save(
                    new DataOfExchange(
                            nbpClient.getActualNbpCurrency().getTradingDate(),
                            rateOfExchangeList)
            );
            DataOfExchange newDataOfExchange = dataOfExchangeMapper.maptoDataOfExchange(
                    nbpClient.getActualNbpCurrency(),
                    dataOfExchange.getDataID()
            );
            ratesList.add(dataOfExchangeDatabase.save(newDataOfExchange));
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
}

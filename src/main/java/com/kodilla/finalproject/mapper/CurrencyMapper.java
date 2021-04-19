package com.kodilla.finalproject.mapper;

import com.kodilla.finalproject.controller.CurrencyNotFoundException;
import com.kodilla.finalproject.domain.Currency;
import com.kodilla.finalproject.domain.CurrencyCode;
import com.kodilla.finalproject.domain.CurrencyDTO;
import com.kodilla.finalproject.domain.User;
import com.kodilla.finalproject.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class CurrencyMapper {
    @Autowired
    UserRepository userRepository;
    private Map<CurrencyCode, String> currencyMap;
    public CurrencyMapper(){
        this.currencyMap = new HashMap<>();
        currencyMap.put(CurrencyCode.USD , "dolar amerykański");
        currencyMap.put(CurrencyCode.AUD , "dolar australijski");
        currencyMap.put(CurrencyCode.CAD , "dolar kanadyjski");
        currencyMap.put(CurrencyCode.EUR , "euro");
        currencyMap.put(CurrencyCode.HUF , "forint (Węgry)");
        currencyMap.put(CurrencyCode.CHF , "frank szwajcarski");
        currencyMap.put(CurrencyCode.GBP , "funt szterling");
        currencyMap.put(CurrencyCode.JPY , "jen (Japonia)");
        currencyMap.put(CurrencyCode.CZK , "korona czeska");
        currencyMap.put(CurrencyCode.DKK , "korona duńska");
        currencyMap.put(CurrencyCode.NOK , "korona norweska");
        currencyMap.put(CurrencyCode.SEK , "korona szwedzka");
        currencyMap.put(CurrencyCode.XDR , "SDR (MFW)");
        currencyMap.put(CurrencyCode.PLN , "nowy złoty polski");
    }

    public Currency maptoCurrency(CurrencyDTO currencyDTO, Long userID){
        User user = userRepository.findById(userID).orElse(new User());
            return new Currency(currencyDTO.getCurrencyName(), currencyDTO.getCurrencyCode(), currencyDTO.getAccount(), user);
    }
    public CurrencyDTO maptoCurrencyDTO(Currency currency){
        return new CurrencyDTO(currency.getCurrencyName(), currency.getCurrencyCode(), currency.getAccount());
    }
    public List<CurrencyDTO> maptoListCurrencyDTO(List<Currency> currencyList){
        List<CurrencyDTO> currencyDTOList = new ArrayList<>();
        for(Currency currency : currencyList)
        {
            currencyDTOList.add(maptoCurrencyDTO(currency));
        }
        return currencyDTOList;
    }
    private CurrencyCode maptoCurrencyCode(String code) throws CurrencyNotFoundException {
        if (code.equals("USD")){
            return CurrencyCode.USD;
        } else if (code.equals("AUD")){
            return CurrencyCode.AUD;
        } else if (code.equals("CAD")){
            return CurrencyCode.CAD;
        } else if (code.equals("EUR")){
            return CurrencyCode.EUR;
        } else if (code.equals("HUF")){
            return CurrencyCode.HUF;
        } else if (code.equals("CHF")){
            return CurrencyCode.CHF;
        } else if (code.equals("GBP")){
            return CurrencyCode.GBP;
        } else if (code.equals("JPY")){
            return CurrencyCode.JPY;
        } else if (code.equals("CZK")){
            return CurrencyCode.CZK;
        } else if (code.equals("DKK")){
            return CurrencyCode.DKK;
        } else if (code.equals("NOK")){
            return CurrencyCode.NOK;
        } else if (code.equals("SEK")){
            return CurrencyCode.SEK;
        } else if (code.equals("XDR")){
            return CurrencyCode.XDR;
        } else if (code.equals("PLN")){
            return CurrencyCode.PLN;
        } else {
            throw new CurrencyNotFoundException();
        }
    }
    public String mapTocurrencyName(String code){
        try {
           return currencyMap.get(maptoCurrencyCode(code));
        } catch (CurrencyNotFoundException e) {
            return "";
        }
    }
}

package com.kodilla.finalproject.scheduler;

import com.kodilla.finalproject.domain.DataOfExchange;
import com.kodilla.finalproject.mapper.DataOfExchangeMapper;
import com.kodilla.finalproject.nbp.client.NbpClient;
import com.kodilla.finalproject.service.DataOfExchangeDatabase;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class NbpScheduler {

    DataOfExchangeDatabase dataOfExchangeDatabase;
    NbpClient nbpClient;
    DataOfExchangeMapper dataOfExchangeMapper;

    @Scheduled(cron = "0 0 9 * * *")
    public void checkRates(){
        List<DataOfExchange> ratesList = dataOfExchangeDatabase.findAll().stream()
                .filter(d -> d.getTradingDate().compareTo(nbpClient.getActualNbpCurrency().getTradingDate())==0)
                .collect(Collectors.toList());
        if(ratesList.size()==0){
            DataOfExchange dataOfExchange = dataOfExchangeDatabase.save(
                    new DataOfExchange(nbpClient.getActualNbpCurrency().getTradingDate(), null));
            DataOfExchange newDataOfExchange = dataOfExchangeMapper.maptoDataOfExchange(
                    nbpClient.getActualNbpCurrency(), dataOfExchange.getDataID());
            dataOfExchangeDatabase.save(newDataOfExchange);
        }
    }
}

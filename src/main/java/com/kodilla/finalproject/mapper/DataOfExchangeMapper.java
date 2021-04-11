package com.kodilla.finalproject.mapper;

import com.kodilla.finalproject.domain.DataOfExchange;
import com.kodilla.finalproject.domain.DataOfExchangeDTO;
import com.kodilla.finalproject.domain.RateOfExchange;
import com.kodilla.finalproject.domain.RateOfExchangeDTO;
import com.kodilla.finalproject.repository.DataOFExchangeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DataOfExchangeMapper {
    @Autowired
    private DataOFExchangeRepository dataOFExchangeRepository;

    public DataOfExchangeMapper(DataOFExchangeRepository dataOFExchangeRepository){

        this.dataOFExchangeRepository = dataOFExchangeRepository;
    }
    public DataOfExchange maptoDataOfExchange(DataOfExchangeDTO dataOfExchangeDTO, Long dataId){
        return new DataOfExchange(dataId, dataOfExchangeDTO.getTradingDate(), maptoListRateOfExchange(dataOfExchangeDTO.getRates(), dataId));
    }
    private RateOfExchange maptoRateOfExchange(RateOfExchangeDTO rateOfExchangeDTO, Long dataId){
        return new RateOfExchange(rateOfExchangeDTO.getCurrencyName(),
                rateOfExchangeDTO.getCurrencyCode(),
                rateOfExchangeDTO.getBid(),
                rateOfExchangeDTO.getAsk(), dataOFExchangeRepository.findById(dataId).orElse(new DataOfExchange()));
    }
    public List<RateOfExchange> maptoListRateOfExchange(List<RateOfExchangeDTO> rateOfExchangeDTOList, Long dataId){
        List<RateOfExchange> list = new ArrayList<>();
        for(RateOfExchangeDTO rateOfExchangeDTO : rateOfExchangeDTOList) {
            list.add(maptoRateOfExchange(rateOfExchangeDTO, dataId));
        }
        return list;
    }
    public DataOfExchangeDTO maptoDataOfExchangeDTO(DataOfExchange dataOfExchange){
        RateOfExchangeDTO[] rateOfExchangeDTOs = new RateOfExchangeDTO[dataOfExchange.getRateOfExchange().size()];
        for(int i = 0 ; i< dataOfExchange.getRateOfExchange().size() ; i++){
            rateOfExchangeDTOs[i] = maptoRateOfExchangeDTO(dataOfExchange.getRateOfExchange().get(i));
        }
        return new DataOfExchangeDTO(dataOfExchange.getTradingDate(), rateOfExchangeDTOs );
    }
    public RateOfExchangeDTO maptoRateOfExchangeDTO(RateOfExchange rateOfExchange){
        return new RateOfExchangeDTO(
                rateOfExchange.getCurrencyName(),
                rateOfExchange.getCurrencyCode(),
                rateOfExchange.getBid(),
                rateOfExchange.getAsk());
    }
    public List<RateOfExchangeDTO> maptoListRateOfExchangeDTO(List<RateOfExchange> rateOfExchangeList){
        List<RateOfExchangeDTO> list = new ArrayList<>();
        for(RateOfExchange rateOfExchange : rateOfExchangeList){
            list.add(maptoRateOfExchangeDTO(rateOfExchange));
        }
        return list;
    }
}

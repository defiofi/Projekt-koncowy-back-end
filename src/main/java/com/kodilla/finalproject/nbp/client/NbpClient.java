package com.kodilla.finalproject.nbp.client;

import com.kodilla.finalproject.domain.DataOfExchangeDTO;
import com.kodilla.finalproject.nbp.config.NbpConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.time.LocalDate;
import java.util.*;

@Component
public class NbpClient {

    private final RestTemplate restTemplate;
    private final NbpConfig nbpConfig;

    public NbpClient(RestTemplateBuilder restTemplateBuilder, NbpConfig nbpConfig){
        this.restTemplate = restTemplateBuilder.build();
        this.nbpConfig = nbpConfig;
    }
    protected final Logger LOGGER = LoggerFactory.getLogger(NbpClient.class);

    private URI getURIBuild(String addHttp){
        URI uri = UriComponentsBuilder.fromHttpUrl(nbpConfig.getNbpApiEndPoint() + addHttp)
                .queryParam("fields", "currency", "code", "bid", "ask", "tradingDate")
                .build()
                .encode()
                .toUri();
        return uri;
    }

    public DataOfExchangeDTO getActualNbpCurrency(){
        URI uri = getURIBuild("/exchangerates/tables/C/");
            DataOfExchangeDTO[] nbpResponse = restTemplate.getForObject(uri , DataOfExchangeDTO[].class);
            List<DataOfExchangeDTO> lista = Optional.ofNullable(nbpResponse)
                    .map(Arrays::asList)
                    .orElse(Collections.emptyList());
            return lista.get(0);
    }
    public List<DataOfExchangeDTO> getNbpCurrencyByDate(LocalDate start , LocalDate end){
        URI uri = getURIBuild("/exchangerates/tables/C/" + start + "/"+ end +"/");
        DataOfExchangeDTO[] nbpResponse = restTemplate.getForObject(uri , DataOfExchangeDTO[].class);
        return Optional.ofNullable(nbpResponse)
                .map(Arrays::asList)
                .orElse(Collections.emptyList());
    }
}

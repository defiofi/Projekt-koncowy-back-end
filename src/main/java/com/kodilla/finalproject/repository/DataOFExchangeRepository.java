package com.kodilla.finalproject.repository;

import com.kodilla.finalproject.domain.DataOfExchange;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface DataOFExchangeRepository extends CrudRepository<DataOfExchange , Long> {
    @Override
    public List<DataOfExchange> findAll();
}

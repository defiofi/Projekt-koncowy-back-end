package com.kodilla.finalproject.repository;

import com.kodilla.finalproject.domain.DataOfExchange;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DataOFExchangeRepository extends CrudRepository<DataOfExchange , Long> {
    @Override
    public List<DataOfExchange> findAll();
}

package com.kodilla.finalproject.repository;

import com.kodilla.finalproject.domain.RateOfExchange;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RateOfExchangeRepository extends CrudRepository<RateOfExchange , Long> {
}

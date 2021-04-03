package com.kodilla.finalproject.repository;

import com.kodilla.finalproject.domain.Currency;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CurrencyRepository extends CrudRepository<Currency, Long> {
    List<Currency> findByUser(Long aLong);
}

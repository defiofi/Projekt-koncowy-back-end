package com.kodilla.finalproject.repository;

import com.kodilla.finalproject.domain.Currency;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional
@Repository
public interface CurrencyRepository extends CrudRepository<Currency, Long> {

    Optional<Currency> findById(Long Id);

     void delete(Currency entity);

    void deleteById(Long currencyId);
}

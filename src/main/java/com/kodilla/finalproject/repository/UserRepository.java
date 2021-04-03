package com.kodilla.finalproject.repository;

import com.kodilla.finalproject.domain.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Long> {
    public List<User> findAll();
    public Optional<User> findUserByUserName(String userName);
}

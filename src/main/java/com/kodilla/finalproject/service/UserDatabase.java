package com.kodilla.finalproject.service;

import com.kodilla.finalproject.domain.User;
import com.kodilla.finalproject.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class UserDatabase {

    @Autowired
    private final UserRepository userRepository;

    public UserDatabase(UserRepository userRepository){
    this.userRepository = userRepository;
    }

    public User save(final User user){
        return userRepository.save(user);
    }
    public void deleteUser(final Long userID){
        userRepository.deleteById(userID);
    }
    public Optional<User> findUser(final Long userID) {return userRepository.findById(userID);}
    public Optional<User> findUserByName(final String userName){return userRepository.findUserByUserName(userName);}
    public List<User> showUsers(){
        return userRepository.findAll();
    }
}
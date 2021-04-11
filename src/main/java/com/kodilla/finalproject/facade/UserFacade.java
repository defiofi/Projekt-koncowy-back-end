package com.kodilla.finalproject.facade;

import com.kodilla.finalproject.controller.UserNotFoundException;
import com.kodilla.finalproject.domain.Currency;
import com.kodilla.finalproject.domain.User;
import com.kodilla.finalproject.domain.UserDTO;
import com.kodilla.finalproject.mapper.UserMapper;
import com.kodilla.finalproject.service.UserDatabase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class UserFacade {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UserDatabase userDatabase;

    public UserDTO getUser(Long userID) throws UserNotFoundException{
        return userMapper.maptoUserDTO(userDatabase.findUser(userID).orElseThrow(UserNotFoundException::new));
    }
    public List<UserDTO> getUsers(){
        return userMapper.mapToListUserDTO(userDatabase.showUsers());
    }
    public UserDTO createUser(UserDTO userDTO){
        if(userDatabase.findUserByName(userDTO.getName()).isPresent()){
            return userDTO;
        } else{
            List<Currency> currencyList = new ArrayList<>();
            User user = userDatabase.save(
                    new User(userDTO.getName(),
                            currencyList));
            return userMapper.maptoUserDTO(user);
        }
    }
    public UserDTO changeUserName(Long userId, String userName){
        User user = userDatabase.save(
                new User(userId,
                        userName,
                        userDatabase.findUser(userId).orElse(new User()).getCurrency()));
        return userMapper.maptoUserDTO(user);
    }
    public void deleteUser(Long userID){
        userDatabase.deleteUser(userID);
    }
}

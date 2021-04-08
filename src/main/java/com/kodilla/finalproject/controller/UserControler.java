package com.kodilla.finalproject.controller;

import com.kodilla.finalproject.domain.User;
import com.kodilla.finalproject.domain.UserDTO;
import com.kodilla.finalproject.mapper.CurrencyMapper;
import com.kodilla.finalproject.mapper.UserMapper;
import com.kodilla.finalproject.service.UserDatabase;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/project")
public class UserControler {

    private final UserDatabase userDatabase;
    private final UserMapper userMapper;

    public UserControler(UserDatabase userDatabase, UserMapper userMapper){
        this.userDatabase = userDatabase;
        this.userMapper = userMapper;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/user/{userID}")
    public UserDTO getUser(@PathVariable Long userID) throws UserNotFoundException{
            return userMapper.maptoUserDTO(userDatabase.findUser(userID).orElseThrow(UserNotFoundException::new));
    }
    @RequestMapping(method = RequestMethod.GET, value = "/user/*")
    public List<UserDTO> getUsers() {
        List<UserDTO> userDTOList = userMapper.mapToListUserDTO(userDatabase.showUsers());
        return  userDTOList;
    }
    @RequestMapping(method = RequestMethod.POST, value = "/user" , consumes = MediaType.APPLICATION_JSON_VALUE)
    public UserDTO createUser(@RequestBody UserDTO userDTO){
        if(userDatabase.findUserByName(userDTO.getName()).isPresent()){
            return userDTO;
        }
        User user = userDatabase.save(new User(userDTO.getName(), null));
        return userMapper.maptoUserDTO(user);
    }
    @RequestMapping(method = RequestMethod.PUT, value = "/user" )
    public UserDTO changeUserName(@RequestParam Long userId , @RequestParam String userName){
        User user = userDatabase.save(new User(userId, userName, userDatabase.findUser(userId).orElse(new User()).getCurrency()));
        return userMapper.maptoUserDTO(user);
    }
    @RequestMapping(method = RequestMethod.DELETE, value = "/user/{userID}")
    public void deleteUser(@PathVariable Long userID){
        userDatabase.deleteUser(userID);
    }
}
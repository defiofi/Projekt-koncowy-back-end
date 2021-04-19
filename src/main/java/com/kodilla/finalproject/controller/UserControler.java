package com.kodilla.finalproject.controller;

import com.kodilla.finalproject.domain.UserDTO;
import com.kodilla.finalproject.facade.UserFacade;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/project")
public class UserControler {

    private final UserFacade userFacade;

     public UserControler(UserFacade userFacade){
        this.userFacade = userFacade;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/user/{userID}")
    public UserDTO getUser(
            @PathVariable Long userID) throws UserNotFoundException{
        return userFacade.getUser(userID);
    }
    @RequestMapping(method = RequestMethod.GET, value = "/user/*")
    public List<UserDTO> getUsers() {
         return userFacade.getUsers();
    }
    @RequestMapping(method = RequestMethod.POST, value = "/user" , consumes = MediaType.APPLICATION_JSON_VALUE)
    public UserDTO createUser(
            @RequestBody UserDTO userDTO){
        return userFacade.createUser(userDTO);
    }
    @RequestMapping(method = RequestMethod.PUT, value = "/user" )
    public UserDTO changeUserName(
            @RequestParam Long userId ,
            @RequestParam String userName){
        return userFacade.changeUserName(userId, userName);
    }
    @RequestMapping(method = RequestMethod.DELETE, value = "/user/{userID}")
    public void deleteUser(
            @PathVariable Long userID){
         userFacade.deleteUser(userID);
    }
}
package com.kodilla.finalproject.controller;

import com.kodilla.finalproject.domain.UserDTO;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/project")
public class UserControler {
    @RequestMapping(method = RequestMethod.GET, value = "/user/{userID}")
    public UserDTO getUser(@PathVariable Long userID) throws UserNotFoundException{
        // DO NAPISANIA WLASCIWA METODA !!
        return new UserDTO(1L,"Mariusz");
    }
    @RequestMapping(method = RequestMethod.GET, value = "/user/*")
    public List<UserDTO> getUser() throws UserNotFoundException{
        // DO NAPISANIA WLASCIWA METODA !!
        List<UserDTO> userDTOList = new ArrayList<>();
        userDTOList.add(new UserDTO(1L,"Mariusz"));
        userDTOList.add(new UserDTO(2L,"Bartosz"));
        return  userDTOList;
    }
    @RequestMapping(method = RequestMethod.POST, value = "/user" , consumes = MediaType.APPLICATION_JSON_VALUE)
    public void createUser(@RequestBody UserDTO userDTO){
        // DO NAPISANIA WLASCIWA METODA !!
    }
    @RequestMapping(method = RequestMethod.PUT, value = "/user" )
    public UserDTO changeUserName(@RequestParam Long userId , @RequestParam String userName){
        // DO NAPISANIA WLASCIWA METODA !!
        return new UserDTO(userId, userName);
    }
    @RequestMapping(method = RequestMethod.DELETE, value = "/user/{userID}")
    public void deleteUser(@PathVariable Long userID){
        // DO NAPISANIA WLASCIWA METODA !!
    }
}

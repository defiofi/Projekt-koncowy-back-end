package com.kodilla.finalproject.mapper;

import com.kodilla.finalproject.domain.Currency;
import com.kodilla.finalproject.domain.User;
import com.kodilla.finalproject.domain.UserDTO;
import com.kodilla.finalproject.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserMapper {
    private UserRepository userRepository;
    public UserMapper( UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    public User maptoUser(final UserDTO userDto){
        User user  = userRepository.findById(userDto.getUserID()).orElse(new User());
        List<Currency> currencyList = user.getCurrency();
        return new User(userDto.getUserID() , userDto.getName() , currencyList);
    }
    public UserDTO maptoUserDTO(final User user){
        return new UserDTO(user.getUserID(), user.getUserName());
    }

    public List<UserDTO> mapToListUserDTO(final List<User> userList){
        List<UserDTO> userDTOList = new ArrayList<>();
        for(User user: userList){
            userDTOList.add(maptoUserDTO(user));
        }
        return userDTOList;
    }
}

package com.kodilla.finalproject.mapper;

import com.kodilla.finalproject.domain.Currency;
import com.kodilla.finalproject.domain.User;
import com.kodilla.finalproject.domain.UserDTO;
import com.kodilla.finalproject.repository.CurrencyRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserMapper {
    private CurrencyRepository currencyRepository;
    public UserMapper(CurrencyRepository currencyRepository) {
        this.currencyRepository = currencyRepository;
    }
    public User maptoUser(final UserDTO userDto){
        List<Currency> currencyList = currencyRepository.findByUser(userDto.getUserID());
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

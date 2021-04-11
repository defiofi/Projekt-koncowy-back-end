package com.kodilla.finalproject.mappers;

import com.kodilla.finalproject.domain.User;
import com.kodilla.finalproject.domain.UserDTO;
import com.kodilla.finalproject.mapper.UserMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class UserMapperTestSuite {

    @Autowired
    private UserMapper userMapper;

    @Test
    public void TestUserMapperMaptoUser() {
        //GIVE
        UserDTO userDTO = new UserDTO(1L, "Marcin");

        //WHEN
        User mappedUser = userMapper.maptoUser(userDTO);

        //THEN
        assertEquals(1, mappedUser.getUserID());
        assertEquals("Marcin", mappedUser.getUserName());
    }
    @Test
    public void TestUserMapperMaptoUserDTO(){
        //GIVE
        User user = new User(1L , "Paweł", null);

        //WHEN
        UserDTO mappedUserDTO = userMapper.maptoUserDTO(user);

        //THEN
        assertEquals(1, mappedUserDTO.getUserID());
        assertEquals("Paweł", mappedUserDTO.getName());
    }
    @Test
    public void TestUserMappermapToListUserDTO() {
        //GIVE
        List<User> userList = new ArrayList<>();
        userList.add(new User(1L, "Marcin" , null));
        userList.add(new User(2L, "Paweł" , null));

        //WHEN
        List<UserDTO> mappedUserDTOList = userMapper.mapToListUserDTO(userList);

        //THEN
        assertEquals(2, mappedUserDTOList.size());
        assertEquals(1, mappedUserDTOList.get(0).getUserID());
        assertEquals("Marcin", mappedUserDTOList.get(0).getName());
        assertEquals(2, mappedUserDTOList.get(1).getUserID());
        assertEquals("Paweł", mappedUserDTOList.get(1).getName());
    }
}

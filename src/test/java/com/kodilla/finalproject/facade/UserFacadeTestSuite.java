package com.kodilla.finalproject.facade;

import com.kodilla.finalproject.controller.UserNotFoundException;
import com.kodilla.finalproject.domain.User;
import com.kodilla.finalproject.domain.UserDTO;
import com.kodilla.finalproject.service.UserDatabase;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class UserFacadeTestSuite {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserFacadeTestSuite.class);
    @Autowired
    private UserFacade userFacade;
    @Autowired
    private UserDatabase userDatabase;

    @Test
    public void TestUserFacadeGetUsers(){
        //GIVE
        User user1 = new User("Mateusz",null);
        User user2 = new User("Tadeusz",null);
        userDatabase.save(user1);
        Long uID1= user1.getUserID();
        userDatabase.save(user2);
        Long uID2= user2.getUserID();
        //WHEN
        List<UserDTO> facadeUserDTOList = userFacade.getUsers();
        //THEN
        assertTrue( facadeUserDTOList.size()>1);
        //CLEAN
        userDatabase.deleteUser(uID1);
        userDatabase.deleteUser(uID2);
    }
    @Test
    public void TestUserFacadeGetUser() {
        //GIVE
        User user1 = new User("Mateusz", null);
        userDatabase.save(user1);
        Long uID1 = user1.getUserID();
        //WHEN
        UserDTO facadeUserDTO = new UserDTO("");
        try {
            facadeUserDTO = userFacade.getUser(uID1);
        } catch (UserNotFoundException e){
            LOGGER.error("UserNotFoundException");
        }
        //THEN
        assertEquals("Mateusz" , facadeUserDTO.getName());

        //CLEAN
        userDatabase.deleteUser(uID1);
    }
    @Test
    public void TestUserFacadeCreateUser() {
        //GIVE
        UserDTO userDTO = new UserDTO("Paweł");

        //WHEN
        UserDTO facadeUserDTO = userFacade.createUser(userDTO);

        //THEN
        assertEquals("Paweł", facadeUserDTO.getName());

        //CLEAN
        userDatabase.deleteUser(facadeUserDTO.getUserID());
    }

    @Test
    public void TestUserFacadeChangeUserName() {
        //GIVE
        User user1 = new User("Mateusz", null);
        userDatabase.save(user1);
        Long uID1 = user1.getUserID();

        //WHEN
        UserDTO facadeUserDTO = userFacade.changeUserName(uID1, "Tadeusz");

        //THEN
        assertEquals(uID1, facadeUserDTO.getUserID());
        assertEquals("Tadeusz", facadeUserDTO.getName());

        //CLEAN
        userDatabase.deleteUser(uID1);
    }
    @Test
    public void TestUserFacadeDeleteUser() {
        //GIVE
        User user1 = new User("Mateusz", null);
        userDatabase.save(user1);
        Long uID1 = user1.getUserID();

        //WHEN
        assertTrue(userDatabase.findUser(uID1).isPresent());

        //THEN
        userFacade.deleteUser(uID1);
        assertFalse(userDatabase.findUser(uID1).isPresent());
    }
}

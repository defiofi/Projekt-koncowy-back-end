package com.kodilla.finalproject.controller;

import com.google.gson.Gson;
import com.kodilla.finalproject.domain.UserDTO;
import com.kodilla.finalproject.facade.UserFacade;
import com.kodilla.finalproject.mapper.UserMapper;
import com.kodilla.finalproject.service.UserDatabase;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringJUnitWebConfig
@WebMvcTest(UserControler.class)
class UserControlerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserDatabase userDatabase;

    @MockBean
    private UserMapper userMapper;

    @MockBean
    private UserFacade userFacade;


    @Test
    public void UserControllerGetUser() throws Exception {
        //GIVEN
        when(userFacade.getUser(any())).thenReturn(new UserDTO(1L, "Mateusz"));

        //WHEN and THEN
        mockMvc.perform(MockMvcRequestBuilders
                .get("/project/user/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$.userID", Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", Matchers.is("Mateusz")));
    }
    @Test
    public void UserControllerGetUsers() throws Exception {
        //GIVEN
        List<UserDTO> userDTOList = new ArrayList<>();
        userDTOList.add(new UserDTO(2L , "Tadeusz"));
        when(userFacade.getUsers()).thenReturn(userDTOList);

        //WHEN and THEN
        mockMvc.perform(MockMvcRequestBuilders
                .get("/project/user/*")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].userID", Matchers.is(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name", Matchers.is("Tadeusz")));
    }
    @Test
    public void UserControllerCreateUser() throws Exception {
        //GIVEN
        UserDTO userDTO = new UserDTO("Zbigniew");
        when(userFacade.createUser(any())).thenReturn(new UserDTO(2L, "Zbigniew"));
        Gson gson = new Gson();
        String jsonContent = gson.toJson(userDTO);

        //WHEN and THEN
        mockMvc.perform(MockMvcRequestBuilders
                .post("/project/user")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .content(jsonContent))
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$.userID", Matchers.is(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", Matchers.is("Zbigniew")));
    }

    @Test
    public void UserControllerChangeUserName() throws Exception {
        //GIVEN
        when(userFacade.changeUserName(any(), any())).thenReturn(new UserDTO(3L, "Marcin"));

        //WHEN and THEN
        mockMvc.perform(MockMvcRequestBuilders
                .put("/project/user?userId=3&userName=Marcin")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$.userID", Matchers.is(3)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", Matchers.is("Marcin")));
    }
    @Test
    public void UserControllerDeleteUser() throws Exception {
        //GIVEN

        //WHEN and THEN
        mockMvc.perform(MockMvcRequestBuilders
                .delete("/project/user/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is(200));
    }
}
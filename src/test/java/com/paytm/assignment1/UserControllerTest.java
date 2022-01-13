package com.paytm.assignment1;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    UserRepository userRepository;

    User USER_1 = new User(1,"Abhinav","Rastogi","9808237443","abhi@test.com","abhi1","2,saudagran,BLY","Jubilee Hall, DU");
    User USER_2 = new User(2,"Vasu","Sehgal","8736838343","vasu@test.com","geekmonk","435,Govindpuri,DLI","");
    User USER_3 = new User(3,"Sristi","Goyal","9937477554","goyal@test.com","goyal2","sec 12, Noida","Shadra,DLI");

    @Test
    public void getAllUsers_success() throws Exception{
        List<User> users = new ArrayList<>(Arrays.asList(USER_1,USER_2,USER_3));
        Mockito.when(userRepository.findAll()).thenReturn(users);
        mockMvc.perform(MockMvcRequestBuilders
                .get("/user/all")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$",hasSize(3)))
                .andExpect(jsonPath("$[2].lastName",is("Goyal")));
    }

    @Test
    public void getUser_success() throws Exception{
        Mockito.when(userRepository.findById(USER_1.getId())).thenReturn(java.util.Optional.of(USER_1));
        mockMvc.perform( MockMvcRequestBuilders.get("/user/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$",notNullValue()))
                .andExpect(jsonPath("$.email",is("abhi@test.com")));
    }

    @Test
    public void getUser_userNotPresent() throws Exception{
        Mockito.when(userRepository.findById(20)).thenReturn(Optional.of(null));
        mockMvc.perform( MockMvcRequestBuilders.get("/user/20")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().string(containsString("User not found")));
    }


}

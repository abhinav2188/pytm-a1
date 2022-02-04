package com.paytm.assignment1.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.paytm.assignment1.Assignment1Application;
import com.paytm.assignment1.dto.AuthenticateRequestDto;
import com.paytm.assignment1.dto.BaseResponseDto;
import com.paytm.assignment1.modals.User;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes= Assignment1Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration
@TestPropertySource("/test.properties")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@AutoConfigureMockMvc
public class UserControllerIT {

    @LocalServerPort
    private int port;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    TestRestTemplate restTemplate = new TestRestTemplate();
    HttpHeaders headers = new HttpHeaders();

    private static String jwtAccessToken;

    @BeforeEach
    public void beforeAnyTest(){
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
    }

    public String createRequestUrl(String path){
        return "http://localhost:" + this.port + path;
    }

    @Test
    @Order(1)
    public void test(){
        System.out.println("TEST PORT: "+port);
        String url = createRequestUrl("/greeting");
        HttpEntity<String> entity = new HttpEntity<>(null,headers);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

        assertTrue(response.getBody().contains("Hello Spring!"));
    }

    @Test
    @Order(2)
    public void userPostTest_success(){
        System.out.println("test order 1");
        User user = User.builder()
                .userName("testUser1")
                .address1("some address")
                .email("email1@test.com")
                .mobile("9259267790")
                .password("user@pass")
                .firstName("Ussop")
                .build();

        HttpEntity<User> entity = new HttpEntity<>(user,headers);
        String url = "http://localhost:" + this.port + "/user";

        ResponseEntity<?> response = restTemplate.exchange(url,HttpMethod.POST, entity, Object.class);

        HashMap<String,Object> map = (HashMap<String,Object>) response.getBody();
        HashMap<String,Object> dataMap = (HashMap<String, Object>) map.get("data");

        assertEquals(map.get("msg"),"new user created");
        assertEquals(map.get("status"),"CREATED");
        assertEquals(dataMap.get("userName"),user.getUserName());

    }


    @Test
    @Order(3)
    public void userPost_failure_duplicateUsername() throws Exception{
        User user = User.builder()
                .userName("testUser1")
                .address1("some address")
                .email("email2@test.com")
                .mobile("8273997032")
                .password("user@pass")
                .firstName("Sanji")
                .build();

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/user")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(user));

        mockMvc.perform(mockRequest)
                .andDo(print())
                .andExpect(jsonPath("$.status",containsString("BAD_REQUEST")))
                .andExpect(jsonPath("$.errorMsg",containsString("userName: "+user.getUserName()+" is already taken, should be unique")));
    }


    @Test
    @Order(4)
    public void userPost_failure_duplicateEmail() throws Exception{
        User user = User.builder()
                .userName("testUser2")
                .address1("some address")
                .email("email1@test.com")
                .mobile("8273997032")
                .password("user@pass")
                .firstName("Nami")
                .build();

        String json = this.objectMapper.writeValueAsString(user);

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/user")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json);

        mockMvc.perform(mockRequest)
                .andDo(print())
                .andExpect(jsonPath("$.status",containsString("BAD_REQUEST")))
                .andExpect(jsonPath("$.errorMsg",containsString("email: "+user.getEmail()+" is already taken, should be unique")));
    }


    @Test
    @Order(5)
    public void userPost_failure_requiredFields() throws Exception{
        User user = User.builder()
                .address1("some address")
                .email("email1@test.com")
                .mobile("8273997032")
                .password("user@pass")
                .firstName("Nami")
                .build();

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/user")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(user));

        mockMvc.perform(mockRequest)
                .andDo(print())
                .andExpect(jsonPath("$.status",containsString("BAD_REQUEST")))
                .andExpect(jsonPath("$.errorMsg",containsString("userName can't be null or empty")));

    }

    @Test
    @Order(6)
    public void userPost_success2() throws Exception{
        User user = User.builder()
                .userName("testUser2")
                .address1("some address")
                .email("email2@test.com")
                .mobile("8273997032")
                .password("user@pass")
                .firstName("Sanji")
                .build();

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/user")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(user));

        mockMvc.perform(mockRequest)
                .andDo(print())
                .andExpect(jsonPath("$.status",containsString("CREATED")));
    }


    @Test
    @Order(7)
    public void authenticateUser_success() throws Exception{
        AuthenticateRequestDto loginDto = new AuthenticateRequestDto("testUser1","user@pass");
        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/authenticate")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(loginDto));

        MvcResult response = mockMvc.perform(mockRequest)
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        BaseResponseDto responseDto = this.objectMapper.readValue(response.getResponse().getContentAsString(),BaseResponseDto.class);

        Map<String,Object> data = (LinkedHashMap<String,Object>)responseDto.getData();

        assertNotNull(data.get("jwt"));
        this.jwtAccessToken = (String)data.get("jwt");

    }

    @Test
    @Order(7)
    public void authenticateUser_failure() throws Exception {
        AuthenticateRequestDto loginDto = new AuthenticateRequestDto("testUser1","incorrectPass");
        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/authenticate")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(loginDto));

        mockMvc.perform(mockRequest)
                .andDo(print())
                .andExpect(jsonPath("$.status",is("BAD_REQUEST")))
                .andExpect(jsonPath("$.errorMsg",is("invalid username or password")));
    }

    @Test
    @Order(8)
    public void getUser_success() throws Exception{
        assertNotNull(this.jwtAccessToken);

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.get("/user/1")
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer "+this.jwtAccessToken);

        mockMvc.perform(mockRequest)
                .andDo(print())
                .andExpect(jsonPath("$.status",is("OK")))
                .andExpect(jsonPath("$.errorMsg", emptyOrNullString()))
                .andExpect(jsonPath("$.data.userName", is("testUser1")));
    }

    @Test
    @Order(9)
    public void getUser_failure_authenticationFailed() throws Exception{
        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.get("/user/1")
                .accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(mockRequest)
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    @Order(10)
    public void getUser_failure_userNotPresent() throws Exception{
        assertNotNull(this.jwtAccessToken);
        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.get("/user/3")
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer "+this.jwtAccessToken);

        mockMvc.perform(mockRequest)
                .andDo(print())
                .andExpect(jsonPath("$.status",is("NOT_FOUND")))
                .andExpect(jsonPath("$.errorMsg", is("User not Found with id 3")))
                .andExpect(jsonPath("$.data", blankOrNullString()));
    }

    @Test
    @Order(11)
    public void getUser_failure_authorizationFailed() throws Exception{
        // getting other users data
        assertNotNull(this.jwtAccessToken);
        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.get("/user/2")
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer "+this.jwtAccessToken);

        mockMvc.perform(mockRequest)
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    //---------------------------------update-user----------------------------------------//

    @Test
    @Order(12)
    public void updateUser_success() throws Exception{
        assertNotNull(this.jwtAccessToken);
        User user = User.builder()
                .userName("testUser1")
                .address1("some address")
                .email("email1@test.com")
                .mobile("9259267790")
                .password("user@pass")
                .firstName("Ussop")
                .lastName("SniperKing")
                .build();
        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.put("/user/1")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer "+this.jwtAccessToken)
                .content(this.objectMapper.writeValueAsString(user));

        mockMvc.perform(mockRequest)
                .andDo(print())
                .andExpect(jsonPath("$.status",is("OK")))
                .andExpect(jsonPath("$.msg",is("user details updated")))
                .andExpect(jsonPath("$.data.lastName",is("SniperKing")));

    }

    @Test
    @Order(13)
    public void updateUser_failure_usernameNotPresent() throws Exception{
        assertNotNull(this.jwtAccessToken);
        User user = User.builder()
                .userName("ussop1")
                .address1("some address")
                .email("email1@test.com")
                .mobile("9259267790")
                .password("user@pass")
                .firstName("Ussop")
                .lastName("SniperKing")
                .build();
        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.put("/user/3")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer "+this.jwtAccessToken)
                .content(this.objectMapper.writeValueAsString(user));

        mockMvc.perform(mockRequest)
                .andDo(print())
                .andExpect(jsonPath("$.status",is("NOT_FOUND")))
                .andExpect(jsonPath("$.errorMsg",is("User not Found with id "+3)));

    }

    @Test
    @Order(14)
    public void updateUser_failure_authenticationFailed() throws Exception{
        User user = User.builder()
                .userName("ussop1")
                .address1("some address")
                .email("email1@test.com")
                .mobile("9259267790")
                .password("user@pass")
                .firstName("Ussop")
                .lastName("SniperKing")
                .build();
        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.put("/user/1")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(user));

        mockMvc.perform(mockRequest)
                .andExpect(status().isForbidden());

    }

    @Test
    @Order(15)
    public void updateUser_failure_validationFailedIfUpdate() throws Exception{
        assertNotNull(this.jwtAccessToken);
        User user = User.builder()
                .userName("testUser2")
                .address1("some address")
                .email("email1@test.com")
                .mobile("9259267790")
                .password("user@pass")
                .firstName("Ussop")
                .lastName("SniperKing")
                .build();
        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.put("/user/1")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer "+this.jwtAccessToken)
                .content(this.objectMapper.writeValueAsString(user));
        mockMvc.perform(mockRequest)
                .andDo(MockMvcResultHandlers.log())
                .andExpect(jsonPath("$.status",is("BAD_REQUEST")))
                .andExpect(jsonPath("$.errorMsg", containsString("userName: "+user.getUserName()+" is already taken, should be unique")));
    }

    @Test
    @Order(16)
    public void updateUser_failure_authorizationFailed() throws Exception{
        // trying to update other user data
        assertNotNull(this.jwtAccessToken);
        User user = User.builder()
                .userName("sanji12")
                .address1("some address")
                .address2("all blue")
                .email("email2@test.com")
                .mobile("8273997032")
                .password("user@pass")
                .firstName("Sanji")
                .build();
        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.put("/user/2")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer "+this.jwtAccessToken)
                .content(this.objectMapper.writeValueAsString(user));

        mockMvc.perform(mockRequest)
                .andDo(log())
                .andExpect(status().isForbidden());

    }

    @Test
    @Order(12)
    public void deleteUser_success() throws Exception{

    }

    @Test
    @Order(12)
    public void deleteUser_failure_usernameNotPresent() throws Exception{

    }

    @Test
    @Order(12)
    public void deleteUser_failure_authenticationFailed() throws Exception{

    }

    @Test
    @Order(12)
    public void deleteUser_failure_authorizationFailed() throws Exception{
        // trying to update other user data
    }

    @Test
    @Order(12)
    public void getAllUsers() throws Exception{

    }

}

package com.paytm.assignment1.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import com.paytm.assignment1.Assignment1Application;
import com.paytm.assignment1.dto.AuthenticateRequestDto;
import com.paytm.assignment1.dto.UserRequestDto;
import com.paytm.assignment1.repositories.UserRepository;
import com.paytm.assignment1.repositories.WalletRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes= Assignment1Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration
@TestPropertySource("/test.properties")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@AutoConfigureMockMvc
public class WalletControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    WalletRepository walletRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ObjectMapper objectMapper;

    private static String jwtAccessToken;
    private int walletId;
    private static String jwtAccessToken2;

    // demo user
    UserRequestDto user1 = UserRequestDto.builder()
            .firstName("Thorfinn")
            .lastName("Snow")
            .email("thorfinn@test.com")
            .userName("thorfinn1")
            .mobile("9808727553")
            .address1("Iceland")
            .password("user@pass")
            .build();
    UserRequestDto user2 = UserRequestDto.builder()
            .firstName("Askalad")
            .lastName("")
            .email("askalad@test.com")
            .userName("askalad1")
            .mobile("9027822400")
            .address1("Britannia")
            .password("user@pass")
            .build();

    @BeforeAll
    public void setupTestData() throws Exception {

        // create users

        mockMvc.perform(MockMvcRequestBuilders.post("/user")
                .content(objectMapper.writeValueAsString(user1))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath("$.status",is("CREATED")));
        mockMvc.perform(MockMvcRequestBuilders.post("/user")
                        .content(objectMapper.writeValueAsString(user2))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath("$.status",is("CREATED")));

        // authenticate the user
        AuthenticateRequestDto auth = new AuthenticateRequestDto(user1.getUserName(),user1.getPassword());
        MvcResult response = mockMvc.perform(MockMvcRequestBuilders.post("/authenticate")
                .content(objectMapper.writeValueAsString(auth))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.data.jwt", notNullValue()))
                .andReturn();

        // save the token
        this.jwtAccessToken = "Bearer "+ JsonPath.read(response.getResponse().getContentAsString(),"$.data.jwt");

        // authenticate the user2
        AuthenticateRequestDto auth2 = new AuthenticateRequestDto(user2.getUserName(),user2.getPassword());
        MvcResult response2 = mockMvc.perform(MockMvcRequestBuilders.post("/authenticate")
                        .content(objectMapper.writeValueAsString(auth2))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.data.jwt", notNullValue()))
                .andReturn();

        // save the token2
        this.jwtAccessToken2 = "Bearer "+ JsonPath.read(response2.getResponse().getContentAsString(),"$.data.jwt");
    }

    @AfterAll
    public void clearDatabase(){
        walletRepository.deleteAll();
        userRepository.deleteAll();
    }

    // --------------------------------- /create-wallet/{mobile} ---------------------- //
    @Test
    @Order(1)
    public void createWallet_success() throws Exception {
        assertNotNull(this.jwtAccessToken);
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/create-wallet/"+user1.getMobile())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization",this.jwtAccessToken))
                .andDo(print())
                .andExpect(jsonPath("$.status",is("CREATED")))
                .andExpect(jsonPath("$.msg",is("user wallet created")))
                .andReturn();
        this.walletId = JsonPath.read(mvcResult.getResponse().getContentAsString(),"$.data.id");
    }

    @Test
    @Order(1)
    public void createWallet_failure_authenticationFailed() throws Exception {
        assertNotNull(this.jwtAccessToken);
        mockMvc.perform(MockMvcRequestBuilders.post("/create-wallet/"+user1.getMobile())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    @Order(1)
    public void createWallet_failure_userNotFound() throws Exception {
        assertNotNull(this.jwtAccessToken);
        mockMvc.perform(MockMvcRequestBuilders.post("/create-wallet/"+"8630018154")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization",this.jwtAccessToken))
                .andExpect(status().isForbidden());

    }

    @Test
    @Order(2)
    public void createWallet_failure_walletAlreadyExist() throws Exception {
        assertNotNull(this.jwtAccessToken);
        mockMvc.perform(MockMvcRequestBuilders.post("/create-wallet/"+user1.getMobile())
                       .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization",this.jwtAccessToken))
                .andDo(print())
                .andExpect(jsonPath("$.status",is("BAD_REQUEST")))
                .andExpect(jsonPath("$.errorMsg",is("wallet already present with id: "+user1.getMobile())));

    }

    @Test
    @Order(1)
    public void createWallet_failure_authorizationFailed() throws Exception {
        assertNotNull(this.jwtAccessToken);
        mockMvc.perform(MockMvcRequestBuilders.post("/create-wallet/"+user2.getMobile())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization",this.jwtAccessToken))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    // ------------------------------------get /wallet/{mobile} ----------------------------------//

    @Test
    @Order(2)
    public void getWallet_success() throws Exception {
        assertNotNull(this.jwtAccessToken);
        this.mockMvc.perform(MockMvcRequestBuilders.get("/wallet/"+user1.getMobile())
                .header("Authorization", this.jwtAccessToken)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath("$.status",is("OK")))
                .andExpect(jsonPath("$.data.id",is(this.walletId)));
    }

    @Test
    @Order(2)
    public void getWallet_failure_UserNotPresent() throws Exception {
        // in case of role user, any user is not allowed to fetch other user details
        assertNotNull(this.jwtAccessToken);
        this.mockMvc.perform(MockMvcRequestBuilders.get("/wallet/8630018154")
                        .header("Authorization", this.jwtAccessToken)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    @Order(2)
    public void getWallet_failure_WalletNotPresent() throws Exception {
        assertNotNull(this.jwtAccessToken2);
        this.mockMvc.perform(MockMvcRequestBuilders.get("/wallet/"+user2.getMobile())
                        .header("Authorization", this.jwtAccessToken2)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath("$.status",is("NOT_FOUND")))
                .andExpect(jsonPath("$.errorMsg",is("wallet not found with userId: "+user2.getMobile())));
    }

    @Test
    public void getWallet_failure_AuthenticationFailed() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/wallet/"+user2.getMobile())
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isForbidden());
    }


    // -------------------------------------- /add-balance -----------------------------------//

    @Test
    @Order(2)
    public void addBalance_success() throws Exception {
        assertNotNull(this.jwtAccessToken);
        MvcResult result1 = this.mockMvc.perform(MockMvcRequestBuilders.get("/wallet/"+user1.getMobile())
                        .header("Authorization", this.jwtAccessToken)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status",is("OK")))
                .andExpect(jsonPath("$.data.id",is(this.walletId)))
                .andReturn();
        double prevBalance = JsonPath.read(result1.getResponse().getContentAsString(),"$.data.balanceAmount");
        MvcResult result2 = this.mockMvc.perform(MockMvcRequestBuilders.post("/add-balance")
                        .param("mobile",user1.getMobile())
                        .param("amount","500.0")
                        .header("Authorization", this.jwtAccessToken)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status",is("OK")))
                .andExpect(jsonPath("$.data.id",is(this.walletId)))
                .andExpect(jsonPath("$.data.balanceAmount",is(prevBalance+500.0)))
                .andReturn();

    }

}

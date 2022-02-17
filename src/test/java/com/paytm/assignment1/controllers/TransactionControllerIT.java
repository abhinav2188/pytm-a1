package com.paytm.assignment1.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import com.paytm.assignment1.Assignment1Application;
import com.paytm.assignment1.dto.AddTransactionRequestDto;
import com.paytm.assignment1.dto.AuthenticateRequestDto;
import com.paytm.assignment1.dto.UserRequestDto;
import com.paytm.assignment1.repositories.TransactionRepository;
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

import java.util.ArrayList;
import java.util.List;

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
public class TransactionControllerIT {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;


    //static data to be used by various tests
    private static String jwtAccessTokenUser1;
    private static String jwtAccessTokenUser2;
    private static List<Integer> txnIds = new ArrayList<>();

    @Autowired
    UserRepository userRepository;
    @Autowired
    WalletRepository walletRepository;
    @Autowired
    TransactionRepository transactionRepository;

    //test users
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
        jwtAccessTokenUser1 = saveUser_createWallet_addBal(user1,2000);
        jwtAccessTokenUser2 = saveUser_createWallet_addBal(user2,3000);
    }

    public String saveUser_createWallet_addBal(UserRequestDto user, double bal) throws Exception {
        //save user
        mockMvc.perform(MockMvcRequestBuilders.post("/user")
                        .content(objectMapper.writeValueAsString(user))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status",is("CREATED")));

        // authenticate user
        AuthenticateRequestDto auth = new AuthenticateRequestDto(user.getUserName(),user.getPassword());
        MvcResult response = mockMvc.perform(MockMvcRequestBuilders.post("/authenticate")
                        .content(objectMapper.writeValueAsString(auth))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.data.jwt", notNullValue()))
                .andReturn();
        String jwtAccessToken = "Bearer "+ JsonPath.read(response.getResponse().getContentAsString(),"$.data.jwt");

        //create-wallet
        mockMvc.perform(MockMvcRequestBuilders.post("/create-wallet/"+user.getMobile())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization",jwtAccessToken))
                .andDo(print())
                .andExpect(jsonPath("$.status",is("CREATED")))
                .andExpect(jsonPath("$.msg",is("user wallet created")));

        // add balance
        this.mockMvc.perform(MockMvcRequestBuilders.post("/add-balance")
                        .param("mobile",user.getMobile())
                        .param("amount",bal+"")
                        .header("Authorization", jwtAccessToken)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status",is("OK")))
                .andExpect(jsonPath("$.data.balanceAmount",is(bal)));

        return jwtAccessToken;

    }

    @AfterAll
    public void clearDatabase(){
        transactionRepository.deleteAll();
        walletRepository.deleteAll();
        userRepository.deleteAll();
    }


    public double getBalance(String mobile, String jwtAccessToken) throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/wallet/"+mobile)
                        .header("Authorization", jwtAccessToken)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath("$.status",is("OK")))
                .andReturn();
        double bal = JsonPath.read(result.getResponse().getContentAsString(),"$.data.balanceAmount");
        return bal;
    }

    //-------------------------post /transaction/{mobile}----------------------------//
    @Test
    @Order(1)
    public void transaction_success_SuccessTransaction() throws Exception{
        assertNotNull(jwtAccessTokenUser1);
        double prevPayerBal = getBalance(user1.getMobile(),jwtAccessTokenUser1);
        double prevPayeeBal = getBalance(user2.getMobile(),jwtAccessTokenUser2);
        double amount = 500;

        AddTransactionRequestDto dto = new AddTransactionRequestDto(user2.getMobile(),amount);
        MvcResult result1 = mockMvc.perform(MockMvcRequestBuilders.post("/transaction/"+user1.getMobile())
                    .accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON)
                    .header("Authorization",jwtAccessTokenUser1)
                    .content(this.objectMapper.writeValueAsString(dto)))
                .andDo(print())
                .andExpect(jsonPath("$.status",is("CREATED")))
                .andExpect(jsonPath("$.msg", is("created transaction")))
                .andExpect(jsonPath("$.data.status",is("SUCCESS")))
                .andExpect(jsonPath("$.data.payerClosingBalance",is(prevPayerBal-amount)))
                .andExpect(jsonPath("$.data.payeeClosingBalance",is(prevPayeeBal+amount)))
                .andReturn();

        int txnId = JsonPath.read(result1.getResponse().getContentAsString(),"$.data.id");
        txnIds.add(txnId);

        double currPayerBal = getBalance(user1.getMobile(),jwtAccessTokenUser1);
        double currPayeeBal = getBalance(user2.getMobile(),jwtAccessTokenUser2);
        Assertions.assertEquals(prevPayerBal-amount, currPayerBal);
        Assertions.assertEquals(prevPayeeBal+amount, currPayeeBal);
    }

    @Test
    @Order(1)
    public void transaction_success_FailedTransaction_InsufficientBalance() throws Exception{
        assertNotNull(jwtAccessTokenUser1);
        double prevPayerBal = getBalance(user1.getMobile(),jwtAccessTokenUser1);
        double prevPayeeBal = getBalance(user2.getMobile(),jwtAccessTokenUser2);
        double amount = 2500;

        AddTransactionRequestDto dto = new AddTransactionRequestDto(user2.getMobile(),amount);
        MvcResult result1 = mockMvc.perform(MockMvcRequestBuilders.post("/transaction/"+user1.getMobile())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization",jwtAccessTokenUser1)
                        .content(this.objectMapper.writeValueAsString(dto)))
                .andDo(print())
                .andExpect(jsonPath("$.status",is("CREATED")))
                .andExpect(jsonPath("$.msg", is("created transaction")))
                .andExpect(jsonPath("$.data.status",is("FAILED")))
                .andReturn();

        int txnId = JsonPath.read(result1.getResponse().getContentAsString(),"$.data.id");
        txnIds.add(txnId);

        double currPayerBal = getBalance(user1.getMobile(),jwtAccessTokenUser1);
        double currPayeeBal = getBalance(user2.getMobile(),jwtAccessTokenUser2);
        Assertions.assertEquals(prevPayerBal, currPayerBal);
        Assertions.assertEquals(prevPayeeBal, currPayeeBal);

    }

    @Test
    @Order(1)
    public void transaction_failure_PayeeNotFound() throws Exception{
        assertNotNull(jwtAccessTokenUser1);
        AddTransactionRequestDto dto = new AddTransactionRequestDto("8630018154",500);
        mockMvc.perform(MockMvcRequestBuilders.post("/transaction/"+user1.getMobile())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization",jwtAccessTokenUser1)
                        .content(this.objectMapper.writeValueAsString(dto)))
                .andDo(print())
                .andExpect(jsonPath("$.status",is("NOT_FOUND")))
                .andExpect(jsonPath("$.errorMsg", is("wallet not found with userId: 8630018154")))
                .andReturn();
    }

    @Test
    @Order(1)
    public void transaction_failure_authorizationFailed() throws Exception {
        // trying to create transaction with user2 wallet using user1 authentication token
        assertNotNull(jwtAccessTokenUser1);
        AddTransactionRequestDto dto = new AddTransactionRequestDto(user1.getMobile(),400);
        mockMvc.perform( MockMvcRequestBuilders.post("/transaction/"+user2.getMobile())
                        .content(this.objectMapper.writeValueAsString(dto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("Authorization",jwtAccessTokenUser1))
                .andExpect(status().isForbidden());
    }

    // -------------------get transactions summary------------------//

    @Test
    @Order(2)
    public void getTransactions_success() throws Exception{
        assertNotNull(jwtAccessTokenUser1);
        mockMvc.perform(MockMvcRequestBuilders.get("/transaction/"+user1.getMobile())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization",jwtAccessTokenUser1)
                        .param("pageNo","0"))
                .andDo(print())
                .andExpect(jsonPath("$.status",is("OK")))
                .andExpect(jsonPath("$.data", hasSize(2)))
                .andExpect(jsonPath("$.data[0].transactionId",is(txnIds.get(1))))
                .andExpect(jsonPath("$.data[1].transactionId",is(txnIds.get(0))));
        mockMvc.perform(MockMvcRequestBuilders.get("/transaction/"+user1.getMobile())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization",jwtAccessTokenUser1)
                        .param("pageNo","1"))
                .andDo(print())
                .andExpect(jsonPath("$.status",is("OK")))
                .andExpect(jsonPath("$.data", hasSize(0)));
    }

    @Test
    @Order(2)
    public void getTransactions_failure_authenticationFailed() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.get("/transaction/"+user1.getMobile())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("pageNo","1"))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    @Order(2)
    public void getTransaction_failure_authorizationFailed() throws Exception {
        // trying to get transactions from user2 wallet using user1 authentication token
        assertNotNull(jwtAccessTokenUser1);
        mockMvc.perform( MockMvcRequestBuilders.get("/transaction/"+user2.getMobile())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }


    //--------------------get Transaction ---------------------------//

    @Test
    @Order(2)
    public void getTransaction_success() throws Exception{
        assertNotNull(jwtAccessTokenUser1);
        mockMvc.perform(MockMvcRequestBuilders.get("/transaction")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization",jwtAccessTokenUser1)
                        .param("txnId",txnIds.get(0)+""))
                .andDo(print())
                .andExpect(jsonPath("$.status",is("OK")))
                .andExpect(jsonPath("$.data.id", is(txnIds.get(0))))
                .andExpect(jsonPath("$.data.status", is("SUCCESS")));
    }

    @Test
    @Order(2)
    public void getTransaction_failure_txnIdDoesNotExist() throws Exception{
        assertNotNull(jwtAccessTokenUser1);
        mockMvc.perform(MockMvcRequestBuilders.get("/transaction")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization",jwtAccessTokenUser1)
                        .param("txnId","10"))
                .andDo(print())
                .andExpect(jsonPath("$.status",is("NOT_FOUND")))
                .andExpect(jsonPath("$.errorMsg", is("No Transaction found with id: 10")));
    }


}

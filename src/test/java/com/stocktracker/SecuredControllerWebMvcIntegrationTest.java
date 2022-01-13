package com.stocktracker;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class SecuredControllerWebMvcIntegrationTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private WebApplicationContext context;

    @BeforeEach
    public void setUp() {
        mvc =  MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();//.apply(springSecurity()).build();
    }

//    @WithMockUser(value = "spring")
//    @Test
//    public void contextLoads() throws Exception {
//        assertThat(userController).isNotNull();
//    }
    @Test
    public void hello() throws Exception {
        mvc.perform(get("/api/users")).andExpect(status().isOk());
    }
}

package com.stocktracker;

import com.stocktracker.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
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
@Slf4j
public class SecuredControllerWebMvcIntegrationTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    UserService userService;

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
    public void noAuthNeeded() throws Exception {
        mvc.perform(get("/api/users")).andExpect(status().isOk());
    }

    @Test
    public void authNeeded() throws Exception {
        mvc.perform(get("/api/user/test")).andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser
    public void authNeededWithUser() throws Exception {
        mvc.perform(get("/api/user/test")).andExpect(status().isOk());
    }

//    @Test
//    public void userNotExists_LoginFails() throws Exception {
//        mvc.perform(formLogin("/auth/login")
//                .user("email", "test@test.com")
//                .password("test"))
//                .andExpect(unauthenticated());
//    }

    // DOESN'T WORK WITH STATELESS
//    @Test
//    @Transactional
//    public void userExists_LoginSuccess() throws Exception {
//
//        User user = new User();
//        user.setUsername("test");
//        user.setEmail("test@test.com");
//        user.setPassword("test");
//        user.setRoles(new ArrayList<Role>() {{ add(userService.getRole("ROLE_USER")); }});
//        userService.saveUser(user);
//
//        MvcResult response = mvc.perform(formLogin("/auth/login")
//                .user("email", user.getEmail())
//                .password(user.getPassword())).andReturn();
////                .andExpect(content().contentType("application/json"))
////                .andExpect(jsonPath("$[0].access_token").exists());
//
//        String content = response.getResponse().getContentAsString();
//        log.info("TEST");
//        log.info(content);
//    }
}

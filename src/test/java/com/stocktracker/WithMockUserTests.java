package com.stocktracker;

import com.stocktracker.controller.UserController;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class WithMockUserTests {

    @Autowired
    UserController userController;

    @Test
    public void getMessageWithMockUser() throws Exception{
        assertThat(userController).isNotNull();
    }
}

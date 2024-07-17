package com.auth.user.controllers;

import com.auth.user.controller.UserController;
import com.auth.user.services.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
class UserControllerTest {

    @Autowired
    private UserController userController;

    @InjectMocks
    private UserService userService;

    @Test
    void validateToken() {


    }
}
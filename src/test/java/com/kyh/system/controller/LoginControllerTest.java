package com.kyh.system.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.nio.charset.StandardCharsets;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.util.DigestUtils;
import org.springframework.web.servlet.ModelAndView;

import com.kyh.system.mapper.UserAccessLogMapper;
import com.kyh.system.model.UserAccessLog;
import com.kyh.system.model.UserAuth;
import com.kyh.system.service.UserService;

@ExtendWith(MockitoExtension.class)
class LoginControllerTest {

    @InjectMocks
    private LoginController loginController;

    @Mock
    private UserService userService;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private HttpSession session;
    
    @Mock
    private UserAccessLogMapper userAccessLogMapper;

    @Test
    void testUserLoginSuccess() {
        when(request.getParameter("userCode")).thenReturn("testuser");
        when(request.getParameter("password")).thenReturn("password123");
        
        UserAuth userAuth = new UserAuth();
        userAuth.setUserCode("testUser");
        
        String md5Password = DigestUtils
                .md5DigestAsHex("password123".getBytes(StandardCharsets.UTF_8))
                .toUpperCase();

        userAuth.setPassword(md5Password);
        
        when(userService.getUserByUserCode(any(String.class))).thenReturn(userAuth);
        ModelAndView mv = loginController.userLogin(request, response, session);


        assertEquals("login/index", mv.getViewName());
        verify(session).setAttribute("user", userAuth);
        verify(userAccessLogMapper).insert(any(UserAccessLog.class));
    }
    
    @Test
    void testUserLoginUserNotFound() {
        when(request.getParameter("userCode")).thenReturn("unknownUser");
        when(request.getParameter("password")).thenReturn("password123");

        when(userService.getUserByUserCode(any(String.class))) .thenReturn(null);

        ModelAndView mv = loginController.userLogin(request, response, session);

        assertEquals("/login/login", mv.getViewName());
        assertEquals("該当ユーザーが存在しません。",mv.getModel().get("MSG")
        );
    }
    
    @Test
    void testUserLoginWrongPassword() {
        when(request.getParameter("userCode")).thenReturn("testuser");
        when(request.getParameter("password")).thenReturn("wrongPassword");

        UserAuth userAuth = new UserAuth();
        userAuth.setUserCode("testuser");

        String correctPassword = DigestUtils .md5DigestAsHex(
                        "password123".getBytes(StandardCharsets.UTF_8)
                ) .toUpperCase();

        userAuth.setPassword(correctPassword);

        when(userService.getUserByUserCode(any(String.class))).thenReturn(userAuth);

        ModelAndView mv = loginController.userLogin(request, response, session);

        assertEquals("/login/login", mv.getViewName());
        assertEquals( "パスワードが間違っています。",mv.getModel().get("MSG"));
    }
}


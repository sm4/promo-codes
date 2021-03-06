package com.czequered.promocodes.controller;

import com.czequered.promocodes.model.User;
import com.czequered.promocodes.service.TokenService;
import com.czequered.promocodes.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import static com.czequered.promocodes.config.Constants.TOKEN_HEADER;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

/**
 * @author Martin Varga
 */
@ActiveProfiles("resttest")
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class UserControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private UserService userService;

    @Autowired
    private TokenService tokenService;

    private MockMvc mockMvc;
    private ObjectMapper mapper;

    @Before
    public void before() {
        mapper = new ObjectMapper();
        reset(userService);
        mockMvc = webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void getUser() throws Exception {
        User user = new User("Krtek");
        user.addAttribute("hello", "world");
        when(userService.getUser(eq("Krtek"))).thenReturn(user);
        String token = tokenService.generateToken("Krtek");
        mockMvc.perform(get("/api/v1/user").header(TOKEN_HEADER, token))
            .andExpect(status().isOk())
                .andExpect(jsonPath("$.attributes.hello").value("world"));
    }

    @Test
    public void saveExistingUser() throws Exception {
        User user = new User("Krtek");
        user.addAttribute("hello", "world");
        when(userService.saveUser(any(User.class))).then(i -> i.getArgumentAt(0, User.class));
        String token = tokenService.generateToken("Krtek");
        String json = mapper.writeValueAsString(user);
        mockMvc.perform(put("/api/v1/user").header(TOKEN_HEADER, token).contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.attributes.hello").value("world"));
    }

    @Test
    public void saveExistingUserSpoofing() throws Exception {
        User user = new User("Sova");
        user.addAttribute("hello", "world");
        String token = tokenService.generateToken("Krtek");
        String json = mapper.writeValueAsString(user);
        mockMvc.perform(put("/api/v1/user").header(TOKEN_HEADER, token).contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isBadRequest());
        verify(userService, never()).saveUser(any(User.class));
    }
}
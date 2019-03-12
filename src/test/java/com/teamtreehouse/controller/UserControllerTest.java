package com.teamtreehouse.controller;

import com.teamtreehouse.core.FlashMessage;
import com.teamtreehouse.exception.UserNotFoundException;
import com.teamtreehouse.model.User;
import com.teamtreehouse.service.RecipeService;
import com.teamtreehouse.service.UserService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.teamtreehouse.data.TestData.user1;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class UserControllerTest
{
    private MockMvc mockMvc;

    @InjectMocks
    private UserController controller;

    @Mock
    private RecipeService recipeService;

    @Mock
    private UserService userService;

    @Before
    public void setUp() throws Exception
    {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders
                .standaloneSetup(controller)
                .build();
    }

    @Test
    public void signup_ShouldRenderForm() throws Exception
    {
        mockMvc.perform(get("/signup"))
                .andExpect(model().attribute("user", new User()))
                .andExpect(status().isOk())
                .andExpect(view().name("user/signup"));
    }

    @Test
    public void signup_ShouldRedirectToIndex() throws Exception
    {
        when(userService.usernameExists("Frank")).thenReturn(false);

        mockMvc.perform(post("/signup")
                .param("username", "Frank")
                .param("password", "password")
        )
                .andExpect(flash().attribute("flash", hasProperty("status", is(FlashMessage.Status.SUCCESS))))
                .andExpect(redirectedUrl("/"))
                .andExpect(status().is3xxRedirection());

        verify(userService).save(any(User.class));
    }

    @Test
    public void signup_ShouldThrowErrorIfUserExists() throws Exception
    {
        when(userService.usernameExists("Frank")).thenReturn(true);

        mockMvc.perform(post("/signup")
                .param("username", "Frank")
                .param("password", "password")
        )
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void login_ShouldRenderForm() throws Exception
    {
        mockMvc.perform(get("/login"))
                .andExpect(model().attribute("user", new User()))
                .andExpect(status().isOk())
                .andExpect(view().name("user/login"));
    }

    @Test
    public void profile_ShouldRenderUserDetails() throws Exception
    {
        when(userService.findByUsername("frank")).thenReturn(user1());

        mockMvc.perform(get("/profile/frank"))
                .andExpect(status().isOk())
                .andExpect(view().name("user/profile"))
                .andExpect(model().attribute("user", user1()));

        verify(userService).findByUsername("frank");
    }

    @Test
    public void myProfile_ShouldRenderUserDetails() throws Exception
    {
        List<GrantedAuthority> list = new ArrayList<GrantedAuthority>(
                Arrays.asList(new SimpleGrantedAuthority("STANDARD")));
        Authentication auth = new UsernamePasswordAuthenticationToken("Frank", "password", list);
        SecurityContextHolder.getContext().setAuthentication(auth);
        when(userService.findByUsername("Frank")).thenReturn(user1());

        mockMvc.perform(get("/myprofile")
                .principal(auth)
        )
                .andExpect(status().isOk())
                .andExpect(view().name("user/profile"))
                .andExpect(model().attribute("user", user1()));

        verify(userService).findByUsername("Frank");
    }

    @Test
    public void profile_ShouldShowErrorIfNotExists() throws Exception
    {
        when(userService.findByUsername("frank"))
                .thenThrow(new UserNotFoundException("No user with username frank was found"));

        mockMvc.perform(get("/profile/frank"))
                .andExpect(status().is4xxClientError());
    }
}
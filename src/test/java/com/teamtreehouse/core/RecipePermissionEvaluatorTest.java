package com.teamtreehouse.core;

import com.teamtreehouse.model.Recipe;
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
import org.springframework.security.core.context.SecurityContextHolder;

import static com.teamtreehouse.data.TestData.*;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;


public class RecipePermissionEvaluatorTest
{
    @InjectMocks
    private RecipePermissionEvaluator permissionEvaluator;

    @Mock
    private UserService userService;

    @Mock
    private RecipeService recipeService;

    @Before
    public void setUp() throws Exception
    {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void hasPermission_ShouldReturnTrueForAdminUser() throws Exception
    {
        User user = admin();
        Authentication auth = new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword(), user.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(auth);
        when(userService.findByUsername("Admin")).thenReturn(user);

        Boolean hasPermission = permissionEvaluator.hasPermission(auth, recipe1(), "edit");

        assertThat(hasPermission, is(true));
    }

    @Test
    public void hasPermissionWithId_ShouldReturnTrueForAdminUser() throws Exception
    {
        User user = admin();
        Authentication auth = new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword(), user.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(auth);
        when(userService.findByUsername("Admin")).thenReturn(user);

        Boolean hasPermission = permissionEvaluator.hasPermission(auth, 1L, "recipe", "edit");

        assertThat(hasPermission, is(true));
    }

    @Test
    public void hasPermission_ShouldReturnTrueForOwner() throws Exception
    {
        User user = user1();
        Authentication auth = new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword(), user.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(auth);
        when(userService.findByUsername("Frank")).thenReturn(user);
        Recipe recipe = recipe1();
        recipe.setCreatedBy(user);

        Boolean hasPermission = permissionEvaluator.hasPermission(auth, recipe, "edit");

        assertThat(hasPermission, is(true));
    }

    @Test
    public void hasPermissionWithId_ShouldReturnTrueForOwner() throws Exception
    {
        User user = user1();
        Authentication auth = new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword(), user.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(auth);
        when(userService.findByUsername("Frank")).thenReturn(user);
        Recipe recipe = recipe1();
        recipe.setCreatedBy(user);
        when(recipeService.findById(1L)).thenReturn(recipe);

        Boolean hasPermission = permissionEvaluator.hasPermission(auth, 1L, "recipe", "edit");

        assertThat(hasPermission, is(true));
    }

    @Test
    public void hasPermission_ShouldReturnFalseForNotOwnerNotAdmin() throws Exception
    {
        User user = user2();
        Authentication auth = new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword(), user.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(auth);
        when(userService.findByUsername("Livia")).thenReturn(user);

        Boolean hasPermission = permissionEvaluator.hasPermission(auth, recipe1(), "edit");

        assertThat(hasPermission, is(false));
    }

    @Test
    public void hasPermissionWithId_ShouldReturnFalseForNotOwnerNotAdmin() throws Exception
    {
        User user = user2();
        Authentication auth = new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword(), user.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(auth);
        when(userService.findByUsername("Livia")).thenReturn(user);
        when(recipeService.findById(1L)).thenReturn(recipe1());

        Boolean hasPermission = permissionEvaluator.hasPermission(auth, 1L, "recipe", "edit");

        assertThat(hasPermission, is(false));
    }
}

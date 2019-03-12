package com.teamtreehouse.service;

import com.teamtreehouse.exception.UserNotFoundException;
import com.teamtreehouse.model.Recipe;
import com.teamtreehouse.model.User;
import com.teamtreehouse.repository.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static com.teamtreehouse.data.TestData.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class UserServiceImplTest
{
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService service = new UserServiceImpl(userRepository);

    @Before
    public void setUp() throws Exception
    {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void findById_ReturnsUser() throws Exception
    {
        when(userRepository.findOne(1L)).thenReturn(user1());

        User user = service.findById(1L);

        assertThat(user, is(equalTo(user1())));
        verify(userRepository).findOne(1L);
    }

    @Test(expected = UserNotFoundException.class)
    public void findById_ThrowsExceptionIfNotFound() throws Exception
    {
        service.findById(1L);
    }

    @Test
    public void findByUsername_ReturnsUser() throws Exception
    {
        when(userRepository.findByUsernameIgnoreCase("frank")).thenReturn(user1());

        User user = service.findByUsername("frank");

        assertThat(user, is(equalTo(user1())));
        verify(userRepository).findByUsernameIgnoreCase("frank");
    }

    @Test(expected = UserNotFoundException.class)
    public void findByUsername_ThrowsExceptionIfNotFound() throws Exception
    {
        service.findByUsername("frank");
    }


    @Test
    public void save_SavesUser() throws Exception
    {
        service.save(user1());

        verify(userRepository).save(user1());
    }

    @Test
    public void usernameExists_ReturnsTrueIfUsernameExists() throws Exception
    {
        when(userRepository.findByUsernameIgnoreCase("frank")).thenReturn(user1());

        Boolean usernameExists = service.usernameExists("frank");

        assertThat(usernameExists, is(true));
    }

    @Test
    public void toggleFavorite_UpdatesFavorite() throws Exception
    {
        User user = user1();
        Recipe recipe = recipe1();

        service.toggleFavorite(user, recipe);

        assertThat(user.getFavorites().contains(recipe), is(true));
        verify(userRepository).save(user);
    }

    @Test
    public void isFavorite_returnsFavoriteStatus() throws Exception
    {
        User user = user1();
        Recipe recipe = recipe1();
        user.getFavorites().add(recipe);

        Boolean isFavorite = service.isFavorite(user, recipe);

        assertThat(isFavorite, is(true));
    }

    @Test
    public void removeFavorites_RemovesRecipeFromUserFavorites() throws Exception
    {
        User user = user1();
        Recipe recipe = recipe1();
        recipe.setId(1L);
        user.getFavorites().add(recipe);

        List<User> users = Arrays.asList(user);
        when(userRepository.findByFavoritesId(1L)).thenReturn(users);

        service.removeFavoriteFromAll(recipe);

        assertThat(users.get(0).getFavorites().isEmpty(), is(true));
        verify(userRepository).save(user);
    }

}

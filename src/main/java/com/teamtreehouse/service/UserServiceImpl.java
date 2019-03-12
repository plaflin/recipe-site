package com.teamtreehouse.service;

import com.teamtreehouse.exception.UserNotFoundException;
import com.teamtreehouse.model.Recipe;
import com.teamtreehouse.model.User;
import com.teamtreehouse.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class UserServiceImpl implements UserService
{
    @Autowired
    private UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
    }

    @Override
    public User findById(Long id)
    {
        User user = userRepository.findOne(id);
        if (user == null)
        {
            throw new UserNotFoundException(String.format("No user with id %s was found", id));
        }

        return user;
    }

    @Override
    public User findByUsername(String username)
    {
        User user = userRepository.findByUsernameIgnoreCase(username);
        if (user == null)
        {
            throw new UserNotFoundException(String.format("No user with username %s was found", username));
        }
        return user;
    }

    @Override
    public Boolean usernameExists(String username)
    {
        return userRepository.findByUsernameIgnoreCase(username) != null;
    }

    @Override
    public void save(User user)
    {
        userRepository.save(user);
    }

    @Override
    public void toggleFavorite(User user, Recipe recipe)
    {
        user.toggleFavorite(recipe);
        userRepository.save(user);
    }

    @Override
    public boolean isFavorite(User user, Recipe recipe)
    {
        if (user.getFavorites() != null)
        {
            return user.getFavorites().contains(recipe);
        }
        return false;
    }

    @Override
    public void removeFavoriteFromAll(Recipe recipe)
    {
        List<User> users = userRepository.findByFavoritesId(recipe.getId());
        users.forEach(user -> user.toggleFavorite(recipe));
        users.forEach(userRepository::save);
    }
}

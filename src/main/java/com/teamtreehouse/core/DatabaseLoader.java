package com.teamtreehouse.core;

import com.teamtreehouse.model.*;
import com.teamtreehouse.repository.RecipeRepository;
import com.teamtreehouse.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class DatabaseLoader implements ApplicationRunner
{
    private final RecipeRepository recipeRepository;
    private final UserRepository userRepository;

    @Autowired
    public DatabaseLoader(RecipeRepository recipeRepository, UserRepository userRepository)
    {
        this.recipeRepository = recipeRepository;
        this.userRepository = userRepository;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception
    {
        User user1 = new User("user1");
        user1.setPassword("user1");
        userRepository.save(user1);

        User user2 = new User("user2");
        user2.setPassword("user2");
        userRepository.save(user2);

        User admin = new User("Admin");
        admin.setPassword("Admin");
        admin.setRole(User.Role.ADMIN);
        userRepository.save(admin);

        List<Ingredient> ingredients1 = new ArrayList<>(Arrays.asList(
                new Ingredient("Elbow Macaroni", "Boxed", "3 boxes"),
                new Ingredient("Milk", "Fresh", "0.5L"),
                new Ingredient("Cheese", "Shredded", "1 lb")
        ));

        List<Ingredient> ingredients2 = new ArrayList<>(Arrays.asList(
                new Ingredient("Flour", "Sifted", "2 cups"),
                new Ingredient("Eggs", "Beaten", "2"),
                new Ingredient("Milk", "Fresh", "2 cups"),
                new Ingredient("Sugar","Sifted", "2 cups")
        ));

        List<Step> steps1 = new ArrayList<>(Arrays.asList(
                new Step("Prep"),
                new Step("Mix"),
                new Step("Heat")
        ));

        List<Step> steps2 = new ArrayList<>(Arrays.asList(
                new Step("Prep"),
                new Step("Mix"),
                new Step("Heat")
        ));

        List<Recipe> recipes = Arrays.asList(
                new Recipe.RecipeBuilder("Mac and Cheese", Category.DINNER)
                        .withDescription("Macaroni and Cheese")
                        .withPrepTime(15)
                        .withCookTime(30)
                        .withIngredients(ingredients1)
                        .withSteps(steps1)
                        .build(),

                new Recipe.RecipeBuilder("Pancakes", Category.BREAKFAST)
                        .withDescription("Pancakes")
                        .withPrepTime(20)
                        .withCookTime(60)
                        .withIngredients(ingredients2)
                        .withSteps(steps2)
                        .build()
        );

        user1.toggleFavorite(recipes.get(0));

        recipes.forEach(recipeRepository::save);
        userRepository.save(user1);
    }
}

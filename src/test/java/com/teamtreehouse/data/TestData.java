package com.teamtreehouse.data;

import com.teamtreehouse.model.*;

import java.util.Arrays;
import java.util.List;

public class TestData
{

    public static List<Recipe> recipeList()
    {
        return Arrays.asList(recipe1(), recipe2());
    }

    public static Recipe recipe1()
    {
        return new Recipe.RecipeBuilder("Cookies", Category.DESSERT)
                .withDescription("Delicious chocolate cookies")
                .withPrepTime(15)
                .withCookTime(30)
                .withSteps(steps1())
                .withIngredients(ingredients1())
                .withUser(user1())
                .build();
    }

    public static Recipe recipe2()
    {
        return new Recipe.RecipeBuilder("Chocolate Cake", Category.DESSERT)
                .withDescription("Fantastic chocolate cake")
                .withPrepTime(20)
                .withCookTime(60)
                .withSteps(steps2())
                .withIngredients(ingredients2())
                .withUser(user1())
                .build();
    }

    public static List<Step> steps1()
    {
        return Arrays.asList(
                new Step("Step 1"),
                new Step("Step 2"),
                new Step("Step 3")
        );
    }

    public static List<Step> steps2()
    {
        return Arrays.asList(
                new Step("Mix"),
                new Step("Bake")
        );
    }

    public static List<Ingredient> ingredients1()
    {
        return Arrays.asList(
                new Ingredient("Eggs", "Fresh", "3"),
                new Ingredient("Milk", "Fresh", "0.5L")
        );
    }

    public static List<Ingredient> ingredients2()
    {
        return Arrays.asList(
                new Ingredient("Eggs", "Fresh", "2"),
                new Ingredient("Chocolate", "Dark", "200g")
        );
    }

    public static User user1()
    {
        User user = new User("Frank");
        user.setPassword("password");
        return user;
    }

    public static User user2()
    {
        User user = new User("Livia");
        user.setPassword("password");
        return user;
    }

    public static User admin()
    {
        User user = new User("Admin");
        user.setPassword("password");
        user.setRole(User.Role.ADMIN);
        return user;
    }
}

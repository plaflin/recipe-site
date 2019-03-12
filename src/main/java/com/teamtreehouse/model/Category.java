package com.teamtreehouse.model;

public enum Category
{
    ALL("All Categories"),
    BREAKFAST("Breakfast"),
    LUNCH("Lunch"),
    DINNER("Dinner"),
    DESSERT("Dessert");

    private String name;

    Category(String name)
    {
        this.name = name;
    }

    public String getName()
    {
        return name;
    }
}

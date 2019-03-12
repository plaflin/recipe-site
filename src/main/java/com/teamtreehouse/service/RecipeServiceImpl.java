package com.teamtreehouse.service;

import com.teamtreehouse.exception.CategoryNotFoundException;
import com.teamtreehouse.exception.FileUploadException;
import com.teamtreehouse.exception.RecipeNotFoundException;
import com.teamtreehouse.model.Category;
import com.teamtreehouse.model.Recipe;
import com.teamtreehouse.repository.RecipeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Service
public class RecipeServiceImpl implements RecipeService
{
    @Autowired
    private RecipeRepository recipeRepository;

    @Autowired
    private UserService userService;

    @Override
    public List<Recipe> findAll()
    {
        return recipeRepository.findAll();
    }

    @Override
    public List<Category> allCategories()
    {
        return Arrays.asList(Category.values());
    }

    @Override
    public List<Recipe> findByCategory(Category category)
    {
        return recipeRepository.findByCategory(category);
    }

    @Override
    public List<Recipe> findByCategory(String category)
    {
        try
        {
            Category searchedCategory = Category.valueOf(category.toUpperCase());
            if (searchedCategory == Category.ALL)
            {
                return findAll();
            } else
            {
                return findByCategory(searchedCategory);
            }
        } catch (IllegalArgumentException e)
        {
            throw new CategoryNotFoundException(String.format("Category %s was not found", category));
        }
    }

    @Override
    public List<Recipe> findByDescription(String search)
    {
        return recipeRepository.findByDescriptionContainingIgnoreCase(search);
    }

    @Override
    public List<Recipe> findByIngredient(String search)
    {
        return recipeRepository.findByIngredientsItemIgnoreCase(search);
    }

    @Override
    public Recipe findById(Long id) throws RecipeNotFoundException
    {
        Recipe recipe = recipeRepository.findOne(id);
        if (recipe == null)
        {
            throw new RecipeNotFoundException(String.format("No recipe with id %s was found", id));
        }
        return recipe;
    }

    @Override
    public void save(Recipe recipe, MultipartFile file)
    {
        if (!file.isEmpty())
        {
            //if a new file was uploaded, set it as image
            try
            {
                recipe.setImage(file.getBytes());
                recipeRepository.save(recipe);
            } catch (IOException e)
            {
                throw new FileUploadException("The file could not be uploaded");
            }
        } else if (recipe.getId() != null)
        {
            //if no new file was uploaded, but recipe exists, associate object with existing image
            byte[] image = recipeRepository.findOne(recipe.getId()).getImage();
            recipe.setImage(image);
            recipeRepository.save(recipe);
        } else
        {
            recipeRepository.save(recipe);
        }
    }

    @Override
    public void delete(Long id) throws RecipeNotFoundException
    {
        Recipe recipe = findById(id);
        userService.removeFavoriteFromAll(recipe);
        recipeRepository.delete(id);
    }
}

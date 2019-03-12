package com.teamtreehouse.service;

import com.teamtreehouse.exception.CategoryNotFoundException;
import com.teamtreehouse.exception.FileUploadException;
import com.teamtreehouse.exception.RecipeNotFoundException;
import com.teamtreehouse.model.Category;
import com.teamtreehouse.model.Recipe;
import com.teamtreehouse.repository.RecipeRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static com.teamtreehouse.data.TestData.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

public class RecipeServiceImplTest
{
    @InjectMocks
    private RecipeService service = new RecipeServiceImpl();

    @Mock
    private RecipeRepository recipeRepository;

    @Mock
    private UserService userService;

    @Before
    public void setUp() throws Exception
    {
        MockitoAnnotations.initMocks(this);

    }

    @Test
    public void findAll_ReturnsAllRecipes() throws Exception
    {
        when(recipeRepository.findAll()).thenReturn(recipeList());

        List<Recipe> recipes = service.findAll();

        assertThat(recipes.size(), is(equalTo(2)));
        verify(recipeRepository).findAll();
    }

    @Test
    public void findByCategoryString_ReturnsOneRecipe() throws Exception
    {
        when(recipeRepository.findByCategory(Category.DESSERT)).thenReturn(Arrays.asList(recipe1()));

        List<Recipe> recipes = service.findByCategory("dessert");

        assertThat(recipes.size(), is(equalTo(1)));
        verify(recipeRepository).findByCategory(Category.DESSERT);
    }

    @Test
    public void allCategories_Returns5Categories() throws Exception
    {
        List<Category> categories = service.allCategories();

        assertThat(categories.size(), is(equalTo(5)));
    }

    @Test(expected = CategoryNotFoundException.class)
    public void findByCategory_ThrowsExceptionIfCategoryNotExists() throws Exception
    {
        service.findByCategory("cupcakes");
    }

    @Test
    public void findByDescription_ReturnsOneRecipe() throws Exception
    {
        when(recipeRepository.findByDescriptionContainingIgnoreCase("cookies")).thenReturn(Arrays.asList(recipe1()));

        List<Recipe> recipes = service.findByDescription("cookies");

        assertThat(recipes.size(), is(equalTo(1)));
        verify(recipeRepository).findByDescriptionContainingIgnoreCase("cookies");
    }

    @Test
    public void findByIngredient_ReturnsOneRecipe() throws Exception
    {
        when(recipeRepository.findByIngredientsItemIgnoreCase("milk")).thenReturn(Arrays.asList(recipe1()));

        List<Recipe> recipes = service.findByIngredient("milk");

        assertThat(recipes.size(), is(equalTo(1)));
        verify(recipeRepository).findByIngredientsItemIgnoreCase("milk");
    }

    @Test
    public void findById_ReturnsRecipe() throws Exception
    {
        when(recipeRepository.findOne(1L)).thenReturn(recipe1());

        Recipe recipe = service.findById(1L);

        assertThat(recipe, is(equalTo(recipe1())));
        verify(recipeRepository).findOne(1L);
    }

    @Test(expected = RecipeNotFoundException.class)
    public void findById_ThrowsExceptionIfNotFound() throws Exception
    {
        when(recipeRepository.findOne(1L)).thenReturn(null);

        service.findById(1L);

        verify(recipeRepository.findOne(1L));
    }

    @Test
    public void save_SavesRecipe() throws Exception
    {
        MockMultipartFile file = new MockMultipartFile("file", "test".getBytes());
        Recipe recipeWithFile = recipe1();
        recipeWithFile.setImage("test".getBytes());

        service.save(recipe1(), file);

        verify(recipeRepository).save(recipeWithFile);
    }

    @Test(expected = FileUploadException.class)
    public void save_ThrowsExceptionIfIOException() throws Exception
    {
        MockMultipartFile file = new MockMultipartFile("file", "test".getBytes());
        doThrow(IOException.class).when(recipeRepository).save(Matchers.any(Recipe.class));

        service.save(recipe1(), file);
    }


    @Test
    public void delete_DeletesRecipe() throws Exception
    {
        when(recipeRepository.findOne(1L)).thenReturn(recipe1());

        service.delete(1L);

        verify(userService).removeFavoriteFromAll(recipe1());
        verify(recipeRepository).delete(1L);
    }

    @Test(expected = RecipeNotFoundException.class)
    public void delete_ThrowsExceptionIfNotFound() throws Exception
    {
        when(recipeRepository.findOne(1L)).thenReturn(null);

        service.delete(1L);

        verify(recipeRepository).delete(1L);
    }

}

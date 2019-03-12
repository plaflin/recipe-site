package com.teamtreehouse.repository;

import com.teamtreehouse.model.Category;
import com.teamtreehouse.model.Recipe;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecipeRepository extends PagingAndSortingRepository<Recipe, Long>
{
    List<Recipe> findAll();

    List<Recipe> findByCategory(Category category);

    List<Recipe> findByDescriptionContainingIgnoreCase(String search);

    List<Recipe> findByIngredientsItemIgnoreCase(String search);

    Recipe findOne(Long id);
}

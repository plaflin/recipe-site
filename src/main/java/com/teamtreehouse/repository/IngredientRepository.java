package com.teamtreehouse.repository;

import com.teamtreehouse.model.Ingredient;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IngredientRepository extends PagingAndSortingRepository<Ingredient, Long>
{
}

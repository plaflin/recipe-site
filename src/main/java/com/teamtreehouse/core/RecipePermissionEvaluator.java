package com.teamtreehouse.core;

import com.teamtreehouse.model.Recipe;
import com.teamtreehouse.model.User;
import com.teamtreehouse.service.RecipeService;
import com.teamtreehouse.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Component
public class RecipePermissionEvaluator implements PermissionEvaluator
{
    @Autowired
    private UserService userService;

    @Autowired
    private RecipeService recipeService;

    @Override
    public boolean hasPermission(Authentication authentication, Object targetDomainObject, Object permission)
    {
        if ((authentication == null) || !authentication.isAuthenticated() || targetDomainObject == null ||
                !(targetDomainObject instanceof Recipe))
        {
            return false;
        }

        User user = userService.findByUsername(authentication.getName());

        if (user.getRole() == User.Role.ADMIN)
        {
            return true;
        }

        Recipe recipe = (Recipe)targetDomainObject;

        return recipe.getCreatedBy() != null && recipe.getCreatedBy().equals(user);
    }

    @Override
    public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType, Object permission)
    {
        if ((authentication == null) || !authentication.isAuthenticated() || targetId == null ||
                !(targetId instanceof Long) || (targetType == null))
        {
            return false;
        }

        User user = userService.findByUsername(authentication.getName());

        if (user.getRole() == User.Role.ADMIN)
        {
            return true;
        }

        Long id = (Long)targetId;
        Recipe recipe = recipeService.findById(id);

        return recipe.getCreatedBy() != null && recipe.getCreatedBy().equals(user);
    }
}

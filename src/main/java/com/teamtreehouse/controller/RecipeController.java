package com.teamtreehouse.controller;

import com.teamtreehouse.core.FlashMessage;
import com.teamtreehouse.model.*;
import com.teamtreehouse.service.RecipeService;
import com.teamtreehouse.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Controller
public class RecipeController
{
    @Autowired
    private RecipeService recipeService;

    @Autowired
    private UserService userService;

    @ModelAttribute("categories")
    public List<Category> allCategories()
    {
        return recipeService.allCategories();
    }

    @RequestMapping("/")
    public String allRecipes(Model model)
    {
        List<Recipe> recipes = recipeService.findAll();
        model.addAttribute("recipes", recipes);
        model.addAttribute("selectedCategory", Category.ALL);

        return "recipe/index";
    }

    @RequestMapping("/recipes/{id}")
    public String recipeDetails(@PathVariable Long id, Model model)
    {
        Recipe recipe = recipeService.findById(id);
        model.addAttribute("recipe", recipe);

        return "recipe/detail";
    }

    @RequestMapping("/recipes/{id}.png")
    @ResponseBody
    public byte[] gifImage(@PathVariable Long id) {
        return recipeService.findById(id).getImage();
    }

    @RequestMapping("/recipes/add")
    @PreAuthorize("isAuthenticated()")
    public String addForm(Model model)
    {
        if (!model.containsAttribute("recipe"))
        {
            Recipe recipe = new Recipe();
            recipe.getSteps().add(new Step());
            recipe.getIngredients().add(new Ingredient());
            model.addAttribute("recipe", recipe);
        }

        model.addAttribute("action", "/recipes");
        return "recipe/form";
    }

    @PostMapping(value = "/recipes")
    @PreAuthorize("isAuthenticated()")
    public String addRecipe(@Valid Recipe recipe, BindingResult result, @RequestParam MultipartFile file,
                            RedirectAttributes redirectAttributes)
    {
        if (result.hasErrors())
        {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.recipe", result);
            redirectAttributes.addFlashAttribute("recipe", recipe);
            redirectAttributes.addFlashAttribute("flash",
                    new FlashMessage("Invalid data", FlashMessage.Status.FAILURE));

            return "redirect:/recipes/add";
        }

        recipeService.save(recipe, file);
        redirectAttributes.addFlashAttribute("flash", new FlashMessage("New recipe added",
                FlashMessage.Status.SUCCESS));

        return "redirect:/";
    }

    @RequestMapping(value = "/recipes/{id}/delete")
    @PreAuthorize("isAuthenticated() and hasPermission(#id, 'Recipe', 'edit')")
    public String deleteRecipe(@PathVariable Long id, RedirectAttributes redirectAttributes)
    {
        recipeService.delete(id);
        redirectAttributes.addFlashAttribute("flash", new FlashMessage("Recipe deleted",
                FlashMessage.Status.SUCCESS));

        return "redirect:/";
    }

    @RequestMapping(value = "/recipes/{id}/edit")
    @PreAuthorize("isAuthenticated() and hasPermission(#id, 'Recipe', 'edit')")
    public String editForm(@PathVariable Long id, Model model)
    {
        Recipe recipe = recipeService.findById(id);
        if (!model.containsAttribute("recipe"))
        {
            model.addAttribute("recipe", recipe);
        }
        model.addAttribute("action", "/recipes/" + recipe.getId());

        return "recipe/form";
    }

    @PostMapping(value = "/recipes/{id}")
    @PreAuthorize("isAuthenticated() and hasPermission(#recipe, 'edit')")
    public String editRecipe(@Valid Recipe recipe, BindingResult result, @RequestParam MultipartFile file, RedirectAttributes redirectAttributes)
    {
        if (result.hasErrors())
        {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.recipe", result);
            redirectAttributes.addFlashAttribute("recipe", recipe);
            redirectAttributes.addFlashAttribute("flash",
                    new FlashMessage("Invalid data", FlashMessage.Status.FAILURE));

            return String.format("redirect:/recipes/%s/edit", recipe.getId());
        }

        recipeService.save(recipe, file);
        redirectAttributes.addFlashAttribute("flash", new FlashMessage("Recipe saved",
                FlashMessage.Status.SUCCESS));

        return String.format("redirect:/recipes/%s", recipe.getId());
    }

    @RequestMapping(value = "/recipes/{id}/favorite")
    @PreAuthorize("isAuthenticated()")
    public String favoriteRecipe(@PathVariable Long id, Authentication authentication)
    {
        User user = userService.findByUsername(authentication.getName());
        Recipe recipe = recipeService.findById(id);
        userService.toggleFavorite(user, recipe);

        return "redirect:/recipes/" + id;
    }

    @RequestMapping("/recipes/category/{category}")
    public String recipesByCategory(@PathVariable String category, Model model)
    {
        List<Recipe> recipes = recipeService.findByCategory(category);
        model.addAttribute("recipes", recipes);
        model.addAttribute("selectedCategory", Category.valueOf(category.toUpperCase()));

        return "recipe/index";
    }

    @RequestMapping("/recipes/search")
    public String search(
            @RequestParam(value = "description", required = false) String description,
            @RequestParam(value = "ingredient", required = false) String ingredient,
            Model model)
    {
        List recipes = new ArrayList();
        if (description != null && !description.isEmpty())
        {
            recipes = recipeService.findByDescription(description);
        } else if (ingredient != null && !ingredient.isEmpty())
        {
            recipes = recipeService.findByIngredient(ingredient);
        }

        model.addAttribute("recipes", recipes);
        model.addAttribute("selectedCategory", Category.ALL);

        return "recipe/index";
    }
}

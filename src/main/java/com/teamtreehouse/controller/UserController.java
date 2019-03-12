package com.teamtreehouse.controller;

import com.teamtreehouse.core.FlashMessage;
import com.teamtreehouse.exception.MultipleUsersWithSameNameException;
import com.teamtreehouse.model.User;
import com.teamtreehouse.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Controller
public class UserController
{
    @Autowired
    private UserService userService;

    @RequestMapping("/signup")
    public String signupForm(Model model, HttpServletRequest request)
    {
        if (!model.containsAttribute("user"))
        {
            User user = new User();
            model.addAttribute("user", user);
        }

        return "user/signup";
    }

    @PostMapping(value = "/signup")
    public String addUser(@Valid User user, BindingResult result, RedirectAttributes redirectAttributes)
    {
        if (result.hasErrors())
        {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.user", result);
            redirectAttributes.addFlashAttribute("user", user);
            redirectAttributes.addFlashAttribute("flash",
                    new FlashMessage("Invalid data", FlashMessage.Status.FAILURE));
            return "redirect:/signup";
        }

        if (userService.usernameExists(user.getUsername()))
        {
            throw new MultipleUsersWithSameNameException(
                    String.format("User could not be added. A user with the name %s already exists.",
                            user.getUsername()));
        }

        userService.save(user);
        redirectAttributes.addFlashAttribute("flash",
                new FlashMessage("Thank you for signing up", FlashMessage.Status.SUCCESS));

        return "redirect:/";
    }

    @RequestMapping(path = "/login")
    public String loginForm(Model model, HttpServletRequest request)
    {
        model.addAttribute("user", new User());
        try
        {
            Object flash = request.getSession().getAttribute("flash");
            model.addAttribute("flash", flash);

            request.getSession().removeAttribute("flash");
        } catch (Exception ex)
        {
            // "flash" session attribute must not exist. Do nothing and proceed normally
        }
        return "user/login";
    }

    @RequestMapping("/myprofile")
    public String myProfile(Model model, Authentication authentication)
    {
        if (authentication == null || !authentication.isAuthenticated())
        {
            return "redirect:/login";
        }

        User user = userService.findByUsername(authentication.getName());
        model.addAttribute("user", user);

        return "user/profile";
    }

    @RequestMapping("/profile/{username}")
    public String profilePage(@PathVariable("username") String username, Model model)
    {
        User user = userService.findByUsername(username);
        model.addAttribute("user", user);

        return "user/profile";
    }
}

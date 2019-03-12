package com.teamtreehouse.core.config;

import com.teamtreehouse.model.User;
import com.teamtreehouse.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class SpringSecurityAuditorAware implements AuditorAware<User>
{
    @Autowired
    private UserService userService;

    @Override
    public User getCurrentAuditor()
    {
        User currentAuditor = null;

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated())
        {
            currentAuditor = userService.findByUsername(authentication.getName());
        }

        return currentAuditor;
    }
}

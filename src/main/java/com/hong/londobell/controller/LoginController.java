package com.hong.londobell.controller;

import com.hong.londobell.tenant.entity.CustomUserDetails;
import com.hong.londobell.tenant.entity.User;
import com.hong.londobell.tenant.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.Optional;

@Controller
public class LoginController {

    @Autowired
    private UserService userService;

    @GetMapping("/")
    public String showHome(Model model) {
        User currentUser = userService.findByUsername(getLoggedInUsername().get());
        List<User> userList = userService.findAllUsers();
        getLoggedInUsername().ifPresent(f -> { model.addAttribute("userName", f); });
        getTenantName().ifPresent(d -> { model.addAttribute("tenantName", d); });
        model.addAttribute("currentUser", currentUser);
        model.addAttribute("userList", userList);
        return "webapp";
    }

    @GetMapping("/login")
    public String showLogin() {
        return "login";
    }

    private Optional<String> getLoggedInUsername() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String userName = null;
        if (auth != null && !auth.getClass().equals(AnonymousAuthenticationToken.class)) {
            // User user = (User) auth.getPrincipal();
            CustomUserDetails userDetails = (CustomUserDetails) auth.getPrincipal();
            userName = userDetails.getUsername();
        }

        return Optional.ofNullable(userName);
    }

    private Optional<String> getTenantName() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String tenantName = null;
        if (auth != null && !auth.getClass().equals(AnonymousAuthenticationToken.class)) {
            // User user = (User) auth.getPrincipal();
            CustomUserDetails userDetails = (CustomUserDetails) auth.getPrincipal();
            tenantName = userDetails.getTenant();
        }
        return Optional.ofNullable(tenantName);
    }
}

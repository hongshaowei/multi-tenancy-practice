package com.hong.londobell.controller;

import com.hong.londobell.tenant.entity.User;
import com.hong.londobell.tenant.service.UserService;
import com.hong.londobell.util.TenantContextHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class UserRestController {

    @Autowired
    private UserService userService;

    /**
     *
     * @param tenantId
     * @return
     */
    @PostMapping("/users")
    public List<User> findAllUsers(@RequestParam("tenantId") String tenantId) {
        TenantContextHolder.setTenantId(tenantId);
        List<User> userList = userService.findAllUsers();
        return userList;
    }
}

package com.hong.londobell.tenant.service;

import com.hong.londobell.tenant.entity.User;
import com.hong.londobell.tenant.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Implementation of the {@link UserService} which accesses the {@link com.hong.londobell.tenant.entity.User}
 * entity. This is the recommended way to access the entities through an
 * interface rather than using the corresponding repository. This allows for
 * separation into repository code and the service layer.
 *
 * @author Hong ShaoWei
 * @since ver 1.0 (March 2019)
 * @version 1.0
 */
@Service
public class UserServiceImpl implements UserService{

    private static final Logger LOG = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public User save(User user) {
        // Encrypt the code
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User justSavedUser = userRepository.save(user);
        LOG.info("User: " + justSavedUser.getUsername() + " saved.");
        return justSavedUser;
    }

    @Override
    public String findLoggedInUsername() {
        Object userDetails = SecurityContextHolder.getContext().getAuthentication().getDetails();
        if (userDetails instanceof UserDetails) {
            String username = ((UserDetails) userDetails).getUsername();
            LOG.info("Logged in username: " + username);
            return username;
        }
        return null;
    }

    @Override
    public User findByUsernameAndTenantname(String username, String tenant) {
        User user = userRepository.findByUsernameAndTenantname(username, tenant);
        LOG.info("Found user with username: " + user.getUsername() + " from tenant: " + user.getTenant());
        return user;
    }

    @Override
    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}

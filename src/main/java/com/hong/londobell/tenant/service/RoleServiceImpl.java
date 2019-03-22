package com.hong.londobell.tenant.service;

import com.hong.londobell.tenant.entity.Role;
import com.hong.londobell.tenant.repository.RoleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Implementation of the {@link RoleService} which accesses the {@link Role}
 * entity. This is the recommended way to access the entities through an
 * interface rather than using the corresponding repository. This allows for
 * separation into repository code and the service layer.
 *
 * @author Hong ShaoWei
 * @since ver 1.0 (March 2019)
 * @version 1.0
 */
@Service
public class RoleServiceImpl implements RoleService{

    private static final Logger LOG = LoggerFactory.getLogger(RoleServiceImpl.class);

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public Role findByRole(String roleName) {
        Role role = roleRepository.findByRole(roleName);
        LOG.info("Role: " + role.getRole() + " found.");
        return role;
    }
}

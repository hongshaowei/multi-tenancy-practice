package com.hong.londobell.tenant.service;

import com.hong.londobell.tenant.entity.Role;

/**
 * Service definition which accesses the {@link Role} entity. This is the
 * recommended way to access the entities through an interface rather than using
 * the corresponding repository. This allows for separation into repository code
 * and the service layer.
 *
 * @author Hong ShaoWei
 * @since ver 1.0 (March 2019)
 * @version 1.0
 */
public interface RoleService {

    Role findByRole(String roleName);
}

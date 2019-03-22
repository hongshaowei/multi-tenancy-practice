package com.hong.londobell.tenant.repository;

import com.hong.londobell.tenant.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for the {@link Role} JPA entity. Any custom methods, not already
 * defined in {@link JpaRepository}, are to be defined here.
 *
 * @author Hong ShaoWei
 * @since ver 1.0 (March 2019)
 * @version 1.0
 */
@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    /**
     * Query to find a Role entity based on the {@link Role} name
     *
     * @param role
     * @return
     */
    Role findByRole(String role);
}

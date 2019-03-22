package com.hong.londobell.tenant.service;

import com.hong.londobell.tenant.entity.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Service definition which accesses the {@link com.hong.londobell.tenant.entity.User} entity.
 * This is the recommended way to access the entities through an interface
 * rather than using the corresponding repository directly. This allows for
 * separation into repository code and the service layer.
 *
 * @author Hong ShaoWei
 * @since ver 1.0 (March 2019)
 * @version 1.0
 */
public interface UserService {

    User save(User user);

    String findLoggedInUsername();

    @Query("select p from User p where p.username = :username and p.tenant = :tenant")
    User findByUsernameAndTenantname(@Param("username") String username, @Param("tenant") String tenant);

    List<User> findAllUsers();

    User findByUsername(String username);
}

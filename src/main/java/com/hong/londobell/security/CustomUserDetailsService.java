package com.hong.londobell.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * @author Hong ShaoWei
 * @since ver 1.0 (March 2019)
 * @version 1.0
 */
public interface CustomUserDetailsService {

    UserDetails loadUserByUsernameAndTenantname(String username, String tenantName) throws UsernameNotFoundException;
}

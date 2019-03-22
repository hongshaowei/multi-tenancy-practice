package com.hong.londobell.security;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * {@link CustomAuthenticationToken} is provided to the
 * {@link AuthenticationProvider} so that the user can be authenticated. This
 * token is enhanced by including the additional tenant field
 * extracted by the {@link CustomAuthenticationFilter} from the user submitted
 * login form.
 *
 * @author Hong ShaoWei
 * @since ver 1.0 (March 2019)
 * @version 1.0
 */
public class CustomAuthenticationToken extends UsernamePasswordAuthenticationToken {

    private static final long serialVersionUID = 1L;

    /** The tenant i.e. database identifier */
    private String tenant;

    /**
     *
     * @param principal
     * @param credentials
     * @param tenant
     */
    public CustomAuthenticationToken(Object principal, Object credentials, String tenant) {
        super(principal, credentials);
        this.tenant = tenant;
        super.setAuthenticated(false);
    }

    /**
     *
     * @param principal
     * @param credentials
     * @param tenant
     * @param authorities
     */
    public CustomAuthenticationToken(Object principal, Object credentials, String tenant,
                                     Collection<? extends GrantedAuthority> authorities) {
        super(principal, credentials, authorities);
        this.tenant = tenant;
        // must use super, as override
        super.setAuthenticated(true);
    }

    public String getTenant() {
        return this.tenant;
    }
}

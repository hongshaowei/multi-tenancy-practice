package com.hong.londobell.security;

import com.hong.londobell.util.TenantContextHolder;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * This is the filter which is called first when the user submits the login form.
 * This filter extracts the username, password, and tenant fields from the request.
 * These values are used to create an instance of
 * {@link CustomAuthenticationToken} which is passed to the
 * {@link AuthenticationProvider} for authentication.
 *
 * @author Hong ShaoWei
 * @since ver 1.0 (March 2019)
 * @version 1.0
 */
public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    public static final String SPRING_SECURITY_FORM_TENANT_NAME_KEY = "tenant";

    /**
     * (non-Javadoc)
     *
     * @see org.springframework.security.web.authentication
     * UsernamePasswordAuthenticationFilter#attemptAuthentication(javax.servlet.http.HttpServletRequest,
     *                                                              javax.servlet.http.HttpServletResponse)
     */
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        if (!request.getMethod().equals("POST")) {
            throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
        }

        CustomAuthenticationToken authRequest = getAuthRequest(request);

        // put in tenant context threadLocal
        String tenant = authRequest.getTenant();
        TenantContextHolder.setTenantId(tenant);

        setDetails(request, authRequest);

        return this.getAuthenticationManager().authenticate(authRequest);
    }

    /**
     *
     * @param request
     * @return
     */
    private CustomAuthenticationToken getAuthRequest(HttpServletRequest request) {

        String username = obtainUsername(request);
        String password = obtainPassword(request);
        String tenant = obtainTenant(request);

        if (username == null) {
            username = "";
        }
        if (password == null) {
            password = "";
        }
        if (tenant == null) {
            tenant = "";
        }

        return new CustomAuthenticationToken(username, password, tenant);
    }

    /**
     *
     * @param request
     * @return
     */
    private String obtainTenant(HttpServletRequest request) {
        return request.getParameter(SPRING_SECURITY_FORM_TENANT_NAME_KEY);
    }
}

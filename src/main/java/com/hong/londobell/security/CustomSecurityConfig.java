package com.hong.londobell.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Configuration of security related beans and methods.
 * The access to different urls within the application is defined here.
 *
 * @author Hong ShaoWei
 * @since ver 1.0 (March 2019)
 * @version 1.0
 */
@Configuration
@EnableWebSecurity
public class CustomSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Bean(name = "passwordEncoder")
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * The page to show if authentication fails
     *
     * @return
     */
    public SimpleUrlAuthenticationFailureHandler failureHandler() {
        return new SimpleUrlAuthenticationFailureHandler("/login?error=true");
    }

    public SimpleUrlAuthenticationSuccessHandler successHandler() {
        return new SimpleUrlAuthenticationSuccessHandler("/");
    }

    /**
     * Create an instance of the custom authentication filter which intercepts
     * and processes the end user's login form submission for further
     * authentication processing. This filter is added before other filters so
     * that it can intercept the user login form submission and extract the the
     * additional 'tenant' field
     *
     * @return
     * @throws Exception
     */
    public CustomAuthenticationFilter authenticationFilter() throws Exception {
        CustomAuthenticationFilter filter = new CustomAuthenticationFilter();
        filter.setAuthenticationManager(authenticationManagerBean());
        filter.setAuthenticationFailureHandler(failureHandler());
        filter.setAuthenticationSuccessHandler(successHandler());
        return filter;
    }

    /**
     * Authentication provider which provides the logged in user's credentials
     * for verification and authentication if they are correct
     *
     * @return
     */
    public AuthenticationProvider authProvider() {
        // The custom authentication provider defined for this app
        CustomUserDetailsAuthenticationProvider provider = new CustomUserDetailsAuthenticationProvider(
                passwordEncoder(), userDetailsService);
        return provider;
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authProvider());
    }

    /**
     * This is where access to various resources (urls)
     * in the application is defined.
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.addFilterBefore(authenticationFilter(), UsernamePasswordAuthenticationFilter.class)
            .authorizeRequests()
                .antMatchers("/static/**").permitAll()
                .antMatchers("/").authenticated()
                .antMatchers("/users/**").authenticated()
            .and()
            .formLogin()
                .loginPage("/login")
                .permitAll()
            .and()
                .logout()
                .permitAll();
    }
}

package com.saskcow.bowling.config;

import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import javax.servlet.http.HttpServletResponse;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .csrf().disable()
            .exceptionHandling().authenticationEntryPoint(
                ((request, response, authException) -> response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Please Login!")))
                .and()
            .authorizeRequests()
                .antMatchers("/login").permitAll()
                .antMatchers("/style").permitAll()
                .antMatchers("/built").permitAll()
                .antMatchers(HttpMethod.GET).permitAll()
                .antMatchers(HttpMethod.POST).authenticated()
                .antMatchers(HttpMethod.DELETE).authenticated()
                .and()
            .oauth2Login();
    }
}

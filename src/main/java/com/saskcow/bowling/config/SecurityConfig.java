package com.saskcow.bowling.config;

import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .authorizeRequests()
                .antMatchers(HttpMethod.GET,"/**").permitAll()
                .antMatchers(HttpMethod.POST,"/**").permitAll()
                .antMatchers(HttpMethod.DELETE,"/**").authenticated()
                .and()
            .oauth2Login();
    }
}

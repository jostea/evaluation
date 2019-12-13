package com.internship.evaluation.config;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .httpBasic()
                .and()
                .authorizeRequests()
                .antMatchers("/").permitAll()
                .antMatchers("/templates/registration").permitAll()
                .antMatchers("/templates/test/testStart").permitAll()
                .antMatchers("/testsrest/**").permitAll();
    }

}

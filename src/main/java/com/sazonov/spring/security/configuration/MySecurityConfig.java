package com.sazonov.spring.security.configuration;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class MySecurityConfig {

    @Autowired
    DataSource dataSource;

//    @Bean
//    public InMemoryUserDetailsManager userDetailsService() {
//        InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
//        manager.createUser(User.withDefaultPasswordEncoder()
//                .username("Vitaliy")
//                .password("Sazonov")
//                .roles("EMPLOYEE").build());
//        manager.createUser(User.withDefaultPasswordEncoder()
//                .username("John")
//                .password("Doe")
//                .roles("HR").build());
//        manager.createUser(User.withDefaultPasswordEncoder()
//                .username("Jane")
//                .password("Doe")
//                .roles("MANAGER","HR").build());
//        return manager;
//    }

    @Bean
    public JdbcUserDetailsManager userDetailsManager(){
        JdbcUserDetailsManager manager = new JdbcUserDetailsManager(dataSource);
        return manager;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        http.authorizeHttpRequests(user ->
                user.requestMatchers(new AntPathRequestMatcher("/"))
                        .hasAnyRole("HR","MANAGER","EMPLOYEE")
                        .requestMatchers(new AntPathRequestMatcher("/manager_info/**"))
                        .hasRole("MANAGER")
                        .requestMatchers(new AntPathRequestMatcher("/hr_info/**"))
                        .hasRole("HR")
                        .anyRequest().authenticated())
                .formLogin(Customizer.withDefaults())
                .httpBasic(Customizer.withDefaults());
        return http.build();
    }
}

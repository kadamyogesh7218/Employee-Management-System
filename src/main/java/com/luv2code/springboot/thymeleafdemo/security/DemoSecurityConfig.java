package com.luv2code.springboot.thymeleafdemo.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import javax.sql.DataSource;

@Configuration
public class DemoSecurityConfig {

    @Bean
    public UserDetailsManager userDetailsManager(DataSource dataSource){
        JdbcUserDetailsManager theUserDetailsManager=new JdbcUserDetailsManager(dataSource);

        theUserDetailsManager
                .setUsersByUsernameQuery("select user_id,pw,active from members where user_id=?");

        theUserDetailsManager
                .setAuthoritiesByUsernameQuery("select user_id,role from roles where user_id=?");

        return new JdbcUserDetailsManager(dataSource);

    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        http.authorizeHttpRequests(configurer->
                configurer
                        .requestMatchers(HttpMethod.GET,"/employees/list").hasRole("EMPLOYEE")
                        .requestMatchers(HttpMethod.GET,"/employees/showFormForAdd").hasRole("MANAGER")
                        .requestMatchers(HttpMethod.GET,"/employees/showFormForUpdate").hasRole("MANAGER")
                        .requestMatchers(HttpMethod.POST,"/employees/save").hasRole("MANAGER")
                        .requestMatchers(HttpMethod.GET,"/employees/delete").hasRole("ADMIN")
                        .anyRequest().authenticated()
                    )
                .formLogin(form->
                        form
                                .loginPage("/showMyLoginPage")
                                .loginProcessingUrl("/authenticateTheUser")
                                .permitAll()
                        )
                .logout(logout->logout.permitAll()
                )
                .exceptionHandling(configurer->
                        configurer
                                .accessDeniedPage("/access-denied")

                );


        http.httpBasic(Customizer.withDefaults());
        http.csrf(csrf->csrf.disable());
        return http.build();
    }

}

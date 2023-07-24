package com.popsyn.springbootuserregristartion.configuration;

import com.popsyn.springbootuserregristartion.entity.User;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class UserRegistrationConfiguration {

    @Bean
    public PasswordEncoder passwordEncoder(){
        return  new BCryptPasswordEncoder();
    }


    @Bean
    @Order(1)
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        /*return httpSecurity.cors(Customizer.withDefaults()).csrf(csrf-> csrf.disable())
                .authorizeHttpRequests(auth -> auth.requestMatchers(HttpMethod.POST,"/register")
                        .permitAll().requestMatchers(HttpMethod.GET ,"/users").hasAnyAuthority("USER", "ADMIN"))
                .formLogin(Customizer.withDefaults()).build();*/

        httpSecurity.securityMatcher("/register/**").cors(Customizer.withDefaults())
                .csrf(csrf->csrf.disable()).authorizeHttpRequests(auth->auth.anyRequest().permitAll())
                .httpBasic(Customizer.withDefaults());
        return httpSecurity.build();
    }

    @Bean
    @Order(2)
    public SecurityFilterChain roleFilter(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.securityMatcher("/users/**").cors(Customizer.withDefaults()).csrf(csrf->csrf.disable()).authorizeHttpRequests(auth->auth.anyRequest()
                .hasAnyAuthority("ADMIN","USER")).httpBasic(Customizer.withDefaults());
        return httpSecurity.build();
    }
    @Bean
    @Order(3)
    public SecurityFilterChain firmFilter(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.cors(Customizer.withDefaults()).
                csrf(csrf->csrf.disable()).authorizeHttpRequests(authorize -> authorize
                        .anyRequest().authenticated()
                )
                .formLogin(Customizer.withDefaults());
        return httpSecurity.build();
    }


}

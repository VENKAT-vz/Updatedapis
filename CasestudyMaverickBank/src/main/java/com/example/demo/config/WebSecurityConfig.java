package com.example.demo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsService userDetailsService;
    
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder());
    }
    
    @Override
    protected void configure(HttpSecurity http) throws Exception {
//    	http
//        .authorizeRequests()
//    		.antMatchers("/users/addUser").permitAll() 
//    		.antMatchers("/registerAdmin").permitAll() 
//            .antMatchers("/admin/**").hasRole("ADMIN")
//            .antMatchers("/manager/**").hasRole("BANK_MANAGER")
//            .antMatchers("/users/**").hasRole("CUSTOMER")
//            .antMatchers("/login/**").hasRole("CUSTOMER")
//            .anyRequest().authenticated()
    	
    	http
        .authorizeRequests()
            .anyRequest().permitAll()
            
 
        .and()
        .httpBasic() 
        .and()
        .formLogin().permitAll()
        .and()
        .logout().permitAll()
        .and()
        .csrf().disable();  
    }
}
package com.example.demo.util;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.domain.Login;
import com.example.demo.repository.LoginRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private LoginRepository loginrepo;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Login login = loginrepo.findByUsername(username);
        if (login == null) {
            throw new UsernameNotFoundException("User not found");
        }

        Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
        
        switch (login.getRole()) {
            case "Admin":
                grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
                break;
            case "BankManager":
                grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_BANK_MANAGER"));
                break;
            case "Customer":
                grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_CUSTOMER"));
                break;
            default:
                throw new IllegalArgumentException("Unknown role: " + login.getRole());
        }

        return new org.springframework.security.core.userdetails.User(
        		login.getUsername(),
        		login.getPassword(),  
                grantedAuthorities);
    }
}

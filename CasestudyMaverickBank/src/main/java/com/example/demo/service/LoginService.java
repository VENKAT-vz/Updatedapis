package com.example.demo.service;


import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.domain.Account;
import com.example.demo.domain.Login;
import com.example.demo.repository.LoginRepository;


@Service
public class LoginService {

	@Autowired
	private LoginRepository lrepo;
	
    @Autowired
    private AccountService accountService;
    
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    
    public String registerUser(Login login) throws ClassNotFoundException, SQLException {
        if (lrepo.findByEmailid(login.getEmailid()) == null && lrepo.findByUsername(login.getUsername()) == null) {
            
            String encryptedPassword = bCryptPasswordEncoder.encode(login.getPassword());
            login.setPassword(encryptedPassword);
            login.setStatus("inactive");
            lrepo.save(login);
            return "New Login successfully registered";
        }
        return "User with credentials already exists";
    }
    


    public String correctEncode(String username, String rawPassword) {
        Login login = lrepo.findByUsername(username);
        
        if (login != null) {
            String storedPasswordHash = login.getPassword();
            
            if (bCryptPasswordEncoder.matches(rawPassword, storedPasswordHash)) {
                return "Password matches!";
            } else {
                return "Incorrect password!";
            }
        }
        return "User not found!";
    }

	public String loginuser(String username, String password) throws ClassNotFoundException, SQLException {

        Login login = lrepo.findByUsername(username);

        if (login == null || !bCryptPasswordEncoder.matches(password, login.getPassword())) {
            return "Invalid Credentials. Try again.";
        } 
        List<Account> accounts = accountService.getAccountByNumberByusername(username);
        
        StringBuilder result = new StringBuilder("Login successful\n");
         for (Account account : accounts) 
        {
            	result.append("In the ")
                  .append(account.getAccountType())
                   .append(" account of account number ")
                  .append(account.getAccountNumber())
                 .append("\nThe available balance is ")
                  .append(account.getBalance())
                  .append("\n\n");
        }

        return result.toString();
	}

}

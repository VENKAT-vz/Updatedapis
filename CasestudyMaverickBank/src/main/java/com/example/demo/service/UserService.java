package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.example.demo.domain.User;
import com.example.demo.repository.UserRepository;

import java.util.List;
import java.util.Map;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepo;
	
//	@Autowired
//	private JdbcTemplate jdbcTemplate;
	
    public String addUser(User user) {
        if (userRepo.existsById(user.getUsername())) {
            return "User with username already exists!";
        } 
        else if (userRepo.findByEmailid(user.getEmailid()) != null) {
            return "User with email ID already exists!";
        }
        userRepo.save(user);
        return "User added successfully!";
    }

    public List<User> showAllUsers() {
        return userRepo.findAll();
    }

    public User searchUser(String usernameOrEmail) {
        User user = userRepo.findByUsername(usernameOrEmail);
        if (user == null) 
        {
            user = userRepo.findByEmailid(usernameOrEmail);
        }

        return user;
    }
    
    public boolean deleteUser(String username) {
        User user = userRepo.findByUsername(username);
        if (user!=null) 
        {
            userRepo.deleteById(username);
            return true;
        } 
        else
            return false;
    }
    
	 public String approveUserAccounts(String username) {
		    int rowsAffected = userRepo.approveUserAccount(username);   
		    if (rowsAffected > 0) 
		    {
		        return "User Account approved successfully.";
		    } 
		    else 
		        return " User Account approval failed. Account not found.";   
		}
	 
	 public String closeUserAccounts(String username) {
		    int rowsAffected = userRepo.closeUserAccount(username);   
		    if (rowsAffected > 0) 
		    {
		        return "User Account closed successfully.";
		    } 
		    else 
		        return " User Account closing failed. Account not found.";   
		}
	 
	 public List<User> unapprovedUserAccounts() {
		    return userRepo.unapproveduseraccounts();
		}


}

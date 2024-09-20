package com.example.demo.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.domain.User;
import com.example.demo.service.LoginService;
import com.example.demo.service.UserService;

@RestController
@RequestMapping("/admin")
@CrossOrigin("http://localhost:60753")
public class AdminController {
		
		@Autowired
		private UserService userservice;
		
		@Autowired
		private UserService service;
		
		@Autowired
		private LoginService lservice;
		
		@GetMapping(value = "/showUnapproved")
		public List<User> showUnapproved() {
		    return service.unapprovedUserAccounts();
		}
		
	    @PutMapping("/approve-user-account/{username}")
	    public String approveAccount(@PathVariable String username) {
	    	return userservice.approveUserAccounts(username);
	    }
	    
	    @PutMapping("/close-user-account/{username}")
	    public String closeuserAccount(@PathVariable String username) {
	    	return userservice.closeUserAccounts(username);
	    }
	    
		@GetMapping(value = "/showAll")
		public List<User> showAllUsers() {
		    return service.showAllUsers();
		}
		
		@GetMapping(value="/searchUser/{username}")
		public User searchuser(@PathVariable String username) {
			return service.searchUser(username);
		}
		
		@PutMapping(value="/approveLogin/{username}")
		public String approveAccess(@PathVariable String username) {
			return lservice.approveaccess(username);
		}
		
		 @DeleteMapping("/delete/{username}")
		public String deleteUser(@PathVariable String username) {
		      boolean isDeleted = service.deleteUser(username);
		        if (isDeleted) {
		            return "User deleted successfully.";
		        } else 
		            return "User not found.";
		    }
	
	}


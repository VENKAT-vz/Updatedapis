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

import com.example.demo.domain.ApprovalRequest;
import com.example.demo.domain.User;
import com.example.demo.domain.Usershow;
import com.example.demo.service.LoginService;
import com.example.demo.service.UserService;

@RestController
@RequestMapping("/admin")
@CrossOrigin("http://localhost:4200")
public class AdminController {
		
		@Autowired
		private UserService userservice;
		
		@Autowired
		private UserService service;
		
		@Autowired
		private LoginService lservice;
		
		//shows every request made to Admin regarding user account 
		@GetMapping(value = "/showRequests")
		public List<ApprovalRequest> showUnapproved() {
		    return service.requests();
		}
		
		//Approval, Rejection and Closing of user account
	    @PutMapping("/approve-user-account/{requestId}")
	    public String approveAccount(@PathVariable int requestId) {
	    	return userservice.approveUserAccounts(requestId);
	    }
	    
	    @PutMapping("/reject-user-account/{requestId}")
	    public String RejectAccount(@PathVariable int requestId) {
	    	return userservice.RejectUserAccount(requestId);
	    }
	    
	    @PutMapping("/close-user-account/{requestId}")
	    public String closeuserAccount(@PathVariable int requestId) {
	    	return userservice.CloseUserAccounts(requestId);
	    }
	  
		//standardmethods
		@GetMapping(value = "/showAll")
		public List<Usershow> showAllUsers() {
		    return service.showAllUsers();
		}
		
		@GetMapping(value="/searchUser/{username}")
		public Usershow searchuser(@PathVariable String username) {
			return service.searchUser(username);
		}
		
		 @DeleteMapping("/delete/{username}")
		public String deleteUser(@PathVariable String username) {
			 return service.deleteUser(username);
		 }
	
	}


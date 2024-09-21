package com.example.demo.controller;

import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.domain.Login;
import com.example.demo.service.LoginService;

@RestController
public class LoginController {
	@Autowired
	private LoginService lservice;
	 
	 @GetMapping("/login/correctEncode/{username}/{password}")
	 public String CheckPassword(@PathVariable String username,@PathVariable String password) throws ClassNotFoundException, SQLException {
		 return lservice.correctEncode(username, password);
	 }
	 
	 @PostMapping("/registerAdmin")
	 public String adminRegister(@RequestBody Login login) throws ClassNotFoundException, SQLException {
		 return lservice.registerUser(login);
	 }

}

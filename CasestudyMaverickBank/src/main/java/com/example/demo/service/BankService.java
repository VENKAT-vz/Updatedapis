package com.example.demo.service;

import java.sql.SQLException;

public interface BankService {
	
    String deposit(String accountNumber, double amount) throws ClassNotFoundException, SQLException;
    String withdraw(String accountNumber, double amount) throws ClassNotFoundException, SQLException;
    String moneyTransfer(String phnum1, String phnum2, double amount) throws ClassNotFoundException, SQLException;
    String accountTransfer(String accountnumber1, String accountnumber2, double amount);

}

package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.example.demo.domain.ApprovalRequest;
import com.example.demo.domain.Login;
import com.example.demo.domain.User;
import com.example.demo.repository.ApprovalRequestRepository;
import com.example.demo.repository.LoginRepository;
import com.example.demo.repository.UserRepository;

import java.sql.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private ApprovalRequestRepository approvalRequestRepository;
	
	@Autowired
	private LoginRepository lrepo;
	
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
        
        ApprovalRequest approvalRequest = new ApprovalRequest();
	    approvalRequest.setToWhom("Admin");
	    approvalRequest.setRequirement("User approval"); 
	    approvalRequest.setActionNeededOn(user.getUsername()); 
	    approvalRequest.setStatus("Pending"); 
	    approvalRequest.setCreatedAt(new Date(System.currentTimeMillis()));
	    approvalRequestRepository.save(approvalRequest);
	    
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
    
//	 public String approveUserAccounts(String username) {
//		    int rowsAffected = userRepo.approveUserAccount(username);   
//		    if (rowsAffected > 0) 
//		    {
//		        return "User Account approved successfully.";
//		    } 
//		    else 
//		        return " User Account approval failed. Account not found.";   
//		}
//	 
//	 public String closeUserAccounts(String username) {
//		    int rowsAffected = userRepo.closeUserAccount(username);   
//		    if (rowsAffected > 0) 
//		    {
//		        return "User Account closed successfully.";
//		    } 
//		    else 
//		        return " User Account closing failed. Account not found.";   
//		}
    
    //admin
	 public String approveUserAccounts(int requestId) {
		 Optional<ApprovalRequest> optionalapprovalRequest = approvalRequestRepository.findById(requestId);
		 ApprovalRequest approvalRequest=optionalapprovalRequest.get();
		 approvalRequest.setStatus("Approved");
		 approvalRequestRepository.save(approvalRequest);
		 
		 String username=approvalRequest.getActionNeededOn();
		 User user=userRepo.findByUsername(username);
		 user.setStatus("active");
		 userRepo.save(user);
		 
		 Login login = lrepo.findByUsername(username);
		 login.setStatus("active");
		 lrepo.save(login);
		 
		 return "The user account is set to active...";
		 
	      
}
	//admin
	 public String CloseUserAccounts(int requestId) {
		 Optional<ApprovalRequest> optionalapprovalRequest = approvalRequestRepository.findById(requestId);
		 ApprovalRequest approvalRequest=optionalapprovalRequest.get();
		 approvalRequest.setStatus("Approved");

		 String username=approvalRequest.getActionNeededOn();
		 User user=userRepo.findByUsername(username);
		 user.setStatus("closed");
		 
		 return "The user account is closed...";
		 
	      
	 }
    //customer
	 public String closeUserAccountsRequest(String username) {
		 ApprovalRequest approvalRequest = new ApprovalRequest();
		    approvalRequest.setToWhom("Admin");
		    approvalRequest.setRequirement("User Account Closure "); 
		    approvalRequest.setActionNeededOn(username); 
		    approvalRequest.setStatus("Pending"); 
		    approvalRequest.setCreatedAt(new Date(System.currentTimeMillis()));
		    approvalRequestRepository.save(approvalRequest); 
		    return"Closing of user account requested to admin..";
}
    
    
    
	 //admin
	 public List<ApprovalRequest> requests(){
		 return approvalRequestRepository.showAllrequestsAdmin();
	 }
	 
	 //bankmanager
	 public List<ApprovalRequest> BankMrequests(){
		 return approvalRequestRepository.showAllrequestsBankManager();
	 }
	 
//	 public List<User> unapprovedUserAccounts() {
//		    return userRepo.unapproveduseraccounts();
//		}


}

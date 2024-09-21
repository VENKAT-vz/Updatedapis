package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.example.demo.domain.ApprovalRequest;
import com.example.demo.domain.Login;
import com.example.demo.domain.User;
import com.example.demo.domain.Usershow;
import com.example.demo.repository.ApprovalRequestRepository;
import com.example.demo.repository.LoginRepository;
import com.example.demo.repository.UserRepository;

import java.sql.Date;
import java.util.ArrayList;
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

    public List<Usershow> showAllUsers() {
    	
    	List<Usershow> usershow=new ArrayList<>();
        List<User> user=userRepo.findAll();
        for(User userdet:user) {
        	Usershow ushow=new Usershow();
        	ushow.setFirstname(userdet.getFirstname());
        	ushow.setLastname(userdet.getLastname());
        	ushow.setAddress(userdet.getAddress());
        	ushow.setCity(userdet.getCity());
        	ushow.setContactNumber(userdet.getContactNumber());
        	ushow.setDateOfBirth(userdet.getDateOfBirth());
        	ushow.setEmailid(userdet.getEmailid());
        	ushow.setStatus(userdet.getStatus());
        	ushow.setUsername(userdet.getUsername());
        	ushow.setGender(userdet.getGender());
        	
        	Login login=lrepo.findByUsername(userdet.getUsername());
        	ushow.setRole(login.getRole());
        	
        	usershow.add(ushow);
        }
        return usershow;

    }

    public Usershow searchUser(String usernameOrEmail) {
        User userdet = userRepo.findByUsername(usernameOrEmail);
        if (userdet == null) 
        {
            userdet = userRepo.findByEmailid(usernameOrEmail);
        }
        
    	Usershow ushow=new Usershow();
    	ushow.setFirstname(userdet.getFirstname());
    	ushow.setLastname(userdet.getLastname());
    	ushow.setAddress(userdet.getAddress());
    	ushow.setCity(userdet.getCity());
    	ushow.setContactNumber(userdet.getContactNumber());
    	ushow.setDateOfBirth(userdet.getDateOfBirth());
    	ushow.setEmailid(userdet.getEmailid());
    	ushow.setStatus(userdet.getStatus());
    	ushow.setUsername(userdet.getUsername());
    	ushow.setGender(userdet.getGender());
    	
    	Login login=lrepo.findByUsername(userdet.getUsername());
    	ushow.setRole(login.getRole());
        
        return ushow;
    }
    
    public String deleteUser(String username) {
        if(userRepo.existsById(username)) {
        	userRepo.deleteById(username);
        	return "User deleted successfully...";
        }
    	return "User not deleted..";

    }
    
    
    //admin
	 public String approveUserAccounts(int requestId) {
		 Optional<ApprovalRequest> optionalapprovalRequest = approvalRequestRepository.findById(requestId);
		 ApprovalRequest approvalRequest=optionalapprovalRequest.get();
		 
		 String username=approvalRequest.getActionNeededOn();
		 User user=userRepo.findByUsername(username);
		 user.setStatus("active");
		 userRepo.save(user);
		 
		 Login login = lrepo.findByUsername(username);
		 login.setStatus("active");
		 lrepo.save(login);
		 
		 approvalRequest.setStatus("Approved");
		 approvalRequestRepository.save(approvalRequest);
		 
		 return "The user account is set to active...";
		 
	      
	 }
	 
	    //admin
	 public String RejectUserAccount(int requestId) {
		 
			 Optional<ApprovalRequest> optionalapprovalRequest = approvalRequestRepository.findById(requestId);
			 ApprovalRequest approvalRequest=optionalapprovalRequest.get();
			 
			 String username=approvalRequest.getActionNeededOn();
			 User user=userRepo.findByUsername(username);
			 user.setStatus("rejected");
			 userRepo.save(user);
			 
			 Login login = lrepo.findByUsername(username);
			 
			 lrepo.delete(login);
			 
			 approvalRequest.setStatus("Rejected");
			 approvalRequest.setRemarks("The user details are not satisfactory to create an user account");
			 approvalRequestRepository.save(approvalRequest);
			 
			 return "The user account is rejected...";
			 
		      
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
	 

}

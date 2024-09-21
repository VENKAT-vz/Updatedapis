package com.example.demo.controller;

import java.sql.Date;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.domain.Account;
import com.example.demo.domain.Beneficiaries;
import com.example.demo.domain.Loan;
import com.example.demo.domain.Login;
import com.example.demo.domain.NewLoan;
import com.example.demo.domain.NewLoanList;
import com.example.demo.domain.User;
import com.example.demo.repository.NewLoanListRepository;
import com.example.demo.service.AccountService;
import com.example.demo.service.BankService;
import com.example.demo.service.BeneficiariesService;
import com.example.demo.service.InstallmentsService;
import com.example.demo.service.LoanService;
import com.example.demo.service.LoginService;
import com.example.demo.service.NewLoanService;
import com.example.demo.service.UserService;

@RestController
@RequestMapping("/users")
@CrossOrigin("http://localhost:60753")
public class UserController {

	@Autowired
	private UserService service;
	
	@Autowired
	private LoginService lservice;

    @Autowired
    private AccountService accountService;

    @Autowired
    private BankService bankService;
    
    @Autowired
    private LoanService loanService;
    
    @Autowired
    private BeneficiariesService benservice;
    
	@Autowired
    private NewLoanService newLoanService;
	
    @Autowired
    private NewLoanListRepository newLoanListRepository;
    
    @Autowired
    private InstallmentsService installmentservice;
    
	 
	@PostMapping(value = "/addUser")
	public void addUser(@RequestBody Map<String, Object> requestBody) throws ClassNotFoundException, SQLException {

		Map<String, Object> userMap = (Map<String, Object>) requestBody.get("users");
	    User user = new User();
	    user.setFirstname((String) userMap.get("firstname"));
	    user.setLastname((String) userMap.get("lastname"));

	    Date dateOfBirth = Date.valueOf((String) userMap.get("dateOfBirth")); 
	    user.setDateOfBirth(dateOfBirth);

	    String genderString = (String) userMap.get("gender");
	    if (genderString != null && genderString.length() == 1) {
	        char gender = genderString.charAt(0); 
	        user.setGender(gender); 
	    }
	    
	    user.setContactNumber((String) userMap.get("contactNumber"));
	    user.setAddress((String) userMap.get("address"));
	    user.setCity((String) userMap.get("city"));
	    user.setState((String) userMap.get("state"));
	    user.setUsername((String) userMap.get("username"));
	    user.setEmailid((String) userMap.get("emailid"));

	    String password = (String) requestBody.get("password");
	    String role = (String) requestBody.get("role");

	    service.addUser(user);

	    Login login = new Login();
	    login.setUsername(user.getUsername());
	    login.setEmailid(user.getEmailid());
	    login.setPassword(password);
	    login.setRole(role);
	    
	    lservice.registerUser(login);
	}
	

    @PostMapping("/addAccounts")
    public Account addAccount(@RequestBody Map<String, Object> userdet) throws ClassNotFoundException, SQLException {
        Map<String, Object> accountdet = (Map<String, Object>) userdet.get("account");
        String aadhaarNumber = (String) userdet.get("aadhaarNumber");
        String panNumber = (String) userdet.get("panNumber");

        Double income = null;
        if (userdet.get("income") != null) {
            income = ((Number) userdet.get("income")).doubleValue(); 
        }
        
        Account account = new Account();
        account.setAccountType((String) accountdet.get("accountType"));
        account.setBalance((Double) accountdet.get("balance"));
        account.setBranchName((String) accountdet.get("branchName"));
        account.setIfscCode((String) accountdet.get("ifscCode"));
        account.setUsername((String) accountdet.get("username"));
        account.setEmailid((String) accountdet.get("emailid"));

        return accountService.addAccount(account, aadhaarNumber, panNumber,income);
    }
    
    @GetMapping("/myaccounts/{username}")
    public List<Account> getAccountByNumberByusername(@PathVariable String username) {
        return accountService.getAccountByNumberByusername(username);
    }
    
    @PostMapping("/deposit/{accountNumber}/{amount}")
    public String deposit(@PathVariable String accountNumber, @PathVariable double amount) throws ClassNotFoundException, SQLException {
        return bankService.deposit(accountNumber, amount);
    }

    @PostMapping("/withdraw/{accountNumber}/{amount}")
    public String withdraw(@PathVariable String accountNumber, @PathVariable double amount) throws ClassNotFoundException, SQLException {
        return bankService.withdraw(accountNumber, amount);
    }

    @PostMapping("/moneytransfer/{phnum1}/{phnum2}/{amount}")
    public String moneyTransfer(@PathVariable String phnum1, @PathVariable String phnum2, @PathVariable double amount) throws ClassNotFoundException, SQLException {
        return bankService.moneyTransfer(phnum1, phnum2, amount);
    }
    
    @PostMapping("/accounttransfer/{accnum1}/{accnum2}/{amount}")
    public String accountransfer(@PathVariable String accnum1, @PathVariable String accnum2, @PathVariable double amount) throws ClassNotFoundException, SQLException {
        return bankService.accountTransfer(accnum1, accnum2, amount);
    }
    
    @PostMapping("/addLoan")
    public String addloan(@RequestBody Loan loan) {
    	return loanService.addLoan(loan);
    }
    
    @GetMapping("/searchLoan/{accountnumber}")
    public List<Loan> searchLoan(@PathVariable String accountnumber) {
    	return loanService.viewLoansByAccountNumber(accountnumber);
    }
    
    @PostMapping("/addBeneficiary")
    public String addBeneficiary(@RequestBody Beneficiaries benficiary) {
    	return benservice.giveBeneficiaryDetails(benficiary);
    }
    
    @GetMapping("/searchBen/{username}")
    public List<Beneficiaries> searchBeneficiary(@PathVariable String username) {
    	return benservice.searchBeneficiaries(username);
    }
    
	
	 @GetMapping("/login/{username}/{password}")
	 public String loginuser(@PathVariable String username,@PathVariable String password) throws ClassNotFoundException, SQLException {
		 return lservice.loginuser(username, password);
	 }
	 
	 @PostMapping("/close-useraccount-request/{username}")
	   public String closerequest(@PathVariable String username) {
	    	return service.closeUserAccountsRequest(username);
	    }
	 
	 @PostMapping("/close-bankaccount-request/{accountNumber}")
	   public String closebrequest(@PathVariable String accountNumber) {
	    	return accountService.closeBankAccountsRequest(accountNumber);
	    }
    
	// New loan methods: //
	    
		//user controller
	    @GetMapping("/loans/list")
	    public List<NewLoanList> getAllLoanTypes() {
	        return newLoanListRepository.findAll();
	    }
	    
	    @PostMapping("/loans/apply")
	    public String applyForLoan(@RequestParam String accountNumber,
	    		@RequestParam String loanName, @RequestParam double loanAmount,double income) {
	        
	        NewLoan newLoan = new NewLoan();
	        newLoan.setAccountNumber(accountNumber);
	        newLoan.setLoanName(loanName);
	        newLoan.setLoanAmount(loanAmount);
	        newLoan.setStatus("Pending"); 
	
	        NewLoan savedLoan = newLoanService.applyloan(newLoan,income);
	
	        return "Loan application successful with ID: " + savedLoan.getLoanId();
		    }
	    
	    @PutMapping("/loans/makepayments")
	    public String payInstallments(@RequestParam int loanId,
	    		@RequestParam String paymentDate,@RequestParam String paymentType,
	    		@RequestParam double paymentAmount,@RequestParam String remarks) {
	    	return installmentservice.payInstallment(loanId, paymentDate, paymentType, paymentAmount, remarks);
	    }
	    
	    @PostMapping("/loans/closerequest/{loanid}")
	    public String closeRequest (@PathVariable int loanid) {
	    	return newLoanService.LoanClose(loanid);
	    }
	    
}
    
 

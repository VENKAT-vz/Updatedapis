package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.demo.domain.ApprovalRequest;

@Repository
public interface ApprovalRequestRepository extends JpaRepository<ApprovalRequest,Integer>{

	@Query("SELECT ar from ApprovalRequest ar WHERE to_whom = 'Admin' and status='Pending'")
	List<ApprovalRequest> showAllrequestsAdmin();
	
	@Query("SELECT ar from ApprovalRequest ar WHERE to_whom = 'BankManager'and status='Pending'")
	List<ApprovalRequest> showAllrequestsBankManager();
}

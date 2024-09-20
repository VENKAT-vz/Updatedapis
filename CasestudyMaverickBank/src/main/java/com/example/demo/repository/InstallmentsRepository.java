package com.example.demo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.domain.Installments;

public interface InstallmentsRepository extends JpaRepository<Installments, Integer>{

    Optional<Installments> findByLoanIdAndInstallmentStatus(int loanId, String status);

    Optional<Installments> findFirstByLoanIdOrderByPaymentDateDesc(int loanId);
}
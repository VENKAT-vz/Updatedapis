package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.domain.Beneficiaries;

public interface BeneficiariesRepository extends JpaRepository<Beneficiaries, String>{

	List<Beneficiaries> findByUsername(String username);
}

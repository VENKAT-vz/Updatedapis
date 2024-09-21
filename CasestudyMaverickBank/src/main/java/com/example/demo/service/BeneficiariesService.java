package com.example.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.domain.Beneficiaries;
import com.example.demo.repository.BeneficiariesRepository;

@Service
public class BeneficiariesService {

    @Autowired
    private BeneficiariesRepository beneficiariesRepo;

    public String giveBeneficiaryDetails(Beneficiaries beneficiaries) {
        beneficiariesRepo.save(beneficiaries);
        return "Beneficiary added";
        }

    public List<Beneficiaries> searchBeneficiaries(String username) {
        return beneficiariesRepo.findByUsername(username);
    }
}
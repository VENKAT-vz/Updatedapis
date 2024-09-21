package com.example.demo.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "beneficiary")
public class Beneficiaries {

	@Id
    @Column(name = "username")
    private String username;

    @Column(name = "emailid")
    private String emailId;

    @Column(name = "beneficiary_name")
    private String beneficiaryName;

    @Column(name = "beneficiary_accnum")
    private String beneficiaryAccnum;

    @Column(name = "beneficiary_bank_name")
    private String beneficiaryBankName;

    @Column(name = "beneficiary_branch_name")
    private String beneficiaryBranchName;

    @Column(name = "beneficiary_ifsc_code")
    private String beneficiaryIfscCode;

    @Column(name = "nickname")
    private String nickname;

    @ManyToOne
    @JoinColumn(name = "username", referencedColumnName = "username", insertable = false, updatable = false)
    private User user;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public String getBeneficiaryName() {
		return beneficiaryName;
	}

	public void setBeneficiaryName(String beneficiaryName) {
		this.beneficiaryName = beneficiaryName;
	}

	public String getBeneficiaryAccnum() {
		return beneficiaryAccnum;
	}

	public void setBeneficiaryAccnum(String beneficiaryAccnum) {
		this.beneficiaryAccnum = beneficiaryAccnum;
	}

	public String getBeneficiaryBankName() {
		return beneficiaryBankName;
	}

	public void setBeneficiaryBankName(String beneficiaryBankName) {
		this.beneficiaryBankName = beneficiaryBankName;
	}

	public String getBeneficiaryBranchName() {
		return beneficiaryBranchName;
	}

	public void setBeneficiaryBranchName(String beneficiaryBranchName) {
		this.beneficiaryBranchName = beneficiaryBranchName;
	}

	public String getBeneficiaryIfscCode() {
		return beneficiaryIfscCode;
	}

	public void setBeneficiaryIfscCode(String beneficiaryIfscCode) {
		this.beneficiaryIfscCode = beneficiaryIfscCode;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@Override
	public String toString() {
		return "Beneficiaries [username=" + username + ", emailId=" + emailId + ", beneficiaryName=" + beneficiaryName
				+ ", beneficiaryAccnum=" + beneficiaryAccnum + ", beneficiaryBankName=" + beneficiaryBankName
				+ ", beneficiaryBranchName=" + beneficiaryBranchName + ", beneficiaryIfscCode=" + beneficiaryIfscCode
				+ ", nickname=" + nickname + ", user=" + user + "]";
	}

	public Beneficiaries(String username, String emailId, String beneficiaryName, String beneficiaryAccnum,
			String beneficiaryBankName, String beneficiaryBranchName, String beneficiaryIfscCode, String nickname,
			User user) {
		super();
		this.username = username;
		this.emailId = emailId;
		this.beneficiaryName = beneficiaryName;
		this.beneficiaryAccnum = beneficiaryAccnum;
		this.beneficiaryBankName = beneficiaryBankName;
		this.beneficiaryBranchName = beneficiaryBranchName;
		this.beneficiaryIfscCode = beneficiaryIfscCode;
		this.nickname = nickname;
		this.user = user;
	}

	public Beneficiaries() {
		super();
		// TODO Auto-generated constructor stub
	}

	
}
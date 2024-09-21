package com.example.demo.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.domain.User;

@Repository
public interface UserRepository extends JpaRepository<User, String>{

    User findByEmailid(String emailid);
    User findByUsername(String username);
    
    @Query("SELECT u From User u")
    List<User> unapproveduseraccounts(); 
    // here aadhar and pan will also print, to avoid that maybe 
    //we can use a new dto class without pan and aadhar
    
//    @Transactional
//    @Modifying
//    @Query("UPDATE User u SET u.status = 'active' WHERE u.username = :username")
//    int approveUserAccount(@Param("username") String username);
//    
//    @Transactional
//    @Modifying
//    @Query("UPDATE User u SET u.status = 'closed' WHERE u.username = :username")
//    int closeUserAccount(@Param("username") String username);
}



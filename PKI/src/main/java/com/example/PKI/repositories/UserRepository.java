package com.example.PKI.repositories;

import com.example.PKI.models.User;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;


@Repository
public interface UserRepository extends JpaRepository<User, Long>{

    public User findUserByEmail(String email);

}

package com.piinfo.db;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

//auto implemented by springboot
@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
	User findByUsername (String username);
}
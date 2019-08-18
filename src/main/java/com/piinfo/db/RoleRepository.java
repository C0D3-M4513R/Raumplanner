package com.piinfo.db;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

//auto implemented by springboot
@Repository
public interface RoleRepository extends JpaRepository<Roles, Integer> {
	Roles findByRole (String role);
}

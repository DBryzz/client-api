package com.bryzz.clientapi.domain.repository;

import java.util.Optional;

import com.bryzz.clientapi.domain.constant.ERole;
import com.bryzz.clientapi.domain.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;



@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
	Optional<Role> findByName(ERole name);
}

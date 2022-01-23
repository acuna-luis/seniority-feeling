package com.turingtask.seniority.user;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String>{

	Optional<User> findById(String userId);

}

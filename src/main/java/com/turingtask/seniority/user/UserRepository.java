package com.turingtask.seniority.user;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User, String>{

	Optional<User> findById(String userId);

	@Query("SELECT question FROM Questions WHERE intent = :intent")
	String getQuestion(@Param("intent") String intent);

	@Query("SELECT answer FROM ExpectedResults WHERE intent = :intent and user_id = :userId")
	String getExpectedResult(@Param("intent") String intent, @Param("userId") String userId);	

	//@Modifying
	//@Transactional
	//@Query("INSERT INTO Results (date, session, question_id, answer, score) select :date , :session , :questionId , :answer , :score from dual")
	//void updateAnswer(String date, String session, int questionId, String answer, double score);



}

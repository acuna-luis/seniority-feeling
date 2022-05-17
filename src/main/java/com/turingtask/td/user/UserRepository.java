package com.turingtask.td.user;

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

	@Query("SELECT SUM(score) FROM Results WHERE intent in ('question1','question2','question3','game1' ) and session = :sessionId")
	String getQuestionSum(@Param("sessionId") String sessionId);

	@Query("SELECT SUM(score) FROM Results WHERE intent in ('sentiment' ) and session = :sessionId")
	String getQuestionSentiment(@Param("sessionId") String sessionId);



}

package com.turingtask.seniority.user;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

@Service
public class UserService {

	private UserRepository userRepository;

	public UserService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	public User save(User user) {
		return this.userRepository.saveAndFlush(user);
	}

	public long count() {
		
		return userRepository.count();
	}

	public Optional<User> findById(String userId) {
		return userRepository.findById(userId);
	}

	public List<User> findAll() {
		// TODO Auto-generated method stub
		return userRepository.findAll();
	}
	public String getQuestion(String intent)	{
		return userRepository.getQuestion(intent);
	}
	public String getExpectedResult(String intent, String userId) {
		return userRepository.getExpectedResult( intent, userId);
	}
//	public void updateAnswer(String date,String session, int questionId, String answer, double score) {
//		userRepository.updateAnswer( date, session, questionId, answer, score);
//	}

	
}

package com.turingtask.seniority.user;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

@Service
public class UserService {

	private UserRepository userRepository;
	@PersistenceContext
    private EntityManager entityManager;
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
	@Transactional
	public void insertAnswer(String session, String intent, String answer, double score, String identityId) {
		entityManager.createNativeQuery("INSERT INTO results (date, session, intent, answer, score, identity_id) values (NOW(),?,?,?,?,?)")
		.setParameter(1, session)
		.setParameter(2, intent)
		.setParameter(3, answer)
		.setParameter(4, score)
		.setParameter(5, identityId)
		.executeUpdate();
	}

	
}

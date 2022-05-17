package com.turingtask.td;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "expected_results")

public class ExpectedResults {

@Id
private String userId;

@Column(name="question_id")
private String questionId;

@Column(name="answer")
private String answer;

}

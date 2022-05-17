package com.turingtask.td;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "results")
public class Results {
    @Id
    private String session;

	@Column(name="date")
	private String date;

	@Column(name="identity_id")
	private String questionId;

	@Column(name="answer")
	private String answer;

	@Column(name="score")
	private double score;
}

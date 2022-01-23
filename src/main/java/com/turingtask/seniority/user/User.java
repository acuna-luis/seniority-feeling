package com.turingtask.seniority.user;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class User {

	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")

	private String id;
	private String name;

	@ManyToOne
	@JsonIgnore
	private User doctor;

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}
}

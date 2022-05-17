package com.turingtask.td.user;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class User {

	@Id
	private String id;
	private String name;

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}
}

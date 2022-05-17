package com.turingtask.td;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "questions")

public class Questions {

@Id
private int id;

@Column(name="question")
private String question;


@Column(name="intent")
private String intent;

}

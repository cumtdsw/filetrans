package com.dsw.filetrans.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="dictionary")
public class DictionaryModel {

	@Column(name="ID")
	@Id
	private int id;
	
	@Column(name="name")
	private String name;
	
	@Column(name="value")
	private int value;
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}


	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	
	
}

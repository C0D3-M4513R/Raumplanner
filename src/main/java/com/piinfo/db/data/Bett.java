package com.piinfo.db.data;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("Bett")
@Getter @RequiredArgsConstructor
public class Bett extends Furniture_Settings{
	@Column(updatable = false,nullable = false)
	public int Persons;
}

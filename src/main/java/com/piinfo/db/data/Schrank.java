package com.piinfo.db.data;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("Schrank")
@Getter @RequiredArgsConstructor
public class Schrank extends Furniture_Settings{
	@Column(name = "isBigRound",nullable = false,updatable = false)
	public boolean isBig;
}

package com.piinfo.db.data;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("Sofatisch")
@Getter
@RequiredArgsConstructor
public class Sofatisch extends Furniture_Settings {
	@Column(name = "isBigRound",updatable = false,nullable = false)
	public boolean isRound;
}

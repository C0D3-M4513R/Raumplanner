package com.piinfo.db.data;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Currency;

import javax.persistence.*;
import java.util.Set;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Getter @Setter @RequiredArgsConstructor
abstract class Furniture_Settings {
	
	@Id
	@Column(updatable = false,nullable = false,unique = true)
	@Setter(AccessLevel.NONE)
	private long id;
	
	@Currency("test")
	@Column
	public int cost;
	
	@Column(name = "DTYPE",insertable = false,updatable = false,nullable = false)
	public String type;
	
	@Column
	public String name;
	
	@Column
	public String width;
	
	@Column
	public String length;
	
	@Column(nullable = false)
	private String imgUrl;
	
	@OneToMany(mappedBy = "furnitureSettings")
	private Set<Furniture> furniture;
	
	
}

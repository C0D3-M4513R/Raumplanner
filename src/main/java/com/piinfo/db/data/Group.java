package com.piinfo.db.data;

import lombok.Getter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.Set;

@Entity(name = "F_Group")
@Getter
public class Group {
	@Id
	@Column(unique = true,nullable = false,updatable = false)
	private long id;
	
	@OneToMany(mappedBy = "group")
	private Set<Furniture> group;
	@OneToMany(mappedBy = "billies")
	private Set<Furniture> furniture;
}

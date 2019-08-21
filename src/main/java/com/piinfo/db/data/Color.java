package com.piinfo.db.data;

import lombok.Getter;
import lombok.experimental.Accessors;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.Set;

/**
 * Translate sql db to java objects
 */


@Entity
@Getter
public class Color {
	@Id
	@Column(unique = true,nullable = false,updatable = false)
	private long id;
	
	@Column
	public String color;
	
	@Column
	public String value;
	
	@OneToMany(mappedBy = "color")
	private Set<Furniture> Furniture;
}

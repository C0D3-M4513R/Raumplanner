package com.piinfo.db.data;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;
import java.util.Set;

@Entity
@Getter @RequiredArgsConstructor
public class Furniture {
	
	@Id
	@Column(nullable = false,unique = true,updatable = false)
	private long id;
	
	@Column(name = "posx",nullable = false)
	public int posX;
	
	@Column(name = "posy",nullable = false)
	public int posY;
	
	@ManyToOne
	@JoinColumn(name = "furnitureType",referencedColumnName = "id",nullable = false,unique = false)
	private Furniture_Settings furnitureSettings;
	
	@ManyToOne
	@JoinColumn(name = "colorId",referencedColumnName = "id",nullable = false)
	private Color color;
	
	@ManyToOne
	@JoinColumn(name = "attached",referencedColumnName = "id")
	private Group billies;
	
	@ManyToOne
	@JoinColumn(name = "groupId",referencedColumnName = "id")
	private Group group;
	
	@ManyToMany
	@JoinTable(joinColumns = {@JoinColumn(name = "FurnitureId", referencedColumnName = "id")},
			inverseJoinColumns = {@JoinColumn(name = "Roomid",referencedColumnName = "id")})
	private Set<Room> rooms;
	
}

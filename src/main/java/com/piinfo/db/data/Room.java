package com.piinfo.db.data;

import lombok.*;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;

import javax.persistence.*;
import java.util.Set;

@Entity(name = "rooms")
@Getter @Setter
@NoArgsConstructor
@RequiredArgsConstructor
@AllArgsConstructor
public class Room {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false,updatable = false)
	private long id;
	//TODO: Fix id not being the right one!!!!!!!!!!!!

	@NonNull
	@Column
	public String name;

	@NonNull
	@Column
	public long no;

	@Transient
	private String Link=String.format("<a href='/room/%d/'>Zum Raum</a><br><a href='/room/%d/delete'>Delete</a>",id,id);

	@Column
	private int width;

	@Column
	private int height;

	public Room(String name, long no, int width, int height){
		this.name = name;
		this.no = no;
		this.width = width;
		this.height = height;
	}

	public Room setLinkCreate(boolean create){
		this.Link="<a href='room/create/'>Zum Raum</a>";
		return this;
	}

	public JSONObject toJson(){
		try {
			return new JSONObject().put("id",id).put("name",name).put("no",no).put("Link",Link);
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}

	@ManyToMany
	@JoinTable(joinColumns = {@JoinColumn(name = "Roomid",referencedColumnName = "id")},
			inverseJoinColumns = {@JoinColumn(name = "FurnitureId",referencedColumnName = "id")})
	private Set<Furniture> furniture;
}

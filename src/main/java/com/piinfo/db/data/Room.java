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
	@Column(nullable = false,updatable = false)
	private long id;

	@NonNull
	@Column
	public String name;

	@NonNull
	@Column
	public long no;

	@Transient
	public String Link=String.format("<a href='room/%d/'>Zum Raum</a>",id);

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

package com.piinfo.db;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.stereotype.Repository;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.validation.constraints.NotEmpty;
import java.util.Iterator;
import java.util.Set;

@Entity
@Repository

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Roles {

	@Id
	private Integer id;

	@NotEmpty(message = "Please Enter a Role name")
	private String role;

	@JsonManagedReference
	@ManyToMany(mappedBy = "role")
	private Set<User> user;

	public Roles setRole (String role) {
		this.role = role;
		return this;
	}

	public Roles setId (Integer id) {
		this.id = id;
		return this;
	}

	public JSONObject roleToJson (boolean User) throws JSONException {
		return roleToJson().put("Users", usersToJson(false));
	}

	public JSONObject roleToJson () throws JSONException {
		return new JSONObject().put("Id", this.id)
				.put("Name", this.role);
	}

	private JSONObject usersToJson (boolean Roles) throws JSONException {
		Iterator<User> users = this.user.iterator();
		JSONObject json = new JSONObject();
		JSONArray jsonUsers = new JSONArray();
		while (users.hasNext()) {
			User user = users.next();
			jsonUsers.put(user.userToJson());
		}
		json.put("Users", jsonUsers);
		return json;
	}

}

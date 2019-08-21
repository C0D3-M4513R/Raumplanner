package com.piinfo.db.auth;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.Iterator;
import java.util.Set;

@SuppressWarnings("JpaDataSourceORMInspection")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

@Entity
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	@NotEmpty(message = "Please enter a Username")
	private String username;

	@NotEmpty(message = "Please enter a Password")
	private String password;

	@JsonBackReference
	@ManyToMany
	@JoinTable(name = "USERROLES",
			foreignKey = @ForeignKey(name = "fk_User-Roles_User"),
			joinColumns = {@JoinColumn(name = "User_ID")},
			inverseJoinColumns = {@JoinColumn(name = "Role_ID")})
	private Set<Roles> role;

	private boolean active;

	public JSONObject userToJson (boolean Role) throws JSONException {

		return userToJson().put("Roles", rolesToJson());
	}

	public JSONObject userToJson () throws JSONException {
		return new JSONObject().put("Id", this.id)
				.put("Username", this.username)
				.put("Password", this.password)
				.put("Active", this.active);
	}

	public JSONArray rolesToJson () throws JSONException {
		Iterator<Roles> roles = this.role.iterator();
		JSONArray jsonRoles = new JSONArray();
		while (roles.hasNext()) {
			Roles role = roles.next();
			jsonRoles.put(role.roleToJson());
		}
		return jsonRoles;
	}

}
package com.piinfo.db;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;

import javax.persistence.Entity;
import javax.persistence.Id;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
public class Settings {

	@Id
	private Integer id;

	private String name;

	private String value;

	public JSONObject toJSON () throws JSONException {
		return new JSONObject().put("id", this.id).put("Name", this.name).put("Value", this.value);
	}


}

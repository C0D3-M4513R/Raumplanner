package com.piinfo.db.auth;

import lombok.*;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;

import javax.persistence.Entity;
import javax.persistence.Id;

@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
@Getter
@Setter
@Entity
public class Settings {

	@Id
	private Integer id;

	@NonNull
	private String name;

	@NonNull
	private String value;

	public JSONObject toJSON () throws JSONException {
		return new JSONObject().put("id", this.id).put("Name", this.name).put("Value", this.value);
	}


}

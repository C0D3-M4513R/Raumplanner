package com.piinfo.springboot;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.piinfo.db.auth.Settings;
import com.piinfo.db.auth.SettingsRepository;
import com.piinfo.db.auth.UserRepository;
import com.piinfo.service.Header;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.util.HtmlUtils;

import java.util.HashSet;
import java.util.Iterator;
import java.util.concurrent.atomic.AtomicBoolean;

@Controller
public class SettingsController {

	private final String errorBox = "<div class='alert alert-danger fade in alert-dismissible show'> <button type='button' class='close' data-dismiss='alert'>&times;</button>";
	private final String successBox = "<div class='alert alert-success fade in alert-dismissible show'> <button type='button' class='close' data-dismiss='alert'>&times;</button>";

	@Autowired
	private SettingsRepository settings;
	@Autowired
	private UserRepository users;

	@GetMapping("/Site_Settings")
	public String siteSettings (Model model, Authentication auth) {
		model.addAttribute("username", HtmlUtils.htmlEscape(auth.getName()));
		String[] scripts = {"webjars/bootstrap-table/1.14.1/dist/bootstrap-table.js"
				, "/mindmup-editabletable.js", "/numeric-input-example.js"};
		String[] links = {"webjars/bootstrap-table/1.14.1/dist/bootstrap-table.css"};
		model.addAttribute("scripts", scripts);
		Header[] headers = {
				new Header("Setting","Name",true),
				new Header("Value","Value",true)
		};

		model.addAttribute("headers",headers);

		return "tableAndEdit";
	}

	@PostMapping("/Site_Settings")
	public ResponseEntity<String> update (Authentication auth
			, @RequestParam String name, @RequestParam String value) throws JSONException {
		AtomicBoolean isAdmin = new AtomicBoolean(false);
		auth.getAuthorities().iterator().forEachRemaining(s -> {
					if (s.getAuthority().equals("Admin")) {
						isAdmin.set(true);
					}
				}
		);
		if (isAdmin.get()) {
			Settings setting = settings.findByName(name);
			System.out.println(settings.findByName(name).toJSON());
			setting.setValue(value);
			System.out.println(settings.findByName(name).toJSON());
			settings.save(setting);
			System.out.println(settings.findByName(name).toJSON());

			return ResponseEntity.status(HttpStatus.OK).body(successBox.concat("Success: Saved Change!"));
		} else {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(errorBox.concat("Error: Not an Admin!</div>"));
		}

	}

	@GetMapping(value = "/Site_Settings",params = "type")
	public @ResponseBody
	String jsonSiteSettings () throws JSONException {
		ObjectMapper om = new ObjectMapper();
		Iterator<Settings> it = settings.findAll().iterator();
		HashSet<Settings> settingsSet = new HashSet<>();
		settingsSet.add(new Settings("test","test1"));
		try {
			return om.writeValueAsString(it);
		}catch (JsonProcessingException e){
			JSONArray json = new JSONArray();
			while (it.hasNext()) {
				json.put(it.next().toJSON());
			}
			return json.toString().concat("1st failed");
		}
		
	}
}

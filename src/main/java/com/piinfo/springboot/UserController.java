package com.piinfo.springboot;

import com.piinfo.db.auth.RoleRepository;
import com.piinfo.db.auth.Roles;
import com.piinfo.db.auth.User;
import com.piinfo.db.auth.UserRepository;
import com.piinfo.service.Alert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.http.HttpHeaders;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.HtmlUtils;

import java.util.*;

@Controller
public class UserController {

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private RoleRepository roleRepository;


	@RequestMapping("/user")

	public String user (Model model, Authentication auth) {


		model.addAttribute("title", "UserController");
		model.addAttribute("username", HtmlUtils.htmlEscape(auth.getName()));
		return "user";
	}

	@GetMapping("/changepw")
	public String changePW (Model model,Authentication auth) {

		model.addAttribute("username",auth.getName());
		return "changepw";
	}

	@PostMapping("/changepw")
	public String pwToDB (@RequestParam String oldPasswd,
	                      @RequestParam String newPasswd,
	                      @RequestParam String newPasswd1,
	                      Authentication auth,
	                      Model model){

		User user = userRepository.findByUsername(auth.getName());
		final String error ="alert alert-danger alert-dismissible fade in show";
		HashSet<Alert> alerts = new HashSet<>();
		if(passwordEncoder.matches(oldPasswd,user.getPassword())) {//check if passwords are the same
			if(!newPasswd.equals(newPasswd1)){
				alerts.add(new Alert("Passwords don't match",error));
			}
			if(newPasswd.length()<=8){
				alerts.add(new Alert("Password is too short. <br>It has to be longer than 8 characters.",error));
			}
			if (!newPasswd.matches("[A-Z]")){
				alerts.add(new Alert("Add at least one Uppercase letter",error));
			}
			if(newPasswd.matches("[a-z]")){
				alerts.add(new Alert("Add at least one Lowercase letter",error));
			}
			if(!newPasswd.matches("[0-9]")){
				alerts.add(new Alert("Add at least one number",error));
			}
			if(!newPasswd.matches("\\S")){
				alerts.add(new Alert("Add at least one special character",error));
			}
			if(alerts.size()==0){
				user.setPassword(passwordEncoder.encode(newPasswd));
				userRepository.save(user);
				alerts.add(new Alert("Successfully changed password.","alert alert-success alert-dismissible fade in show"));
				model.addAttribute("script","history.pushState(null,null,\"user\");");
				model.addAttribute("alerts",alerts);
				return this.user(model,auth);
			}
		} else {
			alerts.add(new Alert("The old password is wrong. <br>Please try again.",error));
		}


		model.addAttribute("alerts",alerts);
		return this.changePW(model,auth);
	}


	//begin debug mappings
	@GetMapping(path = "/add") // Map ONLY GET Requests
	public @ResponseBody
	String addNewUser (@RequestParam String name
			, @RequestParam String pass
			, @RequestParam String role
			, @RequestHeader HttpHeaders header) {

		String host = Objects.requireNonNull(header.getHost()).getHostName();
		if (host.equals("localhost") || host.equals("127.0.0.1")) {
			// @ResponseBody means the returned String is the response, not a view name
			// @RequestParam means it is a parameter from the GET or POST request

			User n = new User();
			n.setUsername(name);
			n.setPassword(passwordEncoder.encode(pass));

			Set<Roles> roles = new LinkedHashSet<>();
			roles.add(roleRepository.findByRole(role));

			n.setRole(roles);
			userRepository.save(n);


			return "Saved";
		}
		throw new AccessDeniedException("Host is not localhost");
	}

	@GetMapping(path = "/all/users")
	public @ResponseBody
	String getAllUsers (@RequestHeader HttpHeaders header) throws JSONException {

		String host = Objects.requireNonNull(header.getHost()).getHostName();
		if (host.equals("localhost") || host.equals("127.0.0.1")) {
			// This returns a JSON or XML with the users
			JSONArray json = new JSONArray();

			for (User user : userRepository.findAll()) {
				json.put(user.userToJson(true));
			}
			return json.toString(4);
		}
		throw new AccessDeniedException("Host is not localhost");
	}

	@GetMapping(path = "/all/roles")
	public @ResponseBody
	String getAllRoles (@RequestHeader HttpHeaders header) throws JSONException {
		String host = Objects.requireNonNull(header.getHost()).getHostName();
		if (host.equals("localhost") || host.equals("127.0.0.1")) {
			// This returns a JSON or XML with the users
			Iterator<Roles> roles = roleRepository.findAll().iterator();
			JSONArray role = new JSONArray();

			while (roles.hasNext()) {
				role.put(roles.next().roleToJson(true));
			}

			return role.toString(4);
		}
		throw new AccessDeniedException("Host is not localhost");
	}

	@RequestMapping(path = "/kill")
	public @ResponseBody
	String kill (@RequestHeader HttpHeaders header) {
		String host = Objects.requireNonNull(header.getHost()).getHostName();
		if (host.equals("localhost") || host.equals("127.0.0.1")) {
			System.exit(0);
			return "Successful failure!";
		}
		throw new AccessDeniedException("Host is not localhost");
	}
}

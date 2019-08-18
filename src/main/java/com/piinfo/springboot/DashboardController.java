package com.piinfo.springboot;

import com.piinfo.db.Settings;
import com.piinfo.db.SettingsRepository;
import com.piinfo.service.Alert;
import com.piinfo.service.SSH;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Controller
public class DashboardController {
	
	@Autowired
	private SettingsRepository settings;
	
	
	private final String IP = settings.findByName("Pi_IP").getValue();
	private final boolean isLocalhost = IP.equals("127.0.0.1") || IP.equals("localhost");
	
	@GetMapping ("/cmd")
	public String cmdSite (@RequestParam String cmd, Model model){
		return "cmd";
	}
	
	@PostMapping("/cmd")
	public ResponseEntity<?> cmd (@RequestParam String cmd) throws JSONException
	{
		HashSet<Alert> alerts = new HashSet<>();
		HttpStatus status = HttpStatus.CONTINUE;
		final String error ="alert alert-danger alert-dismissible fade in show";
		JSONObject body = new JSONObject();
		JSONArray errorHtml = new JSONArray();
		
		if (!isLocalhost){
			try {
				SSH ssh = new SSH(IP, "pi", "raspberry", 22);
				body = body.put("output",ssh.runCmd(cmd));
				status = HttpStatus.OK;
			}catch (UnknownHostException e){
				status = HttpStatus.SERVICE_UNAVAILABLE;
				errorHtml.put(new Alert("The specified IP couldn't be found.\n"+
						"The Message is: "+ e.getMessage(),error));
			} catch (Exception e){
				status = HttpStatus.INTERNAL_SERVER_ERROR;
				errorHtml.put(new Alert("An unknown error occurred.\n"+
						"The Message is: "+ e.getMessage(),error));
			}
		}
		body = body.put("error",errorHtml);
		
		return new ResponseEntity<>(body,status);
		
	}
	

}

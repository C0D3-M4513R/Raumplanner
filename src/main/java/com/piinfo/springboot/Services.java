package com.piinfo.springboot;

import com.piinfo.db.SettingsRepository;
import com.piinfo.service.SSH;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.net.UnknownHostException;

@Controller
public class Services {
	@Autowired
	private SettingsRepository settings;
	
	@RequestMapping("/cmd")
	public @ResponseBody String cmdexec(@RequestParam String cmd ){
		SSH ssh = null;
		String ip = settings.findByName("Pi_IP").getValue();
		try{
			ssh = new SSH(ip,"","",22);
			return ssh.runCmd(cmd);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
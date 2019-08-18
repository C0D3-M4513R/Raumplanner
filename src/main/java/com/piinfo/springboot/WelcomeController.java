package com.piinfo.springboot;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.FileNotFoundException;

@Controller
public class WelcomeController {
	@RequestMapping("/exception")
	public String welcome () throws FileNotFoundException {
		throw new FileNotFoundException();
	}
}

package com.piinfo.service;

import lombok.Data;
import lombok.NonNull;

@Data
public class Alert {
	@NonNull
	private String text;
	
	@NonNull
	private String classes;
	
}

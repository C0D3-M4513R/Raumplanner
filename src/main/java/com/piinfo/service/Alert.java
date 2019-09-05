package com.piinfo.service;

import lombok.Data;
import lombok.NonNull;

@Data
public class Alert {

	public static final String error ="alert alert-danger alert-dismissible fade in show";

	/**
	 * Text to display on the website
	 */
	@NonNull
	private String text;

	/**
	 * What type of alert is it?
	 */
	@NonNull
	private String classes;
	
}

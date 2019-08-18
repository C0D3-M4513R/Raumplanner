package com.piinfo.service;

import lombok.Data;
import lombok.NonNull;
import org.springframework.stereotype.Service;

import java.util.LinkedList;

@Data
public class Header {
	
	@NonNull
	private String name;
	
	@NonNull
	private String dbName;
	
	private LinkedList<String[]> attrList;
	
	public String getAttr() {
		String attr = "";
		setAttr("data-field",dbName);
		for (String[] next : attrList) {
			String thisAttr = String.format(" %s=\"%s\" ", next[0], next[1]);
			attr = attr.concat(thisAttr);
		}
		
		return attr;
	}
	
	public Header setAttr(String name, String value){
		String[] attrBuild = {name,value};
		if (attrList == null)
			attrList=new LinkedList<>();
		attrList.add(attrBuild);
		return this;
	}
}

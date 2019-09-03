package com.piinfo.service;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NonNull;
import lombok.Setter;

import java.util.HashMap;

@Data
public class Header {
	
	@NonNull
	public String name;
	
	@NonNull
	public String dbName;

	@NonNull
	public boolean isDataSortable;

	@Setter(AccessLevel.NONE)
	public HashMap<String,String> attrList=new HashMap<>();

	/*
	private String getAttr() {
		String attr = "";
		setAttr("data-field",dbName);
		for (String[] next : attrList) {
			String thisAttr = String.format(" %s=\"%s\" ", next[0], next[1]);
			attr = attr.concat(thisAttr);
		}
		
		return attr;
	}
	*/

	public Header setAttr(String name, String value){
		attrList.put(name,value);
		return this;
	}

	/*
	public Header build(){
		this.attr=getAttr();
		return this;
	}
	*/
}

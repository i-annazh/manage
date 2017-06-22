package com.nh.manage.until;

import com.fasterxml.jackson.databind.ObjectMapper;

public class JacksonUtils {
	private static final ObjectMapper objectMapper = new ObjectMapper();
	
	public static ObjectMapper getObjectmapper() {
		return objectMapper;
	}

	public static String getJsonString(Object obj){
		if(obj == null){
			return null;
		}
		try {
			return objectMapper.writeValueAsString(obj);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}

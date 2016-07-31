package com.leonickel.dao;

import java.util.HashMap;
import java.util.Map;

import com.leonickel.model.City;

public interface CityDAO {

	City[] getCities(String name);
	
	default Map<String, String> createJsonHeaders() {
		final Map<String, String> map = new HashMap<String, String>();
		map.put("Content-Type", "application/json");
		return map;
	}
}

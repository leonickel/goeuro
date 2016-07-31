package com.leonickel.service.impl;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.leonickel.dao.CityDAO;
import com.leonickel.model.City;
import com.leonickel.service.CityService;

@Singleton
public class CityServiceImpl implements CityService {

	@Inject
	private CityDAO cityDAO;
	
	public City[] getCities(String name) {
		return cityDAO.getCities(name);
	}
}
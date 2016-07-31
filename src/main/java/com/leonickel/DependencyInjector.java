package com.leonickel;

import com.google.inject.AbstractModule;
import com.leonickel.dao.CityDAO;
import com.leonickel.dao.impl.CityDAOImpl;
import com.leonickel.service.CityService;
import com.leonickel.service.impl.CityServiceImpl;

public class DependencyInjector extends AbstractModule {

	@Override
	protected void configure() {
		bind(CityService.class).to(CityServiceImpl.class);
		bind(CityDAO.class).to(CityDAOImpl.class);
	}
}
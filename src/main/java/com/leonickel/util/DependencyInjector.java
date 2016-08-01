package com.leonickel.util;

import com.google.inject.AbstractModule;
import com.leonickel.dao.CityDAO;
import com.leonickel.dao.impl.CityDAOImpl;
import com.leonickel.service.CityService;
import com.leonickel.service.ResponseService;
import com.leonickel.service.impl.CityServiceImpl;
import com.leonickel.service.impl.ResponseServiceFile;

public class DependencyInjector extends AbstractModule {

	@Override
	protected void configure() {
		//Service and DAO classes
		bind(CityService.class).to(CityServiceImpl.class);
		bind(CityDAO.class).to(CityDAOImpl.class);
		
		//Response implementation
		bind(ResponseService.class).to(ResponseServiceFile.class);
	}
}
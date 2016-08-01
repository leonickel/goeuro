package com.leonickel.service.impl;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.leonickel.dao.CityDAO;
import com.leonickel.exception.NoCitiesFoundException;
import com.leonickel.model.City;
import com.leonickel.model.GeoPosition;

public class CityServiceImplTest {

	private CityServiceImpl cityService;
	
	@Before
	public void setUp() throws Exception {
		CityDAO cityDAO = Mockito.mock(CityDAO.class);
		Mockito.when(cityDAO.getCities("berlin")).thenReturn(createCities(8));
		Mockito.when(cityDAO.getCities("fehwuewhfuewihfui321321fwef")).thenThrow(NoCitiesFoundException.class);
		
		cityService = new CityServiceImpl();
		cityService.setCityDAO(cityDAO);
	}

	@After
	public void tearDown() throws Exception {
		cityService = null;
	}
	
	@Test
	public void test_valid_city_name() {
		City[] cities = cityService.getCities("berlin");
		Assert.assertTrue(cities != null && cities.length > 0 && cities.length == 8);
	}
	
	@Test(expected=NoCitiesFoundException.class)
	public void test_invalid_city_name() {
		cityService.getCities("fehwuewhfuewihfui321321fwef");
	}
	
	private City[] createCities(int qntity) {
		final City[] cities = new City[qntity];
		for(int i = 0; i < qntity; i++) {
			City city = new City();
			city.setId(i);
			city.setName("test_city-"+i);
			city.setType("location");
			city.setGeoPosition(createGeoPosition(i));
			cities[i] = city;
		}
		return cities;
	}
	
	private GeoPosition createGeoPosition(int value) {
		final GeoPosition geoPosition = new GeoPosition();
		geoPosition.setLatitude(Double.valueOf(value));
		geoPosition.setLongitude(Double.valueOf(value));
		return geoPosition;
	}
}

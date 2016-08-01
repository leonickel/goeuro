package com.leonickel.dao.impl;

import static com.leonickel.util.DefaultProperties.GO_EURO_URL_CONNECT_TIMEOUT;
import static com.leonickel.util.DefaultProperties.GO_EURO_URL_READ_TIMEOUT;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.leonickel.exception.CityRetrievalException;
import com.leonickel.exception.NoCitiesFoundException;
import com.leonickel.model.City;

public class CityDAOImplTest {

	private CityDAOImpl cityDAO;
	
	@Before
	public void setUp() throws Exception {
		//Do nothing
	}

	@After
	public void tearDown() throws Exception {
		cityDAO = null;
		System.setProperty(GO_EURO_URL_CONNECT_TIMEOUT.property(), GO_EURO_URL_CONNECT_TIMEOUT.defaultValue());
		System.setProperty(GO_EURO_URL_READ_TIMEOUT.property(), GO_EURO_URL_READ_TIMEOUT.defaultValue());
	}
	
	@Test
	public void test_valid_city_name() {
		cityDAO = new CityDAOImpl();
		final City[] cities = cityDAO.getCities("berlin");
		Assert.assertTrue(cities != null && cities.length > 0 && cities.length == 8);
	}
	
	@Test(expected=NoCitiesFoundException.class)
	public void test_invalid_city_name() {
		cityDAO = new CityDAOImpl();
		cityDAO.getCities("fe3432433hwuewhfuewihfui321321fwef");
	}
	
	@Test //Using try/catch to verify correct code inside exception instead of @Test(expected=CityRetrievalException.class)
	public void test_valid_city_name_low_connect_timeout() {
		System.setProperty(GO_EURO_URL_CONNECT_TIMEOUT.property(), "1");
		cityDAO = new CityDAOImpl();
		try {
			cityDAO.getCities("berlin");
		} catch (CityRetrievalException e) {
			Assert.assertEquals(CityRetrievalException.CONNECT_TIMEOUT_CODE, e.getCode());
		}
	}
	
	@Test //Using try/catch to verify correct code inside exception instead of @Test(expected=CityRetrievalException.class)
	public void test_valid_city_name_low_read_timeout() {
		System.setProperty(GO_EURO_URL_READ_TIMEOUT.property(), "1");
		cityDAO = new CityDAOImpl();
		try {
			cityDAO.getCities("berlin");
		} catch (CityRetrievalException e) {
			Assert.assertEquals(CityRetrievalException.READ_TIMEOUT_CODE, e.getCode());
		}
	}
}
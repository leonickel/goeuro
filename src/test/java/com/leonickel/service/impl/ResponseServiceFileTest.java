package com.leonickel.service.impl;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.leonickel.exception.CityRetrievalException;
import com.leonickel.service.ResponseService;
import com.leonickel.util.DefaultProperties;

public class ResponseServiceFileTest {

	private ResponseService responseService;
	
	@Before
	public void setUp() throws Exception {
		responseService = new ResponseServiceFile();
	}
	
	@After
	public void tearDown() throws Exception {
	}
	
	@Test
	public void test_get_error_response_for_connect_timeout() {
		String response = responseService.getErrorResponse(CityRetrievalException.CONNECT_TIMEOUT_CODE, "something");
		Assert.assertTrue(response.contains(DefaultProperties.GO_EURO_URL_CONNECT_TIMEOUT.property()));
	}
	
	@Test
	public void test_get_error_response_for_read_timeout() {
		String response = responseService.getErrorResponse(CityRetrievalException.READ_TIMEOUT_CODE, "something");
		Assert.assertTrue(response.contains(DefaultProperties.GO_EURO_URL_READ_TIMEOUT.property()));
	}
	
	@Test
	public void test_get_error_response_for_unknown_error() {
		Assert.assertNotNull(responseService.getErrorResponse(CityRetrievalException.UNKNOWN_ERROR_CODE, "something"));
	}
	
	@Test
	public void test_get_error_response_for_unmapped_error() {
		Assert.assertNotNull(responseService.getErrorResponse(99, "something"));
	}
}

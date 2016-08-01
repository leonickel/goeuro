package com.leonickel.service;

import com.leonickel.model.City;

public interface ResponseService {

	void writeResponse(City[] cities);
	
	String getErrorResponse(int code, String message);
}
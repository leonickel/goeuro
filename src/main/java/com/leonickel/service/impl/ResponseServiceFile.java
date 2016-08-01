package com.leonickel.service.impl;

import static com.leonickel.exception.CityRetrievalException.CONNECT_TIMEOUT_CODE;
import static com.leonickel.exception.CityRetrievalException.HTTP_CLIENT_ERROR_CODE;
import static com.leonickel.exception.CityRetrievalException.READ_TIMEOUT_CODE;
import static com.leonickel.exception.CityRetrievalException.UNKNOWN_ERROR_CODE;
import static com.leonickel.util.DefaultProperties.GO_EURO_URL_CONNECT_TIMEOUT;
import static com.leonickel.util.DefaultProperties.GO_EURO_URL_READ_TIMEOUT;

import java.io.FileWriter;
import java.io.IOException;

import com.leonickel.exception.FileResultException;
import com.leonickel.model.City;
import com.leonickel.service.ResponseService;
import com.leonickel.util.DefaultProperties;
import com.leonickel.util.PropertyFinder;

public class ResponseServiceFile implements ResponseService {

	@Override
	public void writeResponse(City[] cities) {
		try {
			final FileWriter fileWriter = new FileWriter(PropertyFinder.getPropertyValue(DefaultProperties.CSV_OUTPUT_PATH) + 
					PropertyFinder.getPropertyValue(DefaultProperties.CSV_FILE_NAME));
			final String csvSeparatorValue = PropertyFinder.getPropertyValue(DefaultProperties.CSV_SEPARATOR_VALUE);
			for(City city : cities) {
				fileWriter.write(createCityCsvLine(city, csvSeparatorValue));
				fileWriter.write("\n");
			}
			fileWriter.flush();
			fileWriter.close();
		} catch (IOException e) {
			throw new FileResultException("error on creating/writing result file");
		}
	}

	@Override
	public String getErrorResponse(int code, String message) {
		switch (code) {
		case CONNECT_TIMEOUT_CODE:
			return createMessage(message, ", try increasing " + GO_EURO_URL_CONNECT_TIMEOUT.property() + " property and running again.");
		case READ_TIMEOUT_CODE:
			return createMessage(message, ", try increasing " + GO_EURO_URL_READ_TIMEOUT.property() + " property and running again.");
		case HTTP_CLIENT_ERROR_CODE:
			return createMessage(message, ", try running application again.");
		case UNKNOWN_ERROR_CODE:
			return createMessage(message, ", try running application again.");
		default:
			return createMessage(message, ", try running application again.");
		}
	}
	
	private String createCityCsvLine(City city, String csvSeparatorValue) {
		return new StringBuilder().append(city.getId()).append(csvSeparatorValue).append(city.getName())
				.append(csvSeparatorValue).append(city.getType()).append(csvSeparatorValue).append(city.getGeoPosition().getLatitude())
				.append(csvSeparatorValue).append(city.getGeoPosition().getLongitude()).toString();
	}

	private String createMessage(String exceptionMessage, String otherMessage) {
		return new StringBuilder(exceptionMessage).append(otherMessage).toString();
	}

}
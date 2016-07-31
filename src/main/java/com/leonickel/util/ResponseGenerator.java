package com.leonickel.util;

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

public class ResponseGenerator {

	public static void generate(City[] cities, String csvFileName, String csvPath, String csvSeparatorValue) {
		try {
			final FileWriter fileWriter = new FileWriter(csvPath + csvFileName);
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

	public static String formatErrorResponse(int code, String message) {
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
	
	private static String createCityCsvLine(City city, String csvSeparatorValue) {
		return new StringBuilder().append(city.getId()).append(csvSeparatorValue).append(city.getName())
				.append(csvSeparatorValue).append(city.getType()).append(csvSeparatorValue).append(city.getGeoPosition().getLatitude())
				.append(csvSeparatorValue).append(city.getGeoPosition().getLongitude()).toString();
	}

	private static String createMessage(String exceptionMessage, String otherMessage) {
		return new StringBuilder(exceptionMessage).append(otherMessage).toString();
	}
}

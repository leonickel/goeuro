package com.leonickel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.leonickel.exception.CityRetrievalException;
import com.leonickel.exception.FileResultException;
import com.leonickel.exception.NoCitiesFoundException;
import com.leonickel.model.City;
import com.leonickel.service.CityService;
import com.leonickel.util.DefaultProperties;
import com.leonickel.util.DependencyInjector;
import com.leonickel.util.PropertyFinder;
import com.leonickel.util.ResponseGenerator;

public class Bootstrap {
	private static Logger logger = LoggerFactory.getLogger("application");

	public static void main(String[] args) {
		if(args.length < 1) {
			logger.error("Please provide at least one argument (city name) to run this application, exitting...");
			System.exit(9);
		}
		logger.info("default output path for application.log file: [.]");
		logger.info("city name received: [{}]", args[0]);

		final String csvPath = PropertyFinder.getPropertyValue(DefaultProperties.CSV_OUTPUT_PATH);
		final String csvFileName = PropertyFinder.getPropertyValue(DefaultProperties.CSV_FILE_NAME);
		logger.info("CSV file name: [{}]", csvFileName);
		logger.info("output path for CSV response file: [{}]", csvPath);

		final Injector injector = Guice.createInjector(new DependencyInjector());
		final CityService service = injector.getInstance(CityService.class);

		try {
			final City[] cities = service.getCities(args[0]);
			ResponseGenerator.generate(cities, csvFileName, csvPath, PropertyFinder.getPropertyValue(DefaultProperties.CSV_SEPARATOR_VALUE));
			logger.info("successfull generated file: [{}] in path: [{}]", csvFileName, csvPath);
		} catch (NoCitiesFoundException e) {
			logger.info("no cities were found using city name: [{}], try searching with another name", args[0]);
		} catch (CityRetrievalException e) {
			logger.error(ResponseGenerator.formatErrorResponse(e.getCode(), e.getMessage()));
		} catch (FileResultException e) {
			logger.error("error on creating/writing result file, please check disk space and directory permissions and start application again");
		} catch (Exception e) {
			logger.error("something wrong has occurred, please check parameters provided and try starting application again");
		}
	}
}
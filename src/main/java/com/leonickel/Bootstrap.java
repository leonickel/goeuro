package com.leonickel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.leonickel.exception.CityRetrievalException;
import com.leonickel.exception.FileResultException;
import com.leonickel.exception.NoCitiesFoundException;
import com.leonickel.service.CityService;
import com.leonickel.service.ResponseService;
import com.leonickel.util.DefaultProperties;
import com.leonickel.util.DependencyInjector;
import com.leonickel.util.PropertyFinder;

public class Bootstrap {
	private static Logger logger = LoggerFactory.getLogger("application");

	public static void main(String[] args) {
		if(args.length < 1) {
			logger.error("Please provide at least one argument (city name) to run this application, exitting...");
			return; //instead of System.exit(9) for unit tests flow
		}
		logger.info("default path for application.log file: [.]");
		logger.info("city name received: [{}]", args[0]);

		final String csvPath = PropertyFinder.getPropertyValue(DefaultProperties.CSV_OUTPUT_PATH);
		final String csvFileName = PropertyFinder.getPropertyValue(DefaultProperties.CSV_FILE_NAME);
		final String csvSeparatorValue = PropertyFinder.getPropertyValue(DefaultProperties.CSV_SEPARATOR_VALUE);
		logger.info("response CSV file name: [{}]", csvFileName);
		logger.info("response CSV separator value: [{}]", csvSeparatorValue);
		logger.info("response CSV output path: [{}]", csvPath);

		final Injector injector = Guice.createInjector(new DependencyInjector());
		final CityService service = injector.getInstance(CityService.class);
		final ResponseService responseService = injector.getInstance(ResponseService.class);

		try {
			responseService.writeResponse(service.getCities(args[0]));
			logger.info("successfull generated file: [{}] in path: [{}]", csvFileName, csvPath);
		} catch (NoCitiesFoundException e) {
			logger.info("no cities were found using city name: [{}], try searching with another name", args[0]);
		} catch (CityRetrievalException e) {
			logger.error(responseService.getErrorResponse(e.getCode(), e.getMessage()));
		} catch (FileResultException e) {
			logger.error("error on creating/writing result file, please check disk space and directory permissions and start application again");
		} catch (Exception e) {
			logger.error("something wrong has occurred, please check parameters provided and try starting application again");
		}
	}
}
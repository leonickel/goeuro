package com.leonickel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.leonickel.model.City;
import com.leonickel.service.CityService;
import com.leonickel.util.DefaultProperties;
import com.leonickel.util.DependencyInjector;
import com.leonickel.util.PropertyFinder;

public class Bootstrap {
	private static Logger logger = LoggerFactory.getLogger("application");

	public static void main(String[] args) {
		if(args.length < 1) {
			logger.error("please provide at least city name to run this application");
			System.out.println("Please provide at least city name to run this application");
			System.exit(9);
		}
		logger.info("city name received: [{}]", args[0]);
		logger.info("default output path for application.log file: [.]");
		logger.info("default output path for CSV response file: [{}]", PropertyFinder.getPropertyValue(DefaultProperties.CSV_OUTPUT_PATH));

		final Injector injector = Guice.createInjector(new DependencyInjector());
		final CityService service = injector.getInstance(CityService.class);

		final City[] cities = service.getCities(args[0]);
		if(cities.length < 1) {
			logger.info("no cities were found for received city name: [{}]", args[0]);
			System.exit(9);
		}
		
		for(City city : cities) {
			System.out.println(city.getId() + " " + city.getName());
		}
	}
}
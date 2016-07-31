package com.leonickel;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.leonickel.model.City;
import com.leonickel.service.CityService;

public class Bootstrap {

	public static void main(String[] args) {
		if(args.length < 1) {
			System.out.println("Please provide city name to run this application");
			System.exit(9);
		}
		final Injector injector = Guice.createInjector(new DependencyInjector());
		final CityService service = injector.getInstance(CityService.class);
		
		final City[] cities = service.getCities(args[0]);
		for(City city : cities) {
			System.out.println(city.get_id() + " " + city.getName());
		}
	}
}
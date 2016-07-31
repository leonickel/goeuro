package com.leonickel.dao;

import com.leonickel.model.City;

public interface CityDAO {

	City[] getCities(String name);
}

package com.leonickel.model;

import com.google.gson.annotations.SerializedName;

public class City {

	@SerializedName("_id")
	private int id;
	
	@SerializedName("name")
	private String name;
	
	@SerializedName("type")
	private String type;
	
	@SerializedName("geo_position")
	private GeoPosition geoPosition;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public GeoPosition getGeoPosition() {
		return geoPosition;
	}

	public void setGeoPosition(GeoPosition geoPosition) {
		this.geoPosition = geoPosition;
	}

}
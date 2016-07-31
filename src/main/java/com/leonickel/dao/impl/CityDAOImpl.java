package com.leonickel.dao.impl;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.SocketTimeoutException;

import javax.inject.Singleton;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import com.google.gson.Gson;
import com.leonickel.dao.CityDAO;
import com.leonickel.dao.GoEuroProperties;
import com.leonickel.model.City;

@Singleton
public class CityDAOImpl implements CityDAO {

	private final Gson gson = new Gson();
	private final CloseableHttpClient httpClient = HttpClients.createDefault();
	private final RequestConfig requestConfig = createRequestConfig();

	public City[] getCities(String name) {
		CloseableHttpResponse response = null;
		HttpGet method = null;
		try {
			method = new HttpGet(getUrl(name));
			method.setConfig(requestConfig);
			response = httpClient.execute(method);
		    return gson.fromJson(new InputStreamReader(response.getEntity().getContent()), City[].class);
		} catch (ConnectTimeoutException e) {
			abort(method);
			e.printStackTrace();
		} catch (SocketTimeoutException e) {
			abort(method);
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			abort(method);
			e.printStackTrace();
		} catch (IOException e) {
			abort(method);
			e.printStackTrace();
		} finally {
			if(response != null) {
				try {
					response.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}
	
	private void abort(HttpGet method) {
		if(method != null) {
			method.abort();
		}
	}

	private RequestConfig createRequestConfig() {
		return RequestConfig.custom()
				.setSocketTimeout(Integer.parseInt(getPropertyValue(GoEuroProperties.GO_EURO_URL_READ_TIMEOUT.property(), 
						GoEuroProperties.GO_EURO_URL_READ_TIMEOUT.defaultValue())))
				.setConnectTimeout(Integer.parseInt(getPropertyValue(GoEuroProperties.GO_EURO_URL_CONNECT_TIMEOUT.property(), 
						GoEuroProperties.GO_EURO_URL_CONNECT_TIMEOUT.defaultValue()))).build();
	}
	
	private String getUrl(String name) {
		return new StringBuilder(getPropertyValue(GoEuroProperties.GO_EURO_URL.property(), GoEuroProperties.GO_EURO_URL.defaultValue()))
		.append("/").append(name).toString(); 
	}
	
	private String getPropertyValue(String propertyName, String defaultValue) {
		return System.getProperty(propertyName, defaultValue);
	}

}
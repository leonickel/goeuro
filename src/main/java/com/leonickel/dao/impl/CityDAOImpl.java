package com.leonickel.dao.impl;

import static com.leonickel.util.DefaultProperties.GO_EURO_URL;
import static com.leonickel.util.DefaultProperties.GO_EURO_URL_CONNECT_TIMEOUT;
import static com.leonickel.util.DefaultProperties.GO_EURO_URL_MAX_RETRY;
import static com.leonickel.util.DefaultProperties.GO_EURO_URL_READ_TIMEOUT;
import static com.leonickel.exception.CityRetrievalException.*;

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
import org.apache.http.impl.client.StandardHttpRequestRetryHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.leonickel.dao.CityDAO;
import com.leonickel.exception.CityRetrievalException;
import com.leonickel.exception.NoCitiesFoundException;
import com.leonickel.model.City;
import com.leonickel.util.PropertyFinder;

@Singleton
public class CityDAOImpl implements CityDAO {

	private Logger logger = LoggerFactory.getLogger("application");
	private final Gson gson = new Gson();
	private final CloseableHttpClient httpClient = createHttpClient();
	private final RequestConfig requestConfig = createRequestConfig();

	public City[] getCities(String name) {
		CloseableHttpResponse response = null;
		HttpGet method = null;
		try {
			method = createHttpGet(name);
			logger.info("requested url: [{}]", method.getURI());
			response = httpClient.execute(method);
			logger.info("successfull request: [{}]", method.getURI());
			final City[] cities = gson.fromJson(new InputStreamReader(response.getEntity().getContent()), City[].class);
			if(cities == null || cities.length == 0) {
				throw new NoCitiesFoundException("no cities were found");
			}
			logger.info("found [{}] cities in response", cities.length);
			return cities;
		} catch (ConnectTimeoutException e) {
			logHttpErrors("connect timeout when trying to connect on url: [{}]"); 
			abort(method);
			throw new CityRetrievalException("connect timeout when trying to connect on url", CONNECT_TIMEOUT_CODE);
		} catch (SocketTimeoutException e) {
			logHttpErrors("read timeout when trying to get response from url: [{}]"); 
			abort(method);
			throw new CityRetrievalException("read timeout when trying to get response from url", READ_TIMEOUT_CODE);
		} catch (ClientProtocolException e) {
			logHttpErrors("http client error when trying to execute this url: [{}]"); 
			abort(method);
			throw new CityRetrievalException("http client error when trying to execute this url", HTTP_CLIENT_ERROR_CODE);
		} catch (IOException e) {
			logger.error("unknown i/o error when trying to execute this url: [{}] exception: [{}]", PropertyFinder.getPropertyValue(GO_EURO_URL), e);
			abort(method);
			throw new CityRetrievalException("unknown i/o error when trying to execute this url", UNKNOWN_ERROR_CODE);
		} finally {
			if(response != null) {
				try {
					response.close();
				} catch (IOException e) {
					logger.error("error on closing CloseableHttpResponse object [{}]", e);
				}
			}
		}
	}
	
	private void logHttpErrors(String message) {
		logger.error(message, PropertyFinder.getPropertyValue(GO_EURO_URL));
	}
	
	private void abort(HttpGet method) {
		if(method != null) {
			method.abort();
		}
	}

	private HttpGet createHttpGet(String name) {
		final HttpGet method = new HttpGet(getUrl(name));
		method.setConfig(requestConfig);
		return method;
	}

	private CloseableHttpClient createHttpClient() {
		return HttpClients.custom().setRetryHandler(new StandardHttpRequestRetryHandler(getMaxRetryValue(), true)).build();
	}
	
	private RequestConfig createRequestConfig() {
		return RequestConfig.custom().setSocketTimeout(getSocketTimeoutValue()).setConnectTimeout(getConnectTimeoutValue()).build();
	}
	
	private int getMaxRetryValue() {
		return Integer.parseInt(PropertyFinder.getPropertyValue(GO_EURO_URL_MAX_RETRY));
	}

	private int getSocketTimeoutValue() {
		return Integer.parseInt(PropertyFinder.getPropertyValue(GO_EURO_URL_READ_TIMEOUT));
	}

	private int getConnectTimeoutValue() {
		return Integer.parseInt(PropertyFinder.getPropertyValue(GO_EURO_URL_CONNECT_TIMEOUT));
	}

	private String getUrl(String name) {
		return new StringBuilder(PropertyFinder.getPropertyValue(GO_EURO_URL)).append("/").append(name).toString(); 
	}

}
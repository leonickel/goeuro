package com.leonickel.dao.impl;

import static com.leonickel.util.DefaultProperties.GO_EURO_URL;
import static com.leonickel.util.DefaultProperties.GO_EURO_URL_CONNECT_TIMEOUT;
import static com.leonickel.util.DefaultProperties.GO_EURO_URL_MAX_RETRY;
import static com.leonickel.util.DefaultProperties.GO_EURO_URL_READ_TIMEOUT;

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
			method = new HttpGet(getUrl(name));
			method.setConfig(requestConfig);
			logger.info("requested url: [{}]", method.getURI());
			response = httpClient.execute(method);
			logger.info("successfull http call: [{}] response: [{}]", method.getURI());
		    return gson.fromJson(new InputStreamReader(response.getEntity().getContent()), City[].class);
		} catch (ConnectTimeoutException e) {
			logHttpErrors("connect timeout when trying to connect on url: [{}]"); 
			abort(method);
		} catch (SocketTimeoutException e) {
			logHttpErrors("read timeout when trying to get response from url: [{}]"); 
			abort(method);
		} catch (ClientProtocolException e) {
			logHttpErrors("http client error when trying to execute this url: [{}]"); 
			abort(method);
		} catch (IOException e) {
			logger.error("unknown i/o error when trying to execute this url: [{}] exception: [{}]", PropertyFinder.getPropertyValue(GO_EURO_URL), e);
			abort(method);
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
	
	private void logHttpErrors(String message) {
		logger.error(message, PropertyFinder.getPropertyValue(GO_EURO_URL));
	}
	
	private void abort(HttpGet method) {
		if(method != null) {
			method.abort();
		}
	}

	private CloseableHttpClient createHttpClient() {
		return HttpClients.custom().setRetryHandler(new StandardHttpRequestRetryHandler(Integer.parseInt(
				PropertyFinder.getPropertyValue(GO_EURO_URL_MAX_RETRY)), true)).build();
	}
	
	private RequestConfig createRequestConfig() {
		return RequestConfig.custom()
				.setSocketTimeout(Integer.parseInt(PropertyFinder.getPropertyValue(GO_EURO_URL_READ_TIMEOUT)))
				.setConnectTimeout(Integer.parseInt(PropertyFinder.getPropertyValue(GO_EURO_URL_CONNECT_TIMEOUT))).build();
	}
	
	private String getUrl(String name) {
		return new StringBuilder(PropertyFinder.getPropertyValue(GO_EURO_URL)).append("/").append(name).toString(); 
	}

}
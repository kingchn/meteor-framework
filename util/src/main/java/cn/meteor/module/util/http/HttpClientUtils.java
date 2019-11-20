package cn.meteor.module.util.http;

import org.apache.http.client.CookieStore;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

public class HttpClientUtils {

	public static HttpClientConnectionManager httpClientConnectionManager = null;

	public void setHttpClientConnectionManager(HttpClientConnectionManager httpClientConnectionManager) {
		HttpClientUtils.httpClientConnectionManager = httpClientConnectionManager;
	}

	public static CloseableHttpClient getHttpClient() {
		CloseableHttpClient httpClient = HttpClients.custom().setConnectionManager(HttpClientUtils.httpClientConnectionManager).build();
		return httpClient;
	}
	
	public static CloseableHttpClient getHttpClient(CookieStore cookieStore) {
		CloseableHttpClient httpClient = HttpClients.custom().setConnectionManager(HttpClientUtils.httpClientConnectionManager)
				.setDefaultCookieStore(cookieStore).build();
		return httpClient;
	}
	
	public static CloseableHttpClient getHttpClient(RequestConfig config) {
		CloseableHttpClient httpClient = HttpClients.custom().setConnectionManager(HttpClientUtils.httpClientConnectionManager)
				.setDefaultRequestConfig(config).build();
		return httpClient;
	}
	
	
}

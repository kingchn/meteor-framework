package cn.meteor.module.util.http;

import org.apache.http.client.CookieStore;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.util.EntityUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath*:/applicationContext.xml",
		"classpath*:/applicationContext*.xml",
		"classpath*:conf/**/meteor*applicationContext*.xml" }) 
public class HttpClientUtilsTestCase {

	@Test
	public void testHttpClientConnectionManager() {
		try {
			String url = "https://www.baidu.com";
			CloseableHttpClient httpClient = HttpClientUtils.getHttpClient();
			HttpGet httpGet = new HttpGet(url);
			RequestConfig requestConfig = RequestConfig.custom()
			        .setConnectionRequestTimeout(2000).setConnectTimeout(5000)
			        .setSocketTimeout(5000).build();//RequestTimeout（连接池获取到连接的超时时间）、ConnectTimeout（建立连接的超时）、SocketTimeout（获取数据的超时时间）
			httpGet.setConfig(requestConfig); 
			CloseableHttpResponse httpResponse = httpClient.execute(httpGet);
			if (httpResponse.getStatusLine().getStatusCode() == 200) {
				System.out.println("response:" + httpResponse.getStatusLine().getStatusCode());
				String content = EntityUtils.toString(httpResponse.getEntity(), "utf-8");
				System.out.println("response:\n" + content);
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testHttpClientConnectionManagerWithCookie() {
		try {
			String url = "https://test.etax-gd.gov.cn/abc.do";
			
			CookieStore cookieStore = new BasicCookieStore();
			BasicClientCookie clientCookie = new BasicClientCookie("DZSWJ_TGC", "c45be0806aa74f4d9b94c3b945d76e3c");
			clientCookie.setVersion(0);
			clientCookie.setDomain("test.etax-gd.gov.cn");
//			clientCookie.setDomain("etax.fj-n-tax.gov.cn");
			clientCookie.setPath("/");
			cookieStore.addCookie(clientCookie);
			
			CloseableHttpClient httpClient = HttpClientUtils.getHttpClient(cookieStore);
			HttpGet httpGet = new HttpGet(url);
			RequestConfig requestConfig = RequestConfig.custom()
//					.setProxy(new HttpHost("127.0.0.1", 3128, null))
			        .setConnectionRequestTimeout(2000).setConnectTimeout(5000)
			        .setSocketTimeout(5000).build();//RequestTimeout（连接池获取到连接的超时时间）、ConnectTimeout（建立连接的超时）、SocketTimeout（获取数据的超时时间）
			httpGet.setConfig(requestConfig); 
			CloseableHttpResponse httpResponse = httpClient.execute(httpGet);
			if (httpResponse.getStatusLine().getStatusCode() == 200) {
				System.out.println("response:" + httpResponse.getStatusLine().getStatusCode());
				String content = EntityUtils.toString(httpResponse.getEntity(), "utf-8");
				System.out.println("response:\n" + content);
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

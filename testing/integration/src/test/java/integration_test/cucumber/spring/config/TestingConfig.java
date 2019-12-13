package integration_test.cucumber.spring.config;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@Configuration
@ComponentScan(basePackages = { 
		"integration_test.spring.testing"
})
public class TestingConfig {
	
	private static final int DEFAULT_MAX_TOTAL_CONNECTIONS = 1000;
	private static final int DEFAULT_MAX_CONNECTIONS_PER_ROUTE = 100;
	private static final int TIMEOUT_CONNECTION = 10000;
	private static final int TIMEOUT_REQUEST = 10000;
	
	@Bean
	public RestTemplate restTemplate(){
		return new RestTemplate(httpRequestFactory());
	}
	
	@Bean
	public ClientHttpRequestFactory httpRequestFactory() {		
		HttpComponentsClientHttpRequestFactory ht = new HttpComponentsClientHttpRequestFactory(httpClient());
		ht.setConnectionRequestTimeout(TIMEOUT_CONNECTION);
		ht.setReadTimeout(TIMEOUT_REQUEST);
		ht.setConnectTimeout(TIMEOUT_CONNECTION);
		return ht;
	}
	
	@Bean
	public CloseableHttpClient httpClient() {
		PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();
		connectionManager.setMaxTotal(DEFAULT_MAX_TOTAL_CONNECTIONS);
		connectionManager.setDefaultMaxPerRoute(DEFAULT_MAX_CONNECTIONS_PER_ROUTE);
		RequestConfig config = RequestConfig.custom()
				.setConnectTimeout(TIMEOUT_CONNECTION)
				.setSocketTimeout(TIMEOUT_REQUEST)
				.build();
		
		return HttpClientBuilder.create()
				.setConnectionManager(connectionManager).setDefaultRequestConfig(config).build();
	}
}

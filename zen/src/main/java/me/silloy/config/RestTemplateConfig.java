package me.silloy.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

/**
 * @author 李洪春
 * @create 2018/5/16 下午2:02
 **/
@Configuration
public class RestTemplateConfig {

    @Bean(name = "loadBalancedRestTemplate")
    public RestTemplate loadBalancedRestTemplate(@Qualifier("clientHttpRequestFactory") ClientHttpRequestFactory factory) {
        RestTemplate restTemplate = new RestTemplate(factory);
        return restTemplate;
    }

    @Bean(name = "restTemplate")
    public RestTemplate restTemplate(@Qualifier("clientHttpRequestFactory") ClientHttpRequestFactory factory) {
        RestTemplate restTemplate = new RestTemplate(factory);
        return restTemplate;
    }

    @Bean("clientHttpRequestFactory")
    public ClientHttpRequestFactory simpleClientHttpRequestFactory() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        //ms
        factory.setReadTimeout(5000);
        //ms
        factory.setConnectTimeout(15000);
        return factory;
    }
}

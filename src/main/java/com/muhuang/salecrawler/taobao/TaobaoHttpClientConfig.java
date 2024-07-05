package com.muhuang.salecrawler.taobao;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

/**
 * @author Frank An
 */
@Configuration
public class TaobaoHttpClientConfig {

    @Bean
    public TaobaoHttpClient taobaoHttpClient() {
        RestClient client = RestClient.builder()
                .baseUrl("http://152.136.237.210")
                .defaultHeader("Accept", "application/json")
                .defaultHeader("Content-Type", "application/json")
                .build();
        var fac = HttpServiceProxyFactory.builderFor(RestClientAdapter.create(client))
                .build();
        return fac.createClient(TaobaoHttpClient.class);
    }
}

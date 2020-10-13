package com.meri.test_task.config;


import com.meri.test_task.service.ApiSupplier;
import com.meri.test_task.service.MainService;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.RestClients;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

import static com.meri.test_task.controller.MainController.*;

@Configuration
@EnableElasticsearchRepositories(basePackages = "com.meri.test_task.repository")
@ComponentScan
public class Config {


    @Bean
    public RestHighLevelClient client() {
        ClientConfiguration clientConfiguration =
                ClientConfiguration.builder().connectedTo("localhost:9300").build();

        return RestClients.create(clientConfiguration).rest();
    }

    @Bean
    public ElasticsearchOperations elasticsearchTemplate(){
        return new ElasticsearchRestTemplate(client());
    }

    @Bean
    public ApiSupplier firstApiSupplier(MainService mainService){
        return new ApiSupplier(mainService::callFirstApi, "first", URI_FIRST, PRESET_SEARCH_FILTER);
    }

    @Bean
    public ApiSupplier secondApiSupplier(MainService mainService){
        return new ApiSupplier(mainService::callSecondApi, "second", URI_SECOND, PRESET_SEARCH_FILTER);
    }

}

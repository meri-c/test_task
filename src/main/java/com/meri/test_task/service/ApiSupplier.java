package com.meri.test_task.service;

import com.meri.test_task.entity.ApiLog;
import com.meri.test_task.entity.CacheElastic;
import com.meri.test_task.service.service_pojo.ApiResult;
import com.meri.test_task.service.service_pojo.PriceResult;
import com.meri.test_task.service.service_pojo.SearchResult;
import lombok.extern.java.Log;
import org.springframework.util.CollectionUtils;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Log
public class ApiSupplier {

    private ApiCallable apiCaller;
    private String api_name;
    private String uri_value;
    private String search_filter;


    public ApiSupplier(ApiCallable apiCaller, String api_name, String uri_value, String search_filter) {
        this.apiCaller = apiCaller;
        this.api_name = api_name;
        this.uri_value = uri_value;
        this.search_filter = search_filter;
    }


    public ApiResult get(MainService mainService) {

        log.info("Running the " + api_name + " api thread");

        ApiResult apiResult = new ApiResult();

        //get and parse data from the first api
        List<SearchResult> searchResults = apiCaller.callApi(uri_value, search_filter);

        if (CollectionUtils.isEmpty(searchResults)) {
            return apiResult;
        }

        //Fill the result object
        apiResult = setApiResult(searchResults.get(0));


        //create api_log obj
        ApiLog apiLog = new ApiLog(new Timestamp((new Date().getTime())), api_name, search_filter);

        //insert to mysql
        mainService.saveApiLog(apiLog);

        log.info("api_log from " + api_name + " was saved to mysql");

        //insert all search results to elastic
        for (SearchResult sr : searchResults) {

            CacheElastic req = new CacheElastic(api_name, String.valueOf(new Date().getTime()),
                    sr.getStreet(), sr.getCity(), sr.getCountry(), sr.getTerm(),
                    sr.getMrc(), sr.getNrc());

            mainService.saveCacheElastic(req);

            //add to result api
            apiResult.getPrices().add(new PriceResult(api_name, sr.getTerm(), sr.getMrc(), sr.getNrc()));

            log.info("search result from " + api_name + " was saved to elastic");

        }
        return apiResult;
    }



    private static ApiResult setApiResult(SearchResult searchResult) {
        ApiResult apiResult = new ApiResult();
        apiResult.setCity(searchResult.getCity());
        apiResult.setStreet(searchResult.getStreet());
        apiResult.setCountry(searchResult.getCountry());
        apiResult.setPrices(new ArrayList<>());

        return apiResult;
    }

    public String getApi_name() {
        return api_name;
    }
}

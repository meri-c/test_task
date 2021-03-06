package com.meri.test_task.service;

import com.meri.test_task.entity.ApiLog;
import com.meri.test_task.entity.CacheElastic;
import com.meri.test_task.repository.ApiLogRepository;
import com.meri.test_task.repository.CacheElasticRepository;
import com.meri.test_task.service.service_pojo.ApiResult;
import com.meri.test_task.service.service_pojo.SearchResult;
import lombok.extern.java.Log;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.XML;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Log
@Service
public class MainService {

    private final ApiLogRepository apiLogRepository;
    private final CacheElasticRepository cacheElasticRepository;

    @Autowired
    public MainService(ApiLogRepository apiLogRepository, CacheElasticRepository cacheElasticRepository) {
        this.apiLogRepository = apiLogRepository;
        this.cacheElasticRepository = cacheElasticRepository;
    }

    public static ApiResult combineTwoResults(ApiResult first_result, ApiResult second_result){
        log.info("combining results");
        ApiResult commonResult = new ApiResult();
        /*commonResult.setCountry(first_result.getCountry()||first_result.getCountry());*/
        if(first_result.getCountry() == null && second_result.getCountry() == null){
            return commonResult;
        }
        if(first_result.getCountry() == null){
            return second_result;
        }
        commonResult.setCountry(first_result.getCountry());
        commonResult.setStreet(first_result.getStreet());
        commonResult.setCity(first_result.getCity());

        commonResult.setPrices(new ArrayList<>());
        commonResult.getPrices().addAll(first_result.getPrices());
        commonResult.getPrices().addAll(second_result.getPrices());

        return commonResult;
    }

    void saveApiLog(ApiLog apiLog) {
        log.info(apiLog.getApi_name() + " saving log");
        apiLogRepository.save(apiLog);
    }

    void saveCacheElastic(CacheElastic cacheElastic) {
        log.info(cacheElastic.getApiName() + " saving cache");
        cacheElasticRepository.save(cacheElastic);
    }

    //call post search request to "first" api with json body, get xml in return, process to SearchResult list obj
    public List<SearchResult> callFirstApi(String uri_value, String search_filter) {
        log.info("In call firstApi");
        RestTemplate restTemplate = new RestTemplate();


        URI uri = stringToUriConverting(uri_value);
        if(uri == null){
            return Collections.emptyList();
        }


        RequestEntity request = RequestEntity.post(uri)
                .contentType(MediaType.APPLICATION_XML)
                .body(search_filter);

        String result = restTemplate.postForObject(uri, search_filter, String.class);


        return processResultXmlToSearchResultList(result);

    }

    //call post search request to "second" api with json body, get json in return, process to SearchResult list obj
    public List<SearchResult> callSecondApi(String uri_value, String search_filter) {
        log.info("In call secondApi");
        RestTemplate restTemplate = new RestTemplate();

        URI uri = stringToUriConverting(uri_value);
        if(uri == null){
            return Collections.emptyList();
        }

        RequestEntity request = RequestEntity.post(uri)
                .contentType(MediaType.APPLICATION_JSON)
                .body(search_filter);


        ResponseEntity<List<SearchResult>> result = restTemplate.exchange
                (uri, HttpMethod.POST, request, new ParameterizedTypeReference<List<SearchResult>>() {
                });

        return result.getBody();

    }


    //*Turns xml string to json, removes unnecessary parts, returns filled ArrayList of SearchResult objects
    private static List<SearchResult> processResultXmlToSearchResultList(String xml_string) {
        JSONObject json_xml_result = XML.toJSONObject(xml_string);

        List<SearchResult> searchResults = new ArrayList<>();

        JSONObject temp_json = json_xml_result.getJSONObject("pricing");
        JSONArray quotes = temp_json.getJSONArray("quote");

        for (int i = 0; i < quotes.length(); i++) {
            searchResults.add(jsonXmlValuesToSearchResult(quotes.getJSONObject(i)));
        }

        return searchResults;
    }


    private static URI stringToUriConverting(String uri_value){
        try {
            return new URI(uri_value);
        } catch (URISyntaxException e) {
            e.printStackTrace();
            log.warning("Uri converting troubles with the string "+ uri_value);
            return null;
        }
    }

    //* Transforms cropped from xml json to a SearchResult obj
    private static SearchResult jsonXmlValuesToSearchResult(JSONObject json) {
        JSONObject price = json.getJSONObject("price");
        JSONObject location = json.getJSONObject("location");

        return new SearchResult(location.getString("address"),
                location.getString("city"), location.getString("country"), String.valueOf(json.get("term")),
                price.getDouble("mrc"), price.getDouble("nrc"));

    }
}

package com.meri.test_task.service;

import com.meri.test_task.service.service_pojo.SearchResult;

import java.util.List;

@FunctionalInterface
public interface ApiCallable {
    List<SearchResult> callApi(String uri_value, String search_filter);

}


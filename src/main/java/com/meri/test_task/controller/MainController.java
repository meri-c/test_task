package com.meri.test_task.controller;

import com.meri.test_task.service.ApiRunnable;
import com.meri.test_task.service.MainService;
import com.meri.test_task.service.service_pojo.ApiResult;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URISyntaxException;

@Log
@RestController("/")
public class MainController {
    private final String uri_first = "http://localhost:8090/first";
    private final String uri_second = "http://localhost:8090/second";

    final String preset_search_filter = "[\n" +
            "  {\n" +
            "    \"street\": \"Pushkin st. 59\",\n" +
            "    \"city\": \"Odessa\",\n" +
            "    \"country\": \"Ukraine\",\n" +
            "    \"terms\": [\n" +
            "      12,\n" +
            "      24\n" +
            "    ]\n" +
            "  }\n" +
            "]\n";

    private final MainService mainService;

    @Autowired
    ApiRunnable runFirstApi;

    @Autowired
    ApiRunnable runSecondApi;


    public MainController(MainService mainService) {

        this.mainService = mainService;
    }


    @GetMapping("start")
    public ApiResult multiThreadFirstSecondApi() throws URISyntaxException {

        log.info("=================START===================");


        //create runnables for threads
        runFirstApi.setApiRunnableParams(mainService::callFirstApi, "first", uri_first, preset_search_filter);
        runSecondApi.setApiRunnableParams(mainService::callSecondApi, "second", uri_second, preset_search_filter);


        //create threads
        Thread first = new Thread(runFirstApi, "first");
        Thread second = new Thread(runSecondApi, "second");

        first.start();
        second.start();

        //for main thread to wait
        try {
            first.join();
            second.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return mainService.combineApiResults(runFirstApi.getApiResult(), runSecondApi.getApiResult());

    }

}

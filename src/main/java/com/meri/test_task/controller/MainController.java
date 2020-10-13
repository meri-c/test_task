package com.meri.test_task.controller;

import com.meri.test_task.service.ApiSupplier;
import com.meri.test_task.service.MainService;
import com.meri.test_task.service.service_pojo.ApiResult;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URISyntaxException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Log
@RestController("/")
public class MainController {
    public static final String URI_FIRST = "http://localhost:8090/first";
    public static final String URI_SECOND = "http://localhost:8090/second";

    public final static String PRESET_SEARCH_FILTER = "[\n" +
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
    ApiSupplier firstApiSupplier;

    @Autowired
    ApiSupplier secondApiSupplier;


    public MainController(MainService mainService) {

        this.mainService = mainService;
    }


    @GetMapping("start")
    public ApiResult multiThreadFirstSecondApi() throws URISyntaxException {

        log.info("=================START===================");

        //call two threads for one filter request
        CompletableFuture<ApiResult> first = CompletableFuture.supplyAsync(() -> {
            return firstApiSupplier.get(mainService);
        });

        CompletableFuture<ApiResult> second = CompletableFuture.supplyAsync(() -> {
            return secondApiSupplier.get(mainService);
        });

        CompletableFuture<ApiResult> combinedResult = first.thenCombine(second, MainService::combineTwoResults);

        try {
            return combinedResult.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }

}

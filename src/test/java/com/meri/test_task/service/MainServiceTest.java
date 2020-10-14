package com.meri.test_task.service;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import com.meri.test_task.service.service_pojo.SearchResult;
import org.junit.Before;
import org.junit.Rule;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.net.URISyntaxException;
import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;

@RunWith(SpringRunner.class)
@SpringBootTest
class MainServiceTest {
    private String xml_result = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
            "<pricing>\n" +
            "    <quote term=\"12\">\n" +
            "        <location address=\"Pushkin st. 59\" city=\"Odessa\" country=\"Ukraine\"/>\n" +
            "        <price mrc=\"12.5\" nrc=\"150.0\"/>\n" +
            "    </quote>\n" +
            "    <quote term=\"24\">\n" +
            "        <location address=\"Pushkin st. 59\" city=\"Odessa\" country=\"Ukraine\"/>\n" +
            "        <price mrc=\"10.5\" nrc=\"130.0\"/>\n" +
            "    </quote>\n" +
            "</pricing>\n";

    private String json_result = "[\n" +
            "  {\n" +
            "    \"street\": \"Pushkin st. 59\",\n" +
            "    \"city\": \"Odessa\",\n" +
            "    \"country\": \"Ukraine\",\n" +
            "    \"term\": \"1yr\",\n" +
            "    \"mrc\": 300,\n" +
            "    \"nrc\": null\n" +
            "  }\n" +
            "]\n";

    private String preset_search_filter = "[\n" +
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


    @Rule
    public static WireMockRule wireMockRule = new WireMockRule(8090);

   @Autowired
   MainService mainService;

    @Before
    void init(){
        stubFor(post(urlEqualTo("/first"))
                .withRequestBody(containing("\"country\": \"Ukraine\""))
                .withRequestBody(containing("\"city\": \"Odessa\""))
                .withRequestBody(containing("\"street\": \"Pushkin st. 59\""))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/xml")
                        .withBody(xml_result)));

        stubFor(post(urlEqualTo("/second"))
                .withRequestBody(containing("\"country\": \"Ukraine\""))
                .withRequestBody(containing("\"city\": \"Odessa\""))
                .withRequestBody(containing("\"street\": \"Pushkin st. 59\""))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody(json_result)));
    }

    @Test
    void callFirstApi() throws URISyntaxException {
        String uri_first = "http://localhost:8090/first";


        List<SearchResult> searchResult = mainService.callFirstApi(uri_first, preset_search_filter);


        assertFalse(searchResult.isEmpty());
        assertThat(2).isEqualTo(searchResult.size());
        assertThat("Ukraine").isEqualTo(searchResult.get(0).getCountry());

    }

    @Test
    void callSecondApi() {
        String uri_second = "http://localhost:8090/second";


        List<SearchResult> searchResult = mainService.callSecondApi(uri_second, preset_search_filter);


        assertFalse(searchResult.isEmpty());
        assertThat(1).isEqualTo(searchResult.size());
        assertThat("Odessa").isEqualTo(searchResult.get(0).getCity());
    }
}



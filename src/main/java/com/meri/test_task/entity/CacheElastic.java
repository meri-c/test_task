package com.meri.test_task.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

//for elasticsearch
@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(indexName = "cache")
public class CacheElastic {

    @Id
    String id;
    String apiName;
    String  timestamp;
    String street;
    String city;
    String country;
    String term;
    Double mrc;
    Double nrc;

    public CacheElastic(String apiName, String timestamp, String street, String city, String country, String term, Double mrc, Double nrc) {
        this.apiName = apiName;
        this.timestamp = timestamp;
        this.street = street;
        this.city = city;
        this.country = country;
        this.term = term;
        this.mrc = mrc;
        this.nrc = nrc;
    }
}

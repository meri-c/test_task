package com.meri.test_task.service.service_pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class SearchResult {
    String street;
    String city;
    String country;
    String term;
    double mrc;
    double nrc;

}

package com.meri.test_task.service.service_pojo;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PriceResult {
    String api;
    String term;
    Double mrc;
    Double nrc;
}

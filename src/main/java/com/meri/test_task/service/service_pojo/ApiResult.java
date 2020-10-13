package com.meri.test_task.service.service_pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApiResult {
    String street;
    String city;
    String country;
    List<PriceResult> prices;
}

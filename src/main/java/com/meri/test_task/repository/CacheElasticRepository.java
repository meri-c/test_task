package com.meri.test_task.repository;

import com.meri.test_task.entity.CacheElastic;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface CacheElasticRepository extends ElasticsearchRepository<CacheElastic, String> {
}

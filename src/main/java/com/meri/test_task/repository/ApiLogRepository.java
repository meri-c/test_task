package com.meri.test_task.repository;

import com.meri.test_task.entity.ApiLog;
import org.springframework.data.repository.CrudRepository;

public interface ApiLogRepository extends CrudRepository<ApiLog, Long> {
}

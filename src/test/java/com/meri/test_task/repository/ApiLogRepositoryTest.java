package com.meri.test_task.repository;

import com.meri.test_task.entity.ApiLog;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;


@RunWith(SpringRunner.class)
@DataJpaTest
class ApiLogRepositoryTest {

    @Autowired
    TestEntityManager entityManager;

    @Autowired
    ApiLogRepository apiLogRepository;

    @Test
    public void presaveLog_save_thanReturn(){
        //given
        ApiLog persetLog =
                new ApiLog(new Timestamp((new Date().getTime())), "preset_api", "some-request");
        ApiLog realLog =
                new ApiLog(new Timestamp((new Date().getTime())), "real api", "real request");

        entityManager.persist(persetLog);
        entityManager.flush();

        //when
        apiLogRepository.save(realLog);


        Iterable<ApiLog> saved_logs_iterable = apiLogRepository.findAll();
        List<ApiLog> saved_logs = new ArrayList<>();

        saved_logs_iterable.forEach(saved_logs::add);


        //then
        assertEquals(2, saved_logs.size());
        assertThat(realLog.getApi_name()).isEqualTo(saved_logs.get(1).getApi_name());

    }

}

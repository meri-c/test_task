package com.meri.test_task.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.sql.Timestamp;

@Data
@NoArgsConstructor
@Entity
public class ApiLog {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;

    //to standart mysql func
    Timestamp created_at;

    String api_name;

    String request_body;

    public ApiLog(Timestamp created_at, String api_name, String request_body) {
        //this.created_at = String.valueOf(created_at);
        this.created_at = created_at;
        this.api_name = api_name;
        this.request_body = request_body;
    }
}

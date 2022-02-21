package com.jiang.football;

import com.jiang.football.dao.daoImpl.GetFootballDataImpl;
import com.jiang.football.models.mapperModels.Football;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import java.util.List;


@SpringBootApplication
public class DemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

}

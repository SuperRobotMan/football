package com.jiang.football.job;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class reportJobTest {

    @Autowired
    private ReportJob reportJob;


    @Test
    public void startGetFootballDataJob() {
        reportJob.startGetFootballDataJob("2022-02-25", "2022-02-26");
    }


    @Test
    public void startAnalyseFootballDataJob() {
        reportJob.startAnalyseFootballDataJob("2022-02-19", "2022-02-19");
    }

}

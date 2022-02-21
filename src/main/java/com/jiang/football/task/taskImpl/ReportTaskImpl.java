package com.jiang.football.task.taskImpl;

import com.jiang.football.dao.AnalyseData;
import com.jiang.football.dao.GetFootballData;
import com.jiang.football.models.mapperModels.Football;
import com.jiang.football.service.GetService;
import com.jiang.football.service.WriteService;
import com.jiang.football.service.serviceImpl.IM02GetServiceImpl;
import com.jiang.football.task.ReportTask;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;


@Slf4j
@Service
public class ReportTaskImpl implements ReportTask {

    @Autowired
    private WriteService writeService;

    @Resource(name = "IM02GetReport")
    public IM02GetServiceImpl IM02GetService;

    @Autowired
    private GetFootballData getFootballData;

    @Autowired
    private AnalyseData analyseData;

    @Autowired
    private GetService GetService;

    @Override
    public void startGetFootballDataTask(String dateStr){
        int j = 0;
        //调用方法，获取每天的足球数据
        List<Football> footballData = getFootballData.getFootballData(dateStr);
        if (footballData.size() == 0){
            log.info("{}-当天没有比赛, 任务结束", dateStr);
            return;
        }else {
            //查看数据正确性
//        System.out.println(footballData);
            //这个方法就是将数据写入到数据库中的方法
            for (Football footballDatum : footballData) {
                int i = writeService.writeFootballDataModel(footballDatum);
                log.info("{}-批量插入足球数据到数据库, 状态:{}", dateStr, i);
                j ++;
            }
        }

        log.info("{}-插入足球数据到数据库完成--应存{}, 实存{}.", dateStr, footballData.size(), j);

    }

    @Override
    public void startAnalyseFootballDataTask(String dateStr) {

        //获取到足球的历史所有数据(后期数据多了可以分析联赛数据)
        List<Football> historyFootballDataFromDB = IM02GetService.getHistoryFootballDataFromDB(dateStr);
        List<Football> footballData = getFootballData.getFootballData(dateStr);
        //返回最后的每场比赛和概率
        analyseData.analyseFootballData(footballData, historyFootballDataFromDB);




    }


}

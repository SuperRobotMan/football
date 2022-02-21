package com.jiang.football.job.jobImpl;

import com.jiang.football.Utils.TimeUtils;
import com.jiang.football.task.IMTaskInterface;
import com.jiang.football.job.ReportJob;
import com.jiang.football.task.ReportTask;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.joda.time.Seconds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class ReportJobImpl implements ReportJob {

    @Autowired
    private ReportTask taskService;


    @Override
    public void startGetFootballDataJob(String startDateStr, String endDateStr) {
        invokeTask(startDateStr, endDateStr, dateStr ->  taskService.startGetFootballDataTask(dateStr));
    }


    @Override
    public void startAnalyseFootballDataJob(String startDateStr, String endDateStr) {
        invokeTask(startDateStr, endDateStr, dateStr ->  taskService.startAnalyseFootballDataTask(dateStr));
    }



    //invokeTask方法， 这个是统一的调用方法，通过传入接口，根据不同的实现从而出发不同的job
    private void invokeTask(String startDateStr, String endDateStr, IMTaskInterface taskInterface){

        //获取给定起止时间的时间列表
        List<String> dateStringList = TimeUtils.getDateStringList(startDateStr, endDateStr);
        if (dateStringList==null) {
            log.info("日期格式错误");
            return;
        }

        for (String dateStr : dateStringList) {
            log.info("{}-{}: task开启", dateStr, Thread.currentThread().getStackTrace()[1].getMethodName());
            //这个任务大概线下29s，线上未测试
            DateTime time01 = new DateTime();
            //这里必须要捕获异常，要不然可能会影响到后续任务执行
            try {
                //这里根据具体的内容执行task
                taskInterface.runTask(dateStr);
            } catch (Exception e) {
                log.info("{}-{}: task任务执行异常", dateStr, Thread.currentThread().getStackTrace()[1].getMethodName(), e);
            }
            DateTime time02 = new DateTime();
            Seconds seconds = Seconds.secondsBetween(time01, time02);
            log.info("{}-{}: task结束，耗时:{}", dateStr, Thread.currentThread().getStackTrace()[1].getMethodName(), seconds.getSeconds());
        }
    }
}

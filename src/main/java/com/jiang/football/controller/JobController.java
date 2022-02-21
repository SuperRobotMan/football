package com.jiang.football.controller;

import com.jiang.football.job.IMJobInterface;
import com.jiang.football.job.ReportJob;
import com.jiang.football.models.common.ResultModel;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.joda.time.Seconds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * #Author : 山河
 * #Date   : 06/02/2022-5:53 PM
 * #Desc   : 这里手动跑job的一个控制器
 **/


@Slf4j
@RestController
@RequestMapping("/job")
public class JobController {

    @Autowired
    private ReportJob reportJob;


    @RequestMapping("/test")
    public ResultModel controllerTest(){
        ResultModel resultModel = new ResultModel();
        resultModel.setOKResult("连接成功-----");
        return resultModel;
    }


    @RequestMapping("/startGetFootballDataJob")
    public ResultModel startGetFootballDataJob(String dateStr) {
        //1, 初始化返回模型，并计算时间列表
        ResultModel resultModel = new ResultModel();
        invokeJobInNewThread(dateStr, theDateStr -> reportJob.startGetFootballDataJob(theDateStr, theDateStr));
        resultModel.setOKResult("任务已提交");
        return resultModel;
    }


    @RequestMapping("/startAnalyseFootballDataJob")
    public ResultModel startAnalyseFootballDataJob(String dateStr) {
        //1, 初始化返回模型，并计算时间列表
        ResultModel resultModel = new ResultModel();
        invokeJobInNewThread(dateStr, theDateStr -> reportJob.startAnalyseFootballDataJob(theDateStr, theDateStr));
        resultModel.setOKResult("任务已提交");
        return resultModel;
    }


        //invokeJob方法， 这个是统一的调用方法，通过传入接口，根据不同的实现从而出发不同的job
        private void invokeJobInNewThread(String dateStr, IMJobInterface jobInterface){
            new Thread(() -> {
                log.info("{}: 定时任务开启", Thread.currentThread().getStackTrace()[1].getMethodName());
                DateTime time01 = new DateTime();
                //这里根据具体的内容执行job
                jobInterface.runJob(dateStr);
                DateTime time02 = new DateTime();
                Seconds seconds = Seconds.secondsBetween(time01, time02);
                log.info("{}: 定时任务结束，耗时:{}", Thread.currentThread().getStackTrace()[1].getMethodName(), seconds.getSeconds());
            }).start();
        }
    }


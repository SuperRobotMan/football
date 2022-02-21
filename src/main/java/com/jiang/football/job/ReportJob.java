package com.jiang.football.job;


public interface ReportJob {

    void startGetFootballDataJob(String startDateStr, String endDateStr);

    void startAnalyseFootballDataJob(String startDateStr, String endDateStr);


}

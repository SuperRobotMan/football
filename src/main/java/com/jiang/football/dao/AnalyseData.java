package com.jiang.football.dao;

import com.jiang.football.models.mapperModels.Football;

import java.util.List;
import java.util.Map;

public interface AnalyseData {
    //分析当天每场比赛的胜率
    void analyseFootballData(List<Football> football, List<Football> hisFootball);
}

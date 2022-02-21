package com.jiang.football.service;

import com.jiang.football.models.mapperModels.Football;

import java.util.List;

public interface GetService {

    List<Football> getHistoryFootballDataFromDB(String dateStr);

}

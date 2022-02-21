package com.jiang.football.service;

import com.jiang.football.models.mapperModels.Football;

import java.util.List;

public interface HandlerService {
    List<Football> getHistoryFootballDataFromDB(String dateStr);
}

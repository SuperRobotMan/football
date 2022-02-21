package com.jiang.football.service.serviceImpl;

import com.jiang.football.models.mapperModels.Football;
import com.jiang.football.service.HandlerService;

import java.util.List;

public abstract class IM01HandlerServiceImpl implements HandlerService {


    @Override
    public List<Football> getHistoryFootballDataFromDB(String dateStr) {
        return null;
    }
}

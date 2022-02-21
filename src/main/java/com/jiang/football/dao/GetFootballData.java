package com.jiang.football.dao;

import com.jiang.football.models.mapperModels.Football;

import java.util.List;

public interface GetFootballData {
    public List<Football> getFootballData (String date);

}

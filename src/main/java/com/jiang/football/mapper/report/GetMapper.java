package com.jiang.football.mapper.report;


import com.jiang.football.models.mapperModels.Football;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface GetMapper {

    //从数据库中将历史的数据全部取出来
    List<Football> getHistoryFootballDataFromDB(String dateStr);

}

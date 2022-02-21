package com.jiang.football.mapper.report;

import com.jiang.football.models.mapperModels.Football;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * #Author : 山河
 * #Date   : 02/06/2022-22:52:21
 * #Desc   : 保存报表的一个mapper
 */
@Mapper
public interface WriteMapper {

    //将数据用mybatis写入到数据库中的方法
    int writeFootballDataModel(Football models);
}

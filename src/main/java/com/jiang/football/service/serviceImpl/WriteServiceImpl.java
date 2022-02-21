package com.jiang.football.service.serviceImpl;

import com.jiang.football.mapper.report.WriteMapper;
import com.jiang.football.models.mapperModels.Football;
import com.jiang.football.service.WriteService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Slf4j
@Service
@Transactional
public class WriteServiceImpl implements WriteService {

    @Autowired
    private WriteMapper reportMapper;



    @Override
    public int writeFootballDataModel(Football models) {
        return reportMapper.writeFootballDataModel(models);
    }
}

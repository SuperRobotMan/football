package com.jiang.football.service.serviceImpl;

import com.jiang.football.mapper.report.GetMapper;
import com.jiang.football.models.mapperModels.Football;
import com.jiang.football.service.GetService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@Transactional
public class GetServiceImpl implements GetService {

    @Autowired
    private GetMapper GetMapper;



    @Override
    public List<Football> getHistoryFootballDataFromDB(String dateStr) {

        return GetMapper.getHistoryFootballDataFromDB(dateStr);
    }
}

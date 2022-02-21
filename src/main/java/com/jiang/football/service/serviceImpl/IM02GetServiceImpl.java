package com.jiang.football.service.serviceImpl;

import com.jiang.football.models.mapperModels.Football;
import com.jiang.football.service.GetService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Transactional
@Service("IM02GetReport")
public class IM02GetServiceImpl extends IM01HandlerServiceImpl{

    @Autowired
    private GetService reportGetService;

    @Override
    public List<Football> getHistoryFootballDataFromDB(String dateStr) {
        return reportGetService.getHistoryFootballDataFromDB(dateStr);
    }

}

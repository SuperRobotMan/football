package com.jiang.football.Utils;

import org.joda.time.*;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class TimeUtils {

    public static List<String> getDateStringList(String startDateStr, String endDateStr) {

        DateTime startDateTime = null;
        DateTime endDateTime = null;
        try {
            startDateTime = DateTime.parse(startDateStr);
            endDateTime = DateTime.parse(endDateStr);

            Interval interval = new Interval(startDateTime, endDateTime);
            Period period = interval.toPeriod(PeriodType.days());

            //System.out.println(period.getDays());

            List<String> dateStrList = new ArrayList<>();
            for (int i = 0; i < period.getDays()+1; i++) {
                String dateStr = startDateTime.minusDays(-i).toString("yyyy-MM-dd");
                dateStrList.add(dateStr);
            }
            return dateStrList;
        } catch (Exception e) {
            log.info("日期填写有误，请检查日期格式或者时间是否满足需求，格式：2019-10-01");
        }

        return null;
    }

}

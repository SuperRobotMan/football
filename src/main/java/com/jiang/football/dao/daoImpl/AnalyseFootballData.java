package com.jiang.football.dao.daoImpl;

import com.jiang.football.dao.AnalyseData;
import com.jiang.football.models.mapperModels.Football;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.*;

@Slf4j
@Service
@Repository
public class AnalyseFootballData implements AnalyseData {

    @Override
    public void analyseFootballData(List<Football> football, List<Football> hisFootball) {
        LinkedHashMap<String, ArrayList<Football>> res = new LinkedHashMap<>();

        //将两个比赛数据进行双重for循环，拿每一场比赛的数据进行比较
        for (Football todayData : football) {
            ArrayList<Football> tmp = new ArrayList<>();
            for (Football hisData : hisFootball) {
                //初始模型，联赛相同，澳门初盘和终盘相同，把比赛结果取出来算概率
                if
                 //赛事相同
                (/*todayData.getCompetition().equals(hisData.getCompetition())
                        //澳门初始盘口相同
                    &&*/ todayData.getYaInitAoMenTape().equals(hisData.getYaInitAoMenTape())
                        //澳门终盘盘口相同
                    && todayData.getYaJSAoMenTape().equals(hisData.getYaJSAoMenTape())
                        //澳门的初始胜赔率和初始负赔率都分别大于即时胜赔率和及时负赔率
                    && ((todayData.getYaInitSAoMenOdds() >= hisData.getYaInitSAoMenOdds()
                        && todayData.getYaInitFAoMenOdds() >= hisData.getYaInitFAoMenOdds())
                        //澳门的初始胜赔率和初始负赔率都分别小于即时胜赔率和及时负赔率
                        || (todayData.getYaInitSAoMenOdds() <= hisData.getYaInitSAoMenOdds()
                        && todayData.getYaInitFAoMenOdds() <= hisData.getYaInitFAoMenOdds()))
                ){
                    //再这样的条件下，将历史数据中的比赛取出来
                    tmp.add(hisData);
                }
                res.put(todayData.getHomeTeam(),tmp);
            }
        }


        //这样比赛的结果就出来了，map集合中的长度就是当天比赛的场数
        //key存的是每场比赛的主队，value存的是相同的比赛场数
        Set<String> homeTeams = res.keySet();
        for (String homeTeam : homeTeams) {
            //获取到当天每场比赛匹配模型后的比赛
            ArrayList<Football> footballs = res.get(homeTeam);

            //初始化胜平负的场数
            int s = 0;
            int p = 0;
            int f = 0;
            int rs = 0;
            int rp = 0;
            int rf = 0;

            //初始化每场比赛的概率
            String sr =   "";
            String pr = "";
            String fr = "";
            String rsr = "";
            String rpr = "";
            String rfr = "";


            //遍历历史数据，查看胜平负的个数
            for (Football football1 : footballs) {
                if (football1.getHomeScore() > football1.getGuestScore()){
                    s++;
                }else if (football1.getHomeScore() == football1.getGuestScore()){
                    p++;
                }else if (football1.getHomeScore() < football1.getGuestScore()){
                    f++;
                }
                String concedePoints = football1.getConcedePoints();
                String zf = concedePoints.substring(0, 1);
                int score = Integer.parseInt(concedePoints.substring(1, 2));
                //- 代表的是主队让球
                if ("-".equals(zf)){
                    if (football1.getHomeScore()-score > football1.getGuestScore()){
                        rs++;
                    }else if (football1.getHomeScore()-score == football1.getGuestScore()){
                        rp++;
                    }else if (football1.getHomeScore()-score < football1.getGuestScore()){
                        rf++;
                    }
                }else if ("+".equals(zf)){
                    if (football1.getHomeScore()+score > football1.getGuestScore()){
                        rs++;
                    }else if (football1.getHomeScore()+score == football1.getGuestScore()){
                        rp++;
                    }else if (football1.getHomeScore()+score < football1.getGuestScore()){
                        rf++;
                    }
                }
                //给赔率赋值
                sr = doubleToRate(s, footballs.size());
                pr = doubleToRate(p , footballs.size());
                fr = doubleToRate(f , footballs.size());
                rsr = doubleToRate(rs , footballs.size());
                rpr = doubleToRate(rp , footballs.size());
                rfr = doubleToRate(rf , footballs.size());

            }
            System.out.println("主队：\t"+ homeTeam + "\t胜概率：" +sr + "\t平概率："+ pr + "\t负概率："+ fr + "\t让胜概率："+ rsr + "\t让平概率："+ rpr + "\t让负概率："+ rfr );

        }


    }

    private String doubleToString(double d){
        DecimalFormat decimalFormat = new DecimalFormat(d + "");
        String res = decimalFormat.toString() + "%";
        return res;
    }


    private String doubleToRate(double a,double b){
        //将两数相除后保留两位小数
        BigDecimal res = new BigDecimal((float)a/b);
        Double result = res.setScale(4,BigDecimal.ROUND_HALF_UP).doubleValue();

        //将小数转换成保留两位小数的百分数
        NumberFormat num = NumberFormat.getPercentInstance();
        num.setMaximumFractionDigits(2);
        String rates = num.format(result);
        return rates;
    }


}

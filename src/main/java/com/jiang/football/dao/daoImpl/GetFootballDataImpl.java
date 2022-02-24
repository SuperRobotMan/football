package com.jiang.football.dao.daoImpl;

import com.jiang.football.dao.GetFootballData;
import com.jiang.football.models.mapperModels.Football;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Slf4j
@Service
@Repository
public class GetFootballDataImpl implements GetFootballData {

    //value注解怎么我就玩不起来呢
//    @Value("${url}")
//    private String url;



    @Override
    public List<Football> getFootballData(String dateStr){

        //明天来研究这个@value注解，把下面这些都配置到properties里面去
        /**
         * 欧盘
         */
        //初盘胜的str
        String ouSStr = "#1 border-l	";
        String ouSBlackStr = "#1 border-l	bold-bla";
        String ouSGreenStr = "#1 border-l	bold-g";
        //初盘平的str
        String ouPStr = "#1			";
        String ouPBlackStr = "#1			bold-bla";
        String ouPGreenStr = "#1			bold-g";
        //初盘负的str
        String ouFStr = "#1 border-r	";
        String ouFBlackStr = "#1 border-r	bold-bla";
        String ouFGreenStr = "#1 border-r	bold-g";

        //即时盘
        String ouJsSStr = "#2 border-l tdbg";
        String ouJsPFStr = "#2 tdbg";

        /**
         * 亚盘
         */
        //初盘胜的str
        String yaSStr = "#1-yp border-l	";
        String yaSBlackStr = "#1-yp border-l	bold-bla";
        String yaSGreenStr = "#1-yp border-l	bold-g";
        //初盘盘口
        String yaPkStr = "#1-yp			";
        //初盘负的str
        String yaFStr = "#1-yp border-r	";
        String yaFBlackStr = "#1-yp border-r	bold-bla";
        String yaFGreenStr = "#1-yp border-r	bold-g";

        //即时盘
        String yaJsSStr = "#2-yp border-l tdbg";
        String yaJsFStr = "#2-yp tdbg";

        String url = "https://cp.zgzcw.com/lottery/jchtplayvsForJsp.action?lotteryId=47&type=jcmini&issue=" + dateStr;

        Document parse = null;
        try {
            parse = Jsoup.parse(new URL(url), 10000);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Element tw = parse.getElementById("tw");
        Element dcc = tw.getElementById("dcc");

        /**
         * 创建结果表，直接就可以将数据写进去
         */
        ArrayList<Football> res = new ArrayList<>();

        /**
         * 获取到头信息
         * 判断下，是否有数据，没有就直接返回了，代表当天没有比赛
         */
        Element hide_box_1 = dcc.getElementById("hide_box_1");
        if (hide_box_1 == null){
            return res;
        }


        /**
         * 获取赛事
         */
        Elements competition = hide_box_1.getElementsByClass("wh-2");
        for (Element byClass : competition) {
            Football football = new Football();
            String com = byClass.text();
            //将赛事添加到结果模型中
            football.setCompetition(com);
            res.add(football);
        }

        /**
         * 获取日期和星期
         */
        Elements dateAndWeek = dcc.getElementsByClass("fl ps");
        String strong = dateAndWeek.get(0).getElementsByTag("strong").text();
        for (Football re : res) {
            //将日期和星期添加到结果模型中
            re.setReportDate(strong.substring(0, 10));
            re.setWeekDate(strong.substring(10, 13));
        }

        /**
         * 提取主队名
         */
        Elements homeTeam = hide_box_1.getElementsByClass("wh-4 t-r");
        //共用一个i
        for (int i = 0; i < res.size(); i++) {
            String homeTeamName = homeTeam.get(i).text().split(" ")[0];
            //将主队名添加到结果模型中
            res.get(i).setHomeTeam(homeTeamName);

        }

        /**
         * 提取比分
         */
        Elements score = hide_box_1.getElementsByClass("wh-5 bf");
        for (int i = 0; i < res.size(); i++) {
            String sc = score.get(i).text();
            if ("VS".equals(sc)){
                //如果是当天的比赛，没有结果的情况下，就直接设置比分为-1
                res.get(i).setHomeScore(-1);
                res.get(i).setGuestScore(-1);
            }else {
                String[] split = sc.split(":");
                //历史记录有比分的情况下,将比分的结果添加到结果模型中去
                res.get(i).setHomeScore(Integer.parseInt(split[0]));
                res.get(i).setGuestScore(Integer.parseInt(split[1]));
            }
        }

        /**
         * 提取客队名
         */
        Elements guestTeam = hide_box_1.getElementsByClass("wh-6 t-l");
        //共用一个i
        for (int i = 0; i < res.size(); i++) {
            String guestTeamName = guestTeam.get(i).text().split(" ")[guestTeam.get(i).text().split(" ").length - 1];
            //将主队名添加到结果模型中
            res.get(i).setGuestTeam(guestTeamName);
        }

        /**
         * 提取让球和赔率
         */
        Elements odds = hide_box_1.getElementsByClass("wh-8 b-l");

        for (int i = 0; i < res.size(); i++) {
            Football football = res.get(i);
            //取出非让球胜平负
            String fConcedePoints = odds.get(i).getElementsByClass("tz-area frq").eachText().get(0);
            //可能不会有胜平负的赔率，分类判断下分别设置胜平负的值
            if (fConcedePoints.length() >10){
                String[] spfOdds = fConcedePoints.split(" ");
                String[] split = spfOdds[1].split("\\.");
                football.setSOdds(Double.parseDouble(split[0] + "." + split[1].substring(0, 2)));
                football.setPOdds(Double.parseDouble(split[1].substring(2, split[1].length()) + "." + split[2].substring(0, 2)));
                football.setFOdds(Double.parseDouble(split[2].substring(2, split[2].length()) + "." + split[3]));
            }

            //取出让球个数和让球胜平负
            String concedePoints = odds.get(i).getElementsByClass("tz-area tz-area-2 rqq").eachText().get(0);
            String[] rqSpfOdds = concedePoints.split(" ");
            //设置让球
            football.setConcedePoints(rqSpfOdds[0]);
            String[] rSpfOdds = rqSpfOdds[1].split("\\.");
            //设置让球胜平负，将赔率分别切开后设置进去
            football.setRsOdds(Double.parseDouble(rSpfOdds[0]+"."+rSpfOdds[1].substring(0,2)));
            football.setRpOdds(Double.parseDouble(rSpfOdds[1].substring(2,rSpfOdds[1].length()) + "." + rSpfOdds[2].substring(0,2)));
            football.setRfOdds(Double.parseDouble(rSpfOdds[2].substring(2,rSpfOdds[2].length()) + "." + rSpfOdds[3]));
        }

        /**
         * 下面就要开始跳转页面，去查询欧赔和亚赔的数据
         *
         * https://fenxi.zgzcw.com/2811312/bjop
         * https://fenxi.zgzcw.com/2811312/ypdb
         *
         * 观察上述的两个url发现，其实就是用后面的bjop和ypdb来区分的（只要获取到前面的数据后拼接即可）
         */
        //创建集合来存放每场比赛的新id
        ArrayList<String> newIURLs = new ArrayList<>();



        Elements newIDs = hide_box_1.getElementsByClass("wh-10 b-l");
        for (Element element : newIDs) {
            String newID = element.attr("newplayid");
            newIURLs.add("https://fenxi.zgzcw.com/" + newID);
        }


        log.info("{}-设置每场比赛查询睡眠3s后继续", dateStr);

        for (int i = 0; i < res.size(); i++) {
            log.info("00{}-开始睡眠3s后查询",i+1);
            Football football = res.get(i);
            /**
             * 开始获取欧盘的数据
             */
            String ouUrl = newIURLs.get(i);
            //提升作用域范围
            Document parse1 = null;
            try {
                Thread.currentThread().sleep(10000);
                parse1 = Jsoup.parse(new URL(ouUrl + "/bjop"), 10000);
            } catch (Exception e) {
                e.printStackTrace();
            }
            //获取到数据的整体部分
            Element ouDataBody = parse1.getElementById("data-body");
            ////不是空就代表能进去，就是有数据的
            if (ouDataBody != null){
                Elements tbody = ouDataBody.getElementsByTag("tbody");
                //tbody里面只有一个元素，就把所有的数据全部取出来了，再不断向下挖
                Elements tr = tbody.get(0).getElementsByTag("tr");
                for (Element element1 : tr) {
                    //获取到所有的公司
                    String company = element1.getElementsByClass("border-r border-l").text();
                    //我们只需要三家公司的数据，判断公司，调用方法即可
                    if ("威廉希尔".equals(company)){
                        //初盘
                        football.setOuInitSWilliamOdds(getInitOddS(element1, ouSStr,ouSBlackStr,ouSGreenStr));
                        football.setOuInitPWilliamOdds(getInitOddS(element1, ouPStr,ouPBlackStr,ouPGreenStr));
                        football.setOuInitFWilliamOdds(getInitOddS(element1, ouFStr,ouFBlackStr,ouFGreenStr));
                        //及时盘
                        ArrayList<Double> ouJSOddS = getOuJSOddS(element1, ouJsSStr, ouJsPFStr);
                        football.setOuJSSWilliamOdds(ouJSOddS.get(0));
                        football.setOuJSPWilliamOdds(ouJSOddS.get(1));
                        football.setOuJSFWilliamOdds(ouJSOddS.get(2));
                    }else if ("立博".equals(company)){
                        //初盘
                        football.setOuInitSLiBoOdds(getInitOddS(element1, ouSStr,ouSBlackStr,ouSGreenStr));
                        football.setOuInitPLiBoOdds(getInitOddS(element1, ouPStr,ouPBlackStr,ouPGreenStr));
                        football.setOuInitFLiBoOdds(getInitOddS(element1, ouFStr,ouFBlackStr,ouFGreenStr));
                        //及时盘
                        ArrayList<Double> ouJSOddS = getOuJSOddS(element1, ouJsSStr, ouJsPFStr);
                        football.setOuJSSLiBoOdds(ouJSOddS.get(0));
                        football.setOuJSPLiBoOdds(ouJSOddS.get(1));
                        football.setOuJSFLiBoOdds(ouJSOddS.get(2));
                    }else if ("Bet365".equals(company)){
                        //初盘
                        football.setOuInitSBet365Odds(getInitOddS(element1, ouSStr,ouSBlackStr,ouSGreenStr));
                        football.setOuInitPBet365Odds(getInitOddS(element1, ouPStr,ouPBlackStr,ouPGreenStr));
                        football.setOuInitFBet365Odds(getInitOddS(element1, ouFStr,ouFBlackStr,ouFGreenStr));
                        //及时盘
                        ArrayList<Double> ouJSOddS = getOuJSOddS(element1, ouJsSStr, ouJsPFStr);
                        football.setOuJSSBet365Odds(ouJSOddS.get(0));
                        football.setOuJSPBet365Odds(ouJSOddS.get(1));
                        football.setOuJSFBet365Odds(ouJSOddS.get(2));
                    }

                }
            }


            /**
             * 下面获取亚盘的数据
             */
            String yaUrl = newIURLs.get(i);
            //提升作用域范围
            Document parse2 = null;
            try {
                Thread.currentThread().sleep(10000);
                parse2 = Jsoup.parse(new URL(yaUrl + "/ypdb"), 10000);
            } catch (Exception e) {
                e.printStackTrace();
            }

            Element yaDataBody = parse2.getElementById("data-body");
            //不空就代表有数据，直接继续向下挖
            if (yaDataBody != null){
                Elements tbody1 = yaDataBody.getElementsByTag("tbody");
                Elements tr = tbody1.get(0).getElementsByTag("tr");
                for (Element element : tr) {
                    String company = element.getElementsByClass("border-r border-l").text();
                    if ("澳门".equals(company)){
                        //设置初盘
                        football.setYaInitSAoMenOdds(getInitOddS(element, yaSStr, yaSBlackStr, yaSGreenStr));
                        football.setYaInitAoMenTape(element.getElementsByClass(yaPkStr).text());
                        football.setYaInitFAoMenOdds(getInitOddS(element, yaFStr, yaFBlackStr, yaFGreenStr));
                        //设置即时盘
                        ArrayList<String> yaJSOddS = getYaJSOddS(element, yaJsSStr, yaJsFStr);
                        football.setYaJSSAoMenOdds(Double.parseDouble(yaJSOddS.get(0)));
                        football.setYaJSAoMenTape(yaJSOddS.get(1));
                        football.setYaJSFAoMenOdds(Double.parseDouble(yaJSOddS.get(2)));

                    }else if("ＳＢ/皇冠".equals(company)){
                        //设置初盘
                        football.setYaInitSCrownOdds(getInitOddS(element, yaSStr, yaSBlackStr, yaSGreenStr));
                        football.setYaInitCrownTape(element.getElementsByClass(yaPkStr).text());
                        football.setYaInitFCrownOdds(getInitOddS(element, yaFStr, yaFBlackStr, yaFGreenStr));
                        //设置即时盘
                        ArrayList<String> yaJSOddS = getYaJSOddS(element, yaJsSStr, yaJsFStr);
                        football.setYaJSSCrownOdds(Double.parseDouble(yaJSOddS.get(0)));
                        football.setYaJSCrownTape(yaJSOddS.get(1));
                        football.setYaJSFCrownOdds(Double.parseDouble(yaJSOddS.get(2)));

                    }else if("Bet365".equals(company)){
                        //设置初盘
                        football.setYaInitSBet365Odds(getInitOddS(element, yaSStr, yaSBlackStr, yaSGreenStr));
                        football.setYaInitBet365Tape(element.getElementsByClass(yaPkStr).text());
                        football.setYaInitFBet365Odds(getInitOddS(element, yaFStr, yaFBlackStr, yaFGreenStr));
                        //设置即时盘
                        ArrayList<String> yaJSOddS = getYaJSOddS(element, yaJsSStr, yaJsFStr);
                        football.setYaJSSBet365Odds(Double.parseDouble(yaJSOddS.get(0)));
                        football.setYaJSBet365Tape(yaJSOddS.get(1));
                        football.setYaJSFBet365Odds(Double.parseDouble(yaJSOddS.get(2)));

                    }else if("Interwetten".equals(company)){
                        //设置初盘
                        football.setYaInitSInterOdds(getInitOddS(element, yaSStr, yaSBlackStr, yaSGreenStr));
                        football.setYaInitInterTape(element.getElementsByClass(yaPkStr).text());
                        football.setYaInitFInterOdds(getInitOddS(element, yaFStr, yaFBlackStr, yaFGreenStr));
                        //设置即时盘
                        ArrayList<String> yaJSOddS = getYaJSOddS(element, yaJsSStr, yaJsFStr);
                        football.setYaJSSInterOdds(Double.parseDouble(yaJSOddS.get(0)));
                        football.setYaJSInterTape(yaJSOddS.get(1));
                        football.setYaJSFInterOdds(Double.parseDouble(yaJSOddS.get(2)));

                    }else if("易胜博".equals(company)){
                        //设置初盘
                        football.setYaInitSYSBOdds(getInitOddS(element, yaSStr, yaSBlackStr, yaSGreenStr));
                        football.setYaInitYSBTape(element.getElementsByClass(yaPkStr).text());
                        football.setYaInitFYSBOdds(getInitOddS(element, yaFStr, yaFBlackStr, yaFGreenStr));
                        //设置即时盘
                        ArrayList<String> yaJSOddS = getYaJSOddS(element, yaJsSStr, yaJsFStr);
                        football.setYaJSSYSBOdds(Double.parseDouble(yaJSOddS.get(0)));
                        football.setYaJSYSBTape(yaJSOddS.get(1));
                        football.setYaJSFYSBOdds(Double.parseDouble(yaJSOddS.get(2)));
                    }
                }
            }
        }
        log.info("{}-的比赛场数为:{}", dateStr, res.size());
        return res;
    }

    private static ArrayList<String> getYaJSOddS(Element element1,String sStr,String pfStr) {
//       "#2-yp border-l tdbg"
//       "#2-yp tdbg"
        ArrayList<String> jsodds = new ArrayList<>();
        jsodds.add("0.0");
        jsodds.add("null");
        jsodds.add("0.0");

        String jsSOdd = element1.getElementsByClass(sStr).text();
        //设置即时盘胜赔率
        if (jsSOdd.length() >= 4) {
            String[] split = jsSOdd.split("\\.");
            //取小数点前面的所有数加上小数点后面的两位，刚刚好
            jsodds.set(0,split[0] + "." + split[1].substring(0, 2));
        }

        List<String> jsPFOdds = element1.getElementsByClass(pfStr).eachText();
        //因为这边是这届获取的两个数值，一个是平，一个是负
        //[4.20↑, 6.00]
        if (jsPFOdds.size() == 2) {
            //设置即时盘盘口
            if (jsPFOdds.get(0).contains("↑") || jsPFOdds.get(0).contains("↓") ) {
                //去掉最后的上下键
                jsodds.set(1,jsPFOdds.get(0).substring(0, jsPFOdds.get(0).length() - 1));
            }else {
                jsodds.set(1,jsPFOdds.get(0));
            }
            //设置即时盘负赔率
            if (jsPFOdds.get(1).length() >= 4) {
                String[] split = jsPFOdds.get(1).split("\\.");
                //取小数点前面的所有数加上小数点后面的两位，刚刚好
                jsodds.set(2,split[0] + "." + split[1].substring(0, 2));
            }

        }

        return jsodds;
    }




    private static ArrayList<Double> getOuJSOddS(Element element1,String sStr,String pfStr) {
//        "#2 border-l tdbg"
//        "#2 tdbg"
        ArrayList<Double> jsodds = new ArrayList<>();
        jsodds.add(0.0);
        jsodds.add(0.0);
        jsodds.add(0.0);

        String jsSOdd = element1.getElementsByClass(sStr).text();
        //暂时就这么判断吧，空的话直接就设置为0，有其他逻辑的话，在修改
        if (jsSOdd.length() >= 4) {
            String[] split = jsSOdd.split("\\.");
            //取小数点前面的所有数加上小数点后面的两位，刚刚好
            jsodds.set(0,Double.parseDouble(split[0] + "." + split[1].substring(0, 2)));
        }

        List<String> jsPFOdds = element1.getElementsByClass(pfStr).eachText();
        //因为这边是这届获取的两个数值，一个是平，一个是负
        //[4.20↑, 6.00]
        if (jsPFOdds.size() == 2) {
            for (int i = 0; i < jsPFOdds.size(); i++) {
                if (jsPFOdds.get(i).length() >= 4) {
                    String[] split = jsPFOdds.get(i).split("\\.");
                    //取小数点前面的所有数加上小数点后面的两位，刚刚好
                    jsodds.set(1+i,Double.parseDouble(split[0] + "." + split[1].substring(0, 2)));
                }
            }
        }

        return jsodds;
    }

    /**
     * 写一个方法，来获取欧赔初始的胜平负赔率
     */
    public static double getInitOddS(Element element1, String str, String blackStr, String greenStr){
        double odd = 0;
        /**
         * 首先取的是初盘的胜
         * 当数据过大或者过小的时候，
         * 数据会标记有颜色，分情况讨论
         */
        //先按照初盘胜没有颜色的时候取
        String initOdd = element1.getElementsByClass(str).text();

        //如果有颜色就走这边
        if ("".equals(initOdd)){
            /**
             * 有颜色的时候还需要再次分类，是绿色还是黑色
             */
            //黑色
            String black = element1.getElementsByClass(blackStr).text();
            if ("".equals(black)){
                //这边就不是黑色，走绿色
                String green = element1.getElementsByClass(greenStr).text();
                //最后再防止他这边是没有数据的，所以再做一次最后的判断
                if (green.length()>=4){
                    //原本这边取的前4位，现在想想不行呢，如果是10.02这种的这怎么办，最后一位就被舍弃了
                    //所以无法判断他的长度具体是多少，用之前的老办法，截取后再拼接比较靠谱
                    String[] split = green.split("\\.");
                    //取小数点前面的所有数加上小数点后面的两位，刚刚好
                    odd = Double.parseDouble(split[0] + "." + split[1].substring(0, 2));
                }
            }
            //这边就肯定是黑色了
            else if (black.length() >= 4){
                String[] split = black.split("\\.");
                odd = Double.parseDouble(split[0] + "." + split[1].substring(0, 2));
            }
            //没颜色，直接就取到了数据,防止箭头，取前四位
        }else if (initOdd.length()>=4){
            String[] split = initOdd.split("\\.");
            odd = Double.parseDouble(split[0] + "." + split[1].substring(0, 2));
        }
        return odd;
    }
}

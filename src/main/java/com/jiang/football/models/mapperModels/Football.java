package com.jiang.football.models.mapperModels;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Football {
    //日期
    private String reportDate;
    //周
    private String weekDate;
    //赛事
    private String competition;
    //主队名
    private String homeTeam;
    //把这个改成主队进球数和客队进球数
    //比分结果
//    private String score;
    //主队进球数
    private int homeScore;
    //客队进球数
    private int guestScore;
    //客队名
    private String guestTeam;
    //非让球个数
    private int noConcedePoints = 0;
    //胜平负赔率
    private double sOdds = 0;
    private double pOdds = 0;
    private double fOdds = 0;

    //让球个数
    private String concedePoints;
    //让球胜平负赔率
    private double rsOdds = 0;
    private double rpOdds = 0;
    private double rfOdds = 0;

    /**
     * 欧赔的公司初盘和即时盘
     */

    //威廉希尔
    private double ouInitSWilliamOdds = 0;
    private double ouInitPWilliamOdds = 0;
    private double ouInitFWilliamOdds = 0;
    private double ouJSSWilliamOdds = 0;
    private double ouJSPWilliamOdds = 0;
    private double ouJSFWilliamOdds = 0;

    //立博
    private double ouInitSLiBoOdds = 0;
    private double ouInitPLiBoOdds = 0;
    private double ouInitFLiBoOdds = 0;
    private double ouJSSLiBoOdds = 0;
    private double ouJSPLiBoOdds = 0;
    private double ouJSFLiBoOdds = 0;

    //Bet365
    private double ouInitSBet365Odds = 0;
    private double ouInitPBet365Odds = 0;
    private double ouInitFBet365Odds = 0;
    private double ouJSSBet365Odds = 0;
    private double ouJSPBet365Odds = 0;
    private double ouJSFBet365Odds = 0;


    /**
     * 亚赔的公司初盘和即时盘
     */

    //澳门
    private double yaInitSAoMenOdds = 0;
    private String yaInitAoMenTape = "null";
    private double yaInitFAoMenOdds = 0;
    private double yaJSSAoMenOdds = 0;
    private String yaJSAoMenTape = "null";
    private double yaJSFAoMenOdds = 0;

    //SB/皇冠
    private double yaInitSCrownOdds = 0;
    private double yaInitFCrownOdds = 0;
    private String yaInitCrownTape = "null";
    private double yaJSSCrownOdds = 0;
    private double yaJSFCrownOdds = 0;
    private String yaJSCrownTape = "null";


    //Bet365
    private double yaInitSBet365Odds = 0;
    private double yaInitFBet365Odds = 0;
    private String yaInitBet365Tape = "null";
    private double yaJSSBet365Odds = 0;
    private double yaJSFBet365Odds = 0;
    private String yaJSBet365Tape = "null";


    //易胜博
    private double yaInitSYSBOdds = 0;
    private double yaInitFYSBOdds = 0;
    private String yaInitYSBTape = "null";
    private double yaJSSYSBOdds = 0;
    private double yaJSFYSBOdds = 0;
    private String yaJSYSBTape = "null";


    // Interwetten
    private double yaInitSInterOdds = 0;
    private double yaInitFInterOdds = 0;
    private String yaInitInterTape = "null";
    private double yaJSSInterOdds = 0;
    private double yaJSFInterOdds = 0;
    private String yaJSInterTape = "null";


    //这个是判断数据库中是否已有数据, 有的话需要做更新操作而非插入操作
    private int count;
}

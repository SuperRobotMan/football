package com.jiang.football.job;

/**
 * #Author : 山河
 * #Date   : 02/06/2022-9:35 AM
 * #Desc   : job的一个接口类
 **/
@FunctionalInterface
public interface IMJobInterface {


    //这里是执行相关的job的代码块，使用匿名内部类时候可以直接使用lamda表达式进行
    void runJob(String dateStr);

}

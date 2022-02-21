package com.jiang.football.task;

/**
 * #Author : 山河
 * #Date   : 02/06/2022-11:30 PM
 * #Desc   : task的一个接口类
 **/
// 这里有时候不写也可以用lambda表达式，是因为编译的时候默认添加了这个注释
@FunctionalInterface
public interface IMTaskInterface {

    //这里是执行相关的task的代码块，使用匿名内部类时候可以直接使用lamda表达式进行
    void runTask(String dateStr);


    //-----------------------------------------下面的两个方法都是为了测试用的------------------------------------------------------------
    public default void test(){
        System.out.println("测试一下");
    }

    public static void test01(){
        System.out.println("打印个啥呢");
    }
}

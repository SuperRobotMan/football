package com.jiang.football.models.common;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * #Author : 山河
 * #Date   : 02/06/2022-11:54 AM
 * #Desc   : 返回结果模型
 **/
@Getter
@Setter
@ToString
public class ResultModel {

    private String status;//存放返回状态
    private String msg;//存放状态信息，如果失败，就是失败信息
    private Object data;//这里是返回的结果

    //如果出错 那么状态和数据直接设置为error和null，消息可自定义
    public void setErrorResult(String msg) {
        this.status = "error";
        this.msg = msg;
        this.data = null;
    }

    public void setOKResult(Object data) {
        this.status = "ok";
        this.msg = "success";
        this.data = data;
    }

}

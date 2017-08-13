package cn.com.sfn.juqi.model;

/***
 * ﻿{"status":1,"id":"58","info":"\u767b\u5f55\u9a8c\u8bc1\u6210\u529f\uff01","referer":"","state":"success"}
 */
public class LoginModel {

    private int status;// 状态码
    private String id;// 用户id
    private String state; // success
    private String info; // 信息
    private String referer; // 头像链接
    private String session_id; // cookie

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getReferer() {
        return referer;
    }

    public void setReferer(String referer) {
        this.referer = referer;
    }

    public String getSession_id() {
        return session_id;
    }

    public void setSession_id(String session_id) {
        this.session_id = session_id;
    }

    public String getId() {
        return id;
    }

    public void setUserid(String id) {
        this.id = id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}

package com.resolute.ResoluteTagUtils.utils;

public class HttpResponse {
    private int status;
    private String msg;

    public HttpResponse(){}
    public HttpResponse(int status, String msg){
        this.status = status;
        this.msg = msg;
    }

    public int getStatus() {
        return status;
    }

    public String getMsg() {
        return msg;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}

package cn.com.sfn.juqi.model;

/**
 * Created by wuxiaojun on 17-8-10.
 */

public class BaseResponse<T> {

    public int status;
    public String info;
    public T data;

}

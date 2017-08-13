package cn.com.sfn.juqi.rxbus.rxtype;

/**
 * Created by wuxiaojun on 2017/8/12.
 */

public class RxtypeUpdateBean {

    public int updateFlag; // 1:更新头像  2:更新数据

    public RxtypeUpdateBean(int updateFlag) {
        this.updateFlag = updateFlag;
    }

}

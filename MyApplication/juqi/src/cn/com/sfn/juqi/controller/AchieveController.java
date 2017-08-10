/**
 * 有关成就的后台操作
 * @author wangwb
 *
 */

package cn.com.sfn.juqi.controller;

import java.util.ArrayList;
import java.util.List;

import cn.com.sfn.juqi.dejson.AchieveDejson;
import cn.com.sfn.juqi.model.AchieveModel;
import cn.com.sfn.juqi.net.MyHttpClient;

public class AchieveController {

    private MyHttpClient httpClient;

    public AchieveController() {
        httpClient = new MyHttpClient();
    }

    // 获取参与成就列表
    public List<AchieveModel> getJoinAchieveList(String type) {
        List<AchieveModel> achieves = new ArrayList<AchieveModel>();
        String action = "index/rank";
        String params = "type=" + type;
        String str = httpClient.doPost(action, params);
        System.out.println(str);
        if (str.equals("time out")) {
            return achieves;
        } else {
            AchieveDejson ad = new AchieveDejson();
            achieves = ad.joinAchieveListDejson(str);
            return achieves;
        }
    }

    // 获取发布成就列表
    public List<AchieveModel> getReleaseAchieveList(String type) {
        List<AchieveModel> achieves = new ArrayList<AchieveModel>();
        String action = "index/rank";
        String params = "type=" + type;
        String str = httpClient.doPost(action, params);
        System.out.println(str);
        if (str.equals("time out")) {
            return achieves;
        } else {
            AchieveDejson ad = new AchieveDejson();
            achieves = ad.ReleaseAchieveListDejson(str);
            return achieves;
        }
    }
}

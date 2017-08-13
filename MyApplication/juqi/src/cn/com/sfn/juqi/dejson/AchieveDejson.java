
package cn.com.sfn.juqi.dejson;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cn.com.sfn.juqi.model.AchieveModel;

public class AchieveDejson {
    private List<AchieveModel> joinAcList;
    private String Joinachieves;

    public List<AchieveModel> joinAchieveListDejson(String str) {
        joinAcList = new ArrayList<AchieveModel>();
        try {
            JSONObject jObject = new JSONObject(str);
            Joinachieves = jObject.getString("data");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JSONArray jsonArray;
        try {
            jsonArray = new JSONArray(Joinachieves);
            for (int i = 0; i < jsonArray.length(); i++) {
                AchieveModel achieveModel = new AchieveModel();
                JSONObject json = (JSONObject) jsonArray.get(i);
                achieveModel.setId(json.getString("id"));
                achieveModel.setRank(i + 1 + "");
                achieveModel.setUsername(json.getString("u_name"));
                if (json.has("join_entire")) {
                    achieveModel.setScore(json.getString("join_entire"));
                }
                achieveModel.setScore(json.getString("total_score"));
                joinAcList.add(achieveModel);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return joinAcList;
    }

    private List<AchieveModel> releaseAcList;
    private String Releaseachieves;

    public List<AchieveModel> ReleaseAchieveListDejson(String str) {
        releaseAcList = new ArrayList<AchieveModel>();
        try {
            JSONObject jObject = new JSONObject(str);
            Releaseachieves = jObject.getString("data");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JSONArray jsonArray;
        try {
            jsonArray = new JSONArray(Releaseachieves);
            for (int i = 0; i < jsonArray.length(); i++) {
                AchieveModel achieveModel = new AchieveModel();
                JSONObject json = (JSONObject) jsonArray.get(i);
                achieveModel.setId(json.getString("id"));
                achieveModel.setRank(i + 1 + "");
                achieveModel.setUsername(json.getString("u_name"));
                if (json.has("release_entire")) {
                    achieveModel.setScore(json.getString("release_entire"));
                }
                achieveModel.setScore(json.getString("total_score"));
                releaseAcList.add(achieveModel);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return releaseAcList;
    }
}

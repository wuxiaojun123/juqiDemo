
package cn.com.sfn.juqi.dejson;

import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cn.com.sfn.juqi.model.AuthModel;
import cn.com.sfn.juqi.model.FriendModel;
import cn.com.sfn.juqi.model.MatchModel;
import cn.com.sfn.juqi.model.UserModel;
import cn.com.sfn.juqi.net.MyHttpClient;
import cn.com.sfn.juqi.util.Config;
import cn.com.wx.util.LogUtils;

public class UserDejson {
    private JSONObject infoObject;

    public UserModel userInfoDejson(String str) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        UserModel userInfo = new UserModel();
        try {
            JSONObject jObject = new JSONObject(str);
            if (jObject.getInt("status") == 0) {
                return null;
            } else {
                infoObject = jObject.getJSONObject("data");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            userInfo.setUserId(infoObject.getString("id"));
            userInfo.setNickName(infoObject.getString("user_nicename"));
            String userAvatar = infoObject.getString("u_img").trim();
//            LogUtils.e("打印的图片地址是：" + userAvatar);
            if (!TextUtils.isEmpty(userAvatar) && !userAvatar.endsWith("avatar/")) {
                userInfo.setUserAvatar(Config.URL_BASE + userAvatar);// 210.72.13.135
            }
            userInfo.setUserSex(infoObject.getString("sex"));
            userInfo.setAge(infoObject.getString("age"));
            userInfo.setUage(infoObject.getString("u_age"));
            userInfo.setHeight(infoObject.getString("height"));
            userInfo.setWeight(infoObject.getString("weight"));
            userInfo.setSignature(infoObject.getString("signature"));
            userInfo.setUserMobile(infoObject.getString("mobile"));
            userInfo.setOffense(infoObject.getString("offense"));
            userInfo.setDefense(infoObject.getString("defense"));
            userInfo.setComprehensive(infoObject.getString("comprehensive"));
            userInfo.setStandard(infoObject.getString("standard"));
            userInfo.setPosition(infoObject.getString("position"));
            userInfo.setGrade(infoObject.getString("grade"));
            userInfo.setJoinEntire(infoObject.getString("join_entire"));
            userInfo.setJoinLevel(infoObject.getString("join_level"));
            userInfo.setReleaseEntire(infoObject.getString("release_entire"));
            userInfo.setReleaseLevel(infoObject.getString("release_level"));
            userInfo.setFriendNum(infoObject.getString("friend_num"));
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return userInfo;
    }

    private JSONObject friendObject;
    private String release, friendship;
    private List<MatchModel> releaseList;

    public FriendModel friendInfoDejson(String str) {
        FriendModel friendDetail = new FriendModel();
        try {
            JSONObject jObject = new JSONObject(str);
            if (jObject.getInt("status") == 0) {
                return null;
            } else {
                friendObject = jObject.getJSONObject("data");
                release = jObject.getString("game");
                friendship = jObject.getString("friend_ship");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JSONArray jsonArray;
        try {
            releaseList = new ArrayList<MatchModel>();
            jsonArray = new JSONArray(release);
            for (int i = 0; i < jsonArray.length(); i++) {
                MatchModel matchModel = new MatchModel();
                JSONObject json = (JSONObject) jsonArray.get(i);
                matchModel.setId(json.getString("id"));
                matchModel.setTitle(json.getString("title"));
                matchModel.setU_id(json.getString("u_id"));
                matchModel.setU_mobile(json.getString("u_mobile"));
                matchModel.setAttendance(json.getString("attendance"));
                matchModel.setNum(json.getString("num"));
                matchModel.setDuration(json.getString("duration"));
                matchModel.setCreate_time(json.getString("create_time"));
                matchModel.setStart_time(json.getString("start_time"));
                matchModel.setSpec(json.getString("spec"));
                matchModel.setDetail(json.getString("detail"));
                matchModel.setS_name(json.getString("s_name"));
                matchModel.setLocation(json.getString("location"));
                matchModel.setLongitude(Double.valueOf(json
                        .getString("longitude")));
                matchModel.setLatitude(Double.valueOf(json
                        .getString("latitude")));
                matchModel.setFee(json.getString("fee"));
                matchModel.setuImg(Config.URL_BASE
                        + friendObject.getString("u_img"));// 210.72.13.135
                releaseList.add(matchModel);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            friendDetail.setUserId(friendObject.getString("id"));
            friendDetail.setNickName(friendObject.getString("user_nicename"));
            friendDetail.setUserAvatar(Config.URL_BASE + friendObject.getString("u_img"));// 210.72.13.135
            friendDetail.setUserSex(friendObject.getString("sex"));
            friendDetail.setAge(friendObject.getString("age"));
            friendDetail.setSignature(friendObject.getString("signature"));
            friendDetail.setOffense(friendObject.getString("offense"));
            friendDetail.setDefense(friendObject.getString("defense"));
            friendDetail.setComprehensive(friendObject
                    .getString("comprehensive"));
            friendDetail.setJoinEntire(friendObject.getString("join_entire"));
            friendDetail.setReleaseEntire(friendObject
                    .getString("release_entire"));
            friendDetail.setStandard(friendObject.getString("standard"));
            friendDetail.setFriendNum(friendObject.getString("friend_num"));
            friendDetail.setPosition(friendObject.getString("position"));
            friendDetail.setReleaseList(releaseList);
            friendDetail.setFriendship(friendship);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return friendDetail;
    }

    private JSONObject friendDataObject;
    private String info;
    private List<UserModel> friends;

    public List<UserModel> getsearchedFriendList(String str) {
        try {
            JSONObject jObject = new JSONObject(str);
            if (jObject.getInt("status") == 0) {
                return null;
            } else {
                // friendDataObject = jObject.getJSONObject("data");
                info = jObject.getString("data");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JSONArray jsonArray;
        try {
            friends = new ArrayList<UserModel>();
            jsonArray = new JSONArray(info);
            for (int i = 0; i < jsonArray.length(); i++) {
                UserModel friendModel = new UserModel();
                JSONObject json = (JSONObject) jsonArray.get(i);
                friendModel.setUserId(json.getString("id"));
                friendModel.setNickName(json.getString("user_nicename"));
                friendModel.setFriendship(json.getString("friend_ship"));
                friendModel.setStandard(json.getString("standard"));
                friendModel.setUserAvatar(Config.URL_BASE
                        + json.getString("u_img"));// 210.72.13.135
                friendModel.setJoinEntire(json.getString("join_entire"));
                friendModel.setReleaseEntire(json.getString("release_entire"));
                friends.add(friendModel);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return friends;
    }

    public List<UserModel> getFriendList(String str) {
        try {
            JSONObject jObject = new JSONObject(str);
            if (jObject.getInt("status") == 0) {
                return null;
            } else {
                friendDataObject = jObject.getJSONObject("data");
                info = friendDataObject.getString("friend_info");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JSONArray jsonArray;
        try {
            friends = new ArrayList<UserModel>();
            jsonArray = new JSONArray(info);
            for (int i = 0; i < jsonArray.length(); i++) {
                UserModel friendModel = new UserModel();
                JSONObject json = (JSONObject) jsonArray.get(i);
                friendModel.setUserId(json.getString("id"));
                friendModel.setNickName(json.getString("user_nicename"));
                friendModel.setStandard(json.getString("standard"));
                friendModel.setUserAvatar(Config.URL_BASE
                        + json.getString("u_img"));// 210.72.13.135
                friendModel.setJoinEntire(json.getString("join_entire"));
                friendModel.setReleaseEntire(json.getString("release_entire"));
                friends.add(friendModel);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return friends;
    }

    public String getThirdId(String str) {
        String id = "";
        try {
            JSONObject jObject = new JSONObject(str);

            if (jObject.getInt("status") == 0) {
                return id;
            } else {
                id = jObject.getString("id");
                Config.SessionID = jObject.getString("session_id");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return id;
    }

    private JSONObject authObject1;

    public AuthModel authDejson1(String str) {
        AuthModel auth = new AuthModel();
        try {
            JSONObject jObject = new JSONObject(str);
            if (jObject.getInt("status") == 0) {
                return null;
            } else {
                authObject1 = jObject.getJSONObject("data");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            auth.setAvatar(Config.URL_BASE
                    + authObject1.getString("u_img"));
            auth.setAccount(authObject1.getString("account_name"));
            auth.setType(authObject1.getString("type"));
            auth.setResidula(authObject1.getString("max_withdraw"));
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return auth;
    }

    private JSONObject authObject;

    public AuthModel authDejson(String str) {
        AuthModel auth = new AuthModel();
        try {
            JSONObject jObject = new JSONObject(str);
            if (jObject.getInt("status") == 0) {
                return null;
            } else {
                authObject = jObject.getJSONObject("data");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            auth.setId(authObject.getString("id"));
            auth.setName(authObject.getString("user_nicename"));
            auth.setAvatar(Config.URL_BASE + authObject.getString("u_img"));
            auth.setStatus(authObject.getString("verify_status"));
            auth.setBalance(authObject.getString("coin"));
            auth.setIncome(authObject.getString("add_up"));
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return auth;
    }

    private JSONObject filename;

    public String imageName(String str) {
        String name = "";
        try {
            JSONObject jObject = new JSONObject(str);
            if (jObject.getInt("status") == 0) {
                return "";
            } else {
                filename = jObject.getJSONObject("data");
                name = filename.getString("file");
                return name;
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return "";
        }
    }
}

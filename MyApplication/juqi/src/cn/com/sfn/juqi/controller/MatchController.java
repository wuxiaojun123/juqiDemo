/**
 * 有关球局的后台操作
 *
 * @author wangwb
 */
package cn.com.sfn.juqi.controller;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;

import com.sina.weibo.sdk.utils.LogUtil;

import cn.com.sfn.juqi.dejson.AppointDejson;
import cn.com.sfn.juqi.dejson.ManageListDejson;
import cn.com.sfn.juqi.dejson.MatchDejson;
import cn.com.sfn.juqi.dejson.StandardDejson;
import cn.com.sfn.juqi.dejson.UserDejson;
import cn.com.sfn.juqi.model.CommentsModel;
import cn.com.sfn.juqi.model.FriendModel;
import cn.com.sfn.juqi.model.MatchModel;
import cn.com.sfn.juqi.model.MatchModelResponse;
import cn.com.sfn.juqi.model.StandardModel;
import cn.com.sfn.juqi.model.UserModel;
import cn.com.sfn.juqi.net.MyHttpClient;
import cn.com.sfn.juqi.util.Config;
import cn.com.wx.util.JsonUtils;
import cn.com.wx.util.LogUtils;

public class MatchController {

    private MyHttpClient httpClient;

    public MatchController() {
        httpClient = new MyHttpClient();
    }

    // 获取全部球局列表
    public List<MatchModel> getMatch(int page) throws Exception {
        List<MatchModel> matches = new ArrayList<MatchModel>();
        String action = "index/game_list";
        String params = "p=" + page;
        String str = httpClient.doPost(action, params);
        System.out.println(str);
        if (str.equals("time out")) {
            return matches;
        } else {
            MatchDejson md = new MatchDejson();
            matches = md.matchListDejson(str);
            return matches;
        }
    }

    // 获取时间排序球局列表
    public List<MatchModel> getMatchPlusOneDay(String limit, int page)
            throws Exception {
        String action = "index/game_list";
        String params = "time_limit=" + limit + "&p=" + page;
        String str = httpClient.doPost(action, params);
        System.out.println(str);
        List<MatchModel> matches = new ArrayList<MatchModel>();
        MatchDejson md = new MatchDejson();
        matches = md.matchListDejson(str);
        if (matches.size() != 0) {
            return matches;
        } else
            return matches;
    }

    // 获取时间排序球局列表
    public List<MatchModel> getMatchByCondition2(String area, int page)
            throws Exception {
        String action = "index/game_list";
        String params = "area=" + area + "&p=" + page;
        String str = httpClient.doPost(action, params);
        System.out.println(str);
        List<MatchModel> matches = new ArrayList<MatchModel>();
        MatchDejson md = new MatchDejson();
        matches = md.matchListDejson(str);
        if (matches.size() != 0) {
            return matches;
        } else
            return matches;
    }

    // 获取时间排序球局列表
    public List<MatchModel> getMatchByCondition(String order, int page)
            throws Exception {
        String action = "index/game_list";
        String params = "order_by=" + order + "&p=" + page;
        String str = httpClient.doPost(action, params);
        System.out.println(str);
        List<MatchModel> matches = new ArrayList<MatchModel>();
        MatchDejson md = new MatchDejson();
        matches = md.matchListDejson(str);
        if (matches.size() != 0) {
            return matches;
        } else
            return matches;
    }

    // 获取地理位置排序球局列表
    public List<MatchModel> getMatchByCondition1(String order, double lon,
                                                 double lat, int page) throws Exception {
        String action = "index/game_list";
        String params = "order_by=" + order + "&lat=" + lat + "&lng=" + lon
                + "&p=" + page;
        String str = httpClient.doPost(action, params);
        System.out.println(str);
        List<MatchModel> matches = new ArrayList<MatchModel>();
        MatchDejson md = new MatchDejson();
        matches = md.matchListDejson(str);
        if (matches.size() != 0) {
            return matches;
        } else
            return matches;
    }

    // 获取球局详细信息
    public MatchModel getinfo(String id) {
        MatchModel matchDetail = new MatchModel();
        String action = "index/game_info";
        String param = "id=" + id;
        String str = httpClient.doPost(action, param);
        System.out.println(str);
        if (str.equals("time out")) {
            return null;
        } else {
            MatchDejson mdd = new MatchDejson();
            matchDetail = mdd.matchDetailDejson(str);
            if (matchDetail == null) {
                return null;
            } else {
                return matchDetail;
            }
        }
    }

    // 获取评论列表
    public List<CommentsModel> getComment(String id) {
        List<CommentsModel> comments = new ArrayList<CommentsModel>();
        String action = "comment/widget/get_comment/table/game/post_id/" + id;
        String str = httpClient.doGet(action);
        System.out.println(str);
        if (str.equals("time out")) {
            return comments;
        } else {
            MatchDejson md = new MatchDejson();
            comments = md.commentListDejson(str);
            return comments;
        }
    }

    // 还有用吗？
    public String getTable(String id) {
        String action = "comment/widget/get_comment/table/game/post_id/" + id;
        String str = httpClient.doGet(action);
        System.out.println(str);
        if (str.equals("time out")) {
            return "";
        } else {
            String table = "";
            MatchDejson md = new MatchDejson();
            table = md.commentTable(str);
            return table;
        }
    }

    // 参与详情
    public MatchModel getJoinDetail(String id) {
        MatchModel matchDetail = new MatchModel();
        String action = "game/join_detail";
        String param = "id=" + id;
        String str = httpClient.doPost(action, param);
        System.out.println(str);
        if (str.equals("time out")) {
            return null;
        } else {
            MatchDejson mdd = new MatchDejson();
            matchDetail = mdd.manageJoinDejson(str);
            if (matchDetail == null) {
                return null;
            } else {
                return matchDetail;
            }
        }
    }

    // 获取球局凭证列表
    public List<MatchModel> getCertificateList(String id) {
        String action = "user/center/join_game";
        String param = "id=" + id;
        String str = httpClient.doPost(action, param);
        System.out.println(str);
        List<MatchModel> matches = new ArrayList<MatchModel>();
        if (str.equals("time out")) {
            return matches;
        } else {
            ManageListDejson md = new ManageListDejson();
            matches = md.cetificateDejson(str);
            return matches;
        }
    }

    // 获取管理球局列表
    public List<MatchModel> getManageMatchList(String id) {
        String action = "game/game_list";
        String param = "id=" + id;
        String str = httpClient.doPost(action, param);
        System.out.println(str);
        LogUtils.e("管理球局列表是:" + str);
        List<MatchModel> matches = new ArrayList<MatchModel>();
        if (str.equals("time out")) {
            return matches;
        } else {
            ManageListDejson md = new ManageListDejson();
            matches = md.matchListDejson(str);
            /*try {
                if(str.startsWith(" ")){
                    str = str.replaceFirst(" ","");
                }
                MatchModelResponse response = (MatchModelResponse) JsonUtils.toObject(str, MatchModelResponse.class);
                if (response.status != 0) {
                    matches.addAll(response.data);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }*/
            return matches;
        }
    }

    // 发布球局
    public int release(String title, String uid, String startTime,
                       int duration, String num, int type, int format, String fee,
                       String detail, String sname, String location, double lat,
                       double lon, String phone, String district) {
        String action = "game/release_game";
        String param = "title=" + title + "&u_id=" + uid + "&start_time="
                + startTime + "&duration=" + duration + "&num=" + num
                + "&type=" + type + "&spec=" + format + "&fee=" + fee
                + "&detail=" + detail + "&s_name=" + sname + "&location="
                + location + "&longitude=" + lon + "&latitude=" + lat
                + "&mobile=" + phone + "&area=" + district;
        Log.e("release_game-param", param);
        String str = httpClient.doPost(action, param);
        System.out.println(str);
        Log.e("release_game-return", str);
        AppointDejson appointdejson = new AppointDejson();
        StandardModel appoint = appointdejson.dejson(str);
        if (appoint == null)
            return -1;
        if (appoint.getStatus() == 1) {
            return Config.AppointSuccess;
        } else
            return Config.AppointFailed;
    }

    // 修改球局
    public int changeMatch(String id, String title, String uid,
                           String startTime, int duration, String num, int type, int format,
                           String fee, String detail, String sname, String location,
                           double lat, double lon, String phone, String district) {
        String action = "game/modify_game_post";
        String param = "id=" + id + "&title=" + title + "&u_id=" + uid
                + "&start_time=" + startTime + "&duration=" + duration
                + "&num=" + num + "&type=" + type + "&spec=" + format + "&fee="
                + fee + "&detail=" + detail + "&s_name=" + sname + "&location="
                + location + "&longitude=" + lon + "&latitude=" + lat
                + "&mobile=" + phone + "&area=" + district;
        String str = httpClient.doPost(action, param);
        System.out.println(str);
        StandardDejson changedejson = new StandardDejson();
        StandardModel change = changedejson.dejson(str);
        if (change == null)
            return -1;
        if (change.getStatus() == 1) {
            return Config.ChangeMatchSuccsee;
        } else
            return Config.ChangeMatchFailed;
    }

    // 删除球局
    public int deleteMatch(String matchId) {
        String action = "game/cancel_game";
        String param = "id=" + matchId;
        String str = httpClient.doPost(action, param);
        System.out.println(str);
        StandardDejson changedejson = new StandardDejson();
        StandardModel delete = changedejson.dejson(str);
        if (delete == null)
            return -1;
        if (delete.getStatus() == 1) {
            return Config.DeleteMatchSuccess;
        } else
            return Config.DeleteMatchFailed;
    }

    // 参与球局
    public int attend(String matchId, String name, String mobile) {
        String action = "game/join_game";
        String param = "gid=" + matchId + "&name=" + name + "&mobile=" + mobile;
        String str = httpClient.doPost(action, param);
        System.out.println(str);
        StandardDejson attendDejson = new StandardDejson();
        StandardModel attendModel = attendDejson.dejson(str);
        if (attendModel == null)
            return -1;
        if (attendModel.getStatus() == 1) {
            return Config.AttendSuccess;
        } else
            return Config.AttendFailed;
    }

    // 评论球局
    public int comments(String content, String parentId, String matchId,
                        String table, String toUid) {
        String action = "comment/comment/post";
        String param = "content=" + content + "&parentid=" + parentId
                + "&post_id=" + matchId + "&post_table=" + "game" + "&to_uid="
                + toUid;
        String str = httpClient.doPost(action, param);
        System.out.println(str);
        StandardDejson commentDejson = new StandardDejson();
        StandardModel commentModel = commentDejson.dejson(str);
        if (commentModel == null)
            return -1;
        if (commentModel.getStatus() == 1) {
            return Config.CommentSuccess;
        } else
            return Config.CommentFailed;
    }

    // 获取二维码
    public String getQR(String id) {
        String action = "user/center/create_qrcode";
        String param = "id=" + id;
        String str = httpClient.doPost(action, param);
        System.out.println(str);
        if (str.equals("time out")) {
            return "";
        } else {
            String qrContent = "";
            MatchDejson md = new MatchDejson();
            qrContent = md.qrCon(str);
            return qrContent;
        }
    }

    // 支付宝支付球局
    public String paygame(String id, String type) {
        String action = "game/pay_game";
        String param = "id=" + id + "&type=" + type;
        String str = httpClient.doPost(action, param);
        System.out.println(str);
        if (str.equals("time out")) {
            return "";
        } else {
            String outtrade = "";
            MatchDejson md = new MatchDejson();
            outtrade = md.outTrade(str);
            return outtrade;
        }
    }

    // 微信支付球局
    public String weixinpay(String u_id, String g_id, String fee) {
        String action = "weixinpay/weixinpay";
        String param = "u_id=" + u_id + "&g_id=" + g_id + "&fee=" + fee;
        String str = httpClient.doPost(action, param);
        System.out.println(str);
        Log.e("weixinpay_returnstr", str);
        if (str.equals("time out")) {
            return "";
        } else {
            return str;
        }
    }


    // 获得扫二维码结果
    public int getQRresult(String result) {
        String action = "user/center/scan_qrcode/" + result;
        String str = httpClient.doGet(action);
        System.out.println(str);
        StandardDejson qrDejson = new StandardDejson();
        StandardModel qrModel = qrDejson.dejson(str);
        if (qrModel == null)
            return -1;
        if (qrModel.getStatus() == 1) {
            return Config.ScanQrSuccess;
        } else
            return Config.ScanQrFailed;
    }

    // 退出球局
    public int quit(String matchId) {
        String action = "user/center/quit_game";
        String param = "id=" + matchId;
        String str = httpClient.doPost(action, param);
        System.out.println(str);
        StandardDejson quitdejson = new StandardDejson();
        StandardModel quit = quitdejson.dejson(str);
        if (quit == null)
            return -1;
        // 判断是否退出成功
        if (quit.getStatus() == 1) {
            return Config.QuitSuccess;
        } else
            return Config.QuitFailed;
    }

    //取消关注
    public int cancelFriend(String id) {  //friendid
        String action = "user/center/delete_friends";
        String param = "id=" + id;
        // 发送加好友请求
        String str = httpClient.doPost(action, param);
        StandardDejson editdejson = new StandardDejson();
        StandardModel cancel = editdejson.dejson(str);
        if (cancel == null)
            return -1;
        // 判断是否取消成功
        if (cancel.getStatus() == 1) {
            return Config.CancelFriendSuccess;
        } else
            return Config.CancelFriendFailed;
    }

    //按昵称搜索局友
    public List<UserModel> searchFriend(String keyword) {
        String action = "user/center/search_friends";
        //String param = "id=" + keyword;
        String param = "keyword=" + keyword;
        Log.e("searchfriend-param", param);
        // 发送搜索请求
        String str = httpClient.doPost(action, param);
        LogUtil.e("searchfriend-httpClient.doPost", str);
        if (str.equals("time out")) {
            return null;
        } else {
            List<UserModel> searchedfriends = new ArrayList<UserModel>();
            UserDejson md = new UserDejson();
            searchedfriends = md.getsearchedFriendList(str);
            return searchedfriends;
        }
    }

}
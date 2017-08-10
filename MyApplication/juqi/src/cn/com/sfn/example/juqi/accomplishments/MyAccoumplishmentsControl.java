
package cn.com.sfn.example.juqi.accomplishments;

import android.os.Handler;
import android.os.Message;

import cn.com.sfn.juqi.net.MyHttpClient;
import cn.com.sfn.juqi.util.Config;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 帐号:18810543162 123456 15201148094 123456
 */
public class MyAccoumplishmentsControl {
    // 网络请求类
    private MyHttpClient httpClient;
    // 未获得组织成就接口
    public static final String NOT_OBTAIN_ZUZHI_PATH = "game/check_noorgan_achieve";
    // 未获得参与成就接口
    public static final String NOT_OBTAIN_CANYU_PATH = "game/check_nojoin_achieve";
    // 获得组织成就接口
    public static final String OBTAIN_ZUZHI_PATH = "game/check_organ_achieve";
    // 获得参与成就接口
    public static final String OBTAIN_CANYU_PATH = "game/check_join_achieve";
    // 线程池
    private ExecutorService mExecutorService = Executors.newSingleThreadExecutor();
    // 表示用那一个接口
    String action = null;
    // 用于thread和handler通信
    private Handler mHandler;

    public MyAccoumplishmentsControl(Handler handler) {
        this.mHandler = handler;
        httpClient = new MyHttpClient();
    }

    public void getJsonStr(String type) {
        int flag = 0;
        if (NOT_OBTAIN_ZUZHI_PATH.equals(type)) {
            action = NOT_OBTAIN_ZUZHI_PATH;
            flag = 1;
        } else if (NOT_OBTAIN_CANYU_PATH.equals(type)) {
            action = NOT_OBTAIN_CANYU_PATH;
            flag = 2;
        } else if (OBTAIN_ZUZHI_PATH.equals(type)) {
            action = OBTAIN_ZUZHI_PATH;
            flag = 3;
        } else if (OBTAIN_CANYU_PATH.equals(type)) {
            action = OBTAIN_CANYU_PATH;
            flag = 4;
        }
        MyRunnable runnable = new MyRunnable(flag);
        mExecutorService.submit(runnable);
    }

    private class MyRunnable implements Runnable {
        private int flag;

        public MyRunnable(int flag) {
            this.flag = flag;
        }

        @Override
        public void run() {
            String str = null;
            int arg1 = 0;
            try {
                String param = "id=" + Config.login_userid;
                str = httpClient.doPost(action, param);
                if ("time out".equals(str)) {
                    arg1 = -1;
                }
            } catch (Exception e) {
                e.printStackTrace();
                str = "error";
                arg1 = -2;
            }
            Message msg = mHandler.obtainMessage();
            msg.obj = str;
            msg.arg1 = arg1;
            msg.arg2 = flag;
            mHandler.sendMessage(msg);
        }
    }

    public List<MyAccomplishmentsBean> gsonParseJson(String json) {
        List<MyAccomplishmentsBean> list = null;
        try {
            System.out.println("json数据是："+json);
            JSONObject jsonObject = new JSONObject(json);
            String state = jsonObject.getString("state");
            if ("success".equals(state)) {
                // 获取集合对象
                String result = jsonObject.getString("result");
                list = fromJsonArray(result, MyAccomplishmentsBean.class);
            }
        } catch (Exception e) {
            e.printStackTrace();
            list = null;
        }
        return list;
    }

    /***
     * 解析json数据获取list对象集合
     * 
     * @param json
     * @param clazz
     * @return
     * @throws Exception
     */
    public <T> List<T> fromJsonArray(String json, Class<T> clazz) throws Exception {
        List<T> lst = new ArrayList<T>();
        JsonArray array = new JsonParser().parse(json).getAsJsonArray();
        for (final JsonElement elem : array) {
            T fromJson = new Gson().fromJson(elem, clazz);
            if (fromJson != null) {
                lst.add(fromJson);
            }
        }
        return lst;
    }

    public void shutDown() {
        if (!mExecutorService.isShutdown()) {
            mExecutorService.shutdownNow();
            mExecutorService = null;
        }
    }

}

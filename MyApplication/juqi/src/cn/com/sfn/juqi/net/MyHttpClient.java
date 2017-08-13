
package cn.com.sfn.juqi.net;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import cn.com.sfn.juqi.dejson.UserDejson;
import cn.com.sfn.juqi.util.Config;
import cn.com.wx.util.LogUtils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.util.Log;

public class MyHttpClient {

    private String result = "";
//    private String url = ""; // 192.168.3.2 210.72.13.135
    // www.juqilife.cn

    private SharedPreferences settings;


    @SuppressWarnings("deprecation")
    public String doPost(Context context, String action, String params) {
        settings = context.getSharedPreferences(Config.PREFS_NAME, 0);
        if (TextUtils.isEmpty(Config.SessionID)) {
            System.out.println("action:" + action + "  params:" + params);
        } else {
            System.out.println("action:" + action + "  params:" + params
                    + "ID:" + Config.SessionID);
            // 记住登录状态
            Editor editor = settings.edit();
            // 存入数据
            editor.putString("sessionId", Config.SessionID);
            // 提交修改
            editor.commit();
        }
        HttpURLConnection conn = null;
        try {
            URL requestURL = new URL(Config.URL_BASE + action);
            conn = (HttpURLConnection) requestURL.openConnection();
            if (Config.SessionID != null) {
                conn.setRequestProperty("Cookie", Config.SessionID);
            }
            conn.setRequestMethod("POST");
            conn.setConnectTimeout(6000);
            conn.setDoOutput(true);
            byte[] bytes = params.getBytes();
            conn.getOutputStream().write(bytes, 0, bytes.length);
            conn.getOutputStream().flush();
            conn.getOutputStream().close();

            InputStream iStream = conn.getInputStream();
            /*Map<String, List<String>> headerFields = conn.getHeaderFields();
            Iterator<Map.Entry<String, List<String>>> iterator = headerFields.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, List<String>> next = iterator.next();
                LogUtils.e("key=" + next.getKey() + "--" + next.getValue());
            }*/
            String cookieval = conn.getHeaderField("set-cookie");
            LogUtils.e("cookie的值是:" + cookieval + "----");
            if (cookieval != null) {
                Config.SessionID = cookieval.substring(0,
                        cookieval.indexOf(";"));
            }
            BufferedReader rd = new BufferedReader(new InputStreamReader(
                    iStream, "UTF-8"));
            String tempLine = null;
            while ((tempLine = rd.readLine()) != null) {
                this.result += tempLine.toString();
            }
            rd.close();
            iStream.close();
        } catch (IOException e) {
            e.printStackTrace();
            return "time out";
        } finally {
            if (null != conn) {
                conn.disconnect();
            }
        }
//        this.result = jsontourl(this.result);
        try {
//            LogUtils.e("登录信息没有解码之前" + result);
            this.result = URLDecoder.decode(this.result, "UTF-8");

        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return this.result;
    }

    @SuppressWarnings("deprecation")
    public String doPost(String action, String params) {
        HttpURLConnection conn = null;
        try {
            URL requestURL = new URL(Config.URL_BASE + action);
            conn = (HttpURLConnection) requestURL.openConnection();
            LogUtils.e("设置cookie=" + Config.SessionID);
            if (Config.SessionID != null) {
                conn.setRequestProperty("Cookie", Config.SessionID);
            }
            conn.setRequestMethod("POST");
            conn.setConnectTimeout(6000);
            conn.setDoOutput(true);
            byte[] bytes = params.getBytes();
            conn.getOutputStream().write(bytes, 0, bytes.length);
            conn.getOutputStream().flush();
            conn.getOutputStream().close();
            conn.connect();
            if (conn.getResponseCode() == 200) {
                InputStream iStream = conn.getInputStream();
                String cookieval = conn.getHeaderField("set-cookie");
                LogUtils.e("返回的cookie值是:" + cookieval);
                if (cookieval != null) {
                    Config.SessionID = cookieval.substring(0,
                            cookieval.indexOf(";"));
                }
                BufferedReader rd = new BufferedReader(new InputStreamReader(
                        iStream, "UTF-8"));
                String tempLine = null;
                while ((tempLine = rd.readLine()) != null) {
                    this.result = tempLine.toString();
                }
                rd.close();
                iStream.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
            return "time out";
        } finally {
            if (null != conn) {
                conn.disconnect();
            }
        }
        this.result = jsontourl(this.result);
        this.result = URLDecoder.decode(this.result);
        return this.result;
    }

    @SuppressWarnings("deprecation")
    public String doGet(String action) {
        HttpURLConnection conn = null;
        try {
            System.out.println(Config.URL_BASE + action);
            URL requestURL = new URL(Config.URL_BASE + action);
            conn = (HttpURLConnection) requestURL.openConnection();
            if (Config.SessionID != null) {
                conn.setRequestProperty("Cookie", Config.SessionID);
            }
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(6000);
            conn.setDoOutput(true);

            InputStream iStream = conn.getInputStream();
            String cookieval = conn.getHeaderField("set-cookie");
            if (cookieval != null) {
                Config.SessionID = cookieval.substring(0,
                        cookieval.indexOf(";"));
            }
            BufferedReader rd = new BufferedReader(new InputStreamReader(
                    iStream, "UTF-8"));
            String tempLine = null;
            while ((tempLine = rd.readLine()) != null) {
                this.result = tempLine.toString();
            }
            rd.close();
            iStream.close();
        } catch (IOException e) {
            e.printStackTrace();
            return "time out";
        } finally {
            if (null != conn) {
                conn.disconnect();
            }
        }
        this.result = jsontourl(this.result);
        this.result = URLDecoder.decode(this.result);
        return this.result;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    /*public static Bitmap getImage(String urlpath) throws Exception {
        urlpath = StrReplace(urlpath, "192.168.3.2", "www.juqilife.cn"); // 绗簩澶勬敼鍦板潃鐨勫湴鏂�
        URL url = new URL(urlpath);

        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setConnectTimeout(5 * 1000);
        Bitmap bitmap = null;
        if (conn.getResponseCode() == 200) {
            InputStream inputStream = conn.getInputStream();
            bitmap = BitmapFactory.decodeStream(inputStream);
        }
        return bitmap;
    }*/

    public String uploadFile(String postUrl, String type, File uploadFile) {
        String end = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";
        try {
            URL url = new URL(postUrl + type);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            /*
             * Output to the connection. Default is false, set to true because
             * post method must write something to the connection
             */
            con.setDoOutput(true);
            /* Read from the connection. Default is true. */
            con.setDoInput(true);
            /* Post cannot use caches */
            con.setUseCaches(false);
            /* Set the post method. Default is GET */
            if (Config.SessionID != null) {
                con.setRequestProperty("Cookie", Config.SessionID);
            }
            con.setRequestMethod("POST");
            /* 璁剧疆璇锋眰灞炴�� */
            con.setRequestProperty("Connection", "Keep-Alive");
            con.setRequestProperty("Charset", "UTF-8");
            con.setRequestProperty("Content-Type",
                    "multipart/form-data;boundary=" + boundary);
            /* 璁剧疆DataOutputStream锛実etOutputStream涓粯璁よ皟鐢╟onnect() */
            DataOutputStream ds = new DataOutputStream(con.getOutputStream()); // output
            // to
            // the
            // connection
            ds.writeBytes(twoHyphens + boundary + end);
            ds.writeBytes("Content-Disposition: form-data; "
                    + "name=\"file\";filename=\"" + uploadFile.getName() + "\"" + end);
            ds.writeBytes(end);
            /* 鍙栧緱鏂囦欢鐨凢ileInputStream */
            FileInputStream fStream = new FileInputStream(uploadFile);
            /* 璁剧疆姣忔鍐欏叆8192bytes */
            int bufferSize = 8192;
            byte[] buffer = new byte[bufferSize]; // 8k
            int length = -1;
            /* 浠庢枃浠惰鍙栨暟鎹嚦缂撳啿鍖� */
            while ((length = fStream.read(buffer)) != -1) {
                /* 灏嗚祫鏂欏啓鍏ataOutputStream涓� */
                ds.write(buffer, 0, length);
            }
            ds.writeBytes(end);
            ds.writeBytes(twoHyphens + boundary + twoHyphens + end);
            /* 鍏抽棴娴侊紝鍐欏叆鐨勪笢瑗胯嚜鍔ㄧ敓鎴怘ttp姝ｆ枃 */
            fStream.close();
            /* 鍏抽棴DataOutputStream */
            ds.close();
            /* 浠庤繑鍥炵殑杈撳叆娴佽鍙栧搷搴斾俊鎭� */
            InputStream is = con.getInputStream(); // input from the connection
            // 姝ｅ紡寤虹珛HTTP杩炴帴
            int ch;
            StringBuffer b = new StringBuffer();
            while ((ch = is.read()) != -1) {
                b.append((char) ch);
            }
            /* 鏄剧ず缃戦〉鍝嶅簲鍐呭 */
            LogUtils.e("上传图片：" + b.toString().trim());
            String str = "";
            UserDejson ud = new UserDejson();
            str = ud.imageName(b.toString().trim());
            return str;
        } catch (Exception e) {
            /* 鏄剧ず寮傚父淇℃伅 */
            return "";
        }
    }

    /* 涓婁紶鏂囦欢鑷砈erver鐨勬柟娉� */
    public String uploadFile(String postUrl, String fileName, String uploadFile) {
        String end = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";
        try {
            URL url = new URL(postUrl);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            /*
             * Output to the connection. Default is false, set to true because
             * post method must write something to the connection
             */
            con.setDoOutput(true);
            /* Read from the connection. Default is true. */
            con.setDoInput(true);
            /* Post cannot use caches */
            con.setUseCaches(false);
            /* Set the post method. Default is GET */
            if (Config.SessionID != null) {
                con.setRequestProperty("Cookie", Config.SessionID);
            }
            con.setRequestMethod("POST");
            /* 璁剧疆璇锋眰灞炴�� */
            con.setRequestProperty("Connection", "Keep-Alive");
            con.setRequestProperty("Charset", "UTF-8");
            con.setRequestProperty("Content-Type",
                    "multipart/form-data;boundary=" + boundary);
            /* 璁剧疆DataOutputStream锛実etOutputStream涓粯璁よ皟鐢╟onnect() */
            DataOutputStream ds = new DataOutputStream(con.getOutputStream()); // output
            // to
            // the
            // connection
            ds.writeBytes(twoHyphens + boundary + end);
            ds.writeBytes("Content-Disposition: form-data; "
                    + "name=\"file\";filename=\"" + fileName + "\"" + end);
            ds.writeBytes(end);
            /* 鍙栧緱鏂囦欢鐨凢ileInputStream */
            FileInputStream fStream = new FileInputStream(uploadFile);
            /* 璁剧疆姣忔鍐欏叆8192bytes */
            int bufferSize = 8192;
            byte[] buffer = new byte[bufferSize]; // 8k
            int length = -1;
            /* 浠庢枃浠惰鍙栨暟鎹嚦缂撳啿鍖� */
            while ((length = fStream.read(buffer)) != -1) {
                /* 灏嗚祫鏂欏啓鍏ataOutputStream涓� */
                ds.write(buffer, 0, length);
            }
            ds.writeBytes(end);
            ds.writeBytes(twoHyphens + boundary + twoHyphens + end);
            /* 鍏抽棴娴侊紝鍐欏叆鐨勪笢瑗胯嚜鍔ㄧ敓鎴怘ttp姝ｆ枃 */
            fStream.close();
            /* 鍏抽棴DataOutputStream */
            ds.close();
            /* 浠庤繑鍥炵殑杈撳叆娴佽鍙栧搷搴斾俊鎭� */
            InputStream is = con.getInputStream(); // input from the connection
            // 姝ｅ紡寤虹珛HTTP杩炴帴
            int ch;
            StringBuffer b = new StringBuffer();
            while ((ch = is.read()) != -1) {
                b.append((char) ch);
            }
            /* 鏄剧ず缃戦〉鍝嶅簲鍐呭 */
            LogUtils.e("上传图片：" + b.toString().trim());
            String str = "";
            UserDejson ud = new UserDejson();
            str = ud.imageName(b.toString().trim());
            return str;
        } catch (Exception e) {
            /* 鏄剧ず寮傚父淇℃伅 */
            return "";
        }
    }

    public String jsontourl(String json) {
        json = json.replace("{", "%7B");
        json = json.replace("}", "%7D");
        json = json.replace("\"", "%22");
        json = json.replace("[", "%5B");
        json = json.replace("]", "%5D");
        json = json.replace("\\", "%5C");
        return json;
    }

    public static String StrReplace(String rStr, String rFix, String rRep) {
        int l = 0;
        String gRtnStr = rStr;
        do {
            l = rStr.indexOf(rFix, l);
            if (l == -1)
                break;
            gRtnStr = rStr.substring(0, l) + rRep + rStr.substring(l + rFix.length());
            l += rRep.length();
            rStr = gRtnStr;
        } while (true);
        return gRtnStr.substring(0, gRtnStr.length());
    }

}

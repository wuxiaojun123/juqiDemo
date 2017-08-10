package cn.com.wx.util;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

import java.io.StringReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by wuxiaojun on 16-12-12.
 */

public class JsonUtils {

    /**
     * 将返回结果转化成对象
     *
     * @param s
     * @param t
     * @return
     */
    public static <T> Object toObject(String s, Class<T> t) {
        if (s != null) {
            JsonReader jsonReader = new JsonReader(new StringReader(s));//其中jsonContext为String类型的Json数据
            jsonReader.setLenient(true);
            Gson gson = new Gson();
            return gson.fromJson(jsonReader, t);
        }
        return null;
    }

    /**
     * 返回集合对象
     *
     * @return
     */
    public static <T> List<T> fromJsonArray(String json, Class<T> clazz) throws Exception {
        List<T> lst = new ArrayList<T>();
        JsonArray array = new JsonParser().parse(json).getAsJsonArray();
        for (final JsonElement elem : array) {
            lst.add(new Gson().fromJson(elem, clazz));
        }
        return lst;
    }

    /***
     * 将对象集合转成String
     * @param list
     * @return
     */
    public static <T> String toJsonFromList(List<T> list) {
        Gson gson = new Gson();
        Type type = new TypeToken<List<T>>() {
        }.getType(); // 指定集合对象属性
        String json = gson.toJson(list, type);
        return json;
    }

    /***
     * 将对象转成String
     * @param object
     * @return
     */
    public static <T> String toJsonFromObject(Object object) {
        Gson gson = new Gson();
//        Type type = new TypeToken<T>() {}.getType(); // 指定集合对象属性
        String json = gson.toJson(object, Object.class);
        return json;
    }

    /***
     * 将对象转成String
     * @param map
     * @return
     */
    public static <T> String toJsonFromMap(Map<String,T> map) {
        Gson gson = new Gson();
        Type type = new TypeToken<Map<String,T>>() {}.getType(); // 指定集合对象属性
        String json = gson.toJson(map, type);
        return json;
    }

}

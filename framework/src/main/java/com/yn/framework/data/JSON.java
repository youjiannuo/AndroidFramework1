package com.yn.framework.data;


import com.yn.framework.exception.YNJSONException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class JSON {

    private String html = "";
    private String tag = "";
    private JSONObject jsonObject = null;
    private JSONArray jsonArray = null;
    private int n = 0;  //有多少行数据
    private int i = -1;  //当前第几个
    private int n_n = 0; //一行有多少个数据


    private Map<String, String> map = null;

    public JSON() {
    }

    public JSON(String html, String tag) {
        this.html = html;
        this.tag = tag;

        try {

            JSONObject jo1 = getJSONObjectA(new JSONArray(dealString(html)), 0);

            jsonArray = new JSONArray(jo1.get(tag).toString());

            n = jsonArray.length();

            jsonObject = getJSONObjectA(jsonArray, 0);

            n_n = jsonObject.length();

        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }

    public JSON(String html) {
        try {

            jsonArray = new JSONArray(dealString(html));

            jsonObject = getJSONObjectA(jsonArray, 0);

            n_n = jsonObject.length();

            n = jsonArray.length();

        } catch (Exception e) {
//			e.printStackTrace();
        }

    }

    /*
     * 检查字符串
     */
    private String dealString(String s) {
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == ' ') continue;
            if (s.charAt(i) != '[') {
                s = '[' + s;
            }
            break;
        }
        for (int i = s.length() - 1; i >= 0; i--) {
            if (s.charAt(i) == ' ') continue;
            if (s.charAt(i) != ']') {
                s += ']';
            }
            break;

        }
        return s;

    }


    public long getItemLong(String key) {
        try {
            return jsonObject == null ? 0 : jsonObject.getLong(key);
        } catch (JSONException e) {
            e.printStackTrace();
            new YNJSONException(e).throwException();
        }
        return 0;
    }

    public double getItemFloat(String key) {
        try {
            return jsonObject == null ? 0.0f : jsonObject.getDouble(key);
        } catch (JSONException e) {
            e.printStackTrace();
            new YNJSONException(e).throwException();
        }
        return 0.0f;
    }

    public boolean getItemBoolean(String key) {
        try {
            return jsonObject != null && jsonObject.getBoolean(key);
        } catch (JSONException e) {
//            e.printStackTrace();
            new YNJSONException(e).throwException();
        }
        return false;
    }

    public int getItemInt(String key) {
        try {
            return jsonObject == null ? 0 : jsonObject.getInt(key);
        } catch (JSONException e) {
            new YNJSONException(e).throwException();
        }
        return 0;
    }

    public double getItemDouble(String key) {
        try {
            return jsonObject == null ? 0 : jsonObject.getDouble(key);
        } catch (JSONException e) {
            e.printStackTrace();
            new YNJSONException(e).throwException();
        }
        return 0;
    }


    public Object getItemObject(String key) {
        try {
            return jsonObject == null ? "" : jsonObject.get(key);
        } catch (JSONException e) {
            e.printStackTrace();
            new YNJSONException(e).throwException();
        }
        return null;
    }

    public JSON(String keys[], String values[]) {

        if (keys == null || values == null || keys.length != values.length)
            return;

        map = new HashMap<>();

        for (int i = 0; i < keys.length; i++) {
            map.put(keys[i], values[i]);
        }

        jsonObject = new JSONObject(map);

    }


    public void put(String key, String value) {
        try {
            jsonObject.put(key, value);
        } catch (JSONException e) {
            e.printStackTrace();
            new YNJSONException(e).throwException();
        }
    }

    public String toString() {

        return jsonObject == null ? "" : jsonObject.toString();

    }

    public String getAllString() {
        return jsonArray.toString();
    }

    /*
     * 多少列
     */
    public int Row() {
        return n_n;
    }

    /*
     * 多少行
     */
    public int size() {
        return n;
    }

    public String getHtml() {
        return html;
    }

    public String getString(String key) {

        try {
            return jsonObject == null ? "" : jsonObject.get(key).toString();
        } catch (JSONException e) {
            // TODO Auto-generated catch block
//            e.printStackTrace();
            new YNJSONException(e).throwException();
        }
        return "";

    }

    public Object getStringObject(String key) {
        try {
            return jsonObject == null ? "" : jsonObject.get(key);
        } catch (JSONException e) {
            e.printStackTrace();
            new YNJSONException(e).throwException();
        }
        return new Object();
    }

    /**
     * 修改数据
     *
     * @param key   修改的键
     * @param value 需要修改的值
     */
    public JSON changeData(String key, String value) {
        try {
            jsonObject.put(key, value);
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            new YNJSONException(e).throwException();
        }
        return this;
    }


    public int getInt(String key) {
        try {
            return Integer.parseInt(getString(key));
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
//            new YNJSONException(e).throwException();
        }
        return 0;
    }

    public boolean next() {

        if ((++i) >= n) return false;
        try {
            jsonObject = getJSONObjectA(jsonArray, i);
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            new YNJSONException(e).throwException();
        }

        return true;
    }


    public String getRowString(int index) {
        try {
            return getJSONObjectA(jsonArray, index).toString();
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            new YNJSONException(e).throwException();
        }

        return "";

    }

    public JSONObject getRowObject(int index) {
        try {
            return getJSONObjectA(jsonArray, index);
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            new YNJSONException(e).throwException();
        }
        return new JSONObject();
    }

    /*
     * 将传入的标记返回出对应的字符串数组
     */
    public String[] getArrayString(String keys[]) {
        return getArrayString(keys, keys == null ? 0 : keys.length);
    }

    private String[] getArrayString(String keys[], int n) {
        String values[] = new String[n];

        for (int i = 0; i < keys.length; i++) {
            values[i] = getString(keys[i]);
        }

        return values;
    }

    /**
     * @param keys
     * @param values
     * @return
     */
    public String[] getArrayString(String keys[], String values[]) {
        String result[] = getArrayString(keys,
                (keys == null ? 0 : keys.length) +
                        (values == null ? 0 : values.length));

        for (int i = 0; i < values.length; i++)
            result[keys.length + i] = values[i];

        return result;

    }


    public JSONObject getJSONObjectA(JSONArray jSONArray, int n)
            throws JSONException {
        try {
            return (JSONObject) jSONArray.get(n);
        } catch (ClassCastException e) {
            System.out.println("JSON异常数据：" + jsonArray.get(n).toString());
            new YNJSONException(e).throwException();
        }
        return new JSONObject();
    }

    public static String getString(String[] keys, String[] values) {
        return new JSON(keys, values).toString();
    }

    public String getStrings(String key) {
        String keys[] = key.split("\\.");
        return getStrings(keys);
    }

    public String getStrings(String keys[]) {

        String result = getString(keys[0]);
        for (int i = 1; i < keys.length; i++) {
            result = new JSON(result).getString(keys[i]);
        }
        return result;
    }


}

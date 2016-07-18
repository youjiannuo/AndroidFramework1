package com.yn.framework.data;

import android.annotation.TargetApi;
import android.os.Build;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by youjiannuo on 15/10/31
 */
public class MyGson {

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public <T> T fromJson(String data, Class<?> cls) {
        return (T) getObject(cls, data, null, null);
    }

    public <T> T fromJson(String data, Class<?> cls, List<Class> oldCls, List<Class> replaceCls) {
        return (T) getObject(cls, data, oldCls, replaceCls);
    }


    public <T> List<T> fromJson(String data, Type type) {
        return getList(type, data);
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private Object getObject(Class<?> t, String data, List<Class> cls, List<Class> replaceCls) {
        //修改了
        if (data == null || data.trim().length() == 0 || data.equals("{}")) {
            try {
                return t.newInstance();
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        if (t == String.class) {
            return data;
        }
        JSON json = new JSON(data);
        Object obj = null;
        try {
            obj = t.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        for (Class cl = t; cl != Object.class; cl = cl.getSuperclass()) {
            Field fields[] = cl.getDeclaredFields();
            for (Field field : fields) {
                try {
                    field.setAccessible(true);
                    Object value = getNormalValue(field, json);
                    if (value == null) {
                        if (field.getType() == List.class || field.getType() == ArrayList.class) {
                            value = getList(field, json.getString(field.getName()));
                        } else {
                            Class c = field.getType();
                            if (cls != null && cls.size() != 0 && replaceCls != null && replaceCls.size() != 0) {
                                for (int i = 0; i < cls.size(); i++) {
                                    if (cls.get(i) == c) {
                                        c = replaceCls.get(i);
                                        cls.remove(i);
                                        replaceCls.remove(i);
                                        break;
                                    }
                                }
                            }
                            value = getObject(c, json.getString(field.getName()), cls, replaceCls);
                        }
                    }

                    if (value != null) {
                        field.set(obj, value);
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        return obj;
    }


    private <T> List<T> getList(Field field, String data) {
        if (data == null || data.length() == 0) return null;
        return getList(field.getGenericType(), data);
    }

    private <T> List<T> getList(Type type, String data) {

        Class cls = null;
        if (type instanceof ParameterizedType) {
            cls = (Class) ((ParameterizedType) type).getActualTypeArguments()[0];
        }
        if (cls == null) return new ArrayList<>();
        List<T> list = new ArrayList<>();
        if (data == null || data.equals("{}") || data.length() == 0 || data.equals("null")) {
            return list;
        }
        JSON json = new JSON(data);
        while (json.next()) {
            T t = (T) getObject(cls, json.toString(), null, null);
            //修改了
            if (t != null) {
                list.add(t);
            }
        }
        return list;
    }

    private Object getNormalValue(Field field, JSON json) {
        Type type = field.getType();
        String name = getFiledName(field);
        if (type == (String.class)) {
            return json.getString(name);
        } else if (type == (int.class)) {
            return json.getItemInt(name);
        } else if (type == (Float.class)) {
            return json.getItemFloat(name);
        } else if (type == (long.class)) {
            return json.getItemLong(name);
        } else if (type == (boolean.class)) {
            return json.getItemBoolean(name);
        } else if (type == (double.class)) {
            return json.getItemDouble(name);
        }
        return null;
    }

    public String toJSON(Object obj) {
        if (obj == null) return "";
        if (obj instanceof List) {
            List lists = (List) obj;
            List<JSONObject> jsonObjects = new ArrayList<>();
            for (Object value : lists) {
                jsonObjects.add(toJSONObject(value));
            }
            return new JSONArray(jsonObjects).toString();
        } else {
            JSONObject jsonObject = toJSONObject(obj);
            if (jsonObject == null) return "";
            return jsonObject.toString();
        }

    }


    private JSONObject toJSONObject(Object obj) {
        if (obj == null) {
            try {
                return new JSONObject("{}");
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
        }
        Map<String, Object> map = new HashMap<>();
        for (Class cl = obj.getClass(); cl != Object.class; cl = cl.getSuperclass()) {
            Field fields[] = cl.getDeclaredFields();
            for (Field field : fields) {
                try {
                    field.setAccessible(true);
                    //获取每一个字段的值
                    Object value = field.get(obj);
                    String name = getFiledName(field);
                    if (isObjectPrams(field)) {
                        //这个是参数
                        map.put(name, value);
                    } else {
                        map.put(name, toJSONObject(value));
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        return new JSONObject(map);
    }

    private boolean isObjectPrams(Field field) {
        Type type = field.getType();
        if (type == (String.class)) {
            return true;
        } else if (type == (int.class)) {
            return true;
        } else if (type == (Float.class)) {
            return true;
        } else if (type == (long.class)) {
            return true;
        } else if (type == (boolean.class)) {
            return true;
        } else if (type == (double.class)) {
            return true;
        }
        return false;
    }

    public String getFiledName(Field field) {
        String name = field.getName();
        //读取注解字段
        if (field.isAnnotationPresent(SerializedName.class)) {
            SerializedName inject = field.getAnnotation(SerializedName.class);
            name = inject.value();
        }
        return name;
    }


}

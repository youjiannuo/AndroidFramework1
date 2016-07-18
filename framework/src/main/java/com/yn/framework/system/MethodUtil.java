package com.yn.framework.system;

import android.annotation.TargetApi;
import android.os.Build;

import com.yn.framework.data.SerializedName;
import com.yn.framework.exception.YNException;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

/**
 * Created by youjiannuo on 15/10/28.
 */
public class MethodUtil {

    public static Object invoke(Object obj, String method) {
        return invoke(obj, method, (Object) null);
    }

    public static Object invoke(Object obj, String method, Object... params) {
        Class cl[] = null;
        if (params != null) {
            cl = new Class[params.length];
            for (int i = 0; i < params.length; i++) {
                cl[i] = params[i].getClass();
            }
        }
        return invoke(obj, method, params, cl);
    }

    public static Object invoke(Object obj, String method, Object[] params, Class cl[]) {
        try {
            Method m = obj.getClass().getMethod(method, cl);
            return m.invoke(obj, params);
        } catch (Exception e) {
            e.printStackTrace();
            new YNException(e).throwException();
        }
        return null;
    }


    public static void getParam(Object obj, List<String> keys, List<Object> values) {
        Class t = obj.getClass();
        try {
            for (Class cl = t; cl != Object.class; cl = cl.getSuperclass()) {
                Field fields[] = cl.getDeclaredFields();

                for (Field field : fields) {
                    field.setAccessible(true);
                    keys.add(getFiledName(field));
                    values.add(field.get(obj));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Object getParam(Object object, String method) {
        Class t = object.getClass();
        for (Class cl = t; cl != Object.class; cl = cl.getSuperclass()) {
            try {
                Field field;
                try {
                    field = cl.getDeclaredField(method);
                } catch (NoSuchFieldException e) {
                    continue;
                }
                if (field != null) {
                    field.setAccessible(true);
                    return field.get(object);
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return null;
    }


    public static void getParams(Object obj, List<String> keys, List<String> values) {
        Class t = obj.getClass();
        try {
            for (Class cl = t; cl != Object.class; cl = cl.getSuperclass()) {
                Field fields[] = cl.getDeclaredFields();
                for (Field field : fields) {
                    field.setAccessible(true);
                    keys.add(getFiledName(field));
                    String value = field.get(obj).toString();
                    values.add(value);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static void setParamValue(Object obj, String name, Object value, Class<?> ca) {
        try {

            Field field = ca.getDeclaredField(name);
//            for (Class c = obj.getClass(); c != Object.class; c = c.getSuperclass()) {
//                Field[] fields = c.getDeclaredFields();
//                for (int i = 0; i < fields.length; i++) {
//                    if (fields[i].getName().equals(name)) {
//                        field = fields[i];
//                        break;
//                    }
//                }
//            }

            if (field != null) {
                field.setAccessible(true);
                field.set(obj, value);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private static String getFiledName(Field field) {
        String name = field.getName();
        //读取注解字段
        if (field.isAnnotationPresent(SerializedName.class)) {
            SerializedName inject = field.getAnnotation(SerializedName.class);
            name = inject.value();
        }
        return name;
    }

    public static <T> T newInstance(Class<T> cla, Object[] params, Class<?>... classParams) throws Exception {
        Constructor<T> constructor = cla.getDeclaredConstructor(classParams);
        constructor.setAccessible(true);
        return constructor.newInstance(params);
    }

}

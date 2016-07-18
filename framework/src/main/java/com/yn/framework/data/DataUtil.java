package com.yn.framework.data;


import com.yn.framework.system.StringUtil;

import java.math.RoundingMode;
import java.text.DecimalFormat;

/**
 * Created by youjiannuo on 16/1/26.
 */
public class DataUtil {

    public static int getInt(String s) {
        if (s == null || s.length() == 0) return 0;
        try {
            return Integer.parseInt(s.trim());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static float getFloat(String s) {
        if (s == null || s.length() == 0) return 0;
        try {
            return Float.parseFloat(s.trim());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }


    public static double getDouble(String s) {
        if (s == null || s.length() == 0) return 0;
        try {
            return Double.parseDouble(s.trim());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }


    public static String getMoneyString(String money) {
        return getMoneyString(getMoney(money));
    }

    public static String getMoneyString(double money) {
        DecimalFormat decimalFormat = new DecimalFormat(".00");//构造方法的字符格式这里如果小数不足2位,会以0补足.
        decimalFormat.setMaximumFractionDigits(2);
        decimalFormat.setGroupingSize(0);
        decimalFormat.setRoundingMode(RoundingMode.FLOOR);
        String result = decimalFormat.format(money);//format 返回的是字符串
        if (result.indexOf(".") == 0) {
            return 0 + result;
        }
        
        String s = result.substring(result.length() - 3, result.length());
        String data = result.substring(0, result.length() - 3);
        for (int i = 0; i < data.length(); i++) {
            char c = data.charAt(data.length() - 1 - i);
            if (i % 3 == 0 && i != 0) {
                s = c + "," + s;
            } else {
                s = c + s;
            }
        }
        return s;
    }

    public static double getMoney(String money) {
        if (StringUtil.isEmpty(money)) return 0;
        String result = "";
        for (int i = 0; i < money.length(); i++) {
            char c = money.charAt(i);
            if (c == '.' || (c >= '0' && c <= '9')) {
                result += c;
            }
        }
        return getDouble(result);
    }

    public static float getFloatMoney(String money) {
        int index = money.indexOf(".");
        if (index != -1) {
            String intString = money.substring(0, index);
            String floatString = money.substring(index + 1, money.length());
            return (Integer.parseInt(intString) + (float) (Integer.parseInt(floatString) / Math.pow(10.0f, floatString.length())));
        }
        return Float.parseFloat(money);
    }

    public static double getDoubleString(String result) {
        String s = "";
        if (StringUtil.isEmpty(result)) return 0;
        for (int i = 0; i < result.length(); i++) {
            char c = result.charAt(i);
            if ((c >= '0' && c <= '9') || c == '.') {
                s += c;
            }
        }
        return getDouble(s);
    }

    public static long getLong(String data) {
        try {
            return Long.parseLong(data);
        } catch (Exception e) {

        }
        return 0;
    }

    public static String getBankCard(String text) {
        if (text.length() >= 4) {
            text = text.substring(text.length() - 4, text.length());
        }
        return text;
    }

    public static String getStringTwo(float data) {
        String result = data + "";
        int index = result.indexOf(".");
        if (index > 0) {
            if (result.charAt(index + 2) >= '5') {
                result = (data + (0.01 * (data) / Math.abs(data))) + "";
            }
            return result.substring(0, index + 3);
        }
        return result;
    }

    public static String getNumTwo(String data) {
        int i = data.indexOf(".");
        if (i + 3 >= data.length()) {
            return data.substring(0, data.length());
        }
        String result = data.substring(0, i + 3);
        int a = Integer.parseInt(data.charAt(i + 3) + "");

        if (a >= 5) {
            int b = Integer.parseInt(data.charAt(i + 2) + "");
            return result.substring(0, result.length() - 1) + (b + 1);
        }

        return result;
    }


}

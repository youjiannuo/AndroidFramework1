package com.yn.framework.exception;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.util.Log;

import com.yn.framework.remind.ToastUtil;
import com.yn.framework.system.BuildConfig;
import com.yn.framework.system.ContextManager;
import com.yn.framework.system.SystemUtil;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by youjiannuo on 16/2/15.
 * 程序异常处理的基类
 */
public class YNException {

    public static final String TAG = "YNException";
    //用于格式化日期,作为日志文件名的一部分
    private DateFormat mFormatter = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");

    //用来存储设备信息和异常信息
    private Map<String, String> mInfo = new HashMap<>();

    private Exception mE;

    public YNException(Exception e) {
        mE = e;
    }

    public void throwException() {
        if (!BuildConfig.ENVIRONMENT) {
            try {
                ToastUtil.showNormalMessage("程序出现bug位置:" + toString() + ":  请在文件夹1查看错误日志");
                //保存异常错误到本地文件
                collectDeviceInfo(ContextManager.getContext());
                //保存日志文件
                saveCatchInfo2File(mE);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 保存错误信息到文件中
     *
     * @param ex
     * @return 返回文件名称, 便于将文件传送到服务器
     */
    private String saveCatchInfo2File(Throwable ex) {

        StringBuffer sb = new StringBuffer();
        for (Map.Entry<String, String> entry : mInfo.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            sb.append(key + "=" + value + "\n");
        }

        Writer writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        ex.printStackTrace(printWriter);
        Throwable cause = ex.getCause();
        while (cause != null) {
            cause.printStackTrace(printWriter);
            cause = cause.getCause();
        }
        printWriter.close();
        String result = writer.toString();
        sb.append(result);
        try {
            long timestamp = System.currentTimeMillis();
            String time = mFormatter.format(new Date());
            String fileName = "crash-" + time + "-" + timestamp + ".log";
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                String path = SystemUtil.getSDCardPath() + "/1/";
                File dir = new File(path);
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                File file = new File(path + fileName);
                if (!file.exists()) {
                    file.createNewFile();
                }
                FileOutputStream fos = new FileOutputStream(path + fileName);
                fos.write(sb.toString().getBytes());
                //发送给开发人员
//                sendCrashLog2PM(path + fileName);
                fos.close();
            }
            return fileName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 将捕获的导致崩溃的错误信息发送给开发人员
     * <p/>
     * 目前只将log日志保存在sdcard 和输出到LogCat中，并未发送给后台。
     */
    private void sendCrashLog2PM(String fileName) {

        FileInputStream fis = null;
        BufferedReader reader = null;
        String s = null;
        try {
            fis = new FileInputStream(fileName);
            reader = new BufferedReader(new InputStreamReader(fis, "GBK"));
            while (true) {
                s = reader.readLine();
                if (s == null) break;
                //由于目前尚未确定以何种方式发送，所以先打出log日志。
                Log.i("info", s.toString());
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {   // 关闭流
            try {
                if (reader != null)
                    reader.close();
                if (fis != null)
                    fis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 收集设备参数信息
     *
     * @param ctx
     */
    public void collectDeviceInfo(Context ctx) {
        try {
            PackageManager pm = ctx.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(ctx.getPackageName(), PackageManager.GET_ACTIVITIES);
            if (pi != null) {
                String versionName = pi.versionName == null ? "null" : pi.versionName;
                String versionCode = pi.versionCode + "";
                mInfo.put("versionName", versionName);
                mInfo.put("versionCode", versionCode);
            }
        } catch (PackageManager.NameNotFoundException e) {
            Log.e(TAG, "an error occured when collect package info", e);
        }
        Field[] fields = Build.class.getDeclaredFields();
        for (Field field : fields) {
            try {
                field.setAccessible(true);
                mInfo.put(field.getName(), field.get(null).toString());
                Log.d(TAG, field.getName() + " : " + field.get(null));
            } catch (Exception e) {
                Log.e(TAG, "an error occured when collect crash info", e);
            }
        }
    }

    @Override
    public String toString() {
        String name = getClass().getName();
        int i = name.lastIndexOf(".");
        if (i < 0) return "exception";
        name = name.substring(i + 1, name.length());
        return name.substring(2, name.length() - 9);
    }
}

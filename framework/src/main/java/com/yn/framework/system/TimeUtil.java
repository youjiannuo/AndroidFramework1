package com.yn.framework.system;

import android.os.Looper;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by youjiannuo on 16/1/13.
 */
public class TimeUtil {

    private long mTime = 0;


    public static String getMonth() {
        return getString(new Date().getMonth() + 1);
    }

    public static String getDate() {
        return getString(new Date().getDate());
    }

    public static String getString(int value) {
        String s = value + "";
        if (s.length() == 2) return s;
        return "0" + s;
    }


    public static String getTime(String time) {
        Date date = getDate(time);
        return getString(date.getMonth() + 1) + "." + getString(date.getDate()) + " " + getString(date.getHours()) + ":" + getString(date.getMinutes()) + ":" + getString(date.getSeconds());
    }

    public static String getMonthAndDate(String time) {
        Date date = getDate(time);
        return getString(date.getMonth() + 1) + "月" + getString(date.getDate()) + "日";
    }

    public static String getTime(String time, String format) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        return simpleDateFormat.format(getDate(time));
    }

    public static Date getDate(String time) {
        if (StringUtil.isEmpty(time)) {
            return new Date();
        }
        long value;
        try {
            value = Long.parseLong(time) * 1000;
        } catch (Exception e) {
            e.printStackTrace();
            return new Date();
        }
        return new Date(value);
    }

    public static boolean isSameMonth(Date date1, Date date) {
        return date1.getYear() == date.getYear() && date1.getMonth() == date.getMonth();
    }

    public static boolean isSameDay(Date date1, Date date2) {
        return isSameMonth(date1, date2) && date1.getDate() == date2.getDate();
    }

    public static String getMonthAndDay(String time) {
        Date date = getDate(time);
        return getString(date.getMonth() + 1) + "." + getString(date.getDate());
    }

    public static Timer startTimer(final int startTime, long time, long delay, final OnTimeListener l) {
        final Timer timer = new Timer(true);
        TimerTask task = new TimerTask() {
            int i = startTime;

            @Override
            public void run() {
                if (l != null) {
                    new android.os.Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            if (l.onTime(++i)) {
                                timer.cancel();
                            }
                        }
                    });
                }
            }
        };
        timer.scheduleAtFixedRate(task, delay, time);
        return timer;
    }

    public boolean checkoutTime(long runTime) {
        long a = new Date().getTime();
        boolean is = a - mTime > runTime;
        mTime = a;
        return is;
    }


    public interface OnTimeListener {
        boolean onTime(int index);
    }

    public static Date paraseDate(String time, String pattren) {
        Date date;
        if (StringUtil.isEmpty(time)) {
            return new Date();
        }
        try {
            date = new SimpleDateFormat(pattren).parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
            return new Date();
        }
        return date;
    }

}

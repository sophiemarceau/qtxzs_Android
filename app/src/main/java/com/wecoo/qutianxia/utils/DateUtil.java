package com.wecoo.qutianxia.utils;

import android.content.Context;

import com.wecoo.qutianxia.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by mwl on 2016/10/19.
 * 日期统一管理类
 */
public class DateUtil {
    public static final String FORMAT_DEFAULT = "yyyy-MM-dd";
    public static final String FORMAT_CHINESE = "yyyy年MM月dd日";
    public static final String DATE_FULL_STR = "yyyy-MM-dd HH:mm";
    private static final long TEN_MINUTES = 10 * 60 * 1000;
    public static final long ONE_DAY = 24 * 60 * 60 * 1000;

    /**
     * 客户端和服务器之间的系统时间差值
     */
    private static long TIME_DIFFERENCE = 0;

    public static long getFutureTime(long day) {
        Date d = new Date();
        long time = d.getTime();
        day = day * ONE_DAY;
        time += day;
        return time;
    }

    /**
     * 设置客户端和服务端系统时间差值
     *
     * @param servertime 服务器系统时间
     */
    public static void setTimeDifference(long servertime) {
        TIME_DIFFERENCE = System.currentTimeMillis() - servertime;
        // 服务器时间会缓存10分钟，如果误差在10分钟以内，以客户端时间为准
        if (TIME_DIFFERENCE > 0 && TIME_DIFFERENCE <= TEN_MINUTES) {
            TIME_DIFFERENCE = 0;
        }
        LogUtil.d("time", "setTimeDifference TIME_DIFFERENCE: " + TIME_DIFFERENCE);
    }

    public static String formatShortVideoDuration(int duration) {
        SimpleDateFormat df = new SimpleDateFormat("mm : ss", Locale.CHINA);
        Date date = new Date(duration);
        return df.format(date);
    }

    public static String formatMovieVideoDuration(long duration) {
        SimpleDateFormat df = new SimpleDateFormat("MM月dd日  HH:mm", Locale.CHINA);
        df.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        Date date = new Date(duration * 1000);
        return df.format(date);
    }

    public static String formatTime(long duration, String pattern) {
        SimpleDateFormat df = new SimpleDateFormat(pattern, Locale.CHINA);
        df.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        Date date = new Date(duration * 1000);
        return df.format(date);
    }

    private static String unitFormat(int i) {
        String retStr = null;
        if (i >= 0 && i < 10)
            retStr = "0" + Integer.toString(i);
        else
            retStr = "" + i;
        return retStr;
    }

    public static String formaShortVideoDuration3(int duration) {
        String timeStr = null;
        int hour = 0;
        int minute = 0;
        int second = 0;
        minute = duration / 60;
        if (minute < 60) {
            second = duration % 60;
            timeStr = unitFormat(minute) + ":" + unitFormat(second);
        } else {
            hour = minute / 60;
            if (hour > 99)
                return "99:59:59";
            minute = minute % 60;
            second = duration - hour * 3600 - minute * 60;
            timeStr = unitFormat(hour) + ":" + unitFormat(minute) + ":" + unitFormat(second);
        }
        return timeStr;

    }

    public static String formatShortVideoDuration2(long duration) {
        SimpleDateFormat df = new SimpleDateFormat("hh : mm : ss", Locale.CHINA);
        Date date = new Date(duration);
        return df.format(date);
    }

    public static boolean isSameDay(long date1, long date2) {
        Calendar local = Calendar.getInstance();
        local.setTimeInMillis(date1);
        Calendar other = Calendar.getInstance();
        other.setTimeInMillis(date2);
        return local.get(Calendar.YEAR) == other.get(Calendar.YEAR)
                && local.get(Calendar.MONTH) == other.get(Calendar.MONTH)
                && local.get(Calendar.DAY_OF_MONTH) == other.get(Calendar.DAY_OF_MONTH);
    }

    public static long getLastDayDate(long date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(date);
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        return calendar.getTimeInMillis();
    }

    public static long getAfterDayDate(long date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(date);
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        return calendar.getTimeInMillis();
    }

    @SuppressWarnings("deprecation")
    public static String formaterTime(long timestamp) {
        Date now = new Date();
        Date time = new Date(timestamp);
        int year = now.getYear();
        if (year == time.getYear()) {
            SimpleDateFormat formater = new SimpleDateFormat("MM月dd日", Locale.CHINA);
            return formater.format(time);
        }
        SimpleDateFormat formater = new SimpleDateFormat("yyyy年MM月dd日", Locale.CHINA);
        return formater.format(time);
    }

    public static String formatDate(long time, String format) {
        Date date = new Date(time);
        SimpleDateFormat df = new SimpleDateFormat(format, Locale.CHINA);
        return df.format(date);
    }

    public static String getCurrentDate() {
        return getCurrentDate("MM-dd-yyyy");
    }

    public static String getCurrentDate(String format) {
        SimpleDateFormat df = new SimpleDateFormat(format, Locale.CHINA);
        Date today = new Date();
        return df.format(today);
    }

    /**
     * 获取当前日期，已和服务器做过差值处理。即获取服务器当前时间
     *
     * @return 当前日期。
     */
    public static String getCurrentDateInServer(String format) {
        SimpleDateFormat df = new SimpleDateFormat(format, Locale.CHINA);
        Date today = new Date(getCurrentTimestampInServer());
        return df.format(today);
    }

    /**
     * 刷新时间
     **/
    public static String getRefreshDate() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss", Locale.CHINA);
        Date today = new Date();
        return df.format(today);
    }

    public static String getCurrentDateToSecond() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
        Date today = new Date();
        return df.format(today);
    }

    public static String formatDateToString(long time) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
        Date today = new Date(time);
        return df.format(today);
    }

    public static long getCurrentTime() {
        return System.currentTimeMillis();
    }

    /**
     * 将当前日期转换成Unix时间戳
     *
     * @return long 时间戳
     */
    public static long getCurrentTimestamp() {
        long timestamp = new Date().getTime();
        System.currentTimeMillis();
        return Long.parseLong(String.valueOf(timestamp).substring(0, 10));
    }

    /**
     * 将当前日期时间戳，已和服务器做过时间差值处理。
     *
     * @return long 时间戳
     */
    public static long getCurrentTimestampInServer() {
        return System.currentTimeMillis() - TIME_DIFFERENCE;
    }

    /**
     * 获取已经和服务器时间校对过的时间戳。
     *
     * @return long 时间戳
     */
    public static long getProofreadTimestamp(long timeStamp) {
        return timeStamp + TIME_DIFFERENCE;
    }

    public static String formatVideoDuration(int duration) {
        SimpleDateFormat mDurationFormat = new SimpleDateFormat("H:mm:ss", Locale.CHINA);
        mDurationFormat.setTimeZone(TimeZone.getTimeZone("GMT+00:00"));
        Date date = new Date(duration);
        return mDurationFormat.format(date);
    }

    public static String formatBloackVideoDuration(long duration) {
        SimpleDateFormat mDurationFormat = new SimpleDateFormat("mm分ss秒", Locale.CHINA);
        mDurationFormat.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        Date date = new Date(duration);
        return mDurationFormat.format(date);
    }

    /**
     * 将指定的日期转换成Unix时间戳
     *
     * @param date 需要转换的日期 yyyy-MM-dd HH:mm:ss
     * @return long 时间戳
     */
    public static long dateToUnixTimestamp(String date) {
        return dateToUnixTimestamp(date, DATE_FULL_STR);
    }

    /**
     * 将指定的日期转换成Unix时间戳
     *
     * @param date 需要转换的日期 yyyy-MM-dd
     * @return long 时间戳
     */
    public static long dateToUnixTimestamp(String date, String dateFormat) {
        long timestamp = 0;
        try {
            // timestamp = new SimpleDateFormat(dateFormat,
            // Locale.CHINA).parse(date).getTime();
            SimpleDateFormat sd = new SimpleDateFormat(dateFormat, Locale.CHINA);
            sd.setTimeZone(TimeZone.getTimeZone("GMT+8"));
            return sd.parse(date).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return timestamp;
    }

    /**
     * 将Unix时间戳转换成日期
     *
     * @param timestamp 时间戳
     * @return String 日期字符串
     */
    public static String unixTimestampToDate(long timestamp) {
        SimpleDateFormat sd = new SimpleDateFormat(DATE_FULL_STR, Locale.CHINA);
        sd.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        return sd.format(new Date(timestamp));
    }
    /**
     * 将Unix时间戳转换成日期
     */
    public static String unixToDate(long timestamp) {
        SimpleDateFormat sd = new SimpleDateFormat(DATE_FULL_STR, Locale.CHINA);
        sd.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        return sd.format(new Date(timestamp * 1000));
    }
    /**
     * 取得指定日期是周几
     */
    public static String getStrDayOfWeek(String strDate) {
        Date date = parseDate(strDate);
        int dayOfWeek = getDayOfWeek(date);
        return parseDayOfWeek(dayOfWeek);
    }

    public static String formatDateToWeek(long date) {
        return parseDayOfWeek(getDayOfWeek(new Date(date)));
    }

    /**
     * 取得指定日期是周几
     *
     * @param date
     * @return
     */
    public static int getDayOfWeek(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        int day_of_week = c.get(Calendar.DAY_OF_WEEK);
        return day_of_week;
    }

    /**
     * 根据小时，分钟，秒，获取对应的时间
     *
     * @param hour
     * @param minute
     * @param second
     * @return
     */
    public static long getTimeByHour(int hour, int minute, int second) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, hour);
        cal.set(Calendar.MINUTE, minute);
        cal.set(Calendar.SECOND, second);
        return cal.getTimeInMillis();
    }

    /**
     * @param strDate
     * @return
     */
    private static Date parseDate(String strDate) {
        return parseDate(strDate, null);
    }

    /**
     * parseDate
     *
     * @param strDate
     * @param pattern
     * @return
     */
    private static Date parseDate(String strDate, String pattern) {
        Date date = null;
        try {
            if (pattern == null) {
                pattern = FORMAT_DEFAULT;
            }
            SimpleDateFormat format = new SimpleDateFormat(pattern, Locale.CHINA);
            date = format.parse(strDate);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return date;
    }

    private static String parseDayOfWeek(int day) {
        switch (day) {
            case 1:
                return "周日";
            case 2:
                return "周一";
            case 3:
                return "周二";
            case 4:
                return "周三";
            case 5:
                return "周四";
            case 6:
                return "周五";
            case 7:
                return "周六";
        }
        return "";
    }

    public static String timeWithYearMothDate(Context context, long timemess) {
        String today = context.getString(R.string.today);
        String yestoday = context.getString(R.string.yestoday);
//		if (TextUtils.isEmpty(messtime)) {
//			messtime = "0";
//		}
        Date date = new Date();
        long timenow = date.getTime();
        String showdate = "";
        try {
//			Long timemess = Long.parseLong(messtime);
            String year = new SimpleDateFormat("yyyy").format(new Date(timemess));
            String time = new SimpleDateFormat("HH:mm").format(new Date(timemess));
            String yearnow = new SimpleDateFormat("yyyy").format(new Date(timenow));
            String yearmess = new SimpleDateFormat("yyyy").format(new Date(timemess));
            String monthnow = new SimpleDateFormat("MM").format(new Date(timenow));
            String monthmess = new SimpleDateFormat("MM").format(new Date(timemess));
            String daynow = new SimpleDateFormat("dd").format(new Date(timenow));
            String daymess = new SimpleDateFormat("dd").format(new Date(timemess));
            String week = new SimpleDateFormat("E").format(new Date(timemess));
            int nowyear = Integer.parseInt(yearnow);
            int messyear = Integer.parseInt(yearmess);
            int nowmonth = Integer.parseInt(monthnow);
            int messmonth = Integer.parseInt(monthmess);
            int nowday = Integer.parseInt(daynow);
            int messday = Integer.parseInt(daymess);

            if (nowyear != messyear) {
                showdate = year + "-" + messmonth + "-" + messday + " " + time;
            } else if ((nowmonth == messmonth) && (nowday == messday)) {
                showdate = today + " " + time;
            } else if ((nowmonth == messmonth) && (nowday - messday == 1)) {
                showdate = yestoday + " " + time;
            } else if ((nowmonth == messmonth) && (nowday - messday >= 2) && (nowday - messday <= 5)) {
                showdate = week + " " + time;
            } else {
                showdate = year + "-" + messmonth + "-" + messday + " " + time;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return showdate;
    }

    public static String returnMMDDFormat(long time) {
        if (time > 0) {
            Date date = new Date(time * 1000);
            String MMdd = new SimpleDateFormat("MM-dd HH:mm", Locale.CHINA).format(date);
            return MMdd.replaceAll("^(0+)", "");
        }
        return "";
    }

    /**
     * 判断指定时间是否是今天
     *
     * @param time
     * @return
     */
    public static boolean isToday(long time) {
        Calendar current = Calendar.getInstance();
        Calendar today = getDay(current.get(Calendar.DAY_OF_MONTH));
        current.setTime(new Date(time));
        return current.after(today);
    }

    /**
     * 判断制定之间是不是昨天
     *
     * @param time
     * @return
     */
    public static boolean isYesterday(long time) {
        Calendar current = Calendar.getInstance();

        Calendar today = getDay(current.get(Calendar.DAY_OF_MONTH));
        Calendar yesterday = getDay(current.get(Calendar.DAY_OF_MONTH) - 1);

        current.setTime(new Date(time));
        return current.before(today) && current.after(yesterday);
    }

    private static Calendar getDay(int day) {
        Calendar current = Calendar.getInstance();
        Calendar calendar = Calendar.getInstance();    //昨天
        calendar.set(Calendar.YEAR, current.get(Calendar.YEAR));
        calendar.set(Calendar.MONTH, current.get(Calendar.MONTH));
        calendar.set(Calendar.DAY_OF_MONTH, day);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        return calendar;
    }
}

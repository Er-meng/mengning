package cn.mn.mn;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {
    private final static int ZONE_OFFSET = 8;
    private final static String FORMAT = "yyyy-MM-dd'T'HH:mm:ss";

    /**
     * 获取当前时间的标准XML时间字符串
     *
     * @return
     */
    public static String getCurrentDatetime() {
        return dateTimeToString(LocalDateTime.now());
    }

    /**
     * LocalDateTime转标准XML时间字符串
     *
     * @param localDateTime
     * @return
     */
    public static String dateTimeToString(LocalDateTime localDateTime) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(FORMAT);
        return localDateTime.format(dateTimeFormatter);
    }

    /**
     * 标准XML时间字符串转LocalDateTime
     *
     * @param localDateTime
     * @return
     */
    public static LocalDateTime stringToDateTime(String localDateTime) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(FORMAT);
        return LocalDateTime.parse(localDateTime, dateTimeFormatter);
    }

    /**
     * LocalDate转Date
     *
     * @param localDate
     * @return
     */
    public static Date localDateToDate(LocalDate localDate) {
        return Date.from(localDate.atStartOfDay(ZoneOffset.ofHours(ZONE_OFFSET)).toInstant());
    }

    /**
     * Date转LocalDate
     *
     * @param date
     * @return
     */
    public static LocalDate dateToLocalDate(Date date) {
        return date.toInstant().atZone(ZoneOffset.ofHours(ZONE_OFFSET)).toLocalDate();
    }

    /**
     * LocalDateTime转Date
     *
     * @param localDateTime
     * @return
     */
    public static Date localDateTimeToDate(LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(ZoneOffset.ofHours(ZONE_OFFSET)).toInstant());
    }

    /**
     * Date转LocalDateTime
     *
     * @param date
     * @return
     */
    public static LocalDateTime dateToLocalDateTime(Date date) {
        return date.toInstant().atZone(ZoneOffset.ofHours(ZONE_OFFSET)).toLocalDateTime();
    }

    /**
     * LocalDate转时间戳
     *
     * @param localDate
     * @return
     */
    public static long localDateToTimestamp(LocalDate localDate) {
        return localDate.atStartOfDay(ZoneOffset.ofHours(ZONE_OFFSET)).toInstant().toEpochMilli();
    }

    /**
     * 时间戳转LocalDate
     *
     * @param timestamp
     * @return
     */
    public static LocalDate timestampToLocalDate(long timestamp) {
        return Instant.ofEpochMilli(timestamp).atZone(ZoneOffset.ofHours(ZONE_OFFSET)).toLocalDate();
    }

    /**
     * LocalDateTime转时间戳
     *
     * @param localDateTime
     * @return
     */
    public static long localDateTimeToTimestamp(LocalDateTime localDateTime) {
        return localDateTime.toInstant(ZoneOffset.ofHours(ZONE_OFFSET)).toEpochMilli();
    }

    /**
     * 时间戳转LocalDateTime
     *
     * @param timestamp
     * @return
     */
    public static LocalDateTime timestampToLocalDateTime(long timestamp) {
        return Instant.ofEpochMilli(timestamp).atZone(ZoneOffset.ofHours(8)).toLocalDateTime();
    }

    /**
     * 字符串转化为时间格式
     * @param dateStr 时间字符串
     * @param patten 格式化方式。如"yyyy-MM-dd"
     * @return
     */
    public static Date stringToDate(String dateStr, String patten) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(patten);
            return sdf.parse(dateStr);
        } catch (Exception e) {
            return new Date();
        }

    }

    /**
     * 将时间转日期
     * @param date
     * @param patten
     * @return
     */
    public static String dateToString(Date date, String patten) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(patten);
            return sdf.format(date);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 判断是否同一天
     * @param date1
     * @param date2
     * @return
     */
    public static boolean isSameDay(Date date1, Date date2) {
        if(date1 != null && date2 != null) {
            Calendar cal1 = Calendar.getInstance();
            cal1.setTime(date1);
            Calendar cal2 = Calendar.getInstance();
            cal2.setTime(date2);
            return cal1.get(0) == cal2.get(0) && cal1.get(1) == cal2.get(1) && cal1.get(6) == cal2.get(6);
        } else {
            return false;
        }
    }

    public static Integer getYear(Date date){
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.YEAR);
    }
    public static Integer getMonth(Date date){
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.MONTH) + 1;
    }
    public static Integer getDay(Date date){
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.DAY_OF_MONTH);
    }
}

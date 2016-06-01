package util;

import java.sql.Time;

public class TimeUtil {
    public static final long TIME_OFFSET = 7200000;
    public static final String TIME_FORMAT = "hh:mm:ss";
    private static final long millisInSecond = 1000;
    private static final long millisInMinute = millisInSecond * 60;
    private static final long millisInHour = millisInMinute * 60;
    private static final long millisInDay = millisInHour * 24;

    public static String toString(Time time) {
        if (time == null) {
            return "";
        }
        return time.toString();
    }

    public static Time fromString(String string) {
        return Time.valueOf(string);
    }

    public static String toStringSpecialFormat(long time) {
        long days = time / millisInDay;
        time -= days * millisInDay;
        long hours = time / millisInHour;
        time -= hours * millisInHour;
        long minutes = time / millisInMinute;
        return days + " днів " + hours + " год. " + minutes + " хв.";
    }
}

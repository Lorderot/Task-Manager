package util;

import java.sql.Time;

public class TimeUtil {
    public static final String TIME_FORMAT = "hh:mm:ss";

    public static String toString(Time time) {
        if (time == null) {
            return "";
        }
        return time.toString();
    }

    public static Time fromString(String string) {
        return Time.valueOf(string);
    }
}

package util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {
    public static final String DATE_FORMAT = "yyyy-MM-dd";
    private static final SimpleDateFormat parser
            = new SimpleDateFormat(DATE_FORMAT);
    public static String toString(Date date) {
        if (date == null) {
            return "";
        }
        return parser.format(date);
    }

    public static Date fromString(String string) throws ParseException{

        return parser.parse(string);
    }
}

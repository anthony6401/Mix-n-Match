package bot.utility;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateTime {
    public static final int SECONDS_TO_MILLISECONDS = 1000;
    public static final int MINUTES_TO_SECONDS = 60;
    public static String unixTimeToDate(Integer unixTime) {
        Date date = new java.util.Date(unixTime*1000L);
        SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        sdf.setTimeZone(java.util.TimeZone.getTimeZone("GMT+8"));
        String formattedDate = sdf.format(date);
        return formattedDate;
    }
}

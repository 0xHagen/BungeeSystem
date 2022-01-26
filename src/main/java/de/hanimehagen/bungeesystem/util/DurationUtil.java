package de.hanimehagen.bungeesystem.util;

import de.hanimehagen.bungeesystem.Data;

import java.sql.Timestamp;

public class DurationUtil {

    public static long getDuration(String str) {
        if(str.equals("-1")) {
            return Long.parseLong(str);
        } else if(str.endsWith("h")) {
            return parseDuration(str) * 60 * 60 * 1000;
        } else if(str.endsWith("d")) {
            return parseDuration(str) * 24 * 60 * 60 * 1000;
        } else if(str.endsWith("m")) {
            return parseDuration(str) * 30 * 24 * 60 * 60 * 1000;
        } else if(str.endsWith("y")) {
            return parseDuration(str) * 365 * 24 * 60 * 60 * 1000;
        }
        else return 0;
    }

    public static String getDurationString(String str) {
        if(str.equals("-1")) {
            return "Permanent";
        } else if(str.endsWith("h")) {
            return str.replace("h", "") + " Hours";
        } else if(str.endsWith("d")) {
            return str.replace("d", "") + " Days";
        } else if(str.endsWith("m")) {
            return str.replace("m", "") + " Months";
        } else if(str.endsWith("y")) {
            return str.replace("y", "") + " Years";
        }
        return "";
    }

    public static long parseDuration(String str) {
        String result = str.substring(0, str.length() - 1);
        return Long.parseLong(result);
    }

    public static String getEndDurationString(long end) {
        String endString;
        if(end == -1) {
            endString = DurationUtil.getDurationString(String.valueOf(end));
        } else {
            endString = Data.DATE_FORMAT.format(new Timestamp(end));
        }
        return endString;
    }

}

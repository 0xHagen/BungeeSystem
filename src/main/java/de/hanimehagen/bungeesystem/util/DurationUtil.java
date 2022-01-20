package de.hanimehagen.bungeesystem.util;

public class DurationUtil {

    //bei command execute checken, ob id in config existiert

    public static long getDuration(String str) {
        if(str.equals("-1")) {
            return Long.parseLong(str);
        } else if(str.endsWith("h")) {
            return parseDuration(str) * 60;
        } else if(str.endsWith("d")) {
            return parseDuration(str) * 24 * 60;
        } else if(str.endsWith("m")) {
            return parseDuration(str) * 30 * 24 * 60;
        } else if(str.endsWith("y")) {
            return parseDuration(str) * 365 * 24 * 60;
        }
        else return 0;
    }

    public static long parseDuration(String str) {
        String result = null;
        if ((str != null) && (str.length() > 0)) {
            result = str.substring(0, str.length() - 1);
        }
        return Long.parseLong(result);
    }

}

package com.example.a202sgi_jess_ong;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class NoteUtils {

    private static final int SECOND_MILLIS = 1000;
    private static final int MINUTE_MILLIS = 60*SECOND_MILLIS;
    private static final int HOUR_MILLIS = 60*MINUTE_MILLIS;
    private static final int DAY_MILLIS = 24*HOUR_MILLIS;

    private static DateFormat date = new SimpleDateFormat("hh:mm aaa, dd/MMM/yyyy", Locale.US);

    public static String dateFromLong(long time){
        if (time<100000000000L){
            time *= 1000;
        }
        long now = System.currentTimeMillis();

        final long diff = now - time;

        if (diff < MINUTE_MILLIS){
            return "Just now";
        } else if (diff >= MINUTE_MILLIS && diff < DAY_MILLIS) {
            if (diff < HOUR_MILLIS) {
                return diff / MINUTE_MILLIS + " minutes ago";
            } else if (diff < 2 * HOUR_MILLIS) {
                return "one hour ago";
            } else {
                return diff / HOUR_MILLIS + " hours ago";
            }
        } else {
            return date.format(new Date(time));
        }

    }
}

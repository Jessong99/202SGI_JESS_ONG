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
    private static final int WEEK_MILLIS = 7*DAY_MILLIS;

    public static String dateFromLong(long time){
        if (time<100000000000L){
            time *= 1000;
        }
        long now = System.currentTimeMillis();
        DateFormat week = new SimpleDateFormat("EEEEE", Locale.US);
        DateFormat date = new SimpleDateFormat("dd/MMM/yyyy", Locale.US);
        DateFormat today = new SimpleDateFormat("hh:mm aaa", Locale.US);

        final long diff = now - time;

        if (diff < MINUTE_MILLIS){
            return "Just now";
        }else if (diff >= MINUTE_MILLIS && diff < DAY_MILLIS){
            return today.format(new Date(time));
        }else if (diff >= DAY_MILLIS && diff < 2 * DAY_MILLIS){
            return "Yesterday";
        }else if (diff >= 2 * DAY_MILLIS && diff < WEEK_MILLIS){
            return week.format(new Date(time));
        }else{
            return date.format(new Date(time));
        }

    }
}

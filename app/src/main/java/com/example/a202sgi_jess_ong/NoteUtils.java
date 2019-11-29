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

    private static DateFormat day = new SimpleDateFormat("dd", Locale.US);
    private static DateFormat month = new SimpleDateFormat("MMM", Locale.US);
    private static DateFormat year = new SimpleDateFormat("dd", Locale.US);

    private static DateFormat week = new SimpleDateFormat("EEEE", Locale.US);
    private static DateFormat date = new SimpleDateFormat("dd/MMM/yyyy", Locale.US);
    private static DateFormat today = new SimpleDateFormat("hh:mm aaa", Locale.US);

    public static String dateFromLong(long time){
        if (time<100000000000L){
            time *= 1000;
        }
        long now = System.currentTimeMillis();

        int currentD = Integer.valueOf(day.format(new Date(now)));
        int noteD = Integer.valueOf(day.format(new Date(time)));
        String currentM = month.format(new Date(now));
        String noteM = month.format(new Date(time));
        int currentY = Integer.valueOf(year.format(new Date(now)));
        int noteY = Integer.valueOf(year.format(new Date(time)));

        final long diff = now - time;

        if (diff < MINUTE_MILLIS){
            return "Just now";
        }else if (diff >= MINUTE_MILLIS && currentD == noteD && currentM == noteM && currentY == noteY){
            return today.format(new Date(time));
        }else if (diff >= DAY_MILLIS && diff < WEEK_MILLIS){
            return week.format(new Date(time));
        }else{
            return date.format(new Date(time));
        }

    }
}

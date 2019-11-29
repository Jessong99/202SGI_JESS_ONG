package com.example.a202sgi_jess_ong;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class NoteUtils {

    private static DateFormat date = new SimpleDateFormat("dd/MMM/yyyy", Locale.US);
    private static DateFormat today = new SimpleDateFormat("hh:mm aaa", Locale.US);

    private static DateFormat timescale = new SimpleDateFormat("HHmm", Locale.US);
    private static DateFormat day = new SimpleDateFormat("dd", Locale.US);
    private static DateFormat month = new SimpleDateFormat("MMM", Locale.US);
    private static DateFormat year = new SimpleDateFormat("dd", Locale.US);

    public static String dateFromLong(long time){
        long now = System.currentTimeMillis();

        int currentT = Integer.valueOf(timescale.format(new Date(now)));
        int noteT = Integer.valueOf(timescale.format(new Date(time)));
        int currentD = Integer.valueOf(day.format(new Date(now)));
        int noteD = Integer.valueOf(day.format(new Date(time)));
        String currentM = month.format(new Date(now));
        String noteM = month.format(new Date(time));
        int currentY = Integer.valueOf(year.format(new Date(now)));
        int noteY = Integer.valueOf(year.format(new Date(time)));

        if (currentD == noteD && currentM == noteM && currentY == noteY && ((currentT-noteT)<2)){
            return "Just now";
        }else if (currentD == noteD && currentM == noteM && currentY == noteY){
            return today.format(new Date(time));
        }else if (currentM == noteM && currentY == noteY && ((currentD-noteD)<2)){
            return "Yesterday";
        }else{
            return date.format(new Date(time));
        }

    }
}

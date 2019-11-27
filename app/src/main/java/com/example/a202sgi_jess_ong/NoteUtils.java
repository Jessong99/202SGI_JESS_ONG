package com.example.a202sgi_jess_ong;

public class NoteUtils {

// TODO: 28-Nov-19 delete or not 
    /*public static String dateFromLong(long time){
        DateFormat format = new SimpleDateFormat("EEE, dd MMM yyyy 'at' hh:mm aaa", Locale.US);
        return format.format(new Date(time));
    }*/

    private static final int SECOND_MILLIS = 1000;
    private static final int MINUTE_MILLIS = 60*SECOND_MILLIS;
    private static final int HOUR_MILLIS = 60*MINUTE_MILLIS;
    private static final int DAY_MILLIS = 24*HOUR_MILLIS;


    public static String dateFromLong(long time){
        if (time<100000000000L){
            time *= 1000;
        }
        long now = System.currentTimeMillis();

        if (time > now || time <= 0){
            return null;
        }

        final long diff = now - time;
        if (diff < MINUTE_MILLIS){
            return "Just now";
        }else if (diff < 2 * MINUTE_MILLIS){
            return "A minute ago";
        }else if (diff < 50 * MINUTE_MILLIS){
            return (diff / MINUTE_MILLIS + " minutes ago");
        }else if (diff < 90 * MINUTE_MILLIS){
            return "A hour ago";
        }else if (diff < 24 * HOUR_MILLIS){
            return (diff / HOUR_MILLIS + " hours ago");
        }else if (diff < 48 * HOUR_MILLIS){
            return "Yesterday";
        }else{
            return diff / DAY_MILLIS + " days ago";
        }

    }
}

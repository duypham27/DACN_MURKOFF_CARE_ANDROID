package com.example.dacn_murkoff_care_android.Helper;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.DisplayMetrics;

import com.example.dacn_murkoff_care_android.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

public class Tooltip {
    /** NOTE:
     * Return String today with format yyyy-MM-dd.
     * For example, 2022-05-01
     **/
    public static String getToday()
    {
        TimeZone timeZone = TimeZone.getTimeZone("Asia/Ho_Chi_Minh");
        Calendar calendar = Calendar.getInstance(timeZone);

        int date = calendar.get(Calendar.DATE);
        int month = calendar.get(Calendar.MONTH) + 1;
        int year = calendar.get(Calendar.YEAR);

        String dateValue = String.valueOf(date);
        if( date < 10)
        {
            dateValue = "0" + dateValue;
        }
        String monthValue = String.valueOf(month);
        if( month < 10)
        {
            monthValue = "0" + monthValue;
        }
        String yearValue = String.valueOf(year);

        return yearValue + "-" + monthValue + "-" + dateValue;
    }

    /** NOTE:
     * Lấy ra ngày tháng năm theo cách viết tiếng việt
     **/
    public static String getReadableToday(Context context)
    {
        TimeZone timeZone = TimeZone.getTimeZone("Asia/Ho_Chi_Minh");
        Calendar calendar = Calendar.getInstance(timeZone);

        int day = calendar.get(Calendar.DAY_OF_WEEK);
        int date = calendar.get(Calendar.DATE);
        int month = calendar.get(Calendar.MONTH) + 1;
        int year = calendar.get(Calendar.YEAR);

        /*Dịch giá trị sang ngôn ngữ viết*/
        String dayValue = context.getString(R.string.monday);
        String monday = context.getString(R.string.monday);
        String tuesday = context.getString(R.string.tuesday);
        String wednesday = context.getString(R.string.wednesday);
        String thursday = context.getString(R.string.thursday);
        String friday = context.getString(R.string.friday);
        String saturday = context.getString(R.string.saturday);
        String sunday = context.getString(R.string.sunday);
        switch (day)
        {
            case 2:
                dayValue = monday;
                break;
            case 3:
                dayValue = tuesday;
                break;
            case 4:
                dayValue = wednesday;
                break;
            case 5:
                dayValue = thursday;
                break;
            case 6:
                dayValue = friday;
                break;
            case 7:
                dayValue = saturday;
                break;
            case 1:
                dayValue = sunday;
                break;
        }

        /* Thêm số 0 phía trước cho ngày & tháng nhỏ hơn 10 */
        String dateValue = String.valueOf(date);
        if( date < 10)
        {
            dateValue = "0" + dateValue;
        }
        String monthValue = String.valueOf(month);
        if( month < 10)
        {
            monthValue = "0" + monthValue;
        }
        String yearValue = String.valueOf(year);

        /*Trả kết quả về*/
        return  dayValue + ", "+ dateValue + "/" +  monthValue   + "/" + yearValue;
    }

    /** NOTE:
     * Return String beautifier datetime
     * For instance, 2022-11-24 09:57:53 => 09:57 T5, 24-11-2022
     **/
    @SuppressLint("SimpleDateFormat")
    public static String beautifierDatetime(Context context, String input)
    {
        if( input.length() != 19)
        {
            return "Tooltip - beautifierDatetime - error: value is not valid " + input.length();
        }
        String output = "";

        String dateInput = input.substring(0,10);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

        String dateOutput = "";
        DateFormat dateFormat = new SimpleDateFormat("EE, dd-MM-yyyy");
        try
        {
            Date dateFormatted = formatter.parse(dateInput);
            assert dateFormatted != null;
            dateOutput = dateFormat.format(dateFormatted);
        }
        catch (ParseException e)
        {
            e.printStackTrace();
        }

        String timeOutput = input.substring(11, 16);

        output = dateOutput + " " +context.getString(R.string.at) + " " + timeOutput;

        return output;
    }

    /** NOTE:
     * Get a diff between two dates
     * date1 the oldest date
     * date2 the newest date
     * timeUnit the unit in which you want the diff
     * RETURN the diff value, in the provided unit
     **/
    public static long getDateDifference(Date date1, Date date2, TimeUnit timeUnit) {
        long diffInMillies = date2.getTime() - date1.getTime();
        return timeUnit.convert(diffInMillies,TimeUnit.MILLISECONDS);
    }

    /** NOTE:
     * Set application's language
     * Context is context of application
     * sharedPreferences is shared preference
     **/
    public static void setLocale(Context context, SharedPreferences sharedPreferences)
    {
        /*tu bo nho ROM cua thiet bi lay ra ngon ngu da cai dat cho ung dung*/
        String language = sharedPreferences.getString("language", context.getString(R.string.vietnamese));

        String vietnamese = context.getString(R.string.vietnamese);
        //String deutsch = context.getString(R.string.deutsch);


        Locale myLocale = new Locale("en");
        if(Objects.equals(language, vietnamese))
        {
            myLocale = new Locale("vi");
        }
//        if(Objects.equals(language,deutsch))
//        {
//            myLocale = new Locale("de");
//        }


        Resources resources = context.getResources();
        DisplayMetrics displayMetrics = resources.getDisplayMetrics();


        Configuration configuration = resources.getConfiguration();
        configuration.setLocale(myLocale);


        Locale.setDefault(myLocale);
        resources.updateConfiguration(configuration, displayMetrics);
    }
}

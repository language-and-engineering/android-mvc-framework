package com.android_mvc.framework.common;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 日時関連の共通ロジック
 * @author id:language_and_engineering
 *
 */
public class DateTimeUtil
{

    /**
     * Calendarの日時情報をフォーマットして出力
     */
    public static String calendar2string( Calendar calendar, String formatString )
    {
        return new SimpleDateFormat( formatString ).format( calendar.getTime() );
            // http://www.javaroad.jp/java_date3.htm
    }


    /**
     * 文字列をパースして，時刻情報付きのCalendarに変換
     */
    public static Calendar string2calendar(String s, String formatString)
    {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat(formatString);
        Date d = null;
        try {
            d = sdf.parse(s);
        } catch (ParseException ignore) {
        }
        calendar.setTime(d);
            // http://stackoverflow.com/questions/5301226/convert-string-to-calendar-object-in-java

        return calendar;

/*
        NOTE: 下記の方法で変換しようとすると，時刻情報が消える。
        Date date = null;
        Calendar calendar = Calendar.getInstance();
        try {
            date = DateFormat.getDateInstance().parse(s);
            calendar.setTime(date);
                // http://sauke-11.jugem.jp/?eid=63
                // http://d.hatena.ne.jp/chiheisen/20091123/1258986655
        } catch (ParseException ignore) {
        }
        return calendar;
*/
    }

}

package com.android_mvc.framework.db.entity;

import java.util.Calendar;

import com.android_mvc.framework.common.DateTimeUtil;

/**
 * ORMのLP変換を補助するロジック集。
 * ここにあるもので足りなければ，ユーザ側で拡張する。
 * @author id:language_and_engineering
 *
 */
public class BaseLPUtil
{
    // NOTE:
    // ・論理値を物理値にエンコードする。
    // ・物理値を論理値にデコードする。


    // 真偽値・integerの相互変換


    /**
     * 論理Booleanから物理integerへ
     */
    public static int encodeBooleanToInteger(Boolean b)
    {
        return b ? 1 : 0;
    }


    /**
     * 物理Integerから論理Booleanへ
     */
    public static Boolean decodeIntegerToBoolean(int i)
    {
        return (i == 1) ? true : false;
    }


    // 日時・textの相互変換


    protected static String FWDateTimeFormat = "yyyy/MM/dd HH:mm:ss";


    /**
     * 論理Calendarから物理textへ
     */
    public static String encodeCalendarToText(Calendar calendar)
    {
        return DateTimeUtil.calendar2string(calendar, FWDateTimeFormat);
    }


    /**
     * 物理textから論理Calendatへ
     */
    public static Calendar decodeTextToCalendar( String s )
    {
        return DateTimeUtil.string2calendar(s, FWDateTimeFormat);
    }


}

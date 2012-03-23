package com.android_mvc.framework.controller.validation;

/**
 * 数値の比較検証に用いる述語クラス。
 * @author id:language_and_engineering
 *
 */
public class NumberComparator
{
	// 演算の引数となる数値
    Long value;

    // 演算タイプを格納 TODO: enum
    String type_code;

    public NumberComparator( long v, String type_code )
    {
        value = v;
        this.type_code = type_code;
    }
}

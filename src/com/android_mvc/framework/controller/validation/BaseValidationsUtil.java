package com.android_mvc.framework.controller.validation;

import java.util.regex.Pattern;

import com.android_mvc.framework.activities.IBaseActivity;

/**
 * バリデーションロジックの基底クラス。
 * @author id:language_and_engineering
 *
 */
public class BaseValidationsUtil
{
    // チェック対象のアクティビティのパラメータ
    protected ActivityParams params;

    // バリデーション結果
    protected ValidationResult vres;


    /**
     * あるアクティビティに関するバリデーション処理を開始するため，初期化を行なう。
     */
    protected void initValidationOf(IBaseActivity activity)
    {
        params = activity.toParams();
        vres = new ValidationResult();
    }


    // ------------- assert系メソッド


    /**
     * 該当キーの文字列が空でない事を要求する。
     */
    protected void assertNotEmpty(String key)
    {
        String s = (String)params.getValue(key);

        // 1文字以上であること
        if( isEmpty( s ) )
        {
            vres.err( params.getName(key) + "が入力されていません。");
        }
    }


    /**
     * 該当キーの文字列が，有効な整数としてパース可能であることを要求する。
     */
    protected void assertValidInteger(String key)
    {
        String s = (String)params.getValue(key);
        if( isEmpty(s) ) return;

        // 半角数字のみであること。マイナスやピリオドも許可しない。
        if( ! isNumberCharSequence(s) )
        {
            // パース不可能
            vres.err( params.getName(key) + "には半角数字のみを入力してください。");
        }
        else
        if( ! validParsableAsInteger(s) )
        {
            // パース実行結果が不正
            vres.err( params.getName(key) + "の整数の入力形式が不正です。");
        }
    }


    /**
     * 該当キーの数値に対し，特定の演算条件を満たすことを要求する。
     */
    protected void assertNumberOperation(String key, NumberComparator nc)
    {
        String s = (String)params.getValue(key);
        if( ! validInteger(s) ) return;
        long target_value = Long.parseLong(s);

        // 演算を実行
        if( "greaterThan".equals(nc.type_code) && ! (target_value > nc.value))
        {
            vres.err( params.getName(key) + "には" + nc.value + "より大きい数を入力してください。");
        }
            // TODO: 演算タイプの追加
    }


    /**
     * 文字列の先頭が特定のフレーズで始まることを要求する。
     */
    protected void assertStringHead(String key, String headerTemplate)
    {
        String s = (String)params.getValue(key);
        if( isEmpty(s) ) return;

        // 先頭にマッチすること
        if( s.indexOf(headerTemplate) != 0 )
        {
            vres.err( params.getName(key) + "は「" + headerTemplate +"」で始まる必要があります。");
        }
    }


    // ------------ 補助メソッド


    /**
     * 「よりも大きい」を表す。
     */
    protected NumberComparator greaterThan( long l )
    {
        return new NumberComparator( l, "greaterThan" );
    }


    /**
     * バリデーションの結果を返す。
     */
    protected ValidationResult getValidationResult()
    {
        return vres;
    }


    // ------------ 内部使用


    /**
     * 対象文字列が空でないか検査
     */
    private boolean isEmpty(String s)
    {
        return ( ( s == null ) || ( s.length() < 1 ) );
    }


    /**
     * 対象文字列が半角数字のみであるか検査
     */
    private boolean isNumberCharSequence(String s)
    {
        return ( Pattern.compile("^[0-9]+$").matcher(s).matches() );
    }


    /**
     * 有効な整数としてパース可能か検査
     */
    private boolean validParsableAsInteger( String s )
    {
        // パース実行
        Long long_value = Long.parseLong(s);

        // パース前後で余計な変化がないこと。先頭の0とか。
        return String.valueOf( long_value ).equals(s);
    }


    /**
     * 有効な整数であるか検査
     */
    private boolean validInteger( String s )
    {
        return ( ! isEmpty(s) ) && ( isNumberCharSequence(s) ) && validParsableAsInteger(s);
    }


}

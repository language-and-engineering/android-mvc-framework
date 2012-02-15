package com.android_mvc.sample_project.controller;

import java.util.regex.Pattern;

import com.android_mvc.sample_project.activities.func_db.DBEditActivity;
import com.android_mvc.framework.controller.ActivityParams;
import com.android_mvc.framework.controller.GateValidationResult;

/**
 * Activityごとのバリデーション操作を詰め込んだクラス。
 * もし肥大化したら別クラスに細分化する。
 * @author id:language_and_engineering
 *
 */
public class GateChecker
{

    // Activityごとに引数の型を変えてオーバーロードする。

    /**
     * DB登録画面での入力値を検証
     */
    public static GateValidationResult validate(DBEditActivity activity) {
        ActivityParams params = activity.toParams();
        GateValidationResult vres = new GateValidationResult();

        String name = (String)params.get("name");
        String age = (String)params.get("age");


        // 名前に関してバリデ開始

        // 1文字以上であること。
        if( ( name == null ) || ( name.length() < 1 ) )
        {
            return vres.err("名前が入力されていません。");
        }


        // 年齢に関してバリデ開始

        // 1文字以上であること。
        if( ( age == null ) || ( age.length() < 1 ) )
        {
            return vres.err("年齢が入力されていません。");
        }

        // 半角数字のみであること。マイナスやピリオドも許可しない。
        if( ! Pattern.compile("^[0-9]+$").matcher(age).matches() )
        {
            return vres.err("年齢には半角数字のみを入力してください。");
        }

        // パース
        int age_value = Integer.parseInt(age);

        // パース前後で余計な変化がないこと。先頭の0とか。
        if( ! String.valueOf(age_value ).equals(age) )
        {
            return vres.err("年齢の整数の入力形式が不正です。");
        }

        // 0より大きいこと
        if( age_value < 1 )
        {
            return vres.err("年齢には0より大きい数を入力してください。");
        }


        // OK
        return vres.success();
    }
}

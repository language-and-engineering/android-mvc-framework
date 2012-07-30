package com.android_mvc.sample_project.controller;


import com.android_mvc.sample_project.activities.func_net.HttpNetActivity;
import com.android_mvc.sample_project.controller.util.ValidationsUtil;
import com.android_mvc.framework.controller.validation.ValidationResult;

/**
 * 通信系の画面のバリデーション処理の記述。
 * @author id:language_and_engineering
 *
 */
public class FuncNetValidation extends ValidationsUtil
{

    // Activityごとに引数の型を変えてオーバーロードする。

    /**
     * HTTP通信画面での入力値を検証
     */
    public ValidationResult validate(HttpNetActivity activity)
    {
        initValidationOf(activity);

        assertNotEmpty("http_url");
        assertStringHead("http_url", "http://");

        return getValidationResult();
    }

}

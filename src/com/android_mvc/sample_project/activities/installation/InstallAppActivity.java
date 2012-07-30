package com.android_mvc.sample_project.activities.installation;

import com.android_mvc.sample_project.activities.installation.lib.InstallAppUserBaseActivity;
import com.android_mvc.sample_project.controller.MainController;
import com.android_mvc.framework.ui.UIBuilder;
import com.android_mvc.framework.ui.view.MTextView;

/**
 * アプリの初期化処理を実行する画面。
 * アプリ内でのLAUNCHERアクティビティ。
 * @author id:language_and_engineering
 *
 */
public class InstallAppActivity extends InstallAppUserBaseActivity
{

    // NOTE: アプリの初期化フローの詳細は，親クラスを参照のこと。

    MTextView tv1;

    @Override
    public void defineContentView()
    {
        // ここに，画面上のUI部品の定義を記述する。

        new UIBuilder(context)
            .add(
                tv1 = new MTextView(context)
                    .text("アプリの初期化中です。お待ちください・・・" )
                    .widthWrapContent()
            )
        .display();

    }


    // ------------- アプリ初期化処理 -----------------


    // RDBの初期化は，スキーマ定義の専用クラスを参照。


    @Override
    protected void init_db_preferences()
    {
        // TODO: 必要なら，ここにプリファレンスの初期化処理を記述する。
        // PrefDAOを利用する。

    }


    @Override
    protected void init_etc()
    {
        // TODO：その他に初期化するものがあれば，ここで初期化する。
    }


    // ------------- 初期化完了時 -----------------


    @Override
    protected void onInstallCompleted()
    {
        MainController.submit( this, true );
    }


    @Override
    protected void onInstallSkipped()
    {
        MainController.submit( this, false );
    }

}
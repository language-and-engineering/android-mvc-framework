package com.android_mvc.framework.db.schema;

import com.android_mvc.framework.common.FWUtil;
import com.android_mvc.framework.db.DBHelper;

import android.content.Context;

/**
 * RDBのスキーマを表すクラス。
 * @author id:language_and_engineering
 *
 */
public class RDBSchema {

    private Context context;

    /**
     * 初期化
     */
    public RDBSchema( Context context )
    {
        this.context = context;

    }


    /**
     * スキーマが存在しなければ作成する。同期処理。
     */
    public void createIfNotExists( AbstractSchemaDefinition sd )
    {

        FWUtil.d("スキーマ存在確認を開始。もし必要なら初期化。");

        // NOTE: 初回の場合のみスキーマ初期化処理(onCreate)が内側で「同期的に」呼ばれる。非同期で待つ必要なし。
        new DBHelper( context ).createSchemaIfNotExists( sd );

        FWUtil.d("スキーマ存在確認を終了");

    }

}

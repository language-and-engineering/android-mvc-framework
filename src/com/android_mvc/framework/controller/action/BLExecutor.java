package com.android_mvc.framework.controller.action;


/**
 * 一つのコントロールフロー内でBLを実行するクラス
 */
public abstract class BLExecutor
{

    // BL実行結果
    public ActionResult action_result;


    /**
     * ユーザ側で，具体的なBL処理呼び出しを記述する。
     */
    public abstract ActionResult doAction();


    /**
     * BLを実行して，実行結果を保持する。
     */
    public void execAndStoreActionResult() {
        this.action_result = doAction();
    }

}


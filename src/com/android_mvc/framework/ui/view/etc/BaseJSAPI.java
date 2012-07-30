package com.android_mvc.framework.ui.view.etc;

/**
 * HTMLで画面を描画する際に，JavaScriptから参照可能なオブジェクト。
 * JS側からコールしたいJavaメソッドのプロキシ処理を，継承クラス側で記述する。
 * @author id:language_and_engineering
 *
 */
public abstract class BaseJSAPI {

    // 本クラスの利用方針：
    //
    // ・継承したクラス内部に定義したメソッドは，簡潔にする。
    //   もし本格的なロジックを実行したい場合は，具体的なロジックを他のビジネスロジッククラスに詰め込み，
    //   JSAPIはそれを呼び出すための単なるプロキシにする。


    // NOTE:
    // 本当は内部クラスや個別の継承クラスとして使うのではなく，匿名クラスとして定義したい。
    // 特定のActivityの画面に特化した，使い捨てのクラス定義だから。
    // だがそうすると各メソッドに@SuppressWarnings("unused")が必要になる。
    // そのため，利用Activity側ではインナークラスとしてクラス定義を外出ししている。
    // http://stackoverflow.com/questions/11611493/how-to-avoid-suppresswarnings-with-anonymous-inner-classes-in-java

}

package com.android_mvc.framework.ui.view;

import java.util.ArrayList;
import java.util.HashMap;

import com.android_mvc.framework.annotations.SuppressDebugLog;
import com.android_mvc.framework.common.FWUtil;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

/**
 * LinearLayoutのラッパークラス。
 * @author id:language_and_engineering
 *
 */
@SuppressDebugLog
public class MLinearLayout extends LinearLayout implements IFWView
{

    // このレイアウトが含んでいるビュー達
    public ArrayList<View> includingViews;

    // 描画が終了した内部Viewの個数
    private int numInflatedViews = 0;
        // NOTE: inflate後に後付けでaddして再度inflateする場合，すでに描画済みのViewについては下記の例外が発生する。
        //   Caused by: java.lang.IllegalStateException:
        //   The specified child already has a parent. You must call removeView() on the child's parent first.
        // なので，各Viewが描画済みか・そうでないかを記憶しておく必要がある。


    public MLinearLayout(Context context)
    {
        super(context);
        includingViews = new ArrayList<View>();
    }


    public MLinearLayout(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        includingViews = new ArrayList<View>();
    }
        // NOTE: このコンストラクタがないと描画時に下記の例外になる。
        //   android.view.InflateException: Binary XML file line #～: Error inflating class ～
        // @see http://q.hatena.ne.jp/1322903451


    /**
     * レイアウト内に描画したいビューを１つ以上登録する。
     * @param v 可変個のView
     */
    public MLinearLayout add(View...v)
    {
        for( int i = 0; i < v.length; i ++ )
        {
            FWUtil.d("追加対象のViewは" + v.toString() );
            this.includingViews.add( v[i] );
        }
        FWUtil.d("addによって" + v.length + "個のViewを追加登録。");

        return this;
    }


    /**
     * このレイアウトの描画を実行する。
     * 中身にレイアウトを含む場合，再帰的に呼び出される。
     * すでに描画が完了しているViewはスキップする。
     */
    public void inflateInside()
    {
        // 見描画の全Viewについて
        for(int i = numInflatedViews; i < includingViews.size(); i ++ )
        {
            View v = includingViews.get(i);

            //FWUtil.d("レイアウト中の１Viewを描画前：parentLayout = " + parentLayout.toString() + ", v = " + v.toString());
            registerOneView(v);
        }

        // 描画済みView数を更新
        numInflatedViews = includingViews.size();
    }


    /**
     * 1つのビューをレイアウト内に描画する。
     */
    private void registerOneView(View v)
    {
        FWUtil.d("１ビューの描画を開始。");

        // このViewのタテヨコ値を取得
        int[] arr = getWidthHeightOfView(v);
        int intWidth = arr[0];
        int intHeight = arr[1];

        // FW定義のレイアウト要素であれば，そのレイアウトの中身を具体化する
        if( v instanceof MLinearLayout )
        {
            FWUtil.d("FW定義のレイアウトを発見");
            MLinearLayout innerLayout = (MLinearLayout)v;
            innerLayout.inflateInside();
            FWUtil.d("FW定義の１レイアウト内の描画処理が完了");
        }

        // 親レイアウト内に描画
        this.addView(v, new LinearLayout.LayoutParams(intWidth, intHeight));
            // http://www.javadrive.jp/android/linearlayout/index5.html

        FWUtil.d("１ビューの描画を終了。");
    }


    /**
     * 特定のViewのタテヨコ値を取得
     */
    private int[] getWidthHeightOfView(View v) {

        // デフォルトのタテヨコ値
        int intWidth = ViewGroup.LayoutParams.WRAP_CONTENT;
        int intHeight = ViewGroup.LayoutParams.WRAP_CONTENT;

        // FW定義のViewであれば，タテヨコ設定
        if( v instanceof IFWView )
        {
            FWUtil.d("FW定義のViewを発見。タテヨコ設定を抽出。");

            // キャストして考える
            IFWView fwv = (IFWView)v;

            // 属性取得
            if( fwv.getViewParam("layout_width") != null )
            {
                intWidth = (Integer)fwv.getViewParam("layout_width");
            }
            if( fwv.getViewParam("layout_height") != null )
            {
                intHeight = (Integer)fwv.getViewParam("layout_height");
            }
        }
        FWUtil.d("Viewのタテヨコ設定の抽出が完了。");

        // 配列で返す
        return new int[]{intWidth, intHeight};
    }



    // パラメータ保持
    HashMap<String, Object> view_params = new HashMap<String, Object>();

    @Override
    public Object getViewParam(String key) {
        return view_params.get(key);
    }

    @Override
    public void setViewParam(String key, Object val) {
        view_params.put(key, val);
    }


    // 以下は属性操作


    public MLinearLayout orientationHorizontal() {
        setOrientation(LinearLayout.HORIZONTAL);
        return this;
    }


    public MLinearLayout orientationVertical() {
        setOrientation(LinearLayout.VERTICAL);
        return this;
    }


    public MLinearLayout widthFillParent() {
        setViewParam("layout_width", ViewGroup.LayoutParams.FILL_PARENT );
        return this;
    }


    public MLinearLayout heightWrapContent() {
        setViewParam("layout_width", ViewGroup.LayoutParams.WRAP_CONTENT );
        return this;
    }


    public MLinearLayout paddingPx( int px ) {
        setPadding(px, px, px, px);
        return this;
    }


}

package com.android_mvc.framework.ui.view.etc;

import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.android_mvc.framework.annotations.SuppressDebugLog;
import com.android_mvc.framework.common.FWUtil;
import com.android_mvc.framework.ui.view.IFWLayoutView;
import com.android_mvc.framework.ui.view.IFWView;

/**
 * Layout系のカスタムビューに関するロジック。
 * @author id:language_and_engineering
 *
 */
@SuppressDebugLog
public class LayoutUtil
{
    // NOTE: LinearLayoutとRelativeLayoutなどでロジックを共通化するために，やむなく作ったクラス。


    /**
     * 1つのビューをレイアウト内に描画する。
     */
    public static void registerAndRenderOneViewInLayout(View innerView, IFWLayoutView layout)
    {
        FWUtil.d("１ビューの描画を開始。");

        // このViewのタテヨコ値を取得
        int[] arr = layout.getWidthHeightOfView(innerView);
        int intWidth = arr[0];
        int intHeight = arr[1];

        // FW定義のレイアウト要素であれば，そのレイアウトの中身を具体化する
        if( innerView instanceof IFWLayoutView )
        {
            FWUtil.d("FW定義のレイアウトを発見");

            // 中身を具体化
            ((IFWLayoutView)innerView).inflateInside();
            FWUtil.d("FW定義の１レイアウト内の描画処理が完了");
        }

        // 親レイアウト内に描画
        ((ViewGroup)layout).addView(innerView, new LinearLayout.LayoutParams(intWidth, intHeight));
            // http://www.javadrive.jp/android/linearlayout/index5.html

        FWUtil.d("１ビューの描画を終了。");
    }


    /**
     * Layout内の特定のViewのタテヨコ値を取得
     */
    public static int[] getWidthHeightOfViewInLayout(View v, IFWLayoutView layout)
    {
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


    /**
     * Layout内に存在する，未描画の全Viewを描画する。
     */
    public static void renderAllUnrenderedViewsInsideLayout(IFWLayoutView layout)
    {
        // 未描画の全Viewについて
        for(int i = layout.getNumInflatedViews(); i < layout.getIncludingViewsSize(); i ++ )
        {
            View v = layout.getIncludingViewAt(i);

            // 描画
            layout.registerAndRenderOneView(v);
        }

        // 描画済みView数を更新
        layout.updateNumInflatedViews();
    }


    /**
     * 可変個のViewをレイアウトに追加する。
     */
    public static IFWLayoutView addViewsToLayout(IFWLayoutView layout, View...v)
    {
        for( int i = 0; i < v.length; i ++ )
        {
            FWUtil.d("追加対象のViewは" + v.toString() );
            layout.addOneIncludingView( v[i] );
        }
        FWUtil.d("addによって" + v.length + "個のViewを追加登録。");

        return layout;
    }


}

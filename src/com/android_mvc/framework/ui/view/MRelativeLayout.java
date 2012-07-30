package com.android_mvc.framework.ui.view;

import java.util.ArrayList;
import java.util.HashMap;

import com.android_mvc.framework.annotations.SuppressDebugLog;
import com.android_mvc.framework.ui.view.etc.LayoutUtil;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

/**
 * RelativeLayoutのラッパークラス。
 * @author id:language_and_engineering
 *
 */
@SuppressDebugLog
public class MRelativeLayout extends RelativeLayout implements IFWView, IFWLayoutView
{

    // このレイアウトが含んでいるビュー達
    public ArrayList<View> includingViews;

    // 描画が終了した内部Viewの個数
    private int numInflatedViews = 0;


    public MRelativeLayout(Context context)
    {
        super(context);
        includingViews = new ArrayList<View>();
    }


    public MRelativeLayout(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        includingViews = new ArrayList<View>();
    }
        // NOTE: このコンストラクタがないと描画時に下記の例外になる。
        //   android.view.InflateException: Binary XML file line #～: Error inflating class ～
        // @see http://q.hatena.ne.jp/1322903451


    // 独自レイアウトとして共通のメソッド

    @Override
    public MRelativeLayout add(View...v)
    {
         return (MRelativeLayout)LayoutUtil.addViewsToLayout(this, v);
    }


    @Override
    public void addOneIncludingView( View v )
    {
        this.includingViews.add( v );
    }


    @Override
    public int getNumInflatedViews() {
        return numInflatedViews;
    }


    @Override
    public void inflateInside()
    {
        LayoutUtil.renderAllUnrenderedViewsInsideLayout(this);
    }


    @Override
    public View getIncludingViewAt(int i) {
        return includingViews.get(i);
    }


    @Override
    public void updateNumInflatedViews() {
        numInflatedViews = getIncludingViewsSize();
    }


    @Override
    public int getIncludingViewsSize()
    {
        return includingViews.size();
    }


    @Override
    public void registerAndRenderOneView(View v)
    {
        LayoutUtil.registerAndRenderOneViewInLayout(v, this);
    }


    @Override
    public int[] getWidthHeightOfView(View v) {
        return LayoutUtil.getWidthHeightOfViewInLayout(v, this);
    }


    @Override
    public void removeAllIncludingViews() {
        this.includingViews.clear();
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


    public MRelativeLayout widthFillParent() {
        setViewParam("layout_width", ViewGroup.LayoutParams.FILL_PARENT );
        return this;
    }


    public MRelativeLayout heightWrapContent() {
        setViewParam("layout_height", ViewGroup.LayoutParams.WRAP_CONTENT );
        return this;
    }


    public MRelativeLayout heightFillParent() {
        setViewParam("layout_height", ViewGroup.LayoutParams.FILL_PARENT );
        return this;
    }


    public MRelativeLayout heightPx( int px ) {
        setViewParam("layout_height", px );
            // http://stackoverflow.com/questions/1357577/how-to-set-the-width-of-a-layout-in-android
        return this;
    }


    public MRelativeLayout paddingPx( int px ) {
        setPadding(px, px, px, px);
        return this;
    }


}

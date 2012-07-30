package com.android_mvc.framework.ui.anim.desc;


import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;

/**
 * フェードアウトするアニメーションの記述を簡素化するためのクラス。
 * @author id:language_and_engineering
 *
 */
public class FadeOutAnimationDescription extends AnimationDescription {

    private int after_visibility = View.INVISIBLE;

    @Override
    final protected Animation describe()
    {
        return new AlphaAnimation(1f, 0f);
    }


    /**
     * フェードアウト後の可視性属性値をセット
     */
    public FadeOutAnimationDescription afterVisibility( int visibility )
    {
        this.after_visibility = visibility;
        return this;
    }


    @Override
    protected void modifyAfterAnimation(View v)
    {
        v.setVisibility(after_visibility);
    }

}

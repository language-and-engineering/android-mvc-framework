package com.android_mvc.framework.ui.anim.desc;


import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;

/**
 * フェードインするアニメーションの記述を簡素化するためのクラス。
 * @author id:language_and_engineering
 *
 */
public class FadeInAnimationDescription extends AnimationDescription {

    private int after_visibility = View.VISIBLE;

    @Override
    final protected Animation describe()
    {
        return new AlphaAnimation(0f, 1f);
    }


    /**
     * フェード後の可視性属性値をセット
     */
    public FadeInAnimationDescription afterVisibility( int visibility )
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

package com.android_mvc.framework.ui.anim.desc;


import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;

/**
 * 直線上の移動アニメーションの記述を簡素化するためのクラス。
 * @author id:language_and_engineering
 *
 */
public class TranslateAnimationDescription extends AnimationDescription {

    private TranslateAnimation anim;

    private float x_diff;
    private float y_diff;

    private int axis_type;

    private float fromXValue;
    private float toXValue;
    private float fromYValue;
    private float toYValue;

    private boolean velocityLinearFlag = false;


    /**
     * 移動アニメーションの初期化。
     * @param axis_type Animation.ABSOLUTEなど
     */
    public TranslateAnimationDescription( int axis_type,
        float fromXValue, float toXValue,
        float fromYValue, float toYValue
    ){
        this.axis_type = axis_type;

        this.fromXValue = fromXValue;
        this.toXValue = toXValue;

        this.fromYValue = fromYValue;
        this.toYValue = toYValue;

        x_diff = toXValue - fromXValue;
        y_diff = toYValue - fromYValue;
    }


    @Override
    final protected Animation describe()
    {
        anim = new TranslateAnimation(
            axis_type, fromXValue,
            axis_type, toXValue,
            axis_type, fromYValue,
            axis_type, toYValue
        );

        if( velocityLinearFlag )
        {
            anim.setInterpolator(new LinearInterpolator());
        }

        return anim;
    }


    @Override
    protected void modifyAfterAnimation(View v)
    {
        // 移動した分をmarginとして加減
        modifyMarginsOfOneView( v, x_diff, y_diff );
    }


    /**
     * 移動に加速度をつけない。
     */
    public TranslateAnimationDescription velocityLinear()
    {
        this.velocityLinearFlag  = true;
        return this;
    }


    /**
     * 複数枚の画像を繰り返し入れ替える
     */
/*
    public TranslateAnimationDescription periodicImages(int[] resource_ids) {
        // TODO:
    }
*/

}

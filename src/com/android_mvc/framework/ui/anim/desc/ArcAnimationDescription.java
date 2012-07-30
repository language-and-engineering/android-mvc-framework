package com.android_mvc.framework.ui.anim.desc;

import java.util.ArrayList;
import java.util.List;


import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;

/**
 * 円弧上の移動アニメーションの記述を簡素化するためのクラス。
 * @author id:language_and_engineering
 *
 */
public class ArcAnimationDescription extends AnimationDescription {

    // NOTE: RotateAnimationの挙動が謎で，
    // 一度でも使ってしまうと補正が複雑で対処できなくなるため，代わりに作成。

    // TODO: 円弧の中心点に「幻像」が表示されてしまう，という謎のバグがある。


    private int from_theta;
    private int diff_theta;
    private float radius;
    private int diff_theta_direction;
    private float last_x;
    private float last_y;

    // 円弧を何度ずつの直線が集まった多角形とみなすか？
    private int delta_theta = 15;


    /**
     * 移動アニメーションの初期化。
     * 開始角，差分角，半径を指定。
     * 角度は度数法で，x軸から反時計回りに数える。
     * 開始角は0～360であること。
     */
    public ArcAnimationDescription(int from_theta, int diff_theta, float radius)
    {
        this.from_theta = from_theta % 360;
        this.diff_theta = diff_theta;
        this.radius = radius;

        this.diff_theta_direction = ( diff_theta >= 0 ) ? +1 : -1;
    }


    /**
     * 角度を指定して，円弧を近似する多角形の形状を決定。
     */
    public ArcAnimationDescription deltaDegree( int theta )
    {
        this.delta_theta = theta;
        return this;
    }


    @Override
    protected List<Animation> describeAsList()
    {
        List<Animation> anims = new ArrayList<Animation>();

        // 該当角度までの断片的な直線移動を積み重ねる。

        // 方向付きデルタ
        int delta_theta_with_direction = delta_theta * diff_theta_direction;

        // 移動軸タイプ
        int axis_type = Animation.ABSOLUTE;

        // 移動の中心座標。開始地点を原点とする。
        double theta_center = ( 180 + from_theta ) * 2 * Math.PI / 360f;
        int center_x = + (int)( radius * Math.cos( theta_center ) );
        int center_y = - (int)( radius * Math.sin( theta_center ) ); // NOTE:下向きが正なので負号が付く

        // 繰り返す
        int moved_theta_sum = 0;
        int current_theta = from_theta;
        boolean continue_flag = true;
        Animation anim;
        float fromXValue;
        float fromYValue;
        float toXValue = 0;
        float toYValue = 0;
        while( continue_flag )
        {
            // 移動先の角度
            int next_theta = current_theta + delta_theta_with_direction;

            // 座標を算出
            fromXValue = (float)( center_x + radius * Math.cos( current_theta * 2 * Math.PI / 360f ) );
            fromYValue = (float)( center_y - radius * Math.sin( current_theta * 2 * Math.PI / 360f ) );
            toXValue = (float)( center_x + radius * Math.cos( next_theta * 2 * Math.PI / 360f ) );
            toYValue = (float)( center_y - radius * Math.sin( next_theta * 2 * Math.PI / 360f ) );

            // アニメーションとして追加登録
            anim = new TranslateAnimation(
                axis_type, fromXValue,
                axis_type, toXValue,
                axis_type, fromYValue,
                axis_type, toYValue
            );
            anim.setInterpolator(new LinearInterpolator()); // これがないとデルタ毎にいちいち速度が変化する
            anims.add(anim);

            // 継続判定
            current_theta = next_theta;
            moved_theta_sum += delta_theta_with_direction;
            if( Math.abs(diff_theta) <= Math.abs(moved_theta_sum) )
            {
                continue_flag = false;
            }
        }

        // 最後の座標を補完
        this.last_x = toXValue;
        this.last_y = toYValue;

        return anims;
    }


    @Override
    protected void modifyAfterAnimation(View v)
    {
        // 最終的な移動場所をmarginとして加減
        modifyMarginsOfOneView( v, this.last_x, this.last_y );
    }


}

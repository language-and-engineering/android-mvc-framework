package com.android_mvc.framework.ui.anim.desc;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

/**
 * 順番に実行したいアニメーションの説明を記述。
 * @author id:language_and_engineering
 *
 */
public class AnimationDescription {

    // アニメーションの適用対象
    public View[] new_target_views = null;
    public RelativeLayout root_layout;

    // 開始前の待機時間
    public int wait_before = 0;

    // アニメーションの持続時間
    public int anim_duration = 0;

    // 終了後の待機時間
    public int wait_after = 0;

    // 終了後の補正処理を行うかどうか
    public boolean exec_modify_flag = true;

    // アニメーション対象UI
    public ArrayList<View> current_target_views;
    public Activity target_activity;

    // 制御スレッド側でUIスレッドを追い越さないためのゆとり時間
    private int duration_rest = 200;


    // --------- ユーザ定義用 -----------


    /**
     * 具体的なアニメーションを定義。
     * AnimationまたはAnimationSetを返すこと。
     * ユーザが簡潔に記述する。
     */
    protected Animation describe() {
        // Override me
        return null;
    }


    /**
     * describeの複数版。
     */
    protected List<Animation> describeAsList()
    {
        // Override me
        return null;
    }

    /**
     * アニメーション後に各種属性を変更（setFillAfter()が効かない問題への対処）
     * ※この補正処理が行われないように禁止することも可能。
     */
    protected void modifyAfterAnimation(View v) {
        // Override me
    }


    /**
     * アニメーション実行前のイベントハンドラ。
     * UIスレッド上で任意の処理を記載可能。
     */
    protected void beforeAnimate()
    {
        // Override me
    }


    /**
     * アニメーション実行後のイベントハンドラ。
     * UIスレッド上で任意の処理を記載可能。
     */
    protected void afterAnimate()
    {
        // Override me
    }


    // --------- setter -----------


    /**
     * アニメーションの適用対象をセット
     */
    public AnimationDescription targetViews(View...views) {
        this.new_target_views = views;
        return this;
    }


    /**
     * アニメーション開始前の待機時間をセット
     */
    public AnimationDescription waitBefore(int milli_sec) {
        this.wait_before = milli_sec;
        return this;
    }


    /**
     * アニメーションのdurationをセット
     */
    public AnimationDescription duration(int milli_sec) {
        this.anim_duration = milli_sec;
        return this;
    }


    /**
     * アニメーション終了後の待機時間をセット
     */
    public AnimationDescription waitAfter(int milli_sec) {
        this.wait_after = milli_sec;
        return this;
    }


    /**
     * アニメーション後の属性補正処理を行わない。
     */
    public AnimationDescription dontModify() {
        this.exec_modify_flag  = false;
        return this;
    }


    // --------- 待機処理を実行 -----------


    /**
     * 事前待機処理
     */
    public void execWaitBefore()
    {
        if( wait_before > 0 )
        {
            //FWUtil.d( "事前待機処理を実行します。");
            sleepMS(wait_before);
        }
    }


    /**
     * 事後待機処理
     */
    public void execWaitAfter()
    {
        if( wait_after > 0 )
        {
            //FWUtil.d( "事後待機処理を実行します。");
            sleepMS(wait_after);
        }
    }


    /**
     * アニメーション実行中の待機処理
     */
    public void execWaitDuration(int numAnims) {
        //FWUtil.d( "duration分の待機処理を実行します。");
        sleepMS((int) ( (anim_duration + duration_rest) / (float) numAnims));
    }


    /**
     * 指定されたミリ秒だけスリープ
     */
    private void sleepMS(int ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException ignore) {
        }
    }


    // --------- 具体的なアニメーション -----------


    /**
     * 具体的なアニメーションの指示を持っているか
     */
    public boolean hasDescribedAnimation() {
        return ( getDescribedAnimation() != null );
    }


    /**
     * アニメーションのフローを別スレッド上で実行
     */
    public void carryAnimationFlowOnOtherThread() {
        // 事前待機処理を実行
        this.execWaitBefore();

        // 全ターゲットViewでアニメ実行
        this.kickAnimationsForAllTargetViews();

        // 事後待機時間の分だけ，このスレッドは待つ
        this.execWaitAfter();

        // 全ターゲットViewで事後処理を実行
        this.modifyAfterForAllTargetViews();
    }


    /**
     * 全ターゲットViewでアニメーションを実行。
     * 前後のフックも含める。
     */
    private void kickAnimationsForAllTargetViews()
    {
        // 1DESC内の全アニメ実行前のハンドラを実行
        target_activity.runOnUiThread(new Runnable(){
            @Override
            public void run() {
                beforeAnimate();

                root_layout.invalidate();
            }
        });

        // アニメ本体
        if( hasDescribedAnimation() )
        {
            executeAllConcreteAnimations();
        }

        // 1DESC内の全アニメ実行後のハンドラを実行
        target_activity.runOnUiThread(new Runnable(){
            @Override
            public void run() {
                afterAnimate();

                root_layout.invalidate();
            }
        });
    }


    /**
     * 具体的なアニメーション本体を全部実行
     */
    private void executeAllConcreteAnimations()
    {
        // 1つ以上のAnimationが存在するので取得
        List<Animation> anims = this.getDescribedAnimation();


        // １desc内の全アニメを実行
        int num_anims = anims.size();
        for( final Animation anim : anims )
        {
            executeConcreteOneAnimation( anim, num_anims );
        }
    }


    /**
     * １desc内の１アニメーションを，全Viewで実行
     */
    private void executeConcreteOneAnimation( final Animation anim, int num_anims )
    {
        // Animationにdurationをセット
        if( this.anim_duration > 0 )
        {
            anim.setDuration( (long) (this.anim_duration / (float) num_anims) );
        }

        // NOTE: アニメーション前後で効果が続くようにしたい（連続実行を前提とするので）
        // しかし，下記のメソッドは機能しない。
        anim.setFillEnabled(true);
        anim.setFillBefore(true);
        anim.setFillAfter(true);
            // @see http://graphics-geek.blogspot.jp/2011/08/mysterious-behavior-of-fillbefore.html
            // "When fillEnabled is true, the value of fillBefore will be taken into account"
        // アニメーション終了後の状態を確実に保つためには，終了タイミングで属性をアニメどおりに変化させるしかない。
            // @see http://www.androiddiscuss.com/1-android-discuss/75731.html
            // http://stackoverflow.com/questions/3345084/how-can-i-animate-a-view-in-android-and-have-it-stay-in-the-new-position-size


        // 個々のターゲットViewごとに，具体的なAnimationを実行開始
        for( final View v : current_target_views )
        {
            //FWUtil.d( "アニメーションの開始を登録します。");

            // UI上の処理なので，UIスレッドにゆだねる
            target_activity.runOnUiThread(new Runnable(){
                @Override
                public void run() {
                    //FWUtil.d( "アニメーションを開始します。");

                    // キック
                    v.startAnimation(anim);
                }
            });
        }
            // UIスレッドでキックしておいたアニメーションはこのまま放任しておく。
            // 終了のリスナなどもセットせず，このスレッドからはもう関知しない。

        // 1desc内のこのアニメにかかる分だけ待機
        this.execWaitDuration( num_anims );
    }


    // -------- アニメ本体以外の調整など ----------


    /**
     * 全ターゲットViewで事後処理を実行。補正など
     */
    private void modifyAfterForAllTargetViews()
    {
        for( final View v : current_target_views )
        {
            if( this.exec_modify_flag ) // 属性変更を禁止されていなければ
            {
                // 属性変更が主なので，UIスレッドに頼む
                final AnimationDescription desc = this;
                target_activity.runOnUiThread(new Runnable(){
                    @Override
                    public void run() {
                        // このViewに対する事後処理
                        //v.invalidate();
                        desc.modifyAfterAnimation(v);
                    }
                });
            }
        }
    }


    /**
     * ルートレイアウトにViewを追加
     */
    protected void addViewOnStage( View v )
    {
        root_layout.addView(v, new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT)
        );
    }


    /**
     * １Viewのmarginを調整。
     */
    protected void modifyMarginsOfOneView(View v, float x_diff, float y_diff) {
        // 現状
        ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams)v.getLayoutParams();
            //FWUtil.d(
            //    "調整前：lp.leftMargin = " + lp.leftMargin + ", lp.topMargin = " + lp.topMargin
            //    + ", lp.rightMargin = " + lp.rightMargin + ", lp.bottomMargin = " + lp.bottomMargin
            //);

        // 調整を実行
        lp.setMargins((int)(lp.leftMargin + x_diff), (int)(lp.topMargin + y_diff), lp.rightMargin, lp.bottomMargin);
        v.setLayoutParams(lp);
            //FWUtil.d(
            //    "調整後：lp.leftMargin = " + lp.leftMargin + ", lp.topMargin = " + lp.topMargin
            //    + ", lp.rightMargin = " + lp.rightMargin + ", lp.bottomMargin = " + lp.bottomMargin
            //);
    }


    /**
     * 基点となるViewの真上に特定のViewが来るようにmarginを調整する。
     */
    protected void setPositionAboveOtherView( View target_view, View base_view )
    {
        // 基点
        ViewGroup.MarginLayoutParams lp_base = (ViewGroup.MarginLayoutParams)base_view.getLayoutParams();

        // 動かす対象の現在位置
        ViewGroup.MarginLayoutParams lp_target = (ViewGroup.MarginLayoutParams)target_view.getLayoutParams();

        // 動かす
        float x_diff = lp_base.leftMargin - lp_target.leftMargin;
        float y_diff = lp_base.topMargin - lp_target.topMargin - target_view.getHeight(); // 自分の高さの分だけ上に来るように
        modifyMarginsOfOneView(target_view, x_diff, y_diff);
    }


    // --------- 内部用 -----------


    /**
     * 具体的に定義されたアニメーションを内部用に返す。
     * nullを返却しうる。
     */
    final protected List<Animation> getDescribedAnimation() {
        List<Animation> anims = null;

        // 単一の場合
        Animation anim_single = describe();
        if( anim_single != null )
        {
            anims = new ArrayList<Animation>();
            anims.add(anim_single);
        }
        else // 複数の場合
        {
            anims = describeAsList();
        }

        return anims;
    }


}

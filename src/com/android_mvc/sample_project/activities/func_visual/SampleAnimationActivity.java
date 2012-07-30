package com.android_mvc.sample_project.activities.func_visual;

import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;

import com.android_mvc.framework.activities.base.BaseNormalActivity;
import com.android_mvc.framework.ui.UIBuilder;
import com.android_mvc.framework.ui.UIUtil;
import com.android_mvc.framework.ui.anim.AnimationsFinishListener;
import com.android_mvc.framework.ui.anim.SequentialAnimationsRunner;
import com.android_mvc.framework.ui.anim.desc.AnimationDescription;
import com.android_mvc.framework.ui.anim.desc.ArcAnimationDescription;
import com.android_mvc.framework.ui.anim.desc.FadeInAnimationDescription;
import com.android_mvc.framework.ui.anim.desc.FadeOutAnimationDescription;
import com.android_mvc.framework.ui.anim.desc.TranslateAnimationDescription;
import com.android_mvc.framework.ui.view.MImageView;
import com.android_mvc.framework.ui.view.MRelativeLayout;
import com.android_mvc.framework.ui.view.MTextView;

/**
 * サンプルのアニメーション表示アクティビティ。
 * 複数のViewの動きを，時間的に連続して，シンプルな記述で制御できる。というのがアピールポイント。
 * 試用段階。
 * @author id:language_and_engineering
 *
 */
public class SampleAnimationActivity extends BaseNormalActivity {

    MTextView tv1;
    MImageView iv1;
    MImageView iv2;
    MRelativeLayout rl1;
    MRelativeLayout rl2;
    MRelativeLayout rl3;

    SequentialAnimationsRunner anim_runner;


    @Override
    public void defineContentView() {

        // UIを定義
        new UIBuilder(context)
        .add(

            tv1 = new MTextView(context)
                .text("↓押すと動き出します。" )
                .widthFillParent()
                .heightWrapContent()
                ,

            rl1 = new MRelativeLayout(context)
                .widthFillParent()
                .heightPx(500)
                .add(
                    rl2 = new MRelativeLayout(context)
                        .widthFillParent()
                        .heightPx(500)
                        .add(
                            iv1 = new MImageView(context, android.R.drawable.sym_def_app_icon)
                                .click(new OnClickListener(){
                                    @Override
                                    public void onClick(View v) {
                                        // アニメ開始
                                        startSampleAnimation();
                                    }
                                })

                        )
                    ,

                    rl3 = new MRelativeLayout(context)
                        .widthFillParent()
                        .heightPx(500)
                        .add(
                            iv2 = new MImageView(context, android.R.drawable.sym_def_app_icon)
                                .invisible()
                        )
                )
        )
        .display();


        // アニメーションを定義
        anim_runner = new SequentialAnimationsRunner(this)
            .title("ドロイド君のアイコンの移動")
            .rootLayoutIs( rl1 )
            .add(

                new AnimationDescription(){
                    @Override
                    protected void beforeAnimate()
                    {
                        // クリック不可に
                        iv1.unclickable();
                    }
                }
                ,


                // １個目のアイコンを動かす
                new AnimationDescription().targetViews( iv1 )
                ,

                // 右へ
                new TranslateAnimationDescription( Animation.ABSOLUTE, 0, 200, 0, 0 )
                    .velocityLinear().waitBefore(500).duration( 1000 ).waitAfter( 0 )
                ,

                // 消える
                new FadeOutAnimationDescription()
                    .afterVisibility(View.GONE).duration( 1000 ).waitAfter( 100 )
                ,

                // 現れる
                new FadeInAnimationDescription()
                    .duration( 1000 ).waitAfter( 100 )
                ,

                // 下へ
                new TranslateAnimationDescription( Animation.ABSOLUTE, 0, 0, 0, 200 )
                    .velocityLinear().duration( 1000 ).waitAfter( 500 )
                    .dontModify()
                ,


                // ２個目のアイコンに制御対象を切り替える
                new AnimationDescription().targetViews( iv2 )
                ,


                // 左回り
                new ArcAnimationDescription( 180, 180, 100 ){
                    @Override
                    protected void beforeAnimate()
                    {
                        iv2.visible();
                    }
                }
                    .deltaDegree(30).duration( 2000 ).waitAfter( 500 )
                    .dontModify()
/*                ,

// TODO: この部分は一応動くのだが，場合によって画面がちらついたりする。
http://d.hatena.ne.jp/language_and_engineering/20120416/AndroidAnimationSetSequentialDSL
http://d.hatena.ne.jp/language_and_engineering/20120626/AndroidManipulateBitmapPixels

                // 両方のアイコンを対象とする
                new AnimationDescription().targetViews( iv1, iv2 )
                ,

                // 消える
                new FadeOutAnimationDescription()
                    .afterVisibility(View.GONE).duration( 2000 )
*/
            )
            .onFinish( new AnimationsFinishListener(){
                @Override
                protected void exec()
                {
                    // 終了処理
                    UIUtil.longToast(context, "アニメーションが終了しました。");
                }
            })
        ;
    }


    /**
     * サンプルのアニメーションを開始する。
     */
    protected void startSampleAnimation()
    {
        anim_runner.start();
    }

}
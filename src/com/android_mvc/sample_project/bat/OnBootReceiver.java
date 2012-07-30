package com.android_mvc.sample_project.bat;

import android.content.Context;

import com.android_mvc.framework.bat.BaseOnBootReceiver;
import com.android_mvc.framework.common.FWUtil;
import com.android_mvc.sample_project.common.AppSettings;

/**
 * 端末起動時の処理。
 * @author id:language_and_engineering
 *
 */
public class OnBootReceiver extends BaseOnBootReceiver
{
    @Override
    protected void onDeviceBoot(Context context)
    {
        // アプリ側の設定を初期化
        FWUtil.receiveAppInfoAsFW( new AppSettings(context) );

        // サンプルのサービス常駐を開始
        new SamplePeriodicService().startResident(context, getHandler());
    }

}

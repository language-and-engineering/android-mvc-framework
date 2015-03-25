# Androidアプリの開発を支援する，Java製フレームワーク #

「Android-MVC framework」は，MVCの全層をフルスタックでカバーします。

  * **Strutsのようにスタンダード**な設計パターンを提案し，
  * **Railsのように「CoC」**なDSLを提供し，
  * **jQueryのように流れるスタイル**のコーディングができ，
  * **HTML5やjQuery Mobile** などのWeb技術を使った，素早いUI構築が可能。

そんなオープンソースのフレームワークを目指しています。
<br>
<br>

<b>最新バージョンに関する情報：</b>

<ul><li><font color='red'>ver 0.3がリリースされました。 <a href='ver03FeaturesSummary.md'>こちらのページ</a>で，新バージョンの機能とサンプルコードを概観できます。</font> (2012/7/30)</li></ul>

<ul><li>ver 0.3を使ったAndroidアプリケーション開発手順は，<a href='http://www.oisys.co.jp/techlabo_tool6.html'>こちらのページ</a>に公開されています。 (2012/11/30)</li></ul>

<img src='http://android-mvc-framework.googlecode.com/files/ver0.3Architechture.png' />


<b>サンプルアプリ</b>

右図のUIは，現在地の情報をアイコンでリアルタイムに描画します。<br>
<br>
このUIを実現するためには，下記のようなJavaコードをコーディングします。<br>
<br>
XMLなどは一切，手を触れません。<br>
<br>
<img src='http://android-mvc-framework.googlecode.com/files/ver0.3SampleGPSAppCap.png' align='right' />

<pre><code><br>
 new UIBuilder(context)<br>
 .add(<br>
<br>
   layout1 = new MLinearLayout(context)<br>
     .widthFillParent()<br>
     .heightPx(600)<br>
     .add(<br>
<br>
       // GoogleMap。<br>
       // 自分の現在地を追跡し，<br>
       // なおかつ足跡の履歴をアイコン表示する。<br>
       map1 = new MMapView(context)<br>
         .widthFillParent()<br>
         .heightFillParent()<br>
         .touchable()<br>
         .showZoomControl()<br>
         .zoomToMaxDetail()<br>
<br>
         // GPS関連<br>
         .followMyLocation() // 自分の現在地を追跡し続ける<br>
         .gpsLookupPeriod( 10 * 1000 )<br>
         .onMyLocationChanged(new MapLocationListener(){<br>
           @Override<br>
           public void onLocationChanged( Location location )<br>
           {<br>
             UIUtil.longToast(context, <br>
              "マップが現在位置の変更を検出。\n"<br>
               + String.valueOf(location.getLatitude()) + ","<br>
               + String.valueOf(location.getLongitude())<br>
             );<br>
 <br>
             FuncDBController.submit(activity);<br>
           }<br>
         })<br>
 <br>
         // マップ上に描画するアイコン関連<br>
         .setIconsOverlay(<br>
           new IconsOverlaySettings()<br>
             .setIconImage(android.R.drawable.sym_def_app_icon)<br>
             .setItemsList( getLocationLogItemsList() )<br>
         )<br>
   )<br>
   ,<br>
 <br>
   toggle1 = new MToggleButton(context)<br>
     .textOn("現在地を追跡中")<br>
     .textOff("現在地の追跡を開始する")<br>
     .checked()<br>
     .onCheck( new CheckBox.OnCheckedChangeListener(){<br>
       @Override<br>
       public void onCheckedChanged(CompoundButton src, <br>
                              boolean isChecked) {<br>
         if( isChecked )<br>
         {<br>
           map1.followMyLocation();<br>
         }<br>
         else<br>
         {<br>
           map1.stopFollowMyLocation();<br>
         }<br>
       }<br>
     } )<br>
 <br>
 )<br>
 .display();<br>
<br>
</code></pre>

シンプルで，直感的で，XMLより書きやすく読みやすい。　そう思いませんか？<br>
<br>
※その他，サンプルの詳細な仕様については  <a href='ver03FeaturesSummary.md'>こちらのページ</a> の末尾をご覧ください。<br>
<br>
<br>
<br>
<br>
<hr><br>
<br>
<br>
<br>
<h1>クイックガイド</h1>

<ul><li>ver 0.2は， <a href='ver02FeaturesSummary.md'>こちらのページ</a>で，導入された新機能とサンプルコードを概観できます。 (2012/3/23)</li></ul>

<ul><li>ver 0.1は，<a href='ver01ArchitecturesAndDesignsSummary.md'>このページ</a>で，<b>全体のクラス設計</b>の概要を見る事ができます。 (2012/2/20)</li></ul>

<ul><li><b>さっそく使ってみる</b>には，「<a href='ver01FirstStepGuide.md'>３ステップで完了する導入手順</a>」をご覧ください。</li></ul>

<ul><li>最新のリリース情報やニュースは，<a href='NewsAndReleaseNotes.md'>新着情報のページ</a>からご確認ください。</li></ul>

<br>
<b>ダウンロード：</b>

<ul><li>「Downloads」タブから，zip形式でダウンロードできます。<br>
</li><li>「Source」タブに，SVNでのチェックアウト方法が記載されています。<br>
</li><li>「Source」タブで「Browse」すると，<a href='http://code.google.com/p/android-mvc-framework/source/browse/trunk/'>全ファイルのソースコード</a>を閲覧できます。</li></ul>

<br>

<br>
<br>
<hr><br>
<br>
<br>
<br>
<h1>フレームワークの特徴</h1>

本フレームワークは，迅速かつ堅牢なAndroidアプリ開発をサポートします。<br>
<br>
<br>
<h2>View層</h2>

<ul><li><b>サヨナラ，XML … Come on, jQueryスタイル！</b>
<ul><li>jQuery風の流れるようなスタイルでJavaコードを記述。<br>
</li><li>レイアウトXMLに全く手を触れず，最小限の手間でUIを構築できます。プロトタイピングにも最適。<br>
</li><li>あの面倒な findViewById() は，もう書かなくて済みます。<br>
</li><li>やはり面倒な「Adapter」も，もう作らなくて済みます。forループ内で「リスト」の行を動的に追加するだけ。なんとシンプル！<br>
</li><li>もちろん，レイアウトXMLを利用することも可能です。</li></ul></li></ul>

<ul><li><b>HTML，JavaScript，HTML5，jQuery Mobileを使って画面が作れる</b>
<ul><li>Web制作技術や，Webデザインのノウハウを最大限まで生かして，既存のリソースで素早いアプリ開発ができます。</li></ul></li></ul>

<ul><li><b>タブ構造とオプションメニューを軽々生成</b>
<ul><li>XMLに全く手を触れず，簡単なJavaコードを書くだけで，オプションメニューやタブを実現できます。</li></ul></li></ul>

<ul><li><b>レイアウトXML利用時のCoCなUI割り当て</b>
<ul><li>複雑なUIを記述するためにXMLを利用する場合でも，手間を軽減する仕組みがあります。<br>
</li><li>Activityの名称から，レンダリングすべきXMLを自動的に判断。いちいち指定する手間を省きます。</li></ul></li></ul>

<ul><li><b>MapView設置とオーバーレイ描画が簡単</b>
<ul><li>マップ連携アプリの手間を極限まで減らそうとしています。<br>
</li><li>GPS検出処理もライブラリ化済み。</li></ul></li></ul>

<br>
<h2>Model層</h2>

<ul><li><b>これならAndroid上で使える！ ORマッピング</b>
<ul><li>わかりやすいライブラリ・サンプル・及び設計パターンを提供。<br>
</li><li>SQLiteのシンプルさと，Javaオブジェクトの柔軟さを橋渡しします。（論理エンティティと物理エンティティの相互変換）<br>
</li><li>CRUD機能は用意済みです。</li></ul></li></ul>

<ul><li><b>お手軽なテーブル構築</b>
<ul><li>Railsのマイグレーションのように，Javaコードでスキーマ構築が可能。</li></ul></li></ul>

<ul><li><b>ドメインとビジネスロジックの枠組み</b>
<ul><li>どこに書いたらいいのか，もう迷う事はありません。</li></ul></li></ul>

<ul><li><b>アプリ初期化の枠組み</b>
<ul><li>デフォルトの初期化フローが準備されています。<br>
</li><li>インストール直後のセットアップ処理もサクサク実装しましょう。</li></ul></li></ul>

<ul><li><b>非同期タスクの制御管理を明白に</b>
<ul><li>コールバックの連鎖でお困りですか？その悩みも，もはや今日まで。<br>
</li><li>非同期タスク利用時にも，処理のフローが明白になるようにコードを記述できます。</li></ul></li></ul>

<ul><li><b>その他，HTTP通信のユーティリティを組込み済み。</b></li></ul>

<br>
<h2>Controller層</h2>

<ul><li><b>画面遷移の制御など，Struts相当の処理をコントローラ層に集約</b>
<ul><li>スパゲッティな画面遷移に悩まされることは，もうありません。<br>
</li><li>バリデーションやビジネスロジックの呼び出し，処理結果に応じた遷移の分岐なども最小限のコードで。<br>
</li><li>もちろん，これらの機能を使わないで済ませることも可能です。</li></ul></li></ul>

<ul><li><b>サービス・バッチも楽々配置</b>
<ul><li>常駐型・定期実行型・端末ブート時の自動起動型のサービスのひな型が初めから提供されています。</li></ul></li></ul>

<ul><li><b>その他，アプリ全体の開発をスピーディーにさせる要素たち</b>
<ul><li>アプリの設定項目の集約。<br>
</li><li>詳しい出力で，とってもお手軽なロギング。<br>
</li><li>楽すぎるリソース呼び出し。<br>
</li><li>デバッグ用⇔本番用の振る舞いの切り替え。設定項目ないしアノテーションだけで可能。</li></ul></li></ul>

<br>
<h2>さらに・・・</h2>

本フレームワークには，下記の物も付属します。<br>
<br>
<ul><li><b>サンプルのミニプロジェクト付き</b>
<ul><li>今すぐ実機上で動作します。<br>
</li><li>本フレームワークのリファレンスとして利用できます。<br>
</li><li>フレームワークは，パッケージ内で参照が自己完結しているため，サンプルプロジェクトは丸ごと削除できます。</li></ul></li></ul>

<ul><li><b>開発に必要な各種のアイデアを提供</b>
<ul><li>Android SDKを効率よく利用するためのDSL。ご関心があれば，各種の基底クラス内部をご覧ください。<br>
</li><li>アプリの標準的な設計パターン。<br>
</li><li>コーディングスタイル，命名，パッケージングに関する自然な規約。</li></ul></li></ul>

<ul><li><b>ドキュメントやリファレンスなどの開発情報</b>
<ul><li>公開サイトや開発ブログを通じて，情報が逐次提供されます。</li></ul></li></ul>


<br>

<br>
<br>
<hr><br>
<br>
<br>
<br>
<br>
<h1>プロジェクトの状態</h1>

名称は「Android-MVC」です。<br>
<br>

本フレームワークは今後，あたかも下記のような名前を持つかのようなツールとなってゆくでしょう。<br>
<br>
<ul><li><b>「Android-Struts」</b>。</li></ul>

<ul><li><b>「Android on Rails」</b>。</li></ul>

本フレームワークのロードマップ・イメージは，上記のいずれの名称も包含します。<br>
<br>
<br>

しかし，いずれの名称も本フレームワークの性質と目標をじゅうぶん的確に言い表してはいません。<br>
<br>
StrutsにはORマッパは含まれておらず，またRailsのクライアント層もjQueryとは別物だからです。<br>
<br>
また，Webアプリケーション開発のワークフロー・設計パターン・アナロジー等を，Androidアプリケーション開発に丸ごと移植する事はできないためです。<br>
<br>
<br>
したがって，現時点での最も適切な名称として，「<b>Android-MVC</b>」としか表現できないのです。<br>
<br>
<br>

メジャーバージョンが0から1に上がり次第…<br>
<br>
<ul><li>GitHubへ移行する予定です。</li></ul>

<ul><li>加えて，英語版でのドキュメンテーションも実行し，本フレームワークを世界に発信します。<br>
<br></li></ul>

どうぞお楽しみに。<br>
<br>
<br>

では，皆様がよいAndroidアプリ開発を楽しまれる事と，Androidワールドのさらなる発展とを願いつつ。<br>
<br>
<br>
<br>

<b>presented by <a href='http://d.hatena.ne.jp/language_and_engineering/'>id:language_and_engineering</a>，</b><br>
<b>licensed under <a href='http://www.apache.org/licenses/LICENSE-2.0'>Apache License 2.0</a>，</b><br>
<b>supported by <a href='http://www.oisys.co.jp/'>oisys.co.jp</a>，</b><br>
<b>and thanks to Google for Android & GoogleCode!</b>

<br>
<br>
<br>



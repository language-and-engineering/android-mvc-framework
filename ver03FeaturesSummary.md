# ver0.3の特徴：概観 #

これまで，本フレームワークは，下記のような進歩を遂げてきた。

  * ver0.1：AndroidアプリにおけるMVCアーキテクチャの骨組みを示した。

  * ver0.2：MVCアーキテクチャの構造を強化し，レイヤ間の連携を容易にした。

<br>

そして今回，ver0.3では，<b>「実用性」を向上させる</b>事に主眼を置いたリリースとなった。<br>
<br>
<ul><li>ver0.3：サンプルアプリ充実，および他ツールの特徴を導入。実用性を向上させた。</li></ul>

<br>

各レイヤごとに，機能面での主な改良点を取り上げる。<br>
<br>
<ul><li><b>View層</b>
<ul><li>UIを，４通りの方法で実装可能になった。HTML5やjQuery Mobile，またJS+CSSなど既存のWeb制作技術を使った画面構築をサポート。<br>
</li><li>MapViewの扱いが極めて容易になった。現在地の追跡や，マップ上のアイコン描画をサポート。<br>
</li><li>Viewのアニメーションの連続実行を可能にするようなライブラリを提供。</li></ul></li></ul>

<ul><li><b>Controller層</b>
<ul><li>サービス（バッチ）で，定期的にタスクを実行するような常駐型の仕組みを容易に実現可能になった。<br>
</li><li>端末ブート時に起動するサービスも容易に実現可能になった。<br>
</li><li>複数の非同期タスクを逐次的に連続実行する際，イベント駆動型のタスクも実行できるようになった。<br>
</li><li>GPSで現在地を取得する処理を容易に実現可能になった。位置情報の変換ユーティリティ付き。<br>
</li><li>コントロールフローをより簡潔にカスタマイズ可能に。ダイアログの文言や，BLを介さない画面遷移など。</li></ul></li></ul>

<ul><li><b>Model層</b>
<ul><li>ORMで，論理エンティティと物理エンティティの相互変換をスマートに実行するための方式を整備した。<br>
</li><li>RDBの初期状態の定義を簡潔にした。カラムに論理名コメントを付与可能になり，初期データ投入も統合。<br>
</li><li>SELECT時にLIMITとOFFSETを指定可能にした。</li></ul></li></ul>

<br>

これらの特徴は，同梱のサンプルアプリを動作させて確認することができる。<br>
<br>
下記では，これらの特徴を，サンプルコードを引用しつつ詳細に説明する。<br>
<br>

<br>
<br>
<hr><br>
<br>
<br>

<h1>ver0.3の特徴：詳細</h1>

ver0.2の時に倣って，V→C→Mの順に見てゆく。<br>
<br>
<br>
<br>
<hr><br>
<br>
<br>

<h2>View層：</h2>

<h2>■ UIを，４通りの方法で実装可能になった。HTML5やjQuery Mobile，またJS+CSSなど既存のWeb制作技術を使った画面構築をサポート。</h2>

もともと，従来のレイアウトXMLによる画面描画方法に加えて，UIBuilderクラスを使った手軽なUI構築が可能な造りとなっていた。<br>
<br>
この方法は，画面のプロトタイプ構築などの際に威力を発揮することだろう。<br>
<br>
<br>
今回ver0.3では，他のAndroidアプリ開発フレームワークの特徴を導入した。<br>
<br>
即ち，<b>HTML5等を使ったUI構築</b>である。<br>
<br>
<br>
素のHTML4と，JavaScriptとJavaを連携させる仕組みのサンプルコードは，下記を参照。<br>
<br>
<br>
<blockquote>SampleHtmlActivity.java<br>
<a href='http://code.google.com/p/android-mvc-framework/source/browse/tags/20120730_ver0.3/src/com/android_mvc/sample_project/activities/func_html/SampleHtmlActivity.java'>http://code.google.com/p/android-mvc-framework/source/browse/tags/20120730_ver0.3/src/com/android_mvc/sample_project/activities/func_html/SampleHtmlActivity.java</a></blockquote>


HTML5と，jQuery Mobileを使ったUI構築のサンプルコードは，下記のアクティビティを参照。<br>
<br>
<blockquote>SampleJQueryMobileActivity.java<br>
<a href='http://code.google.com/p/android-mvc-framework/source/browse/tags/20120730_ver0.3/src/com/android_mvc/sample_project/activities/func_html/SampleJQueryMobileActivity.java'>http://code.google.com/p/android-mvc-framework/source/browse/tags/20120730_ver0.3/src/com/android_mvc/sample_project/activities/func_html/SampleJQueryMobileActivity.java</a></blockquote>


本フレームワークのユーザは，用途に応じて，自分の好きなUI実装方法を選んでよい，というわけだ。<br>
<br>
<br>
<br>
これらの機能を実現するためには，下記のエントリが参考になった。<br>
<br>
<blockquote>たった２ファイルで，HTML＋JS製のネイティブAndroidアプリを作る手順　（動作するサンプルコード付き。WebViewの活用方法）<br>
<a href='http://d.hatena.ne.jp/language_and_engineering/20120710/CreateAndroidAppByHtml5JavaScript'>http://d.hatena.ne.jp/language_and_engineering/20120710/CreateAndroidAppByHtml5JavaScript</a></blockquote>

<blockquote>jQuery Mobile と HTML5 で、Androidのネイティブアプリを作成する手順<br>
<a href='http://d.hatena.ne.jp/language_and_engineering/20120717/CreateAndroidNativeAppsByJQueryMobile'>http://d.hatena.ne.jp/language_and_engineering/20120717/CreateAndroidNativeAppsByJQueryMobile</a></blockquote>

<blockquote>AndroidやiOSの「ハイブリッドアプリ」で，JavaScriptとネイティブ・コードが連携する仕組みを図解　（おまけ：HTML側で施すべき，クロスプラットフォーム対策）<br>
<a href='http://d.hatena.ne.jp/language_and_engineering/20120713/p1'>http://d.hatena.ne.jp/language_and_engineering/20120713/p1</a></blockquote>

<blockquote>Javaで，匿名クラス内で定義したpublicメソッドの警告が消せず困った話　（静的なJavaと，動的なJavaScriptを連携させるDSLを作りたい）<br>
<a href='http://d.hatena.ne.jp/language_and_engineering/20120728/AnonymousClassWarnsOnBridgingBetweenJavaAndJS'>http://d.hatena.ne.jp/language_and_engineering/20120728/AnonymousClassWarnsOnBridgingBetweenJavaAndJS</a></blockquote>


<h2>■ MapViewの扱いが極めて容易になった。現在地の追跡や，マップ上のアイコン描画をサポート。</h2>

MapViewを拡張したMMapViewの作成により，マップアプリの開発工数が劇的に削減される。<br>
<br>
マップ上では，自分の現在地を自動的に追跡させる事もできるし，任意の座標にアイコンを描画する事も容易である。<br>
<br>
下記が該当するサンプルコード。<br>
<br>
<blockquote>SampleMapActivity.java<br>
<a href='http://code.google.com/p/android-mvc-framework/source/browse/tags/20120730_ver0.3/src/com/android_mvc/sample_project/activities/func_map/SampleMapActivity.java'>http://code.google.com/p/android-mvc-framework/source/browse/tags/20120730_ver0.3/src/com/android_mvc/sample_project/activities/func_map/SampleMapActivity.java</a></blockquote>


Mapを利用するためには，Eclipse上でプロジェクトを新規作成する際に，ターゲットとしてGoogle APIを含むようなセットアップが必要である。<br>
<br>
また，strings.xmlの中に，自分の取得したGoogle Maps APIのキーを記述しておくことが必要だ。<br>
<br>
<br>
これらの機能を実現するためには，下記のエントリが参考になった。<br>
<br>
<blockquote>Androidアプリで，Google Maps API＋GPS＋Geocoderを使って，現在地の地図と地名を表示させよう<br>
<a href='http://d.hatena.ne.jp/language_and_engineering/20110828/p1'>http://d.hatena.ne.jp/language_and_engineering/20110828/p1</a></blockquote>

<blockquote>Androidアプリで，Google Mapsの地図上にアイコン画像を配置し，そのTapイベントに反応するサンプルコード<br>
<a href='http://d.hatena.ne.jp/language_and_engineering/20110907/p1'>http://d.hatena.ne.jp/language_and_engineering/20110907/p1</a></blockquote>



<h2>■ Viewのアニメーションの連続実行を可能にするようなライブラリを提供。</h2>

AndroidのアニメーションAPIには，「連続実行がしづらい」という欠点があった。<br>
<br>
しかし，それを解消するような便利なライブラリが提供された。<br>
<br>
AnimationDescriptionを列挙するだけで，時系列にアニメーションが実行されるのである。<br>
<br>
<blockquote>SampleAnimationActivity.java<br>
<a href='http://code.google.com/p/android-mvc-framework/source/browse/tags/20120730_ver0.3/src/com/android_mvc/sample_project/activities/func_visual/SampleAnimationActivity.java'>http://code.google.com/p/android-mvc-framework/source/browse/tags/20120730_ver0.3/src/com/android_mvc/sample_project/activities/func_visual/SampleAnimationActivity.java</a></blockquote>

この機能を実現するためには，下記のエントリが役立った。<br>
<br>
<blockquote>Androidで，複数のAnimationを「順番に」実行するためのライブラリ　（XMLを使わずに「連続した動きの変化」を指定し，逐次実行するDSL）<br>
<a href='http://d.hatena.ne.jp/language_and_engineering/20120416/AndroidAnimationSetSequentialDSL'>http://d.hatena.ne.jp/language_and_engineering/20120416/AndroidAnimationSetSequentialDSL</a></blockquote>

<blockquote>Androidで，「ビットマップのピクセル操作」をリアルタイムに実行するサンプルコード<br>
<a href='http://d.hatena.ne.jp/language_and_engineering/20120626/AndroidManipulateBitmapPixels'>http://d.hatena.ne.jp/language_and_engineering/20120626/AndroidManipulateBitmapPixels</a></blockquote>

ただし，本機能には限界もある。利用時には，１個のViewに対する操作にとどめられたい。<br>
<br>
<br>
<br>

<hr>

<br>


<h2>Controller層：</h2>

<h2>■ サービス（バッチ）で，定期的にタスクを実行するような常駐型の仕組みを容易に実現可能になった。</h2>

処理の実行間隔と，処理の実行内容さえ指定すれば，もうそれで常駐型のサービスの出来上がりである。<br>
<br>
<blockquote>SamplePeriodicService.java<br>
<a href='http://code.google.com/p/android-mvc-framework/source/browse/tags/20120730_ver0.3/src/com/android_mvc/sample_project/bat/SamplePeriodicService.java'>http://code.google.com/p/android-mvc-framework/source/browse/tags/20120730_ver0.3/src/com/android_mvc/sample_project/bat/SamplePeriodicService.java</a></blockquote>


<h2>■ 端末ブート時に起動するサービスも容易に実現可能になった。</h2>

マニフェストXML中にも指定が必要だが，ブート時に行なうべきことの記述は極めて少なくて済むようになっている。<br>
<br>
<blockquote>OnBootReceiver.java<br>
<a href='http://code.google.com/p/android-mvc-framework/source/browse/tags/20120730_ver0.3/src/com/android_mvc/sample_project/bat/OnBootReceiver.java'>http://code.google.com/p/android-mvc-framework/source/browse/tags/20120730_ver0.3/src/com/android_mvc/sample_project/bat/OnBootReceiver.java</a></blockquote>


なお，これらサービス関連の機能を実現するにあたり，下記のエントリが役立った。<br>
<br>
<blockquote>Androidで，自動起動する常駐型サービスのサンプルコード　（アプリの裏側で定期的にバッチ処理）<br>
<a href='http://d.hatena.ne.jp/language_and_engineering/20120724/AndroidAutoStartingResidentServiceBatch'>http://d.hatena.ne.jp/language_and_engineering/20120724/AndroidAutoStartingResidentServiceBatch</a></blockquote>


<h2>■ 複数の非同期タスクを逐次的に連続実行する際，イベント駆動型のタスクも実行できるようになった。</h2>

この部分は，本フレームワークの全ての要とも言える。<br>
<br>
非同期で複雑なマルチスレッドの連続した処理を，まるでシングルスレッドでもあるかのように，逐次的に記述できるのだ。<br>
<br>
<br>
今までは，AsyncTasksRunnerに渡されるSequentialAsynkTaskクラスは，ただ単に「重い処理を別スレッドで逐次的に実行するための手段」でしかなかった。<br>
<br>
しかし，今回からはここにイベント駆動型の「SequentialEventTask」が加わった。<br>
<br>
<blockquote>SequentialEventTask.java<br>
<a href='http://code.google.com/p/android-mvc-framework/source/browse/tags/20120730_ver0.3/src/com/android_mvc/framework/task/SequentialEventTask.java'>http://code.google.com/p/android-mvc-framework/source/browse/tags/20120730_ver0.3/src/com/android_mvc/framework/task/SequentialEventTask.java</a></blockquote>

特定のイベントの発火を待つような場合，逐次的な処理はいったん途切れることになる。<br>
<br>
イベントリスナをimplementしつつ，本イベントタスクをextendすれば，逐次的に取り扱い可能なイベントクラスが作成可能になる。<br>
<br>
つまり，イベントの処理結果を次の非同期タスクに容易に渡す事ができる，ということだ。<br>
<br>
<br>
<br>
<h2>■  GPSで現在地を取得する処理を容易に実現可能になった。位置情報の変換ユーティリティ付き。</h2>

GPS情報の取得タスクは，前項で言及したイベントタスクとして実装されている。<br>
<br>
Android中で取り扱われる位置情報オブジェクトの種類はさまざまなので，それらを相互に変換するユーティリティも付属している。<br>
<br>
<blockquote>GetMyLocationEventTask.java<br>
<a href='http://code.google.com/p/android-mvc-framework/source/browse/tags/20120730_ver0.3/src/com/android_mvc/framework/gps/GetMyLocationEventTask.java'>http://code.google.com/p/android-mvc-framework/source/browse/tags/20120730_ver0.3/src/com/android_mvc/framework/gps/GetMyLocationEventTask.java</a></blockquote>

<blockquote>LocationUtil.java<br>
<a href='http://code.google.com/p/android-mvc-framework/source/browse/tags/20120730_ver0.3/src/com/android_mvc/framework/gps/LocationUtil.java'>http://code.google.com/p/android-mvc-framework/source/browse/tags/20120730_ver0.3/src/com/android_mvc/framework/gps/LocationUtil.java</a></blockquote>


<h2>■  コントロールフローをより簡潔にカスタマイズ可能に。ダイアログの文言や，BLを介さない画面遷移など。</h2>

コントローラ上で，BLを介さない画面遷移を非常に簡潔に記述できるようになった。<br>
<br>
サンプルコードを記載する。<br>
<br>
<pre><code><br>
    /**<br>
     * TOP画面からの遷移時<br>
     */<br>
    public static void submit(TopActivity activity, String button_type) {<br>
<br>
        // 送られてきたボタンタイプに応じて，遷移先を振り分ける。<br>
<br>
        // extra付きの遷移を実行<br>
        if( "EDIT_DB".equals(button_type) )<br>
        {<br>
            Router.goWithData(activity, DBEditActivity.class, "DB編集画面へ",<br>
                new Intent().putExtra("hoge", "Intentで値を渡すテスト").putExtra("fuga", 1)<br>
            );<br>
        }<br>
        else<br>
        {<br>
            // extraのない遷移を実行<br>
            Router.goByRoutingTable(activity, button_type,<br>
                new RoutingTable()<br>
                    .map("VIEW_DB",       DBListActivity.class,             "DB一覧画面へ")<br>
                    .map("TAB_SAMPLE",    SampleTabHostActivity.class,      "タブ画面へ")<br>
                    .map("MAP_SAMPLE",    SampleMapActivity.class,          "マップ画面へ")<br>
                    .map("HTML_SAMPLE",   SampleHtmlActivity.class,         "HTMLのサンプル画面へ")<br>
                    .map("JQUERY_SAMPLE", SampleJQueryMobileActivity.class, "jQuery Mobileのサンプル画面へ")<br>
                    .map("ANIM_SAMPLE",   SampleAnimationActivity.class,    "アニメーションのサンプル画面へ")<br>
            );<br>
        }<br>
<br>
/*<br>
    NOTE: 下記のように書くのと同じ。<br>
<br>
        if( "VIEW_DB".equals(button_type) )<br>
        {<br>
            // 一覧画面へ<br>
            Router.go(activity, DBListActivity.class);<br>
        }<br>
        if( "TAB_SAMPLE".equals(button_type) )<br>
        {<br>
            // タブ画面へ<br>
            Router.go(activity, SampleTabHostActivity.class);<br>
        }<br>
        if( "MAP_SAMPLE".equals(button_type) )<br>
        {<br>
            // マップ画面へ<br>
            Router.go(activity, SampleMapActivity.class);<br>
        }<br>
        if( "HTML_SAMPLE".equals(button_type) )<br>
        {<br>
            // HTMLのサンプル画面へ<br>
            Router.go(activity, SampleHtmlActivity.class);<br>
        }<br>
        if( "JQUERY_SAMPLE".equals(button_type) )<br>
        {<br>
            // jQuery Mobileのサンプル画面へ<br>
            Router.go(activity, SampleJQueryMobileActivity.class);<br>
        }<br>
        if( "ANIM_SAMPLE".equals(button_type) )<br>
        {<br>
            // アニメーションのサンプル画面へ<br>
            Router.go(activity, SampleAnimationActivity.class);<br>
        }<br>
*/<br>
<br>
    }<br>
<br>
</code></pre>

要は，RoutingTableの利用可能局面が増えたのである。<br>
<br>
<blockquote>MainController.java<br>
<a href='http://code.google.com/p/android-mvc-framework/source/browse/tags/20120730_ver0.3/src/com/android_mvc/sample_project/controller/MainController.java'>http://code.google.com/p/android-mvc-framework/source/browse/tags/20120730_ver0.3/src/com/android_mvc/sample_project/controller/MainController.java</a></blockquote>

また，ControlFlowDetailが流れる間のダイアログの文言も設定可能になった。<br>
<br>
<br>
<br>

<hr>

<br>


<h2>Model層</h2>

<h2>■ ORMで，論理エンティティと物理エンティティの相互変換をスマートに実行するための方式を整備した。</h2>

Javaの世界の論理エンティティを，SQLiteの世界の物理エンティティと相互変換させるための仕組みが強化された。<br>
<br>
下記のサンプルコートで，「LPUtil」というユーティリティが果たしている役目に注目されたい。<br>
<br>
<br>
Friend.java<br>
<br>
<pre><code><br>
    // ----- LP変換(Logical &lt;-&gt; Physical) -----<br>
<br>
<br>
    /**<br>
     * DBの格納値から論理エンティティを構成<br>
     */<br>
    @Override<br>
    public Friend logicalFromPhysical(Cursor c)<br>
    {<br>
        setId(c.getLong(0));<br>
        setName(c.getString(1));<br>
        setAge( c.getInt(2) );<br>
        setFavorite_flag( LPUtil.decodeIntegerToBoolean( c.getInt(3) ) );<br>
<br>
        return this;<br>
    }<br>
<br>
<br>
    /**<br>
     * 自身をDBに新規登録可能なデータ型に変換して返す<br>
     */<br>
    @Override<br>
    protected ContentValues toPhysicalEntity(ContentValues values)<br>
    {<br>
        // entityをContentValueに変換<br>
<br>
        if( getId() != null)<br>
        {<br>
            values.put("id", getId());<br>
        }<br>
        values.put("name", getName());<br>
        values.put("age", getAge());<br>
        values.put("favorite_flag", LPUtil.encodeBooleanToInteger( getFavorite_flag() ) );<br>
<br>
        return values;<br>
    }<br>
<br>
</code></pre>


LocationLog.java<br>
<br>
<pre><code><br>
    // ----- LP変換(Logical &lt;-&gt; Physical) -----<br>
<br>
<br>
    /**<br>
     * DBの格納値から論理エンティティを構成<br>
     */<br>
    @Override<br>
    public LocationLog logicalFromPhysical(Cursor c)<br>
    {<br>
        setId(c.getLong(0));<br>
        setRecorded_at( LPUtil.decodeTextToCalendar(c.getString(1)) );<br>
        setLatitude( c.getDouble(2) );<br>
        setLongitude( c.getDouble(3) );<br>
        setGeo_str( c.getString(4) );<br>
<br>
        return this;<br>
    }<br>
<br>
<br>
    /**<br>
     * 自身をDBに新規登録可能なデータ型に変換して返す<br>
     */<br>
    @Override<br>
    protected ContentValues toPhysicalEntity(ContentValues values)<br>
    {<br>
        // entityをContentValueに変換<br>
<br>
        if( getId() != null)<br>
        {<br>
            values.put("id", getId());<br>
        }<br>
        values.put("latitude", getLatitude());<br>
        values.put("longitude", getLongitude());<br>
        values.put("recorded_at", LPUtil.encodeCalendarToText( getRecorded_at() ));<br>
        values.put("geo_str", getGeo_str());<br>
<br>
        return values;<br>
    }<br>
<br>
</code></pre>

こういった機構が準備されているおかげで，フレームワークのユーザは，Javaの便利なオブジェクトと，SQLiteの少ない型制限との間でギャップに苦しまないで済む。<br>
<br>
そのギャップは，フレームワークが埋めてくれるからだ。変換ミスも起きない。<br>
<br>
<br>
なにしろ，SQLiteには，BooleanもDatetimeも存在しないから。<br>
<br>
かわりにintegerとtextで代用する必要があるのだ。<br>
<br>
そこを透過的にサポートしてくれるツールがあると心強い。<br>
<br>
<blockquote>BaseLPUtil.java<br>
<a href='http://code.google.com/p/android-mvc-framework/source/browse/tags/20120730_ver0.3/src/com/android_mvc/framework/db/entity/BaseLPUtil.java'>http://code.google.com/p/android-mvc-framework/source/browse/tags/20120730_ver0.3/src/com/android_mvc/framework/db/entity/BaseLPUtil.java</a></blockquote>


<h2>■ RDBの初期状態の定義を簡潔にした。カラムに論理名コメントを付与可能になり，初期データ投入も統合。</h2>

これは，スキーマ定義クラスを見ればわかって頂ける事と思う。<br>
<br>
<blockquote>SchemaDefinition.java<br>
<a href='http://code.google.com/p/android-mvc-framework/source/browse/tags/20120730_ver0.3/src/com/android_mvc/sample_project/db/schema/SchemaDefinition.java'>http://code.google.com/p/android-mvc-framework/source/browse/tags/20120730_ver0.3/src/com/android_mvc/sample_project/db/schema/SchemaDefinition.java</a></blockquote>


<h2>■ SELECT時にLIMITとOFFSETを指定可能にした。</h2>

下記のようなコードが，DAOの基底クラスで標準装備されている。<br>
<br>
<pre><code><br>
    /**<br>
     * idが最新の１件を取得する。<br>
     */<br>
    public T findNewestOne(DBHelper helper, Class&lt;T&gt; entity_class)<br>
    {<br>
        List&lt;T&gt; records =  new Finder&lt;T&gt;(helper)<br>
            .where("id &gt; 0")<br>
            .orderBy("id DESC")<br>
            .offset(1)<br>
            .limit(1)<br>
            .findAll(entity_class)<br>
        ;<br>
        if( records.size() &gt; 0 )<br>
        {<br>
            return records.get(0);<br>
        }<br>
        else<br>
        {<br>
            return null;<br>
        }<br>
    }<br>
<br>
</code></pre>

<blockquote>BaseDAO.java<br>
<a href='http://code.google.com/p/android-mvc-framework/source/browse/tags/20120730_ver0.3/src/com/android_mvc/framework/db/dao/BaseDAO.java'>http://code.google.com/p/android-mvc-framework/source/browse/tags/20120730_ver0.3/src/com/android_mvc/framework/db/dao/BaseDAO.java</a></blockquote>


<br>

<hr>

<br>

<h2>サンプルアプリの仕様</h2>

ver0.3に同梱されているサンプルの仕様を述べる。<br>
<br>
外部仕様：<br>
<br>
<ul><li>TOP画面から，DBを扱うサンプル，GPS系のサンプル，UI系のサンプル等に遷移できる。</li></ul>

<ul><li>TOP画面で「サービスを起動」すると，現在位置の記録が始まる。</li></ul>

<ul><li>GoogleMap表示画面において，取得される位置情報には２パターンある。<br>
<ul><li>「現在地を追跡」のトグルボタンがONである場合，マップ自体がGPS通信を行ない，現在地を取得する。取得された情報は，マップの中心地点を移動させるために利用される。<br>
</li><li>現在地を取得するためのサービスが起動している場合，マップとは別に，サービス自身がGPS通信を行なう。取得された情報は，マップ上に現在地のアイコンを表示するために利用される。</li></ul></li></ul>

<ul><li>端末の電源を再起動すると，起動時にサービスが自動起動する。アプリのTOP画面でサービスの常駐をOFFにすれば，サービスは停止する。<br>
<ul><li>端末がブートするたびにサービスが起動するのが邪魔な場合は，サービスが自動起動しないようにマニフェストXML等を書き換えるか，サンプルアプリをアンインストールすること。</li></ul></li></ul>

内部仕様：<br>
<br>
<ul><li>AppSettingsにおいて，デバッグモードの指定があるため，アプリ起動のたびにDBを初期化するようになっている。DBに登録された情報を消さずに取っておきたい場合は，デバッグモードをOFFにすること。</li></ul>

<ul><li>プロジェクトがGoogleMapを利用しない場合，Googleの提供するマップ系APIを参照しているパッケージはEclipse上でエラーになる。利用しないなら，削除して構わない。</li></ul>



<br>

<hr>

<br>

<h2>結び</h2>

以上が，ver0.3における改良点のサマリである。<br>
<br>
<br>
今回の目玉を要約すると，<br>
<br>
<ul><li>HTML5 を含む，セレクタブルなUI実装方法。</li></ul>

<ul><li>MapとGPSが簡単に使えるようになった。</li></ul>

<ul><li>常駐サービスのひな型。</li></ul>

という事になる。<br>
<br>
<br>
加えて，M・V・Cの全レイヤについて，細かな点で改良が図られ，実際のアプリケーション開発に役立つ仕掛けが詰め込まれているのが分かるはずだ。<br>
<br>
<br>
本フレームワークは，「スタンダードな設計パターンや開発プロセス」を提唱しつつも，ユーザに対して方式を選択する余地を残している。<br>
<br>
ユーザの現実のニーズに応えつつも，ユーザに新たな発想法を提供しようと試みているのである。<br>
<br>
<br>
ver1.0出現の日は，そう遠くない。<br>
<br>
<br>
<br>
<br>

<hr>

<br>

<h2>補足</h2>

ver0.3のJavaDocを，オンラインで閲覧できるように公開した。<br>
<br>
<a href='http://android-mvc-framework.googlecode.com/svn/tags/20120730_ver0.3/doc/index.html'>http://android-mvc-framework.googlecode.com/svn/tags/20120730_ver0.3/doc/index.html</a>



　
# ver0.2の特徴：概観 #

ver0.2では，MVCフレームワークとしての基本構造が，よりいっそう強固になった。
<br><br>

全体のアーキテクチャは，ver0.1の時とほぼ同様である。（※<a href='ver01ArchitecturesAndDesignsSummary.md'>こちらのページ</a>のクラス図をご覧いただきたい。）<br>
<br><br>

このアーキテクチャを基に，ver0.2では<br>
<br>
<ul><li>M・V・Cの各 <b>レイヤ内部</b> の改良</li></ul>

<ul><li>M・V・Cの各 <b>レイヤ間連携</b> の改良</li></ul>

が施されている。<br>
<br><br>

各レイヤごとに，機能面での主な改良点を取り上げる。<br>
<br>
<ul><li><b>View層</b>
<ul><li>オプションメニューを，気軽に構築可能になった。<br>
</li><li>タブレイアウトを，気軽に実現可能になった。<br>
</li><li>文字列リソースの参照が極めて容易になった。</li></ul></li></ul>

<ul><li><b>Controller層</b>
<ul><li>より柔軟な制御フロー・処理フローを，より明快・簡潔なコードで実現可能になった。<br>
</li><li>バリデーション・ロジックが，きわめて簡潔に記述できるようになった。<br>
</li><li>エンティティを含む任意の自作オブジェクトが，Intent経由で気軽に運搬可能になった。</li></ul></li></ul>


<ul><li><b>Model層</b>
<ul><li>ORMの機能が強化された。CRUDの処理は最初から組み込み済みになった。</li></ul></li></ul>

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

<h1>ver0.2の特徴：詳細</h1>

説明の順番としては，V→C→Mの順に見てゆくのが一番わかりやすい。<br>
<br>
（このベクトルが，システムの外側から内側へ向かうため。）<br>
<br>
<br>
<br>
<hr><br>
<br>
<br>

<h2>View層：</h2>

<h2>■オプションメニューを，気軽に構築可能になった。</h2>

下記のようなコードをActivityに記述して，動的にオプションメニューを構築可能である。<br>
<br>
<pre><code>        // オプションメニューを構築<br>
        return new OptionMenuBuilder(context)<br>
            .add(<br>
                new OptionMenuDescription()<br>
                {<br>
                    @Override<br>
                    protected String displayText() {return "DB登録";}<br>
<br>
                    @Override<br>
                    protected void onSelected() {<br>
                        // 画面遷移<br>
                        MainController.submit(activity, "EDIT_DB");<br>
                    }<br>
                }<br>
            )<br>
            .add(<br>
                new OptionMenuDescription()<br>
                {<br>
                    @Override<br>
                    protected String displayText() {return "DB閲覧";}<br>
<br>
                    @Override<br>
                    protected void onSelected() {<br>
                        // 画面遷移<br>
                        MainController.submit(activity, "VIEW_DB");<br>
                    }<br>
                }<br>
            )<br>
        ;<br>
</code></pre>

※オプションが選択された時には，Controllerを呼び出し，画面遷移などを実行させている。<br>
<br>
<blockquote>引用元：TopActivity.java<br>
<a href='http://code.google.com/p/android-mvc-framework/source/browse/tags/20120323_ver0.2/src/com/android_mvc/sample_project/activities/main/TopActivity.java'>http://code.google.com/p/android-mvc-framework/source/browse/tags/20120323_ver0.2/src/com/android_mvc/sample_project/activities/main/TopActivity.java</a></blockquote>


<h2>■タブレイアウトを，気軽に実現可能になった。</h2>

下記のようなコードをActivityに記述して，動的にタブレイアウトを構築可能である。<br>
<br>
<pre><code>        // タブの定義を記述する。<br>
        new TabHostBuilder(context)<br>
            .setChildActivities( FuncDBController.getChildActivities(this) )<br>
            .add(<br>
                new TabDescription("TAB_EDIT_DB")<br>
                    .text("DB登録")<br>
                    .icon(android.R.drawable.ic_menu_add)<br>
                ,<br>
<br>
                new TabDescription("TAB_VIEW_DB")<br>
                    .text("DB閲覧")<br>
                    .icon(android.R.drawable.ic_menu_agenda)<br>
                ,<br>
<br>
                new TabDescription("TAB_FUNC_NET")<br>
                    .text("通信")<br>
                    .noIcon()<br>
<br>
            )<br>
            .display()<br>
        ;<br>
</code></pre>

各タブの中には，コンテンツとしてActivityを表示できる。<br>
<br>
<blockquote>引用元：SampleTabHostActivity.java<br>
<a href='http://code.google.com/p/android-mvc-framework/source/browse/tags/20120323_ver0.2/src/com/android_mvc/sample_project/activities/func_db/SampleTabHostActivity.java'>http://code.google.com/p/android-mvc-framework/source/browse/tags/20120323_ver0.2/src/com/android_mvc/sample_project/activities/func_db/SampleTabHostActivity.java</a></blockquote>


つまりView層では，一般のView部品に加えて，メニューやタブも動的に手軽に構築できるようになった。<br>
<br>
レイアウトXMLに手を加える必要は一切ない。<br>
<br>
<br>
<h2>■文字列リソースの参照が極めて容易になった。</h2>

文字列リソースはRクラスから取得するわけだが，その手間は，もはや手間ではなくなった。<br>
<br>
Activity上のサンプルコード：<br>
<br>
<pre><code>              tv2 = new MTextView(context)<br>
                .text("このアプリの名称：" + $._(R.string.app_name) )<br>
                .widthWrapContent()<br>
</code></pre>

このアイデアの着想については，下記のページを参照のこと。<br>
型安全な方を採用してある。<br>
<br>
<blockquote>Androidアプリで，<code>_</code>("リソース名") と書くだけで，簡単に文字列を参照しよう<br>
<a href='http://d.hatena.ne.jp/language_and_engineering/20110815/p1'>http://d.hatena.ne.jp/language_and_engineering/20110815/p1</a></blockquote>


なお「$」は，CommonActivityUtilのインスタンスである。<br>
<br>
ver0.1では頭文字を取って「cau」という変数名で参照可能だったが，ver0.2ではこのように簡略化された。<br>
<br>
View層の便利オブジェクトとして「$」という変数を利用する，という習慣は，特にWebアプリケーション開発に携わってきた者であれば，非常に親しみやすいだろう。<br>
<br>
「$」は，今後もどんどん便利化していく計画である。<br>
<br>
<br>

<hr>

<br>


<h2>Controller層：</h2>

<h2>■より柔軟な制御フロー・処理フローを，より明快・簡潔なコードで実現可能になった。</h2>

コントローラ層では，処理フローが下記のように明確化された。<br>
<br>
<ul><li>もし必要なら，Activityから受け取った値の <b>バリデーション。</b></li></ul>

<ul><li>もしバリデーションを通過すれば， <b>ビジネスロジック（Action）</b> を実行。</li></ul>

<ul><li>ビジネスロジックの実行が完了したら，実行結果に基づいて， <b>遷移先の画面へルーティング。</b></li></ul>

<ul><li>最後に， <b>UI上で後処理</b> を実行。<br>
<br></li></ul>

これが，コントローラ層の要となるフローである。<br>
<br>
もちろん，複雑な処理が無い場合は，ただ単に「Intentにデータを詰めて画面遷移するだけ」というのも可能。<br>
<br>

サンプルを掲載する。<br>
<br>
まずは，単純に画面遷移するだけのコード。Routerクラスが活躍する。<br>
<br>
<pre><code>    /**<br>
     * TOP画面からの遷移時<br>
     */<br>
    public static void submit(TopActivity activity, String button_type) {<br>
        if( "EDIT_DB".equals(button_type) )<br>
        {<br>
            // 編集画面へ<br>
            Router.goWithData(activity, DBEditActivity.class,<br>
                new Intent().putExtra("hoge", "Intentで値を渡すテスト").putExtra("fuga", 1)<br>
            );<br>
        }<br>
        else<br>
        if( "VIEW_DB".equals(button_type) )<br>
        {<br>
            // 一覧画面へ<br>
            Router.go(activity, DBListActivity.class);<br>
        }<br>
        else<br>
        if( "TAB_SAMPLE".equals(button_type) )<br>
        {<br>
            // タブ画面へ<br>
            Router.go(activity, SampleTabHostActivity.class);<br>
        }<br>
    }<br>
</code></pre>

goなら遷移するだけ。goWithDataなら，インテントにデータを詰め込んで運搬できる。<br>
<br>
<blockquote>引用元：MainController.java<br>
<a href='http://code.google.com/p/android-mvc-framework/source/browse/tags/20120323_ver0.2/src/com/android_mvc/sample_project/controller/MainController.java'>http://code.google.com/p/android-mvc-framework/source/browse/tags/20120323_ver0.2/src/com/android_mvc/sample_project/controller/MainController.java</a></blockquote>


次に，バリデーション＋ビジネスロジック＋ルーティング＋UI後処理　というフローをフルに活用しているサンプルを掲載する。<br>
<br>
<pre><code>    /**<br>
     * DB登録画面からの遷移時<br>
     */<br>
    public static void submit(final DBEditActivity activity)<br>
    {<br>
        new ControlFlowDetail&lt;DBEditActivity&gt;( activity )<br>
            .setValidation( new ValidationExecutor(){<br>
                @Override<br>
                public ValidationResult doValidate()<br>
                {<br>
                    // バリデーション処理<br>
                    return new FuncDBValidation().validate( activity );<br>
                }<br>
<br>
                @Override<br>
                public void onValidationFailed()<br>
                {<br>
                    showErrMessages();<br>
<br>
                    // バリデーション失敗時の遷移先<br>
                    //goOnValidationFailed( DBEditActivity.class );<br>
                    stayInThisPage();<br>
                }<br>
            })<br>
            .setBL( new BLExecutor(){<br>
                @Override<br>
                public ActionResult doAction()<br>
                {<br>
                    // BL<br>
                    return new DBEditAction( activity ).exec();<br>
                }<br>
            })<br>
            .onBLExecuted(<br>
                // BL実行後の遷移先の一覧<br>
                new RoutingTable().map("success", DBListActivity.class )<br>
<br>
                // onBLExecutedにこれを渡せば，BLの実行結果にかかわらず画面遷移を常に抑止。<br>
                //STAY_THIS_PAGE_ALWAYS<br>
<br>
                // BL実行結果が特定の状況のときのみ，画面遷移を抑止することも可能。<br>
                //new RoutingTable().map("success", STAY_THIS_PAGE )<br>
<br>
            )<br>
            .startControl();<br>
        ;<br>
<br>
    }<br>
</code></pre>

<blockquote>引用元：FuncDBController.java<br>
<a href='http://code.google.com/p/android-mvc-framework/source/browse/tags/20120323_ver0.2/src/com/android_mvc/sample_project/controller/FuncDBController.java'>http://code.google.com/p/android-mvc-framework/source/browse/tags/20120323_ver0.2/src/com/android_mvc/sample_project/controller/FuncDBController.java</a></blockquote>


バリデーションは行なわないで済ませる，等の柔軟な書き変えも可能。<br>
<br>
<pre><code>    /**<br>
     * HTTP通信画面からの遷移時<br>
     */<br>
    public static void submit(final HttpNetActivity activity)<br>
    {<br>
        new ControlFlowDetail&lt;HttpNetActivity&gt;( activity )<br>
            .setValidation( new ValidationExecutor(){<br>
                @Override<br>
                public ValidationResult doValidate()<br>
                {<br>
                    // バリデーション処理<br>
                    return new FuncNetValidation().validate( activity );<br>
                }<br>
<br>
                @Override<br>
                public void onValidationFailed()<br>
                {<br>
                    showErrMessages();<br>
<br>
                    // バリデーション失敗時の遷移先<br>
                    stayInThisPage();<br>
                }<br>
            })<br>
            .setBL( new BLExecutor(){<br>
                @Override<br>
                public ActionResult doAction()<br>
                {<br>
                    // BL<br>
                    return new HttpNetAction( activity ).exec();<br>
                }<br>
            })<br>
            .onBLExecuted(<br>
                // BL実行後の遷移先<br>
                STAY_THIS_PAGE_ALWAYS<br>
            )<br>
            .startControl();<br>
        ;<br>
<br>
    }<br>
</code></pre>

<blockquote>引用元：FuncNetController.java<br>
<a href='http://code.google.com/p/android-mvc-framework/source/browse/tags/20120323_ver0.2/src/com/android_mvc/sample_project/controller/FuncNetController.java'>http://code.google.com/p/android-mvc-framework/source/browse/tags/20120323_ver0.2/src/com/android_mvc/sample_project/controller/FuncNetController.java</a></blockquote>


もし，ビジネスロジックを実行した後で，その結果を参照しつつUI操作を実行したい場合は，Activity側に下記のようなメソッドを置くだけで実行される。<br>
<br>
ここでは，HTTP通信ロジックの処理結果を参照している。<br>
<br>
<pre><code>    @Override<br>
    public void afterBLExecuted(ActionResult ares)<br>
    {<br>
        UIUtil.longToast(this, "通信処理が完了しました。");<br>
<br>
        // 通信の結果を表示<br>
        HttpPostResponse response = (HttpPostResponse)ares.get("http_response");<br>
        if( response.isSuccess() )<br>
        {<br>
            tv2.setText( response.getText() );<br>
        }<br>
        else<br>
        {<br>
            tv2.setText( response.getErrMsg() );<br>
        }<br>
    }<br>
</code></pre>

<blockquote>引用元：HttpNetActivity.java<br>
<a href='http://code.google.com/p/android-mvc-framework/source/browse/tags/20120323_ver0.2/src/com/android_mvc/sample_project/activities/func_net/HttpNetActivity.java'>http://code.google.com/p/android-mvc-framework/source/browse/tags/20120323_ver0.2/src/com/android_mvc/sample_project/activities/func_net/HttpNetActivity.java</a></blockquote>


なお，コントローラの処理フローは，UIスレッドとは別のスレッド上で非同期で行なわれる。<br>
<br>
したがって，その上に記述されるDB操作やHTTP通信などのビジネスロジックは，非同期を意識することなく，全く同期的に（単一スレッドモデルとして）記述できる。<br>
<br>
<br>
<h2>■バリデーション・ロジックが，きわめて簡潔に記述できるようになった。</h2>

アクティビティ上の値の妥当性を検証するために，ユーザが記述するコードの量が，劇的に減った。<br>
<br>
<pre><code>    /**<br>
     * DB登録画面での入力値を検証<br>
     */<br>
    public ValidationResult validate(DBEditActivity activity)<br>
    {<br>
        initValidationOf(activity);<br>
<br>
        assertNotEmpty("name");<br>
<br>
        assertNotEmpty("age");<br>
        assertValidInteger("age");<br>
        assertNumberOperation("age", greaterThan(0));<br>
<br>
        return getValidationResult();<br>
    }<br>
</code></pre>

JUnit等でおなじみの，assert系のメソッドを並べるだけでいい。<br>
<br>
このようにバリデーションメソッドが共通化された結果，<br>
<br>
「○○は△△で入力してください。」のようなエラーメッセージは，フレームワーク内部で自動的に構築される事になった。<br>
<br>
よって，検証処理自体も，検証結果の通知処理も，大幅に実装の手間が減った。<br>
<br>
<blockquote>引用元：FuncDBValidation.java<br>
<a href='http://code.google.com/p/android-mvc-framework/source/browse/tags/20120323_ver0.2/src/com/android_mvc/sample_project/controller/FuncDBValidation.java'>http://code.google.com/p/android-mvc-framework/source/browse/tags/20120323_ver0.2/src/com/android_mvc/sample_project/controller/FuncDBValidation.java</a></blockquote>


ユーザが自由にバリデーションメソッドを追加する事も可能である。<br>
<br>
<br>
なお，本機能のアイデアの着想については，下記ページの情報を参照のこと。<br>
<br>
<blockquote>「バリデーション」APIと「単体テスト」APIの類似性，およびそのスタイルが時代と共に洗練される過程の概観<br>
<a href='http://d.hatena.ne.jp/language_and_engineering/20120320/p1'>http://d.hatena.ne.jp/language_and_engineering/20120320/p1</a></blockquote>


<h2>■エンティティを含む任意の自作オブジェクトが，Intent経由で気軽に運搬可能になった。</h2>

IntentPortable（インテント経由で運搬可能である事を表すためのインタフェース）が導入された。<br>
<br>
ユーザは任意のオブジェクトに対して，「implements IntentPortable」するだけで，そのオブジェクトをIntent経由で運搬可能になる。<br>
<br>
エンティティやバリデーション結果など，フレームワーク内の主要なオブジェクトは，自動的に「IntentPortable」になる。<br>
<br>
その結果，画面連携の負担が大幅に削減される。<br>
<br>

下記にサンプルコードを掲載する。<br>
<br>
<br>
Activity上で，送られてきたIntentからデータを取り出す側のコード：<br>
<br>
<pre><code>        if( $.actionResultHasKey( "new_friend_obj" ) )<br>
        {<br>
            // Intentから情報を取得<br>
            Friend f = (Friend)($.getActionResult().get("new_friend_obj"));<br>
<br>
            // UIに表示<br>
            tv2.text(f.getName() + "さんが，たった今新規登録されました。").visible();<br>
        }<br>
</code></pre>

ビジネスロジックの実行結果やバリデーションの実行結果は，遷移先の画面に向かう際に，自動的にIntentの中に格納される。<br>
<br>
つまり，処理の結果を遷移先の画面に運搬したい場合，ビジネスロジックの実行結果（ActionResultオブジェクト）に詰め込んでおけばよい。<br>
ということになる。<br>
<br>
もし手動で任意のオブジェクトを格納したい場合は，上述の通り，Router#goWithData などのメソッドを使えばよい。<br>
<br>
<br>
<blockquote>引用元：DBListActivity.java<br>
<a href='http://code.google.com/p/android-mvc-framework/source/browse/tags/20120323_ver0.2/src/com/android_mvc/sample_project/activities/func_db/DBListActivity.java'>http://code.google.com/p/android-mvc-framework/source/browse/tags/20120323_ver0.2/src/com/android_mvc/sample_project/activities/func_db/DBListActivity.java</a></blockquote>


本機能の着想については，下記ページの情報を参照のこと。<br>
<br>
<blockquote>Android SDKの，ParcelableとSerializableの違いを比較 - Intentで独自オブジェクトを運搬する際，役立つのはどちら？<br>
<a href='http://d.hatena.ne.jp/language_and_engineering/20120313/p1'>http://d.hatena.ne.jp/language_and_engineering/20120313/p1</a></blockquote>


<br>

<hr>

<br>


<h2>Model層</h2>

<h2>■ORMの機能が強化された。CRUDの処理は最初から組み込み済みになった。</h2>

RDBの操作は，CRUDのいずれも極めてシンプルかつ明快なコードになった。<br>
<br>
下記のサンプルを見れば，一目瞭然であろう。<br>
<br>
<pre><code><br>
    // ------------ C --------------<br>
<br>
<br>
    /**<br>
     * 1人の友達を保存。<br>
     */<br>
    public Friend create(String name, Integer age, Boolean favoriteFlag)<br>
    {<br>
        // 論理エンティティを構築<br>
        Friend f = new Friend();<br>
        f.setName(name);<br>
        f.setAge( age );<br>
        f.setFavorite_flag( favoriteFlag );<br>
<br>
        // DB登録<br>
        f.save(helper);<br>
<br>
        return f;<br>
    }<br>
<br>
<br>
    // ------------ R --------------<br>
<br>
<br>
    /**<br>
     * 友達を全て新しい順に返す。<br>
     */<br>
    public ArrayList&lt;Friend&gt; findAll()<br>
    {<br>
        return findAll(helper, Friend.class);<br>
    }<br>
<br>
<br>
    /**<br>
     * 特定のIDの友達を１人返す。<br>
     */<br>
    public Friend findById(Long friend_id)<br>
    {<br>
        return findById( helper, Friend.class, friend_id );<br>
    }<br>
<br>
        // NOTE: 細かい条件で検索したい場合は，Finderを利用すること。<br>
        // findAllやfindByIdの実装を参照。<br>
<br>
<br>
    // ------------ U --------------<br>
<br>
<br>
    /**<br>
     * 既存の友達のお気に入り状態を反転させる。<br>
     */<br>
    public Friend invertFavoriteFlag( Long friend_id )<br>
    {<br>
        // idをもとに検索<br>
        Friend f = findById( friend_id );<br>
<br>
        // フラグを反転する<br>
        f.setFavorite_flag( ! f.getFavorite_flag() );<br>
<br>
        // DB更新<br>
        f.save(helper);<br>
<br>
        return f;<br>
    }<br>
<br>
<br>
    // ------------ D --------------<br>
<br>
<br>
    /**<br>
     * 特定のIDの友達を削除。<br>
     */<br>
    public void deleteById( Long friend_id )<br>
    {<br>
        Friend f = findById(friend_id);<br>
<br>
        // DBからの削除を実行<br>
        f.delete(helper);<br>
    }<br>
<br>
</code></pre>

DAO自体が，findAll() とかfindById()みたいなメソッドを持つようになった。<br>
<br>
またエンティティ自体，save()で新規登録と更新の両方が可能になり，delete()メソッドで自身の削除も可能となった。<br>
<br>
DB操作のためのユーザのコード記述量は，圧倒的に削減された。<br>
<br>
<blockquote>引用元：FriendDAO.java<br>
<a href='http://code.google.com/p/android-mvc-framework/source/browse/tags/20120323_ver0.2/src/com/android_mvc/sample_project/db/dao/FriendDAO.java'>http://code.google.com/p/android-mvc-framework/source/browse/tags/20120323_ver0.2/src/com/android_mvc/sample_project/db/dao/FriendDAO.java</a></blockquote>


それだけではなく，SELECT文を発行する際には，細かい条件を指定する事も可能である。<br>
<br>
findAll()やfindById()の実装を見ればわかる。<br>
<br>
<pre><code>    /**<br>
     * レコードを全て新しい順に返す。<br>
     */<br>
    public ArrayList&lt;T&gt; findAll(DBHelper helper, Class&lt;T&gt; entity_class)<br>
    {<br>
        // 有効な湯キーを持つ全件を降順に<br>
        return new Finder&lt;T&gt;(helper)<br>
            .where("id &gt; 0")<br>
            .orderBy("id DESC")<br>
            .findAll(entity_class)<br>
        ;<br>
    }<br>
<br>
</code></pre>

ユーザは，このコードを模倣して，任意の検索条件を持った検索メソッドを実装可能である。<br>
<br>
<blockquote>引用元：BaseDAO.java<br>
<a href='http://code.google.com/p/android-mvc-framework/source/browse/tags/20120323_ver0.2/src/com/android_mvc/framework/db/dao/BaseDAO.java'>http://code.google.com/p/android-mvc-framework/source/browse/tags/20120323_ver0.2/src/com/android_mvc/framework/db/dao/BaseDAO.java</a></blockquote>

なお，このFinderオブジェクトの考案にあたっては，Ruby on Rails3のActiveRecordのエンジン「Arel」を意識した。<br>
<br>
<br>
<br>

<hr>

<br>


<h2>結び</h2>

以上が，ver0.2における改良点のサマリである。<br>
<br>
M・V・Cの全レイヤについて，DSLが提供され，ユーザのコード記述量が減り，生産性が劇的に向上しているのを分かって頂ける事と思う。<br>
<br>
<br>
<br>
ver0.1は「MVCの骨組み」であった。<br>
<br>
ver0.2は，MVCの構造がよりいっそう強固になり，Androidアプリ開発における「スタンダードな設計方針」を示し，それを理想的な実装コードの形態で具現化した。<br>
<br>

この調子でバージョンアップを重ねていけば，あたかも既存の部品を手早く組み合わせるだけで素早くアプリを完成できるような，魅力的なフレームワークになってゆくのは間違いない。<br>
<br>
ぜひ，次バージョンも楽しみにして頂きたい。<br>
<br>
<br>
<br>
　
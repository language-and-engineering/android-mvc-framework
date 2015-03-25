<br>

<h1>ver0.1 / 導入手順</h1>

本フレームワークに付属するサンプルアプリを，３ステップで動作させる。<br>
<br>
ver0.1, ver0.2で共通。<br>
<br>
<br>

Androidアプリの開発環境をセットアップし，<br>
<br>
サンプルが実機上またはエミュレータ上で仕様通りに動作するのを確認するところまで。<br>
<br>
<br>

もし，既に開発環境が整っていれば，数分で終了する。<br>
<br>
<br>
<br>
<br>
<hr><br>
<br>
<br>
<br>
<h2>（１）Androidアプリの開発環境を構築しよう</h2>

下記のページの手順を実行し，１時間でAndroidアプリの開発環境を構築完了する。<br>
<br>
<blockquote>今から１時間で，Androidアプリの開発環境を構築し，Windows上でサンプルを動作させる手順<br>
<a href='http://d.hatena.ne.jp/language_and_engineering/20110724/p1'>http://d.hatena.ne.jp/language_and_engineering/20110724/p1</a></blockquote>

<br>
<br>
<br>
<hr><br>
<br>
<br>
<br>
<h2>（２）本フレームワークを導入しよう</h2>

<ul><li>Eclipse上で，新規Androidプロジェクトを作成する。プラットフォームバージョンは2.2以上。</li></ul>

<ul><li>本サイトのトップページを参考に，SVNもしくはzip形式で，フレームワークのソースコードを取得する。</li></ul>

<ul><li>取得したフレームワークには，下記のものが同梱されている。これらを，Eclipse上で新規プロジェクト上に上書きする。<br>
<ul><li>srcフォルダ。<br>
</li><li>resフォルダ。<br>
</li><li>assetsフォルダ。<br>
</li><li>AndroidManifest.xml</li></ul></li></ul>

<ul><li>以上で，導入は完了。</li></ul>


<br>
<br>
<br>
<hr><br>
<br>
<br>
<br>
<h2>（３）フレームワークに付属するサンプルを動かそう</h2>

（２）で作成したプロジェクトを，エミュレータか，もしくは実機上で実行する。<br>
<br>
<br>
サンプルとして，「友達の情報」を管理するためのミニアプリが動作する。<br>
<br>
<br>
<br>

このサンプルアプリは，下記のような仕様である。　仕様に対応するコードの存在箇所も，併せて解説する。<br>
<br>
<ul><li>初回起動時に，インストール処理が実行される。DBの初期化など。<br>
<ul><li>インストールに関る設定項目は，com.android_mvc.sample_project.common.AppSettingクラス内に記述してあり，書き換えれば振る舞いを変更できる。ソースコードを参照。<br>
</li><li>インストール処理を行なうアクティビティは，com.android_mvc.sample_project.activities.installation.InstallAppActivityである。<br>
<br></li></ul></li></ul>

<ul><li>インストール完了後には，下記のUIが表示される。<br>
<ul><li>「おめでとうございます。インストールが完了しました。」という文言。<br>
</li><li>画面遷移を行なうためのボタン。押下すれば，トップ画面に遷移する。<br>
<ul><li>ここで知っておくべきことは，この画面はレイアウトXMLで定義されているのではなく，com.android_mvc.sample_project.activities.installation.InstallCompletedActivity内で動的に構築されている。という点である。ソースコードを参照。<br>
<br></li></ul></li></ul></li></ul>

<ul><li>トップ画面では，Toastを表示するボタンと，DB登録画面へ遷移するボタンが表示される。<br>
<ul><li>このUIも動的に構築されている。<br>
<br></li></ul></li></ul>

<ul><li>DB登録画面では，登録したい友達の情報を入力する。<br>
<ul><li>入力に不備があれば，登録実行時にバリデーションではじかれて，エラーメッセージが表示される。<br>
</li><li>名前も年齢もちゃんと入力すれば，入力情報は，端末内のDBに登録される。<br>
<ul><li>該当するアクティビティ（DBEditActivity）内で 「new Controller().submit(activity);」された後の，一連の処理フローを追ってみるとよい。Controllerの中に，画面遷移の制御フローが記述されている。<br>
</li><li>また，DBにCRUDする時には，Java用の「論理エンティティ」と，SQLite用の「物理エンティティ」の間で，オブジェクトの変換が行なわれている。詳細は，com.android_mvc.sample_project.db.entity.Friendクラスの内部を熟読のこと。これが，SQLite向けのORマッピングの手段となる。<br>
<br></li></ul></li></ul></li></ul>

<ul><li>DB参照画面に遷移する。ここでは，DB登録した友達情報の一覧が，リスト形式で表示される。<br>
<ul><li>このUIは，Adapterを使っていない。forループで作っているのである。Activity内のUI構築処理を参照。<br>
<br></li></ul></li></ul>

<ul><li>ここまでの過程で，EclipseのLogCatビュー上に，デバッグしやすい詳細なログが出力されていることにも注目。</li></ul>

<br>
<br>

以上のような，とてもシンプルな仕様である。<br>
<br>
<br>
このサンプルアプリの価値は，下記の点にある。<br>
<br>
<ul><li>開発用のフレームワーク上で構築されているので，アプリの実現のためにエンジニアがコーディングするコード量が少ない。<br>
<ul><li>デベロッパは低レイヤのAPIを意識する必要が無くなり，アプリの仕様の記述に専念できる。<br>
<br></li></ul></li></ul>

<ul><li>ユーザ定義アプリは，フレームワークが提供する「DSL」で実装できるため，さらにコード量が少なくなる。現時点では特に，C層やV層。<br>
<ul><li>結果として，コードが仕様を反映するようになる。（コードがドキュメントになってくれる。）<br>
<br></li></ul></li></ul>

<ul><li>MVCアーキテクチャ上でクラス設計されている。<br>
<ul><li>そのため，クラスの責任分担や配置・結合の仕方で迷う事がなく，メンテナンス・拡張・キャッチアップ・テストしやすい。設計方針および実装の道具が既に与えられているので，プロトタイピングや本番アプリの実装にすぐに取り掛かれる。</li></ul></li></ul>

<br>
<br>
<br>
<hr><br>
<br>
<br>
<br>
<h2>その後</h2>

サンプルアプリ内の処理フローを追ってみて，フレームワークがどのように負担を軽減しているのか，コードの流れを追って理解してみる事をお勧めしたい。<br>
<br>
<br>

　
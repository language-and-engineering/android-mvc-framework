<br>

<h1>ver0.1 / 全体のアーキテクチャと設計</h1>


本フレームワークの，現時点での全体アーキテクチャ。<br>
<br>
<br>
<br>
<br>
<hr><br>
<br>
<br>
<br>
<h2>フレームワークの全体像</h2>

設計の概要を以下に示す。<br>
<br>
<ul><li>フレームワークの基本構造として，<b>MVCパターン</b>を採用している。</li></ul>

<ul><li>各レイヤ内では，各種の<b>デザインパターン</b>を採用している。</li></ul>

<ul><li>その他，可能な限り<b>デファクトスタンダード</b>な手法を盛り込んでいる。<br>
<ul><li>したがって，習得しやすく，利用しやすく，メンテナンスしやすい。</li></ul></li></ul>

<ul><li>しかし同時に<b>「モダンな発想」</b>を意欲的に取り入れている。<br>
<ul><li>したがって，<b>スピーディーで柔軟なAndroidアプリ開発</b>が可能になる。</li></ul></li></ul>

<br>

設計の全体像を要約した簡易クラス図は，下記の通り。<br>
<br>
<img src='http://android-mvc-framework.googlecode.com/files/ver0.1%E3%82%AF%E3%83%A9%E3%82%B9%E6%A6%82%E8%A6%81.png' />

※「Downloads」タブより，この図の原本をダウンロードできる。<br>
<br>

この図の見方：<br>
<br>
<ul><li>図の下部で，７割ほどのエリアを占めているのが，本フレームワークのコア・ライブラリである。</li></ul>

<ul><li>図の上部で，３割ほどのエリアを占めているのが，フレームワークを利用して開発されるユーザ側アプリである。</li></ul>

<ul><li>本フレームワークが相当な厚みの下部レイヤをカバーしているので，上位層を実装するユーザの負担が，かなり軽減される。　…という点が伝わるはずだ。</li></ul>

<ul><li>フレームワーク層にも，ユーザアプリ層にも，M・V・Cの分割が施されている。左から順に，V(iew)，C(ontroller)，M(odel) である。<br>
<ul><li>各パッケージ間が疎結合である様子もご理解頂けるだろう。</li></ul></li></ul>


<br>
<br>


次いで，M・V・Cの各層の特徴的な機能について，さらっとフィーチャーしておく。<br>
<br>
<br>

まずView層では，<b>「jQueryスタイルの，エレガントでスピーディーなコーディング」</b>が可能である。<br>
<br>
クラスとしては，UIBuilderあたりが担当している。<br>
<br>
この機能の詳細については，サンプルアプリケーションを参照。（<a href='http://code.google.com/p/android-mvc-framework/source/browse/tags/20120215_ver0.1/src/com/android_mvc/sample_project/activities/main/TopActivity.java'>ここ</a>とか<a href='http://code.google.com/p/android-mvc-framework/source/browse/tags/20120215_ver0.1/src/com/android_mvc/sample_project/activities/func_db/DBListActivity.java'>ここ</a>）<br>
<br>
また，この機能の試作品については，下記のページを参照。<br>
<br>
<blockquote>Androidアプリの画面レイアウトを，まるでjQueryのようなコードで動的構築できるライブラリ　（の試作品。UIコーディングのためのDSL）<br>
<a href='http://d.hatena.ne.jp/language_and_engineering/20120210/p1'>http://d.hatena.ne.jp/language_and_engineering/20120210/p1</a></blockquote>

<br>

さらにView層では，<b>レイアウトXMLの指定をする必要がない</b>。<br>
<br>
もしXMLが存在する場合，Activityのクラス名から，対応するXMLをommonActivityUtilが自動的に判断して描画する。（<a href='http://code.google.com/p/android-mvc-framework/source/browse/tags/20120215_ver0.1/src/com/android_mvc/framework/activities/CommonActivityUtil.java'>こいつ</a>）<br>
<br>
そのユーティリティクラスを参照するために，Activityの基底クラスの継承関係に工夫を凝らしてある。<br>
<br>
この機能の試作品については，下記のページを参照。<br>
<br>
<blockquote>Androidアプリで，レイアウト用XMLの名前をいちいち指定せずに，自動的に画面を描画させよう　（Rails風のCoCなレンダリング）<br>
<a href='http://d.hatena.ne.jp/language_and_engineering/20110910/p1'>http://d.hatena.ne.jp/language_and_engineering/20110910/p1</a></blockquote>

<br>
<br>

次にController層であるが，コントローラクラスの導入によって，<b>画面遷移などの制御に関する情報が集約された</b>。（<a href='http://code.google.com/p/android-mvc-framework/source/browse/tags/20120215_ver0.1/src/com/android_mvc/sample_project/controller/Controller.java'>こんな感じ</a>に記述）<br>
<br>
おかげで，アプリ全体の制御フローに関する記述が分散せずに，メンテナンス性やテスタビリティが向上する。<br>
<br>
この機能の試作品については，下記のページを参照。<br>
<br>
<blockquote>AndroidアプリにStrutsのようなコントローラを導入し，画面制御させるサンプルコード　（の試作品。バリデーションやビジネスロジックの骨組み）<br>
<a href='http://d.hatena.ne.jp/language_and_engineering/20120213/p1'>http://d.hatena.ne.jp/language_and_engineering/20120213/p1</a></blockquote>

<br>
<br>

さらに，C層やM層を横断して，本フレームワーク全体を支える存在となっているのが<b>非同期タスクの逐次実行ライブラリ</b>である。<br>
<br>
Androidアプリの最大の恥である「ANRダイアログ」が出ないように，DB参照などの重い処理は，UIとは別のスレッドで行なう必要がある。<br>
<br>
こういった非同期タスクをたくさん扱う必要がある場合，処理のフローが追いづらくなるが，これはタスクを逐次実行させれば問題を回避できる。<br>
<br>
図中では，AsyncTasksRunnerあたりが担当している。この<br>
クラスは，本フレームワークで最下層に位置する要であり，縁の下の力持ちである。（<a href='http://code.google.com/p/android-mvc-framework/source/browse/tags/20120215_ver0.1/src/com/android_mvc/framework/task/AsyncTasksRunner.java'>このクラス</a>）<br>
<br>
この機能の試作品については，下記のページを参照。<br>
<br>
<blockquote>Javaの非同期処理を，シングルスレッドのようにシンプルにコーディングするための設計パターン　（並列処理を逐次処理にする）<br>
<a href='http://d.hatena.ne.jp/language_and_engineering/20120205/p1'>http://d.hatena.ne.jp/language_and_engineering/20120205/p1</a></blockquote>

<br>
<br>

このような非同期処理の枠組みがあれば，<b>HTTP通信のタスク</b>は楽々動作する。<br>
<br>
図中では，HTTPPostTaskが該当する。（<a href='http://code.google.com/p/android-mvc-framework/source/browse/tags/20120215_ver0.1/src/com/android_mvc/framework/net/HttpPostTask.java'>これ</a>）<br>
<br>
この機能の試作品については，下記のページを参照。<br>
<br>
<blockquote>Androidアプリで，HTTP通信のPOSTリクエストをする汎用クラス （文字化け無し＋非同期タスク）<br>
<a href='http://d.hatena.ne.jp/language_and_engineering/20111121/p1'>http://d.hatena.ne.jp/language_and_engineering/20111121/p1</a></blockquote>

<br>
<br>

次に，Model層では，<b>SQLiteに特化したORマッピングの方法論</b>を考案している。<br>
<br>
SQLiteには型が少ないので，素の値をJavaで扱おうとすると非常にめんどい事になるから，工夫しているのだ。<br>
<br>
例えばJavaでBooleanの「論理値」であるフィールドは，SQLiteでは「integer」として実装する必要が生じる。<br>
<br>
その橋渡しをしているのが，この図のLogicalEntityとPhysicalEntityだ。<br>
<br>
詳細はサンプルアプリケーションを参照のこと。（<a href='http://code.google.com/p/android-mvc-framework/source/browse/tags/20120215_ver0.1/src/com/android_mvc/sample_project/db/dao/FriendDAO.java'>このソース</a>のコメントに注意）<br>
<br>
<br>
<br>

さらに，<b>アプリのインストール時の処理フロー</b>が一般化されており，DBを初期化する際の仕組みも整っている。<br>
<br>
これは，InstallAppActivityあたりが担当している。<br>
<br>
またDBを初期化するにあたって，Ruby on Railsのような<b>マイグレーション風の記述によって，テーブルを構築</b>する事が可能である。<br>
<br>
これはSchemaDefinitionが担当している。<br>
<br>
詳細はサンプルアプリケーションを参照のこと。（<a href='http://code.google.com/p/android-mvc-framework/source/browse/tags/20120215_ver0.1/src/com/android_mvc/sample_project/db/schema/SchemaDefinition.java'>このクラス</a>）<br>
<br>
<br>
よくコードを追ってみると，アプリ初期化判定部分で下記のページの仕組みが応用されている事に気づくだろう。<br>
<br>
<blockquote>AndroidアプリのSQLiteで，データベースの存在を判定する方法　（ローカルファイルにデータを永続化させる場合の，事前チェック処理）<br>
<a href='http://d.hatena.ne.jp/language_and_engineering/20111202/p1'>http://d.hatena.ne.jp/language_and_engineering/20111202/p1</a></blockquote>

<br>
<br>

ここまででM・V・Cを横断したが，全てにおいて必要になるのが，<b>詳しいログ出力</b>である。<br>
<br>
本フレームワークのデバッグログは，呼び出し元のクラス名とメソッド名が自動的に付与されて出力される。<br>
<br>
これはBaseUtilが受け持っている。<br>
<br>
この機能の試作品については，下記のページを参照。<br>
<br>
<blockquote>制御しやすい「デバッグ用ロガー」を自作して，サクサク開発 （Javaで，メソッド名を含めログ出力する方法のサンプル）<br>
<a href='http://d.hatena.ne.jp/language_and_engineering/20120209/p1'>http://d.hatena.ne.jp/language_and_engineering/20120209/p1</a></blockquote>

<br>
<br>

機能はまだまだあるが，解説は以上とする。<br>
<br>
理由は下記の通り。<br>
<br>
<ul><li>サンプルのソースと，上記で引用したページの内容をよく読めば，十分理解できるから。JavaDocもしっかり書いてあるし。</li></ul>

<ul><li>今後のバージョンアップで，どんどん機能が進歩＋改良してゆくから。<br>
<ul><li>「I」と「Abstract」が一部混合しているのも，そのためである。Abstractで作っていたところ，Javaでは多重継承ができないという制限のために，やむをえず「I」に変えた。なんて事はしょっちゅうある。バージョンが進むにつれて，そういった命名面での整合性も確保してゆく。<br>
</li><li>ver 0.1では，これ以上手を加える必要はないと感じた。その暇があったら，さっさとver0.2を進めるのではないか。</li></ul></li></ul>

<br>

<br>
<br>
<br>
<hr><br>
<br>
<br>
<br>
<h2>クラスパッケージ</h2>

本フレームワーク内のJavaパッケージ構造を以下に示す。<br>
<br>
なお，ver0.1の段階では未実装のパッケージも含まれる。<br>
<br>
<br>
<pre>
com<br>
└─android_mvc<br>
├─framework              : フレームワークのルート。-------------- ↓ここからフレームワーク↓ --------------<br>
│  ├─activities<br>
│  │  └─installation   : アプリのインストール処理の基底<br>
│  │<br>
│  ├─annotations        : FW側のアノテーション<br>
│  │<br>
│  ├─bat                : Service関連<br>
│  │<br>
│  ├─common             : FW内の共通処理ユーティリティ<br>
│  │<br>
│  ├─controller         : FW側のC層の基底<br>
│  │<br>
│  ├─db                 : 永続化レイヤ関連の基底。RDBおよびプリファレンス<br>
│  │  │<br>
│  │  ├─dao            : データソースに接続するためのインタフェースの基底<br>
│  │  │<br>
│  │  ├─entity         : エンティティの基底<br>
│  │  │<br>
│  │  ├─schema         : スキーマ定義のユーティリティ<br>
│  │  │<br>
│  │  └─transaction    : トランザクション管理ユーティリティ<br>
│  │<br>
│  ├─map                : MapView利用のユーティリティ<br>
│  │<br>
│  ├─net                : NW通信のユーティリティ<br>
│  │<br>
│  ├─task               : 非同期タスクのユーティリティ<br>
│  │<br>
│  └─ui                 : UIのユーティリティ<br>
│      │<br>
│      └─view           : FW内の独自拡張View<br>
│<br>
│     ----------------------------------------------------------- ↑ここまでフレームワーク↑ --------------<br>
│<br>
└─sample_project -------------↓ここからは，フレームワークを利用した個別のアプリケーション↓--------------<br>
├─activities<br>
│  ├─func_db        : 機能名ごとにactivitiesパッケージ内を分割する。<br>
│  │<br>
│  ├─installation   : アプリのインストール処理関連<br>
│  │  │<br>
│  │  └─lib<br>
│  │<br>
│  └─main           : ここではトップ画面関連<br>
│<br>
├─annotations        : ユーザ定義アノテーション<br>
│<br>
├─bat                : Serviceの実装<br>
│<br>
├─common             : ユーザ定義の共通処理<br>
│<br>
├─controller         : C層の実装<br>
│<br>
├─db                 : 永続化レイヤの実装<br>
│  ├─dao            : DAOの実装<br>
│  │<br>
│  ├─entity         : 論理エンティティの実装<br>
│  │  │<br>
│  │  └─lib<br>
│  │<br>
│  └─schema         : RDBのスキーマ定義<br>
│<br>
└─domain             : ビジネスロジックの実装<br>
<br>
--------------------------------------------------- ↑ここまでが個別アプリケーション↑ --------------<br>
<br>
</pre>


ネーミングとしては，フレームワーク側とユーザ定義アプリ側で，接頭辞などを下記のように使い分けている。<br>
<br>
<ul><li>フレームワーク側：<br>
<ul><li>「FW」<br>
</li><li>「Base」<br>
</li><li>「I」「Abstract」</li></ul></li></ul>

<ul><li>ユーザ定義アプリ側：<br>
<ul><li>「AP」</li></ul></li></ul>


<br>
<br>
<br>
<hr><br>
<br>
<br>
<br>
<h2>今後</h2>

本フレームワークの設計は，ver0.2以降においても改良を重ねてゆく。<br>
<br>
<br>
<br>
　
# RAW_importer_for_Lr
<hr>

<h1>アプリ概要（About this App）</h1>
<p>
  このアプリはフリー版のLightroomでのカメラからのRAWを編集する事ができない問題を解決します。<br>
  <b>主な機能</b>
  <ol>
    <b>
      <li>無料版LightroomのRAW編集制限の解除</li>
      <li>カメラSDカードからの直接インポート＆変換</li>
      <li>Lightroomへ編集プリセットの追加</li>
    </b>
  </ol>
</p>
<p>
  このアプリの詳細については<a href = "https://play.google.com/store/apps/details?id=com.nasuka.rifl" target="_blank" rel="noopener noreferrer"><b>GooglePlayのストアページ</b></a>を参照してください。
</p>

<h1>このリポジトリについて</h1>
<h3>
  このリポジトリはこのアプリケーションを構成しているソースコードを明示的に公開することを目的として作られています。
</h3>
<p>
  このリポジトリの各ディレクトリには以下の内容が格納されています。
  <ul>
    <li>"RAW_importer_for_Lr/main/"　<b>アプリのソースコードとアセット</b></li>
    <li>"RAW_importer_for_Lr/release/"　<b>アプリケーション本体.apkファイル</b></li><br>
    <li>"RAW_importer_for_Lr/main/assets/"　<b>Lightroom編集プリセットの設定ファイル</b></li>
    <li>"RAW_importer_for_Lr/main/cpp/"　<b>C++ネイティブ拡張のソースコード（本アプリの動作では未使用）</b></li>
    <li>"RAW_importer_for_Lr/main/java/.../RAW_importer_for_Lr"　<b>本アプリのメインソースコード</b></li>
    <li>"RAW_importer_for_Lr/main/res/"　<b>本アプリに使用されるテキストデータや画像データなど</b></li>
</ul>
</p>

<h1>更新履歴</h1>
<h3>β1.4　　2020/12/1</h3>
<p>初回一般公開
  <ul>
    <b>
      <li>カメラRAW変換機能</li>
      <li>Lightroom起動ショートカット</li>
      <li>使い方Tips</li>
    </b>
  </ul>
</p>
<h3>β1.5　　2021/1/10</h3>
<p>機能追加アップデート
  <ul>
    <b>
      <li>外部SDカードからの直接変換機能を実装</li>
      <li>Lightroom編集プリセット追加機能を実装</li>
      <li>使い方Tipsを更新</li>
      <li>多言語対応</li>
    </b>
  </ul>
</p>
<h3>β1.6　　2021/2/1</h3>
<p>プリセット追加＆修正アップデート
  <ul>
    <b>
      <li>RAW画像のみを変換するように変更</li>
      <li>Lightroom編集プリセットを10種類追加</li>
      <li>使い方Tipsを更新</li>
      <li>Android11をサポート（Androidの仕様変更により、現状編集プリセット追加不可）</li>
      <li>ViewPagerの切り替え処理を効率化</li>
      <li>文字リソースファイルを使用して全体的にプログラムを整理</li>
    </b>
  </ul>
</p>
<h3>β1.8　　2021/4/25</h3>
<p>プリセット追加＆修正アップデート
  <ul>
    <b>
      <li>画像選択の際にストレージアクセスフレームワークを使用するように変更</li>
      <li>画像変換をバックグラウンドで行うように変更</li>
      <li>Android11以降端末向けにプリセットファイル書き出し機能を追加</li>
      <li>'requestLegacyExternalStorage'フラグをAndroid11以降で宣言しないように変更</li>
      <li>Lightroom編集プリセットを4種類追加</li>
      <li>使い方Tipsを更新</li>
    </b>
  </ul>
</p>
<h3>β1.9　　2021/6/16</h3>
<p>修正アップデート
  <ul>
    <b>
      <li>一部変換済みファイルがアルバムに登録されない問題を修正</li>
      <li>使い方Tipsを更新</li>
    </b>
  </ul>
</p>
<h3>β2.0　　2021/7/02</h3>
<p>修正アップデート
  <ul>
    <b>
      <li>Android9未満でアルバムに登録されたデータが「0B」表記になる問題を修正</li>
      <li>RAW画像データをサポートしていないOS（Android7.0未満）をサポート対象外に変更</li>
    </b>
  </ul>
</p>
<hr>
<p>作成者：佐藤拓実</p>

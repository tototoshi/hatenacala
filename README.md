# hatenaca
はてなダイアリーの記事をコマンドラインから管理するツールです。
同梱のhateda.elを使えばEmacsからも使えます。

今のところ下書きAPIに対応しています。


## インストール方法

   jarは実行可能になっているので
   次のようなスクリプトを作ってパスを通して下さい。
   スクリプト名は'hateda'とでもしてください。

   #!/bin/sh
   java -Dhatena.username=xxxxxxx -Dhatena.password=yyyyyyy -jar ~/bin/hatenacala_x.x.x-assembly-x.x.jar $@

   xxxxxxxは はてなダイアリーのアカウント名
   yyyyyyyは はてなダイアリーのパスワード    です

## コマンド

### hateda draft list
   現在下書き保存しているエントリの一覧が表示されます。
   表示される項目は、id、タイトル、日時です。

### hateda draft get 'id'
   idで指定した下書きエントリを表示します。

### hateda draft add 'filename'
   指定したファイルを下書き保存します。
   ファイルの一行目がエントリ名になります。

### hateda draft update 'id' 'filename'
   idで指定した下書きエントリを指定したファイルで更新(上書き)します。

### hateda draft rm 'id'
   idで指定した下書きエントリを削除します。


## Emacsから使う

   同梱のhateda.elでEmacsから使えるようになります。
   動作にはanythingが必要です。

### 設定
    次のようにhatedaコマンドのパスと専用のディレクトリを指定します。

    (require 'hateda)
    (setq hateda-executable "~/bin/hateda"
    hateda-draft-dir "~/hateda/")

    hateda-draft-dirは下書きをローカルで管理するのに使うディレクトリです。

### コマンド
    主に次のようなコマンドがあります。

#### anything-hateda
    現在下書き保存しているエントリの一覧が表示されます。
    表示される項目は、id、タイトル、日時です。
    アクションとしてOpen, Deleteが選べます。

##### Open
    選択した下書きエントリを開きます。
    バッファ名は「id-title」となり、
    そのままC-x C-sを押せばhateda-draft-dirで指定したディレクトリ内に保存されます
    (注)ファイル名は変えないでください。

##### Delete
    選択したファイルを削除します。

#### hateda-draft-add-this-buffer
    現在のバッファを下書き保存します。

#### hateda-draft-update
    下書き記事を現在のバッファの内容で更新します。

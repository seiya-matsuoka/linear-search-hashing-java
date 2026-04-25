# linear-search-hashing-java ガイド

## 1. このリポジトリで学ぶこと

このリポジトリでは、アルゴリズムとデータ構造の学習のうち、線形データ構造と探索・ハッシュの基礎を扱う。

対象トピックは次の8つ。

- 連結リスト
- スタック
- キュー
- デック
- 線形探索
- 二分探索
- ハッシュテーブル
- Set / Map

ここでの目的は、**データの保持方法による違い、先頭から探す考え方、整列済み配列を前提に高速に探す考え方、ハッシュを使って存在判定や集計を行う考え方** を、Java のコードを読みながら確認すること。

---

## 2. 学習対象トピック一覧

### 2-1. 連結リスト

- ノードでデータをつなぐ構造を確認する
- 単方向連結リストと双方向連結リストの違いを確認する
- 先頭・末尾への追加、削除、走査の流れを確認する
- 配列と異なり、参照でつながっていることを確認する

対応ドキュメント:

- `docs/topics/linked-list.md`

### 2-2. スタック

- LIFO の考え方を確認する
- push / pop / peek の流れを確認する
- 配列ベースのスタック実装を確認する
- 末尾側だけを使う構造であることを確認する

対応ドキュメント:

- `docs/topics/stack.md`

### 2-3. キュー

- FIFO の考え方を確認する
- enqueue / dequeue / peek の流れを確認する
- 循環配列を使ったキュー実装を確認する
- 先頭と末尾を分けて扱う構造であることを確認する

対応ドキュメント:

- `docs/topics/queue.md`

### 2-4. デック

- 両端から追加・削除できる構造を確認する
- addFirst / addLast / removeFirst / removeLast の流れを確認する
- 双方向連結リストを使った実装を確認する
- スタックとキューの両方の性質を持てることを確認する

対応ドキュメント:

- `docs/topics/deque.md`

### 2-5. 線形探索

- 先頭から順番に比較して探す流れを確認する
- 見つかった時点で終了する基本形を確認する
- 見つからない場合は最後まで調べることを確認する
- target を指定して探索対象を変えられるようにする

対応ドキュメント:

- `docs/topics/linear-search.md`

### 2-6. 二分探索

- 整列済み配列を前提に、探索範囲を半分ずつ絞る流れを確認する
- `low`、`high`、`mid` の動き方を確認する
- 線形探索より少ない比較回数で探索できることを確認する
- 未整列配列では前提を満たさないことを確認する

対応ドキュメント:

- `docs/topics/binary-search.md`

### 2-7. ハッシュテーブル

- 値からバケット位置を求める考え方を確認する
- ハッシュ衝突が起きることを確認する
- 同じバケット内で連結して保持する流れを確認する
- 走査による探索とは異なる考え方を確認する

対応ドキュメント:

- `docs/topics/hash-table.md`

### 2-8. Set / Map

- `HashSet` による重複除去と存在判定を確認する
- `HashMap` による件数集計を確認する
- 値の重複管理や集計を簡潔に書けることを確認する
- 線形探索や二分探索とは別の高速化手段として整理する

対応ドキュメント:

- `docs/topics/set-map.md`

---

## 3. 推奨する学習順

このリポジトリ内では、次の順で進めるのがおすすめ。

1. 連結リスト
2. スタック
3. キュー
4. デック
5. 線形探索
6. 二分探索
7. ハッシュテーブル
8. Set / Map

### 理由

- 連結リストで、配列とは異なるデータのつなぎ方を先に押さえる
- スタック・キュー・デックで、データの取り出し順による違いを確認する
- 線形探索で、先頭から順に探す基本形を押さえる
- 二分探索で、整列済み配列を前提に探索範囲を絞る考え方を確認する
- ハッシュテーブルと Set / Map で、探索や判定、集計を高速化する別の考え方を整理する

---

## 4. ディレクトリ構成

```text
linear-search-hashing-java/
├─ src/
│  └─ io/
│     └─ github/
│        └─ seiya_matsuoka/
│           └─ linearsearchhashing/
│              ├─ App.java
│              ├─ RunnerOptions.java
│              ├─ runner/
│              │  ├─ TopicRunner.java
│              │  ├─ LinkedListRunner.java
│              │  ├─ StackRunner.java
│              │  ├─ QueueRunner.java
│              │  ├─ DequeRunner.java
│              │  ├─ LinearSearchRunner.java
│              │  ├─ BinarySearchRunner.java
│              │  ├─ HashTableRunner.java
│              │  └─ SetMapRunner.java
│              ├─ datastructures/
│              │  ├─ LinkedListBasics.java
│              │  ├─ StackBasics.java
│              │  ├─ QueueBasics.java
│              │  └─ DequeBasics.java
│              └─ algorithms/
│                 ├─ LinearSearchBasics.java
│                 ├─ BinarySearchBasics.java
│                 ├─ HashTableBasics.java
│                 └─ SetMapBasics.java
├─ docs/
│  ├─ guide.md
│  └─ topics/
│     ├─ linked-list.md
│     ├─ stack.md
│     ├─ queue.md
│     ├─ deque.md
│     ├─ linear-search.md
│     ├─ binary-search.md
│     ├─ hash-table.md
│     └─ set-map.md
└─ .gitignore
```

### 4-1. 各ファイル・ディレクトリの役割

#### `App.java`

共通エントリーポイント。  
コマンドライン引数を受け取り、指定された topic に応じて対応する runner に処理を振り分ける。

#### `RunnerOptions.java`

実行時オプション保持用クラス。  
`--topic`、`--input`、`--trace`、`--target`、`--size` の値をまとめて runner に渡す。

#### `runner/`

実行用クラス群。  
入力データの決定、trace 表示、表示用メッセージの整形など、学習用デモとして動かすための周辺処理を担当する。

#### `datastructures/`

学習テーマのうち、データ構造のコアとなる実装本体。  
連結リスト、スタック、キュー、デックの実装はここに置く。

#### `algorithms/`

学習テーマのうち、探索アルゴリズムやハッシュ系処理のコアとなる実装本体。  
線形探索、二分探索、ハッシュテーブル、Set / Map を使った処理はここに置く。

#### `docs/topics/`

各学習トピックの説明ドキュメント。  
コードと対応づけながら読むことを前提とする。

---

## 5. 実行方法

このリポジトリは Gradle などのビルドツールを使わず、素の Java でコンパイル・実行する。

### 5-1. 前提

- Java 21 を使用
- VS Code では、フォルダを開いてターミナルからコンパイル・実行すればよい

### 5-2. bash / zsh でのコンパイル

```bash
javac -encoding UTF-8 -d out $(find src -name "*.java")
```

### 5-3. PowerShell でのコンパイル

```powershell
$files = Get-ChildItem -Recurse -Filter *.java src | ForEach-Object { $_.FullName }
javac -encoding UTF-8 -d out $files
```

### 5-4. 実行コマンドの基本形

```bash
java -cp out io.github.seiya_matsuoka.linearsearchhashing.App --topic <topic> [options]
```

---

## 6. 共通オプション

このリポジトリでは、次の5つの共通オプションを使う。

### 6-1. `--topic`

実行する学習トピックを指定する。

指定できる値:

- `linked-list`
- `stack`
- `queue`
- `deque`
- `linear-search`
- `binary-search`
- `hash-table`
- `set-map`

### 6-2. `--input`

入力値を直接指定する。

例:

- `--input 5,3,8,1,4`
- `--input 10,20,30,40,50`

### 6-3. `--trace`

処理途中の流れを表示する。

使いどころ:

- 連結リストの走査順確認
- スタック、キュー、デックの内部状態変化確認
- 線形探索や二分探索の探索過程確認
- ハッシュテーブルのバケット計算や衝突確認
- Set / Map の集計過程確認

### 6-4. `--target`

探索や判定で使う対象値を指定する。

使いどころ:

- 連結リストで `contains` や `indexOf` を確認する
- 線形探索や二分探索で探索対象を指定する
- ハッシュテーブルや Set / Map で存在判定対象を指定する

不要なトピックでは、指定されていても使わずに無視する。

### 6-5. `--size`

大きい入力データを生成したい場合のサイズを指定する。

使いどころ:

- 連結リストや各データ構造に大きめの連番入力を与える
- 探索で入力サイズに応じた比較を行う
- ハッシュ系で件数を増やした場合の動きを確認する

---

## 7. 入力データの決まり方

各トピックで使う入力データは、次の優先順位で決定する。

1. `--input` が指定されていればそれを使う
2. `--input` がなく、`--size` が指定されていれば、そのサイズで入力を生成する
3. どちらもなければ、学習用の小さいデフォルト値を使う

### 7-1. この順にしている理由

- まずは自分で試したい入力を最優先にできる
- 入力サイズだけ変えて差を見たい場合にも対応できる
- 何も指定しなくてもすぐ実行できる

---

## 8. topic ごとの実行例

### 8-1. 連結リスト

#### 基本実行

```bash
java -cp out io.github.seiya_matsuoka.linearsearchhashing.App --topic linked-list --trace
```

#### target 指定

```bash
java -cp out io.github.seiya_matsuoka.linearsearchhashing.App --topic linked-list --input 10,20,30,40,50 --target 30 --trace
```

#### 大きい入力

```bash
java -cp out io.github.seiya_matsuoka.linearsearchhashing.App --topic linked-list --size 8 --trace
```

### 8-2. スタック

#### 基本実行

```bash
java -cp out io.github.seiya_matsuoka.linearsearchhashing.App --topic stack --trace
```

#### 入力指定

```bash
java -cp out io.github.seiya_matsuoka.linearsearchhashing.App --topic stack --input 10,20,30,40
```

#### 大きい入力

```bash
java -cp out io.github.seiya_matsuoka.linearsearchhashing.App --topic stack --size 8
```

### 8-3. キュー

#### 基本実行

```bash
java -cp out io.github.seiya_matsuoka.linearsearchhashing.App --topic queue --trace
```

#### 入力指定

```bash
java -cp out io.github.seiya_matsuoka.linearsearchhashing.App --topic queue --input 10,20,30,40
```

#### 大きい入力

```bash
java -cp out io.github.seiya_matsuoka.linearsearchhashing.App --topic queue --size 8
```

### 8-4. デック

#### 基本実行

```bash
java -cp out io.github.seiya_matsuoka.linearsearchhashing.App --topic deque --trace
```

#### 入力指定

```bash
java -cp out io.github.seiya_matsuoka.linearsearchhashing.App --topic deque --input 10,20,30,40
```

#### 大きい入力

```bash
java -cp out io.github.seiya_matsuoka.linearsearchhashing.App --topic deque --size 8
```

### 8-5. 線形探索

#### 基本実行

```bash
java -cp out io.github.seiya_matsuoka.linearsearchhashing.App --topic linear-search --trace
```

#### target 指定

```bash
java -cp out io.github.seiya_matsuoka.linearsearchhashing.App --topic linear-search --input 12,7,25,3,18,30,5 --target 30 --trace
```

#### 大きい入力

```bash
java -cp out io.github.seiya_matsuoka.linearsearchhashing.App --topic linear-search --size 20 --target 19
```

### 8-6. 二分探索

#### 基本実行

```bash
java -cp out io.github.seiya_matsuoka.linearsearchhashing.App --topic binary-search --trace
```

#### target 指定

```bash
java -cp out io.github.seiya_matsuoka.linearsearchhashing.App --topic binary-search --input 3,5,7,12,18,25,30 --target 25 --trace
```

#### 大きい入力

```bash
java -cp out io.github.seiya_matsuoka.linearsearchhashing.App --topic binary-search --size 32 --target 26
```

### 8-7. ハッシュテーブル

#### 基本実行

```bash
java -cp out io.github.seiya_matsuoka.linearsearchhashing.App --topic hash-table --trace
```

#### target 指定

```bash
java -cp out io.github.seiya_matsuoka.linearsearchhashing.App --topic hash-table --input 10,17,24,31,38,45,52 --target 31 --trace
```

#### 大きい入力

```bash
java -cp out io.github.seiya_matsuoka.linearsearchhashing.App --topic hash-table --size 8 --trace
```

### 8-8. Set / Map

#### 基本実行

```bash
java -cp out io.github.seiya_matsuoka.linearsearchhashing.App --topic set-map --trace
```

#### target 指定

```bash
java -cp out io.github.seiya_matsuoka.linearsearchhashing.App --topic set-map --input 10,20,30,20,10,40,30,50,20 --target 20 --trace
```

#### 大きい入力

```bash
java -cp out io.github.seiya_matsuoka.linearsearchhashing.App --topic set-map --size 12 --trace
```

---

## 9. 学習時の見方

### 9-1. まず見るべき場所

まずは次の順で見るのがおすすめ。

1. `docs/topics/*.md` で概要と処理の流れを読む
2. `runner/` で入力決定や表示の流れを確認する
3. `datastructures/` または `algorithms/` でコアとなる処理を読む
4. 実際に `--trace` 付きで実行して出力を見る

### 9-2. 特に見るべきポイント

- どこが学習テーマの本体処理か
- どこが入力準備や表示のための補助処理か
- データの取り出し順がどう変わるか
- 線形探索と二分探索で、比較回数や前提条件がどう違うか
- ハッシュテーブルで、バケット計算や衝突がどう起きるか
- Set / Map で、重複除去や件数集計をどう簡潔に書けるか

### 9-3. 実行時間の見方

実行時間は参考値として表示することがあるが、学習の中心は次の順。

1. データ構造やアルゴリズムの考え方
2. 走査回数や比較回数、更新回数
3. 参考実行時間

特に探索トピックでは、秒数そのものより「どこまで見たか」「前提条件があるか」を重視して見る。

---

## 10. このリポジトリを終えた時点で目指す状態

このリポジトリを終えた時点で、次の状態を目指す。

- 連結リストの基本構造が説明できる
- スタック、キュー、デックの違いが説明できる
- 線形探索と二分探索の違いが説明できる
- ハッシュテーブルの基本的な考え方が説明できる
- Set / Map を使った存在判定や件数集計の基本が説明できる
- コードを読んだときに、学習本体処理と補助処理を見分けられる

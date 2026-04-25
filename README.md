# Linear Search / Hashing - Java

<p>
  <img alt="Java" src="https://img.shields.io/badge/Java-21-007396?logo=openjdk&logoColor=ffffff">
  <img alt="Algorithm" src="https://img.shields.io/badge/Algorithm-Study-1F6FEB">
  <img alt="Data Structure" src="https://img.shields.io/badge/Data%20Structure-Study-7C3AED">
</p>

線形データ構造と探索・ハッシュの基礎のうち、**連結リスト / スタック / キュー / デック / 線形探索 / 二分探索 / ハッシュテーブル / Set / Map** を Java で学習するためのリポジトリ。  
コードを読み、実行し、出力を確認しながら、データの持ち方と探索方法の違いを段階的に理解することを目的とする。  
各トピックごとにドキュメントを用意し、実装と対応づけながら見返せる形で整理している。

---

## 学習目的

このリポジトリでは、主に次の内容を目的として学習を行う。

- 連結リストの基本構造と参照でつなぐ考え方を理解する
- スタック / キュー / デックの操作の違いを理解する
- 線形探索と二分探索の流れと前提条件の違いを理解する
- ハッシュテーブルの基本的な考え方と衝突処理の流れを理解する
- `HashSet` / `HashMap` を使った存在判定・重複除去・件数集計を理解する
- Java のコードを読みながら、処理の流れを追えるようにする
- 実行結果や途中経過を見ながら、挙動を確認できるようにする

---

## 学習範囲

このリポジトリで扱うトピックは次の通り。

- 連結リスト
- スタック
- キュー
- デック
- 線形探索
- 二分探索
- ハッシュテーブル
- Set / Map

### 各トピックの位置づけ

- **連結リスト**  
  ノードを参照でつなぐ構造を通して、配列とは異なるデータの持ち方を確認する

- **スタック**  
  LIFO の動きを通して、後入れ先出しの処理を確認する

- **キュー**  
  FIFO の動きを通して、先入れ先出しの処理を確認する

- **デック**  
  両端から追加・削除できる構造を通して、スタックとキューの両方に近い使い方を確認する

- **線形探索**  
  先頭から順に調べる探索方法を確認する

- **二分探索**  
  整列済み配列を前提に、範囲を半分ずつ絞る探索方法を確認する

- **ハッシュテーブル**  
  ハッシュ値からバケット位置を求めて探索する考え方と、衝突時の処理を確認する

- **Set / Map**  
  重複除去、存在判定、件数集計など、ハッシュベースの標準的な使い方を確認する

---

## 学習の進め方

基本的な進め方は次の通り。

1. `docs/guide.md` を読み、このリポジトリ全体の構成と実行方法を把握する
2. `docs/topics/` 配下の対象トピックのドキュメントを読む
3. `App.java` から `--topic` を指定して実行する
4. 必要に応じて `--input`、`--trace`、`--target`、`--size` を使って挙動を変える
5. `runner/` と `datastructures/` / `algorithms/` 配下のコードを読み、コメントと出力を対応させながら理解する

---

## 前提環境

- Java 21
- VS Code などの Java を扱えるエディタ
- ビルドツールは使用しない
- `javac` と `java` でコンパイル・実行を行う

---

## 実行方法

### 1. コンパイル

#### bash / Git Bash

```bash
javac -encoding UTF-8 -d out $(find src -name "*.java")
```

#### PowerShell

```powershell
$files = Get-ChildItem -Path src -Recurse -Filter *.java | ForEach-Object { $_.FullName }
javac -encoding UTF-8 -d out $files
```

### 2. 実行

基本形は次の通り。

```bash
java -cp out io.github.seiya_matsuoka.linearsearchhashing.App --topic <topic>
```

例:

```bash
java -cp out io.github.seiya_matsuoka.linearsearchhashing.App --topic linked-list
java -cp out io.github.seiya_matsuoka.linearsearchhashing.App --topic stack
java -cp out io.github.seiya_matsuoka.linearsearchhashing.App --topic queue
java -cp out io.github.seiya_matsuoka.linearsearchhashing.App --topic deque
java -cp out io.github.seiya_matsuoka.linearsearchhashing.App --topic linear-search
java -cp out io.github.seiya_matsuoka.linearsearchhashing.App --topic binary-search
java -cp out io.github.seiya_matsuoka.linearsearchhashing.App --topic hash-table
java -cp out io.github.seiya_matsuoka.linearsearchhashing.App --topic set-map
```

---

## 共通オプション

このリポジトリでは、共通で次のオプションを使う。

- `--topic`  
  実行するトピックを指定する

- `--input`  
  任意の入力値を直接指定する  
  例: `--input 5,3,8,1,4` / `--input 10,20,30,20,10`

- `--trace`  
  処理途中の流れを表示する

- `--target`  
  探索対象値など、別途指定したい値を渡す  
  必要なトピックのみで使用する

- `--size`  
  大きい入力データを自動生成したい場合のサイズ指定に使う  
  比較確認や差分把握で使用する

---

## 実行例

### 連結リスト

```bash
java -cp out io.github.seiya_matsuoka.linearsearchhashing.App --topic linked-list --trace
```

### スタック

```bash
java -cp out io.github.seiya_matsuoka.linearsearchhashing.App --topic stack --input 10,20,30,40 --trace
```

### キュー

```bash
java -cp out io.github.seiya_matsuoka.linearsearchhashing.App --topic queue --input 10,20,30,40 --trace
```

### デック

```bash
java -cp out io.github.seiya_matsuoka.linearsearchhashing.App --topic deque --input 10,20,30,40 --trace
```

### 線形探索

```bash
java -cp out io.github.seiya_matsuoka.linearsearchhashing.App --topic linear-search --input 12,7,25,3,18,30,5 --target 30 --trace
```

### 二分探索

```bash
java -cp out io.github.seiya_matsuoka.linearsearchhashing.App --topic binary-search --input 3,5,7,12,18,25,30 --target 25 --trace
```

### ハッシュテーブル

```bash
java -cp out io.github.seiya_matsuoka.linearsearchhashing.App --topic hash-table --input 10,17,24,31,38,45,52 --target 31 --trace
```

### Set / Map

```bash
java -cp out io.github.seiya_matsuoka.linearsearchhashing.App --topic set-map --input 10,20,30,20,10,40,30,50,20 --target 20 --trace
```

---

## リポジトリ構成

```text
.
├─ src/
│  └─ io/
│     └─ github/
│        └─ seiya_matsuoka/
│           └─ linearsearchhashing/
│              ├─ App.java
│              ├─ RunnerOptions.java
│              ├─ runner/
│              ├─ datastructures/
│              └─ algorithms/
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
└─ README.md
```

### 各ディレクトリ・ファイルの役割

- `App.java`  
  共通エントリーポイント  
  引数を読み取り、対象の runner に振り分ける

- `RunnerOptions.java`  
  実行オプションを保持する

- `runner/`  
  入力の決定、実装呼び出し、出力表示を担当する

- `datastructures/`  
  学習対象となるデータ構造の実装本体を置く

- `algorithms/`  
  学習対象となる探索・ハッシュ処理の実装本体を置く

- `docs/guide.md`  
  リポジトリ全体の案内と実行方法をまとめる

- `docs/topics/`  
  各トピックの個別ドキュメントを置く

---

## ドキュメント

- ガイド: [`docs/guide.md`](docs/guide.md)
- 連結リスト: [`docs/topics/linked-list.md`](docs/topics/linked-list.md)
- スタック: [`docs/topics/stack.md`](docs/topics/stack.md)
- キュー: [`docs/topics/queue.md`](docs/topics/queue.md)
- デック: [`docs/topics/deque.md`](docs/topics/deque.md)
- 線形探索: [`docs/topics/linear-search.md`](docs/topics/linear-search.md)
- 二分探索: [`docs/topics/binary-search.md`](docs/topics/binary-search.md)
- ハッシュテーブル: [`docs/topics/hash-table.md`](docs/topics/hash-table.md)
- Set / Map: [`docs/topics/set-map.md`](docs/topics/set-map.md)

---

## このリポジトリで確認できること

このリポジトリを一通り進めることで、次の状態を目指す。

- 連結リストの基本構造と走査の流れを説明できる
- スタック / キュー / デックの操作の違いを理解できる
- 線形探索と二分探索の違いを説明できる
- 二分探索に整列済み配列が必要な理由を説明できる
- ハッシュテーブルの基本的な考え方と衝突処理の流れを理解できる
- `HashSet` / `HashMap` を使った重複除去・存在判定・件数集計を説明できる
- Java の実装と出力を対応させながら読める
- `--topic` や各オプションを使って、自分で入力を変えながら確認できる

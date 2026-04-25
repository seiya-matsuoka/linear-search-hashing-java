# キュー

## 1. 概要

キューは、**最初に入れた要素を最初に取り出す** データ構造。  
この性質は FIFO（First In, First Out）と呼ばれる。

今回のトピックでは、配列ベースのキューを自前実装し、次の操作を確認する。

- enqueue
- dequeue
- peek
- isEmpty

## 2. 基本の動き

キューでは、新しい要素を常に末尾側へ追加し、取り出すときは先頭側から取り出す。

イメージ:

```text
enqueue 10 -> [10]
enqueue 20 -> [10, 20]
enqueue 30 -> [10, 20, 30]
dequeue    -> 10 を取り出す
```

このトピックでは、内部的には配列を使い、`front` と `rear` の考え方で管理する。

## 3. 処理の流れ

このトピックでは、次の順でキューの動きを確認する。

1. 入力値を順に enqueue して初期状態を作る
2. `enqueue` で値を追加する
3. `peek` で先頭値を確認する
4. `dequeue` で値を取り出す
5. `isEmpty` で空かどうかを確認する

## 4. 計算量

代表的な計算量は次の通り。

| 操作    | 計算量 | 補足                   |
| ------- | ------ | ---------------------- |
| enqueue | O(1)   | 末尾位置へ値を追加する |
| dequeue | O(1)   | 先頭位置の値を取り出す |
| peek    | O(1)   | 先頭位置の値を見るだけ |
| isEmpty | O(1)   | size を確認するだけ    |

### このトピックで特に見たい点

- 先頭から取り出し、末尾へ追加する
- front と size を使って現在位置を管理する
- 先に入れた要素が先に出る

## 5. メリット / デメリット

### メリット

- FIFO の流れが分かりやすい
- 先頭取り出しと末尾追加が効率よく行える
- 待ち行列のような考え方と相性がよい

### デメリット

- 末尾だけを見るスタックより管理項目が増える
- 配列ベースでは先頭位置の扱いに工夫が必要になる
- 途中要素への直接アクセスには向かない

## 6. Java実装のポイント

- 学習用として、標準ライブラリの `Queue` 実装をそのまま使わず自前実装を採用する
- 配列を循環利用する形で、要素を詰め直さずに先頭位置を進める
- `front` と `size` を使って現在の論理的な範囲を表す
- runner 側では入力決定や表示整形を担当し、キュー本体の実装は `datastructures/` に分離する

## 7. コアとなる処理・重要なコードの見どころ

キュー学習で重要なのは、**先頭から取り出し、末尾へ追加すること** と、**front を進めながら扱うこと**。

### enqueue の見どころ

```java
int rearIndex = physicalIndex(size);
elements[rearIndex] = value;
size++;
```

現在の要素数から末尾位置を求め、その位置へ値を追加する。  
front からの相対位置で rear を決めている点が重要。

### dequeue の見どころ

```java
int value = elements[front];
elements[front] = 0;
front = (front + 1) % elements.length;
size--;
```

取り出す位置は常に `front`。  
取り出したあとに `front` を進めることで、次の先頭を表す。

## 8. 実行例

### 基本実行

```bash
java -cp out io.github.seiya_matsuoka.linearsearchhashing.App --topic queue
```

### trace あり実行

```bash
java -cp out io.github.seiya_matsuoka.linearsearchhashing.App --topic queue --trace
```

### 任意入力

```bash
java -cp out io.github.seiya_matsuoka.linearsearchhashing.App --topic queue --input 10,20,30,40
```

### size 指定

```bash
java -cp out io.github.seiya_matsuoka.linearsearchhashing.App --topic queue --size 8
```

### 出力で確認する内容

- 要素列（front -> rear）
- size
- front / rear の値
- enqueue / dequeue 後の状態変化
- peek の結果

## 9. 関連トピック

- スタック
- デック
- 連結リスト

キューは、スタックやデックと比較することで、要素の取り出し順と追加位置の違いが見えやすくなる。  
また、連結リストを使った実装にもつながる。

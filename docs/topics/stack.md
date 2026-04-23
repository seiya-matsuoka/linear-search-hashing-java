# スタック

## 1. 概要

スタックは、**最後に入れた要素を最初に取り出す** データ構造。  
この性質は LIFO（Last In, First Out）と呼ばれる。

今回のトピックでは、配列ベースのスタックを自前実装し、次の操作を確認する。

- push
- pop
- peek
- isEmpty

## 2. 基本の動き

スタックでは、新しい要素を常に先頭側ではなく **一番上** に積む。  
取り出すときも、一番上の要素から取り出す。

イメージ:

```text
push 10 -> [10]
push 20 -> [10, 20]
push 30 -> [10, 20, 30]
pop     -> 30 を取り出す
```

このトピックでは、内部的には配列を使い、末尾位置を top とみなして操作を行う。

## 3. 処理の流れ

このトピックでは、次の順でスタックの動きを確認する。

1. 入力値を順に push して初期状態を作る
2. `push` で値を追加する
3. `peek` で先頭値を確認する
4. `pop` で値を取り出す
5. `isEmpty` で空かどうかを確認する

## 4. 計算量

代表的な計算量は次の通り。

| 操作    | 計算量 | 補足                   |
| ------- | ------ | ---------------------- |
| push    | O(1)   | 末尾位置へ値を追加する |
| pop     | O(1)   | 末尾位置の値を取り出す |
| peek    | O(1)   | 末尾位置の値を見るだけ |
| isEmpty | O(1)   | size を確認するだけ    |

### このトピックで特に見たい点

- top に相当する位置だけを更新する
- 最後に入れた要素が最初に出る
- 追加も削除も末尾側だけを見る

## 5. メリット / デメリット

### メリット

- push / pop / peek が単純
- 計算量が分かりやすい
- LIFO の性質を直感的に理解しやすい

### デメリット

- 先頭や途中の要素に直接アクセスする用途には向かない
- 取り出し順は常に逆順になる
- 内部配列の容量管理が必要になる

## 6. Java実装のポイント

- 学習用として、標準ライブラリの `Stack` ではなく自前実装を採用する
- 配列の末尾位置を top とみなして管理する
- `size` を使って、現在の要素数と次の挿入位置を表す
- runner 側では入力決定や表示整形を担当し、スタック本体の実装は `datastructures/` に分離する

## 7. コアとなる処理・重要なコードの見どころ

スタック学習で重要なのは、**追加も削除も top に相当する位置だけを更新すること**。

### push の見どころ

```java
 elements[size] = value;
 size++;
```

`size` が示す次の空き位置へ値を入れ、そのあと件数を増やす。  
末尾側だけを更新する点が重要。

### pop の見どころ

```java
int topIndex = size - 1;
int value = elements[topIndex];
elements[topIndex] = 0;
size--;
```

取り出す位置は常に `size - 1`。  
末尾値を返したあとに `size` を減らすことで、top を 1 つ戻している。

## 8. 実行例

### 基本実行

```bash
java -cp out io.github.seiya_matsuoka.linearsearchhashing.App --topic stack
```

### trace あり実行

```bash
java -cp out io.github.seiya_matsuoka.linearsearchhashing.App --topic stack --trace
```

### 任意入力

```bash
java -cp out io.github.seiya_matsuoka.linearsearchhashing.App --topic stack --input 10,20,30,40
```

### size 指定

```bash
java -cp out io.github.seiya_matsuoka.linearsearchhashing.App --topic stack --size 8
```

### 出力で確認する内容

- 要素列（bottom -> top）
- size
- top
- push / pop 後の状態変化
- peek の結果

## 9. 関連トピック

- キュー
- デック
- 連結リスト

スタックは、キューやデックと比較することで、要素の取り出し順の違いが見えやすくなる。  
また、連結リストをベースに実装する考え方にもつながる。

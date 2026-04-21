# 連結リスト

## 1. 概要

連結リストは、各要素をノードとして持ち、ノード同士を参照でつないで管理するデータ構造。  
配列のように連続した領域に並べるのではなく、**値 + 次のノードへの参照** を持つことで順番を表現する。

このトピックでは、次の2種類を扱う。

- 単方向連結リスト
- 双方向連結リスト

## 2. 基本の動き

### 単方向連結リスト

単方向連結リストでは、各ノードが次のノードだけを指す。  
先頭ノードから `next` をたどることで、全要素を順に見ていく。

イメージ:

```text
head -> [10|next] -> [20|next] -> [30|null]
```

### 双方向連結リスト

双方向連結リストでは、各ノードが前後両方のノードを指す。  
`next` で前から後ろへ進めるだけでなく、`prev` で後ろから前へもたどれる。

イメージ:

```text
null <- [10] <-> [20] <-> [30] -> null
```

## 3. 処理の流れ

このトピックでは、次の順で連結リストの動きを確認する。

### 単方向連結リスト

1. 入力値から連結リストを構築する
2. 先頭追加を行う
3. 末尾追加を行う
4. 指定位置への挿入を行う
5. 指定位置の削除を行う
6. `contains` と `indexOf` で値の存在確認を行う

### 双方向連結リスト

1. 入力値から連結リストを構築する
2. 先頭追加を行う
3. 末尾追加を行う
4. 指定位置の削除を行う
5. 前方向・後方向の両方からたどれることを確認する

## 4. 計算量

代表的な計算量は次の通り。

| 操作             | 単方向連結リスト | 双方向連結リスト | 補足                         |
| ---------------- | ---------------- | ---------------- | ---------------------------- |
| 先頭追加         | O(1)             | O(1)             | head の差し替えで済む        |
| 末尾追加         | O(1) ※tailあり   | O(1) ※tailあり   | tail を持たない場合は O(n)   |
| 指定位置アクセス | O(n)             | O(n)             | 先頭から順にたどる必要がある |
| 指定位置挿入     | O(n)             | O(n)             | 挿入位置までたどる必要がある |
| 指定位置削除     | O(n)             | O(n)             | 削除位置までたどる必要がある |
| 探索             | O(n)             | O(n)             | 先頭から順に確認する         |

### このトピックで特に見たい点

- 配列のように `index` から直接取り出せない
- 参照のつなぎ替えで追加・削除を行う
- 先頭追加は得意だが、途中アクセスは得意ではない

## 5. メリット / デメリット

### メリット

- 先頭追加・先頭削除がしやすい
- 要素数の増減に対して柔軟
- ノードのつなぎ替えで挿入・削除の考え方を学びやすい

### デメリット

- 配列のようなランダムアクセスができない
- 指定位置まで順にたどる必要がある
- 参照の扱いを誤ると構造が壊れやすい
- ノードごとに参照を持つため、管理コストが増える

## 6. Java実装のポイント

- 学習用として、`LinkedList` 標準クラスではなく自前実装を採用する
- 単方向連結リストでは `value` と `next` を持つノードを使う
- 双方向連結リストでは `value`、`next`、`prev` を持つノードを使う
- `head` と `tail` を持つことで、先頭・末尾操作を分かりやすくする
- runner 側では、入力決定や表示整形を担当し、連結リスト本体の実装は `datastructures/` に分離する

## 7. コアとなる処理・重要なコードの見どころ

連結リスト学習で重要なのは、**ノード同士を参照でつなぐこと** と、**参照のつなぎ替えで挿入・削除を行うこと**。

### 挿入処理の見どころ

単方向連結リストの `insertAt` では、挿入位置の1つ前まで順にたどり、参照のつなぎ先を差し替える。

```java
SinglyNode previous = nodeAt(index - 1);
SinglyNode newNode = new SinglyNode(value);
newNode.next = previous.next;
previous.next = newNode;
```

ここでは、`previous` の次にあったノードをいったん `newNode.next` へ保持し、そのあと `previous.next` を `newNode` に差し替えている。

### 削除処理の見どころ

単方向連結リストの `removeAt` では、削除対象の1つ前のノードから next を付け替える。

```java
SinglyNode previous = nodeAt(index - 1);
SinglyNode removedNode = previous.next;
previous.next = removedNode.next;
```

削除対象そのものを物理的に詰めるのではなく、**参照を飛ばすことで構造から外す** 点が配列との違い。

### 双方向連結リストの見どころ

双方向連結リストでは、`next` だけでなく `prev` も更新する必要がある。

```java
current.prev.next = current.next;
current.next.prev = current.prev;
```

前後のノードを両方向でつなぎ直す必要がある点が、単方向連結リストとの違い。

## 8. 実行例

### 基本実行

```bash
java -cp out io.github.seiya_matsuoka.linearsearchhashing.App --topic linked-list
```

### trace あり実行

```bash
java -cp out io.github.seiya_matsuoka.linearsearchhashing.App --topic linked-list --trace
```

### 任意入力

```bash
java -cp out io.github.seiya_matsuoka.linearsearchhashing.App --topic linked-list --input 10,20,30,40,50 --target 30
```

### size 指定

```bash
java -cp out io.github.seiya_matsuoka.linearsearchhashing.App --topic linked-list --size 8
```

### 出力で確認する内容

- 単方向連結リストの要素列
- 双方向連結リストの forward / backward の要素列
- size / first / last
- 追加・挿入・削除後の状態変化
- `contains` と `indexOf` の結果

## 9. 関連トピック

- スタック
- キュー
- デック
- 線形探索

連結リストは、今後扱うスタックやキューの理解にもつながる。  
また、先頭から順にたどる処理は線形探索とも関係が深い。

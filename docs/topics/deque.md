# デック

## 1. 概要

デックは、**先頭側と末尾側の両方から追加と削除ができる** データ構造。  
Double Ended Queue の略で、キューの拡張として考えることができる。

今回のトピックでは、双方向連結リストベースのデックを自前実装し、次の操作を確認する。

- addFirst
- addLast
- removeFirst
- removeLast
- peekFirst
- peekLast

## 2. 基本の動き

デックでは、先頭側にも末尾側にも要素を追加でき、取り出しも両端から行える。

イメージ:

```text
addLast 10  -> [10]
addLast 20  -> [10, 20]
addFirst 5  -> [5, 10, 20]
removeLast  -> 20 を取り出す
removeFirst -> 5 を取り出す
```

このトピックでは、内部的には双方向連結リストを使い、`prev` と `next` で前後のノードをつなぐ。

## 3. 処理の流れ

このトピックでは、次の順でデックの動きを確認する。

1. 入力値を順に addLast して初期状態を作る
2. `addFirst` で先頭側へ値を追加する
3. `addLast` で末尾側へ値を追加する
4. `removeFirst` で先頭側から値を取り出す
5. `removeLast` で末尾側から値を取り出す
6. `peekFirst` / `peekLast` で両端の値を確認する

## 4. 計算量

代表的な計算量は次の通り。

| 操作        | 計算量 | 補足                     |
| ----------- | ------ | ------------------------ |
| addFirst    | O(1)   | 先頭ノード前へ接続する   |
| addLast     | O(1)   | 末尾ノード後ろへ接続する |
| removeFirst | O(1)   | head を次ノードへ進める  |
| removeLast  | O(1)   | tail を前ノードへ戻す    |
| peekFirst   | O(1)   | head の値を見るだけ      |
| peekLast    | O(1)   | tail の値を見るだけ      |

### このトピックで特に見たい点

- 先頭側と末尾側のどちらも操作対象になる
- 双方向連結リストの prev / next を使って参照を更新する
- スタックとキューの両方の性質を一部含む

## 5. メリット / デメリット

### メリット

- 両端操作を効率よく行える
- スタックとキューの中間的な使い方ができる
- 先頭・末尾のどちらを重視するケースにも対応しやすい

### デメリット

- スタックやキューより操作種類が多い
- 双方向連結リストの参照更新が必要になる
- 実装が少し複雑になる

## 6. Java実装のポイント

- 学習用として、標準ライブラリの `Deque` 実装をそのまま使わず自前実装を採用する
- 双方向連結リストを内部表現として使う
- `head` と `tail` を持ち、両端の操作を分かりやすくする
- runner 側では入力決定や表示整形を担当し、デック本体の実装は `datastructures/` に分離する

## 7. コアとなる処理・重要なコードの見どころ

デック学習で重要なのは、**先頭側と末尾側の両方で参照更新を行うこと**。

### addFirst の見どころ

```java
newNode.next = head;
head.prev = newNode;
head = newNode;
```

新しいノードを先頭の前へ差し込み、head を更新する。  
双方向連結リストなので、旧 head 側の `prev` も更新する点が重要。

### removeLast の見どころ

```java
tail = tail.prev;
if (tail == null) {
    head = null;
} else {
    tail.next = null;
}
```

末尾を 1 つ前へ戻し、旧 tail を構造から外す。  
要素がなくなるケースでは head も null にそろえる必要がある。

## 8. 実行例

### 基本実行

```bash
java -cp out io.github.seiya_matsuoka.linearsearchhashing.App --topic deque
```

### trace あり実行

```bash
java -cp out io.github.seiya_matsuoka.linearsearchhashing.App --topic deque --trace
```

### 任意入力

```bash
java -cp out io.github.seiya_matsuoka.linearsearchhashing.App --topic deque --input 10,20,30,40
```

### size 指定

```bash
java -cp out io.github.seiya_matsuoka.linearsearchhashing.App --topic deque --size 8
```

### 出力で確認する内容

- 要素列（front -> rear）
- size
- first / last の値
- addFirst / addLast 後の状態変化
- removeFirst / removeLast 後の状態変化
- peekFirst / peekLast の結果

## 9. 関連トピック

- スタック
- キュー
- 連結リスト

デックは、スタックとキューの性質を比較すると理解しやすい。  
また、双方向連結リストを使った参照更新の練習にもつながる。

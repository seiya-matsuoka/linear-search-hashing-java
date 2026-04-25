# 二分探索

## 1. 概要

二分探索は、**整列済み配列に対して、探索範囲を半分ずつ絞り込みながら目的の値を探す**探索方法。  
線形探索より比較回数を大きく減らせるのが特徴。

今回のトピックでは、次の内容を確認する。

- 整列済み配列が前提であること
- low / high / mid を使って範囲を絞る流れ
- target より大きいか小さいかで探索範囲を変える流れ
- 比較回数の増え方

## 2. 基本の動き

二分探索では、まず探索範囲の中央を見る。  
中央の値と target を比べ、target が大きければ右半分、小さければ左半分だけを残す。

イメージ:

```text
[3, 5, 7, 12, 18, 25, 30]
 target = 25

mid=12 -> target の方が大きいので右へ
[18, 25, 30]
mid=25 -> 一致
```

このように、1 回の比較ごとに探索範囲を大きく減らせる。  
ただし、整列済み配列でなければ正しく動かない。

## 3. 処理の流れ

このトピックでは、次の順で二分探索の動きを確認する。

1. 入力配列を決定する
2. 配列が昇順に整列済みか確認する
3. 探索対象値を決定する
4. `low` と `high` で探索範囲を持つ
5. `mid` を計算して中央の値を確認する
6. target と比較して、右半分か左半分へ絞り込む
7. 一致した時点で index を返して探索を終了する
8. 範囲がなくなったら未発見として終了する

trace あり実行では、各段階の `low`、`high`、`mid` の変化を確認できる。  
そのため、探索範囲が半分ずつ狭まっていく流れを見て理解できる。

## 4. 計算量

### 見つかった場合 / 見つからない場合

- 時間計算量: O(log n)
- 空間計算量: O(1)

比較のたびに探索範囲を半分に減らすため O(log n)。  
補助的に使う変数は定数個なので O(1)。

### このトピックで特に見たい点

- 線形探索のように全件を見ない
- 中央比較によって範囲を絞る
- 整列済みであることが前提になる

## 5. メリット / デメリット

### メリット

- 比較回数を大きく減らせる
- データ量が増えても探索回数の増え方が緩やか
- 探索アルゴリズムの代表例として重要

### デメリット

- 整列済み配列でなければ使えない
- low / high / mid の管理が少し複雑
- 連結リストのようなランダムアクセスしにくい構造とは相性が悪い

## 6. Java実装のポイント

- `low` と `high` で探索範囲を表す
- `mid` は `low + (high - low) / 2` で求める
- `values[mid]` と target を比較する
- target の大小に応じて `low` または `high` を更新する
- 実行前に整列済みか確認する補助処理を持つ

## 7. コアとなる処理・重要なコードの見どころ

二分探索学習で重要なのは、**中央を比較して探索範囲を半分に絞る**点。

### 探索本体の見どころ

```java
while (low <= high) {
    int mid = low + (high - low) / 2;

    if (values[mid] == target) {
        return ...;
    }

    if (values[mid] < target) {
        low = mid + 1;
    } else {
        high = mid - 1;
    }
}
```

ここが二分探索のコア処理。  
中央値との比較結果によって、次に見るべき半分だけを残している。

### 学習時に特に見たい点

- `mid` の値がどのように変わるか
- target が大きいときは右へ、小さいときは左へ絞る流れ
- 範囲がなくなったら未発見で終了すること

## 8. 実行例

### 基本実行

```bash
java -cp out io.github.seiya_matsuoka.linearsearchhashing.App --topic binary-search
```

### trace あり実行

```bash
java -cp out io.github.seiya_matsuoka.linearsearchhashing.App --topic binary-search --trace
```

### 任意入力

```bash
java -cp out io.github.seiya_matsuoka.linearsearchhashing.App --topic binary-search --input 3,5,7,12,18,25,30 --target 25
```

### size 指定

```bash
java -cp out io.github.seiya_matsuoka.linearsearchhashing.App --topic binary-search --size 32 --target 26
```

### 出力で確認する内容

- 入力値
- 探索対象値
- found
- index
- comparisons
- trace 時の low / high / mid の変化

## 9. 関連トピック

- 線形探索
- 配列
- ソート
- ハッシュテーブル

二分探索は、線形探索との比較で理解しやすい。  
また、整列済み配列が前提となるため、後で学ぶソートともつながる。

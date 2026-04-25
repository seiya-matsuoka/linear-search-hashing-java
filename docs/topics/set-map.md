# Set / Map を使った探索・判定・集計

## 1. 概要

このトピックでは、Java の `Set` と `Map` を使い、**重複除去・存在判定・件数集計** を確認する。  
ハッシュテーブルの考え方を踏まえたうえで、実務でも使いやすい標準ライブラリの利用イメージをつかむことが目的。

今回のトピックでは、次の内容を確認する。

- `Set` による重複除去
- `Set` による存在判定
- `Map` による件数集計
- 重複している値の抽出
- 確認対象値の出現回数の取得

## 2. 基本の動き

### Set の役割

`Set` は同じ値を 2 回以上持たない。  
そのため、値を順に追加していくと、**重複値は自動的に 1 つにまとまる**。

```text
入力: [10, 20, 30, 20, 10]
Set: [10, 20, 30]
```

### Map の役割

`Map` は「キー -> 値」の対応を持つ。  
今回のトピックでは、キーを数値、値を件数として扱い、**何回出てきたか** を集計する。

```text
入力: [10, 20, 30, 20, 10]
Map: {10=2, 20=2, 30=1}
```

## 3. 処理の流れ

このトピックでは、次の順で Set / Map の動きを確認する。

1. 入力値を先頭から順に読み取る
2. 各値を `Set` へ追加する
3. 各値の件数を `Map` で更新する
4. `Set` の結果から一意な値一覧を確認する
5. `Set.contains(target)` で存在判定を確認する
6. `Map.getOrDefault(target, 0)` で対象値の件数を確認する
7. `Map` の集計結果から重複値を抽出する

trace あり実行では、各値に対して Set と Map がどのように更新されたかを確認できる。

## 4. 計算量

### Set の追加 / 存在判定

- 平均: O(1)
- 最悪: O(n)

### Map の更新 / 取得

- 平均: O(1)
- 最悪: O(n)

平均的には高速だが、内部的な衝突が多い場合は性能が悪化する。  
この点は、前のハッシュテーブルトピックで確認した考え方とつながる。

## 5. メリット / デメリット

### メリット

- 重複除去を簡潔に書ける
- 存在判定を高速に行いやすい
- 件数集計を分かりやすく書ける
- 実務コードでも使う場面が多い

### デメリット

- 内部の順序はそのまま保証されないことがある
- 配列やリストより内部構造が見えにくい
- 衝突や内部実装の詳細までは標準ライブラリの外側に隠れる

## 6. Java実装のポイント

- `HashSet` を使って一意な値を保持する
- `HashMap` を使って値ごとの件数を持つ
- `getOrDefault` を使うと件数更新を簡潔に書ける
- 出力確認時の順序を安定させるため、結果返却時に `TreeSet` / `TreeMap` で昇順へ整えている
- runner 側では入力決定と表示整形を担当し、Set / Map を使った本体処理は `algorithms/` に分離する

## 7. コアとなる処理・重要なコードの見どころ

このトピックで重要なのは、**1回の走査の中で Set と Map を同時に更新すること**。

### Set 更新の見どころ

```java
boolean added = uniqueSet.add(value);
```

重複がなければ追加され、既にあれば追加されない。  
この戻り値を使うと、重複の有無もその場で確認できる。

### Map 更新の見どころ

```java
int nextCount = frequencyMap.getOrDefault(value, 0) + 1;
frequencyMap.put(value, nextCount);
```

値が初登場なら 0 から 1 へ、既に存在するなら件数を 1 増やす。  
件数集計の基本形として重要。

## 8. 実行例

### 基本実行

```bash
java -cp out io.github.seiya_matsuoka.linearsearchhashing.App --topic set-map
```

### trace あり実行

```bash
java -cp out io.github.seiya_matsuoka.linearsearchhashing.App --topic set-map --trace
```

### 任意入力

```bash
java -cp out io.github.seiya_matsuoka.linearsearchhashing.App --topic set-map --input 10,20,30,20,10,40,30,50,20 --target 20
```

### size 指定

```bash
java -cp out io.github.seiya_matsuoka.linearsearchhashing.App --topic set-map --size 12 --trace
```

### 出力で確認する内容

- 一意な値一覧
- 一意な値の件数
- target が含まれるかどうか
- 値ごとの件数一覧
- target の出現回数
- 重複していた値
- trace 時の Set / Map 更新順

## 9. 関連トピック

- ハッシュテーブル
- 線形探索
- 二分探索

Set / Map は、探索や集計を簡潔に書ける実務寄りの道具。  
内部ではハッシュテーブルの考え方が土台にあり、探索の高速化という観点でも重要。

package io.github.seiya_matsuoka.linearsearchhashing.algorithms;

import java.util.ArrayList;
import java.util.List;

/**
 * ハッシュテーブルの基礎を確認するためのクラス
 *
 * <p>このクラスは今回の学習テーマの中心となる部分。
 *
 * <p>ハッシュ関数でバケット位置を決め、衝突時は同じバケット内を連結して保持する流れを 学習用の自前実装で確認できる形にしている。
 */
public final class HashTableBasics {

  private HashTableBasics() {
    // インスタンス化しないユーティリティクラス
  }

  /**
   * バケット内で値を保持するノード
   *
   * <p>衝突時に同じバケットへ複数要素をつなげるために使う。 ここは自前ハッシュテーブル実装の補助構造。
   */
  private static final class Entry {
    private final int value;
    private Entry next;

    private Entry(int value) {
      this.value = value;
    }
  }

  /** 検索結果をまとめる */
  public record SearchResult(boolean found, int bucketIndex, int comparisons, List<String> steps) {}

  /** 構築後の状態をまとめるスナップショット */
  public record HashTableSnapshot(
      int bucketCount,
      int size,
      int collisionCount,
      List<String> bucketDescriptions,
      List<String> buildSteps) {}

  /**
   * 学習用の単純なハッシュテーブル
   *
   * <p>固定長バケット配列と単方向連結で構成する。
   *
   * <p>値の存在確認に用途を絞り、Map のようなキー・値ペアまでは扱わない。
   */
  public static final class SimpleHashTable {
    private static final int DEFAULT_BUCKET_COUNT = 7;

    private final Entry[] buckets;
    private final List<String> buildSteps = new ArrayList<>();
    private int size;
    private int collisionCount;

    public SimpleHashTable() {
      this(DEFAULT_BUCKET_COUNT);
    }

    public SimpleHashTable(int bucketCount) {
      this.buckets = new Entry[Math.max(3, bucketCount)];
    }

    /**
     * 値をハッシュテーブルへ追加する
     *
     * <p>ここが学習対象となるコア処理の1つ。
     *
     * <p>ハッシュ値からバケット位置を求め、衝突した場合は同じバケットの連結末尾へ追加する。
     */
    public void add(int value, boolean trace) {
      int bucketIndex = bucketIndex(value);
      Entry head = buckets[bucketIndex];

      if (head == null) {
        buckets[bucketIndex] = new Entry(value);
        size++;
        if (trace) {
          buildSteps.add("value=" + value + " は bucket[" + bucketIndex + "] が空のため先頭に追加");
        }
        return;
      }

      Entry current = head;
      int chainLength = 0;
      while (true) {
        chainLength++;

        // 重複追加は避け、存在確認用途のテーブルとして扱う。
        if (current.value == value) {
          if (trace) {
            buildSteps.add("value=" + value + " は bucket[" + bucketIndex + "] に既に存在するため追加しない");
          }
          return;
        }

        if (current.next == null) {
          current.next = new Entry(value);
          size++;
          collisionCount++;
          if (trace) {
            buildSteps.add(
                "value="
                    + value
                    + " は bucket["
                    + bucketIndex
                    + "] で衝突。連結末尾へ追加（連結長="
                    + (chainLength + 1)
                    + "）");
          }
          return;
        }

        current = current.next;
      }
    }

    /**
     * 値が含まれているか確認する
     *
     * <p>ここが学習対象となるコア処理のもう1つ。
     *
     * <p>バケット位置を求めたあと、そのバケット内を順にたどって確認する。
     */
    public SearchResult contains(int target, boolean trace) {
      int bucketIndex = bucketIndex(target);
      List<String> steps = new ArrayList<>();
      Entry current = buckets[bucketIndex];
      int comparisons = 0;

      if (trace) {
        steps.add("target=" + target + " の bucket index を計算: " + bucketIndex);
      }

      if (current == null) {
        if (trace) {
          steps.add("bucket[" + bucketIndex + "] は空のため未発見");
        }
        return new SearchResult(false, bucketIndex, comparisons, List.copyOf(steps));
      }

      while (current != null) {
        comparisons++;
        if (trace) {
          steps.add("bucket[" + bucketIndex + "] の値 " + current.value + " を確認");
        }

        if (current.value == target) {
          if (trace) {
            steps.add("target と一致したため探索終了");
          }
          return new SearchResult(true, bucketIndex, comparisons, List.copyOf(steps));
        }

        current = current.next;
      }

      if (trace) {
        steps.add("同じ bucket 内を最後まで確認したが一致しなかったため未発見");
      }
      return new SearchResult(false, bucketIndex, comparisons, List.copyOf(steps));
    }

    /** 現在の状態を表示しやすい形でまとめる */
    public HashTableSnapshot snapshot() {
      List<String> bucketDescriptions = new ArrayList<>();
      for (int i = 0; i < buckets.length; i++) {
        bucketDescriptions.add("bucket[" + i + "]: " + describeBucketChain(buckets[i]));
      }
      return new HashTableSnapshot(
          buckets.length,
          size,
          collisionCount,
          List.copyOf(bucketDescriptions),
          List.copyOf(buildSteps));
    }

    /**
     * 値からバケット位置を求める
     *
     * <p>ここがハッシュテーブル学習で重要な入口。
     *
     * <p>今回は学習用として、値を bucket 数で割った余りを使う単純な方式にする。
     */
    private int bucketIndex(int value) {
      return Math.floorMod(value, buckets.length);
    }

    /**
     * バケット内の連結状態を表示用文字列へ変換する
     *
     * <p>出力確認用の補助処理。ハッシュテーブルのコア処理ではない。
     */
    private String describeBucketChain(Entry head) {
      if (head == null) {
        return "(empty)";
      }

      List<String> values = new ArrayList<>();
      Entry current = head;
      while (current != null) {
        values.add(String.valueOf(current.value));
        current = current.next;
      }
      return String.join(" -> ", values);
    }
  }

  /**
   * 入力配列からハッシュテーブルを構築する
   *
   * <p>runner 側から呼び出しやすくするための補助処理。
   */
  public static SimpleHashTable buildHashTable(int[] values, boolean trace) {
    SimpleHashTable table = new SimpleHashTable();
    for (int value : values) {
      table.add(value, trace);
    }
    return table;
  }
}

package io.github.seiya_matsuoka.linearsearchhashing.algorithms;

import java.util.ArrayList;
import java.util.List;

/**
 * 二分探索の基礎を確認するためのクラス
 *
 * <p>このクラスは今回の学習テーマの中心となる部分。
 *
 * <p>整列済み配列に対して、探索範囲を半分ずつ絞り込みながら目的の値を探す基本形を自前実装で確認できる形にしている。
 */
public final class BinarySearchBasics {

  private BinarySearchBasics() {
    // インスタンス化しないユーティリティクラス
  }

  /**
   * 二分探索の結果をまとめる
   *
   * @param found 見つかったかどうか
   * @param index 見つかった位置。見つからない場合は -1
   * @param comparisons 比較回数
   * @param steps trace 用の手順一覧
   */
  public record SearchResult(boolean found, int index, int comparisons, List<String> steps) {}

  /**
   * 配列が昇順に整列済みかどうかを判定する
   *
   * <p>これは二分探索の前提確認用の補助処理。 学習テーマの本体は search 側にある。
   */
  public static boolean isSortedAscending(int[] values) {
    for (int i = 1; i < values.length; i++) {
      if (values[i - 1] > values[i]) {
        return false;
      }
    }
    return true;
  }

  /**
   * 二分探索を実行する
   *
   * <p>ここが学習対象となるコア処理。
   *
   * <p>low / high / mid を使って探索範囲を絞り込み、一致した時点で探索を終了する。
   */
  public static SearchResult search(int[] values, int target, boolean trace) {
    List<String> steps = new ArrayList<>();
    int comparisons = 0;
    int low = 0;
    int high = values.length - 1;

    // ここが二分探索の本体。
    // 範囲の中央を見ることで、次に調べるべき半分だけを残す。
    while (low <= high) {
      int mid = low + (high - low) / 2;
      int midValue = values[mid];
      comparisons++;

      if (trace) {
        steps.add(
            "low=" + low + ", high=" + high + ", mid=" + mid + ", value=" + midValue + " を確認");
      }

      if (midValue == target) {
        if (trace) {
          steps.add("target と一致したため探索終了");
        }
        return new SearchResult(true, mid, comparisons, List.copyOf(steps));
      }

      if (midValue < target) {
        if (trace) {
          steps.add("target の方が大きいため右半分へ絞り込む");
        }
        low = mid + 1;
      } else {
        if (trace) {
          steps.add("target の方が小さいため左半分へ絞り込む");
        }
        high = mid - 1;
      }
    }

    if (trace) {
      steps.add("探索範囲がなくなったため探索終了");
    }
    return new SearchResult(false, -1, comparisons, List.copyOf(steps));
  }
}

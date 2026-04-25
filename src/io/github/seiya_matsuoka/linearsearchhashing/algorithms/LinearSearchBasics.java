package io.github.seiya_matsuoka.linearsearchhashing.algorithms;

import java.util.ArrayList;
import java.util.List;

/**
 * 線形探索の基礎を確認するためのクラス
 *
 * <p>このクラスは今回の学習テーマの中心となる部分。
 *
 * <p>先頭から末尾まで順に確認し、目的の値が見つかるまで探索を続ける基本形を自前実装で確認できる形にしている。
 */
public final class LinearSearchBasics {

  private LinearSearchBasics() {
    // インスタンス化しないユーティリティクラス
  }

  /**
   * 線形探索の結果をまとめる
   *
   * @param found 見つかったかどうか
   * @param index 見つかった位置。見つからない場合は -1
   * @param comparisons 比較回数
   * @param steps trace 用の手順一覧
   */
  public record SearchResult(boolean found, int index, int comparisons, List<String> steps) {}

  /**
   * 線形探索を実行する
   *
   * <p>ここが学習対象となるコア処理。 配列の先頭から順に値を確認し、一致した時点で探索を終了する。
   */
  public static SearchResult search(int[] values, int target, boolean trace) {
    List<String> steps = new ArrayList<>();
    int comparisons = 0;

    // ここが線形探索の本体。
    // 先頭から末尾まで順に確認し、見つかった時点で終了する。
    for (int i = 0; i < values.length; i++) {
      comparisons++;

      if (trace) {
        steps.add("index=" + i + ", value=" + values[i] + " を確認");
      }

      if (values[i] == target) {
        if (trace) {
          steps.add("target と一致したため探索終了");
        }
        return new SearchResult(true, i, comparisons, List.copyOf(steps));
      }
    }

    if (trace) {
      steps.add("末尾まで確認したが一致しなかったため探索終了");
    }
    return new SearchResult(false, -1, comparisons, List.copyOf(steps));
  }
}

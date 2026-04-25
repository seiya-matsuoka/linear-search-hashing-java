package io.github.seiya_matsuoka.linearsearchhashing.algorithms;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * Set / Map を使った探索・判定・集計の基礎を確認するためのクラス
 *
 * <p>このクラスは今回の学習テーマの中心となる部分。
 *
 * <p>Set による重複除去・存在判定と、Map による件数集計を標準ライブラリで確認できる形にしている。
 */
public final class SetMapBasics {

  private SetMapBasics() {
    // インスタンス化しないユーティリティクラス
  }

  /** Set / Map を使った分析結果をまとめる */
  public record SetMapResult(
      List<Integer> uniqueValues,
      int uniqueCount,
      boolean containsTarget,
      Map<Integer, Integer> frequencyByValue,
      int targetCount,
      List<Integer> duplicateValues,
      List<String> steps) {}

  /**
   * 入力配列を Set / Map で分析する
   *
   * <p>ここが学習対象となるコア処理。
   *
   * <p>Set で存在判定と重複除去、Map で値ごとの件数集計を行う。
   */
  public static SetMapResult analyze(int[] values, int target, boolean trace) {
    Set<Integer> uniqueSet = new HashSet<>();
    Map<Integer, Integer> frequencyMap = new HashMap<>();
    List<String> steps = new ArrayList<>();

    // ここが学習の本体。
    // 1回の走査の中で Set と Map を同時に更新し、存在判定と件数集計を進める。
    for (int value : values) {
      boolean added = uniqueSet.add(value);
      int nextCount = frequencyMap.getOrDefault(value, 0) + 1;
      frequencyMap.put(value, nextCount);

      if (trace) {
        String setMessage =
            added ? "Set に " + value + " を新規追加" : "Set には " + value + " が既に存在するため追加しない";
        String mapMessage = "Map の件数を更新: " + value + " -> " + nextCount;
        steps.add(setMessage + " / " + mapMessage);
      }
    }

    // 表示順が毎回ぶれないよう、結果返却時は昇順へ整える。
    // ここは学習本体ではなく、出力確認を安定させるための補助処理。
    List<Integer> uniqueValues = new ArrayList<>(new TreeSet<>(uniqueSet));
    Map<Integer, Integer> frequencyByValue = new TreeMap<>(frequencyMap);
    List<Integer> duplicateValues = collectDuplicateValues(frequencyByValue);

    return new SetMapResult(
        List.copyOf(uniqueValues),
        uniqueValues.size(),
        uniqueSet.contains(target),
        Collections.unmodifiableMap(new LinkedHashMap<>(frequencyByValue)),
        frequencyMap.getOrDefault(target, 0),
        List.copyOf(duplicateValues),
        List.copyOf(steps));
  }

  /**
   * 件数が 2 回以上の値だけを抽出する
   *
   * <p>Map 集計結果から、重複していた値を確認しやすくするための補助処理。
   */
  private static List<Integer> collectDuplicateValues(Map<Integer, Integer> frequencyByValue) {
    List<Integer> duplicates = new ArrayList<>();
    for (Map.Entry<Integer, Integer> entry : frequencyByValue.entrySet()) {
      if (entry.getValue() >= 2) {
        duplicates.add(entry.getKey());
      }
    }
    return duplicates;
  }
}

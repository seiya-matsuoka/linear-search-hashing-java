package io.github.seiya_matsuoka.linearsearchhashing.runner;

import io.github.seiya_matsuoka.linearsearchhashing.RunnerOptions;
import io.github.seiya_matsuoka.linearsearchhashing.algorithms.HashTableBasics;
import io.github.seiya_matsuoka.linearsearchhashing.algorithms.HashTableBasics.HashTableSnapshot;
import io.github.seiya_matsuoka.linearsearchhashing.algorithms.HashTableBasics.SearchResult;
import io.github.seiya_matsuoka.linearsearchhashing.algorithms.HashTableBasics.SimpleHashTable;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * ハッシュテーブルトピックを実行する runner
 *
 * <p>このクラスの役割は、学習用入力の決定、探索対象値の決定、結果表示。
 *
 * <p>ハッシュテーブルそのものの実装と、バケット計算・衝突処理の本体は {@link HashTableBasics} にまとめている。
 */
public final class HashTableRunner implements TopicRunner {

  private static final int[] DEFAULT_INPUT = {10, 17, 24, 31, 38, 45, 52};

  @Override
  public void run(RunnerOptions options) {
    int[] input = resolveInput(options);
    int target = resolveTarget(options, input);

    System.out.println("=== ハッシュテーブルの基礎 ===");
    System.out.println("入力値: " + formatArray(input));
    System.out.println("探索対象値: " + target);
    System.out.println();

    // ここが学習用のデータ構造構築処理。
    // 入力値を順に add し、バケットへの配置と衝突の様子を確認できる状態を作る。
    SimpleHashTable table = HashTableBasics.buildHashTable(input, options.isTrace());
    HashTableSnapshot snapshot = table.snapshot();

    printSnapshot(snapshot);
    System.out.println();

    // 含まれる値と含まれない値の両方を確認することで、検索の流れを比較しやすくする。
    SearchResult hitResult = table.contains(target, options.isTrace());
    printSearchResult("contains(" + target + ")", hitResult);
    System.out.println();

    int missingTarget = resolveMissingTarget(input, target);
    SearchResult missResult = table.contains(missingTarget, options.isTrace());
    printSearchResult("contains(" + missingTarget + ")", missResult);
  }

  /** 実行に使う入力配列を決定する */
  private int[] resolveInput(RunnerOptions options) {
    if (options.getInput() != null && !options.getInput().isBlank()) {
      return parseCsvInput(options.getInput());
    }

    if (options.getSize() != null) {
      return generateCollisionFriendlyInput(options.getSize());
    }

    return Arrays.copyOf(DEFAULT_INPUT, DEFAULT_INPUT.length);
  }

  /** 探索対象値を決定する */
  private int resolveTarget(RunnerOptions options, int[] input) {
    if (options.getTarget() != null && !options.getTarget().isBlank()) {
      try {
        return Integer.parseInt(options.getTarget().trim());
      } catch (NumberFormatException e) {
        System.out.println("--target の値が整数ではないため無視する: " + options.getTarget());
        System.out.println();
      }
    }

    int defaultIndex = Math.max(0, input.length / 2);
    return input[defaultIndex];
  }

  /** ヒット確認とは別に使う未存在値を決定する */
  private int resolveMissingTarget(int[] input, int target) {
    int candidate = target + 7;
    while (containsValue(input, candidate)) {
      candidate += 7;
    }
    return candidate;
  }

  /** 指定値が入力配列に含まれるか確認する */
  private boolean containsValue(int[] values, int target) {
    for (int value : values) {
      if (value == target) {
        return true;
      }
    }
    return false;
  }

  /** カンマ区切り文字列を int 配列へ変換する */
  private int[] parseCsvInput(String input) {
    String[] tokens = input.split(",");
    int[] values = new int[tokens.length];
    for (int i = 0; i < tokens.length; i++) {
      values[i] = Integer.parseInt(tokens[i].trim());
    }
    return values;
  }

  /**
   * --size 用の入力を生成する
   *
   * <p>固定バケット数でも衝突がある程度起きるよう、7刻みの値を混ぜて生成する。
   */
  private int[] generateCollisionFriendlyInput(int size) {
    int actualSize = Math.max(1, size);
    int[] values = new int[actualSize];
    int current = 10;
    for (int i = 0; i < actualSize; i++) {
      values[i] = current;
      current += 7;
    }
    return values;
  }

  /** バケット状態を見やすい形式で表示する */
  private void printSnapshot(HashTableSnapshot snapshot) {
    System.out.println("構築結果");
    System.out.println("  bucketCount: " + snapshot.bucketCount());
    System.out.println("  size: " + snapshot.size());
    System.out.println("  collisions: " + snapshot.collisionCount());
    System.out.println("  buckets:");
    for (String bucketLine : snapshot.bucketDescriptions()) {
      System.out.println("    " + bucketLine);
    }

    if (!snapshot.buildSteps().isEmpty()) {
      System.out.println();
      System.out.println("構築 trace");
      for (String step : snapshot.buildSteps()) {
        System.out.println("  " + step);
      }
    }
  }

  /** 検索結果を見やすい形式で表示する */
  private void printSearchResult(String label, SearchResult result) {
    System.out.println(label);
    System.out.println("  found: " + result.found());
    System.out.println("  bucketIndex: " + result.bucketIndex());
    System.out.println("  comparisons: " + result.comparisons());

    if (!result.steps().isEmpty()) {
      System.out.println("  trace:");
      for (String step : result.steps()) {
        System.out.println("    " + step);
      }
    }
  }

  /** 入力配列を表示用文字列へ変換する */
  private String formatArray(int[] values) {
    return Arrays.stream(values)
        .mapToObj(String::valueOf)
        .collect(Collectors.joining(", ", "[", "]"));
  }
}

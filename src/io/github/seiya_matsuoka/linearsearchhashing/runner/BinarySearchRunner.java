package io.github.seiya_matsuoka.linearsearchhashing.runner;

import io.github.seiya_matsuoka.linearsearchhashing.RunnerOptions;
import io.github.seiya_matsuoka.linearsearchhashing.algorithms.BinarySearchBasics;
import io.github.seiya_matsuoka.linearsearchhashing.algorithms.BinarySearchBasics.SearchResult;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * 二分探索トピックを実行する runner
 *
 * <p>このクラスの役割は、学習用入力の決定、整列済み前提の確認、探索対象値の決定、結果表示。
 *
 * <p>二分探索そのものの処理本体は {@link BinarySearchBasics} にまとめている。
 */
public final class BinarySearchRunner implements TopicRunner {

  private static final int[] DEFAULT_INPUT = {3, 5, 7, 12, 18, 25, 30};

  @Override
  public void run(RunnerOptions options) {
    int[] input = resolveInput(options);
    if (!BinarySearchBasics.isSortedAscending(input)) {
      System.out.println("=== 二分探索の基礎 ===");
      System.out.println("入力値: " + formatArray(input));
      System.out.println();
      System.out.println("二分探索は昇順に整列された配列が前提");
      System.out.println("この入力では前提を満たさないため実行しない");
      System.out.println("整列済みの値を --input で指定するか、--size を使って連番入力を生成する");
      return;
    }

    int target = resolveTarget(options, input);

    System.out.println("=== 二分探索の基礎 ===");
    System.out.println("入力値: " + formatArray(input));
    System.out.println("探索対象値: " + target);
    System.out.println();

    SearchResult result = BinarySearchBasics.search(input, target, options.isTrace());
    printResult(result);
  }

  /** 実行に使う入力配列を決定する */
  private int[] resolveInput(RunnerOptions options) {
    if (options.getInput() != null && !options.getInput().isBlank()) {
      return parseCsvInput(options.getInput());
    }

    if (options.getSize() != null) {
      return generateSequentialInput(options.getSize());
    }

    return Arrays.copyOf(DEFAULT_INPUT, DEFAULT_INPUT.length);
  }

  /**
   * 探索対象値を決定する
   *
   * <p>target 指定があればそれを優先し、未指定なら真ん中より右側の値を使う。
   *
   * <p>何回か範囲を絞る動きが出やすい位置を選ぶ。
   */
  private int resolveTarget(RunnerOptions options, int[] input) {
    if (options.getTarget() != null && !options.getTarget().isBlank()) {
      try {
        return Integer.parseInt(options.getTarget().trim());
      } catch (NumberFormatException e) {
        System.out.println("--target の値が整数ではないため無視する: " + options.getTarget());
        System.out.println();
      }
    }

    int defaultIndex = Math.min(input.length - 1, Math.max(0, input.length / 2 + 1));
    return input[defaultIndex];
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

  /** --size 用の連番入力を生成する */
  private int[] generateSequentialInput(int size) {
    int actualSize = Math.max(1, size);
    int[] values = new int[actualSize];
    for (int i = 0; i < actualSize; i++) {
      values[i] = i + 1;
    }
    return values;
  }

  /** 探索結果を見やすい形式で表示する */
  private void printResult(SearchResult result) {
    System.out.println("探索結果");
    System.out.println("  found: " + result.found());
    System.out.println("  index: " + result.index());
    System.out.println("  comparisons: " + result.comparisons());
    System.out.println();

    if (!result.steps().isEmpty()) {
      System.out.println("trace");
      for (String step : result.steps()) {
        System.out.println("  " + step);
      }
      System.out.println();
    }
  }

  /** 入力配列を表示用文字列へ変換する */
  private String formatArray(int[] values) {
    return Arrays.stream(values)
        .mapToObj(String::valueOf)
        .collect(Collectors.joining(", ", "[", "]"));
  }
}

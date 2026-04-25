package io.github.seiya_matsuoka.linearsearchhashing.runner;

import io.github.seiya_matsuoka.linearsearchhashing.RunnerOptions;
import io.github.seiya_matsuoka.linearsearchhashing.algorithms.LinearSearchBasics;
import io.github.seiya_matsuoka.linearsearchhashing.algorithms.LinearSearchBasics.SearchResult;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * 線形探索トピックを実行する runner
 *
 * <p>このクラスの役割は、学習用入力の決定、探索対象値の決定、結果表示。
 *
 * <p>線形探索そのものの処理本体は {@link LinearSearchBasics} にまとめている。
 */
public final class LinearSearchRunner implements TopicRunner {

  private static final int[] DEFAULT_INPUT = {12, 7, 25, 3, 18, 30, 5};

  @Override
  public void run(RunnerOptions options) {
    int[] input = resolveInput(options);
    int target = resolveTarget(options, input);

    System.out.println("=== 線形探索の基礎 ===");
    System.out.println("入力値: " + formatArray(input));
    System.out.println("探索対象値: " + target);
    System.out.println();

    SearchResult result = LinearSearchBasics.search(input, target, options.isTrace());
    printResult(result);
  }

  /**
   * 実行に使う入力配列を決定する
   *
   * <p>ここは学習本体ではなく、試しやすさのための補助処理。
   */
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
   * <p>target 指定があればそれを優先し、未指定なら入力配列の末尾寄りの値を使う。
   *
   * <p>線形探索では後ろ側の値を探すと、先頭から順に見る動きが分かりやすい。
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

    return input[Math.max(0, input.length - 2)];
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

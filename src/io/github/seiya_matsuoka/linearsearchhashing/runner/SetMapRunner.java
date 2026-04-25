package io.github.seiya_matsuoka.linearsearchhashing.runner;

import io.github.seiya_matsuoka.linearsearchhashing.RunnerOptions;
import io.github.seiya_matsuoka.linearsearchhashing.algorithms.SetMapBasics;
import io.github.seiya_matsuoka.linearsearchhashing.algorithms.SetMapBasics.SetMapResult;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * Set / Map トピックを実行する runner
 *
 * <p>このクラスの役割は、学習用入力の決定、確認対象値の決定、結果表示。
 *
 * <p>Set / Map を使った探索・判定・集計の本体は {@link SetMapBasics} にまとめている。
 */
public final class SetMapRunner implements TopicRunner {

  private static final int[] DEFAULT_INPUT = {10, 20, 30, 20, 10, 40, 30, 50, 20};

  @Override
  public void run(RunnerOptions options) {
    int[] input = resolveInput(options);
    int target = resolveTarget(options, input);

    System.out.println("=== Set / Map を使った探索・判定・集計 ===");
    System.out.println("入力値: " + formatArray(input));
    System.out.println("確認対象値: " + target);
    System.out.println();

    SetMapResult result = SetMapBasics.analyze(input, target, options.isTrace());
    printResult(result);
  }

  /** 実行に使う入力配列を決定する */
  private int[] resolveInput(RunnerOptions options) {
    if (options.getInput() != null && !options.getInput().isBlank()) {
      return parseCsvInput(options.getInput());
    }

    if (options.getSize() != null) {
      return generateDuplicatedInput(options.getSize());
    }

    return Arrays.copyOf(DEFAULT_INPUT, DEFAULT_INPUT.length);
  }

  /** 確認対象値を決定する */
  private int resolveTarget(RunnerOptions options, int[] input) {
    if (options.getTarget() != null && !options.getTarget().isBlank()) {
      try {
        return Integer.parseInt(options.getTarget().trim());
      } catch (NumberFormatException e) {
        System.out.println("--target の値が整数ではないため無視する: " + options.getTarget());
        System.out.println();
      }
    }

    int defaultIndex = Math.min(input.length - 1, Math.max(0, input.length / 3));
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

  /**
   * --size 用の重複あり入力を生成する
   *
   * <p>Set の重複除去と Map の件数集計が分かりやすいよう、1〜5 の繰り返し値を生成する。
   */
  private int[] generateDuplicatedInput(int size) {
    int actualSize = Math.max(1, size);
    int[] values = new int[actualSize];
    for (int i = 0; i < actualSize; i++) {
      values[i] = i % 5 + 1;
    }
    return values;
  }

  /** Set / Map の分析結果を見やすい形式で表示する */
  private void printResult(SetMapResult result) {
    System.out.println("Set を使った確認");
    System.out.println("  uniqueValues: " + result.uniqueValues());
    System.out.println("  uniqueCount: " + result.uniqueCount());
    System.out.println("  containsTarget: " + result.containsTarget());
    System.out.println();

    System.out.println("Map を使った集計");
    System.out.println("  frequencyByValue: " + result.frequencyByValue());
    System.out.println("  targetCount: " + result.targetCount());
    System.out.println("  duplicateValues: " + result.duplicateValues());

    if (!result.steps().isEmpty()) {
      System.out.println();
      System.out.println("trace");
      for (String step : result.steps()) {
        System.out.println("  " + step);
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

package io.github.seiya_matsuoka.linearsearchhashing.runner;

import io.github.seiya_matsuoka.linearsearchhashing.RunnerOptions;
import io.github.seiya_matsuoka.linearsearchhashing.datastructures.StackBasics;
import io.github.seiya_matsuoka.linearsearchhashing.datastructures.StackBasics.ArrayStack;
import io.github.seiya_matsuoka.linearsearchhashing.datastructures.StackBasics.StackSnapshot;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * スタックトピックを実行する runner
 *
 * <p>このクラスの役割は、学習用入力の決定、実行順の組み立て、結果表示。
 *
 * <p>スタックそのものの push / pop / peek の実装本体は {@link StackBasics} にまとめている。
 */
public final class StackRunner implements TopicRunner {

  private static final int[] DEFAULT_INPUT = {10, 20, 30, 40};
  private static final int PUSH_VALUE = 99;

  @Override
  public void run(RunnerOptions options) {
    int[] input = resolveInput(options);

    System.out.println("=== スタックの基礎 ===");
    System.out.println("入力値: " + formatArray(input));
    System.out.println();

    if (options.getTarget() != null) {
      System.out.println("--target は stack トピックでは使用しないため無視する");
      System.out.println();
    }

    runStackDemo(input, options.isTrace());
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

  /** 入力配列を表示用文字列へ変換する */
  private String formatArray(int[] values) {
    return Arrays.stream(values)
        .mapToObj(String::valueOf)
        .collect(Collectors.joining(", ", "[", "]"));
  }

  /**
   * スタックの基本操作を順に実行する
   *
   * <p>ここでは、LIFO の流れが見えるように push / peek / pop を固定順で確認する。
   *
   * <p>実際に要素を保持し、取り出す本体処理は {@link ArrayStack} 側にある。
   */
  private void runStackDemo(int[] input, boolean trace) {
    System.out.println("[配列ベースのスタック]");

    // ここが学習対象となるデータ構造の生成部分。
    // 入力値を順に push して初期状態を作る。
    ArrayStack stack = StackBasics.buildStack(input);
    printState("初期状態", stack.snapshot());

    if (trace) {
      System.out.println("  trace: 先に入れた要素ほど下側に積まれ、最後に入れた要素が先に取り出される");
    }

    stack.push(PUSH_VALUE);
    printState("push(" + PUSH_VALUE + ") 実行後", stack.snapshot());
    if (trace) {
      System.out.println("  trace: top を 1 つ進め、その位置へ値を格納する");
    }

    int peekValue = stack.peek();
    System.out.println("peek(): " + peekValue);
    if (trace) {
      System.out.println("  trace: peek は top の値を見るだけで、要素は取り除かない");
    }

    int popped = stack.pop();
    printState("pop() 実行後（取り出し値: " + popped + "）", stack.snapshot());
    if (trace) {
      System.out.println("  trace: pop は top の値を返した後に top を 1 つ戻す");
    }

    System.out.println("isEmpty(): " + stack.isEmpty());
  }

  /**
   * スタックの状態を見やすい形式で表示する
   *
   * <p>ここは出力整形用の補助処理。スタックの本体処理ではない。
   */
  private void printState(String label, StackSnapshot snapshot) {
    System.out.println(label);
    System.out.println("  要素(bottom -> top): " + snapshot.elements());
    System.out.println("  size: " + snapshot.size());
    System.out.println("  top: " + snapshot.topValue());
  }
}

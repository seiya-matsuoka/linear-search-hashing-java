package io.github.seiya_matsuoka.linearsearchhashing.runner;

import io.github.seiya_matsuoka.linearsearchhashing.RunnerOptions;
import io.github.seiya_matsuoka.linearsearchhashing.datastructures.QueueBasics;
import io.github.seiya_matsuoka.linearsearchhashing.datastructures.QueueBasics.ArrayQueue;
import io.github.seiya_matsuoka.linearsearchhashing.datastructures.QueueBasics.QueueSnapshot;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * キュートピックを実行する runner
 *
 * <p>このクラスの役割は、学習用入力の決定、実行順の組み立て、結果表示。
 *
 * <p>キューそのものの enqueue / dequeue / peek の実装本体は {@link QueueBasics} にまとめている。
 */
public final class QueueRunner implements TopicRunner {

  private static final int[] DEFAULT_INPUT = {10, 20, 30, 40};
  private static final int ENQUEUE_VALUE = 99;

  @Override
  public void run(RunnerOptions options) {
    int[] input = resolveInput(options);

    System.out.println("=== キューの基礎 ===");
    System.out.println("入力値: " + formatArray(input));
    System.out.println();

    if (options.getTarget() != null) {
      System.out.println("--target は queue トピックでは使用しないため無視する");
      System.out.println();
    }

    runQueueDemo(input, options.isTrace());
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
   * キューの基本操作を順に実行する
   *
   * <p>ここでは、FIFO の流れが見えるように enqueue / peek / dequeue を固定順で確認する。
   *
   * <p>実際に要素を保持し、先頭から取り出す本体処理は {@link ArrayQueue} 側にある。
   */
  private void runQueueDemo(int[] input, boolean trace) {
    System.out.println("[配列ベースのキュー]");

    // ここが学習対象となるデータ構造の生成部分。
    // 入力値を順に enqueue して初期状態を作る。
    ArrayQueue queue = QueueBasics.buildQueue(input);
    printState("初期状態", queue.snapshot());

    if (trace) {
      System.out.println("  trace: 先に入れた要素ほど先に取り出される");
    }

    queue.enqueue(ENQUEUE_VALUE);
    printState("enqueue(" + ENQUEUE_VALUE + ") 実行後", queue.snapshot());
    if (trace) {
      System.out.println("  trace: rear 側へ値を追加し、件数を 1 つ増やす");
    }

    int peekValue = queue.peek();
    System.out.println("peek(): " + peekValue);
    if (trace) {
      System.out.println("  trace: peek は front の値を見るだけで、要素は取り除かない");
    }

    int dequeued = queue.dequeue();
    printState("dequeue() 実行後（取り出し値: " + dequeued + "）", queue.snapshot());
    if (trace) {
      System.out.println("  trace: dequeue は front の値を返した後に front を 1 つ進める");
    }

    System.out.println("isEmpty(): " + queue.isEmpty());
  }

  /** キューの状態を見やすい形式で表示する */
  private void printState(String label, QueueSnapshot snapshot) {
    System.out.println(label);
    System.out.println("  要素(front -> rear): " + snapshot.elements());
    System.out.println("  size: " + snapshot.size());
    System.out.println("  front: " + snapshot.frontValue());
    System.out.println("  rear: " + snapshot.rearValue());
  }
}

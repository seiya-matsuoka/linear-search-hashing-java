package io.github.seiya_matsuoka.linearsearchhashing.runner;

import io.github.seiya_matsuoka.linearsearchhashing.RunnerOptions;
import io.github.seiya_matsuoka.linearsearchhashing.datastructures.DequeBasics;
import io.github.seiya_matsuoka.linearsearchhashing.datastructures.DequeBasics.DequeSnapshot;
import io.github.seiya_matsuoka.linearsearchhashing.datastructures.DequeBasics.LinkedDeque;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * デックトピックを実行する runner
 *
 * <p>このクラスの役割は、学習用入力の決定、実行順の組み立て、結果表示。
 *
 * <p>デックそのものの addFirst / addLast / removeFirst / removeLast の実装本体は {@link DequeBasics} にまとめている。
 */
public final class DequeRunner implements TopicRunner {

  private static final int[] DEFAULT_INPUT = {10, 20, 30, 40};
  private static final int ADD_FIRST_VALUE = 5;
  private static final int ADD_LAST_VALUE = 99;

  @Override
  public void run(RunnerOptions options) {
    int[] input = resolveInput(options);

    System.out.println("=== デックの基礎 ===");
    System.out.println("入力値: " + formatArray(input));
    System.out.println();

    if (options.getTarget() != null) {
      System.out.println("--target は deque トピックでは使用しないため無視する");
      System.out.println();
    }

    runDequeDemo(input, options.isTrace());
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
   * デックの基本操作を順に実行する
   *
   * <p>ここでは、前後どちらからでも追加・削除できる点が見えるように操作順を固定して確認する。
   *
   * <p>実際に前後ノードを接続する本体処理は {@link LinkedDeque} 側にある。
   */
  private void runDequeDemo(int[] input, boolean trace) {
    System.out.println("[双方向連結リストベースのデック]");

    // ここが学習対象となるデータ構造の生成部分。
    // 入力値は addLast で順に後ろへ追加して初期状態を作る。
    LinkedDeque deque = DequeBasics.buildDeque(input);
    printState("初期状態", deque.snapshot());

    if (trace) {
      System.out.println("  trace: デックは先頭側・末尾側のどちらからも追加と削除を行える");
    }

    deque.addFirst(ADD_FIRST_VALUE);
    printState("addFirst(" + ADD_FIRST_VALUE + ") 実行後", deque.snapshot());
    if (trace) {
      System.out.println("  trace: 先頭ノードの前へ新しいノードを差し込み、head を更新する");
    }

    deque.addLast(ADD_LAST_VALUE);
    printState("addLast(" + ADD_LAST_VALUE + ") 実行後", deque.snapshot());
    if (trace) {
      System.out.println("  trace: 末尾ノードの後ろへ新しいノードを接続し、tail を更新する");
    }

    int removedFirst = deque.removeFirst();
    printState("removeFirst() 実行後（取り出し値: " + removedFirst + "）", deque.snapshot());
    if (trace) {
      System.out.println("  trace: head を次ノードへ進め、旧 head を構造から外す");
    }

    int removedLast = deque.removeLast();
    printState("removeLast() 実行後（取り出し値: " + removedLast + "）", deque.snapshot());
    if (trace) {
      System.out.println("  trace: tail を前ノードへ戻し、旧 tail を構造から外す");
    }

    System.out.println("peekFirst(): " + deque.peekFirst());
    System.out.println("peekLast(): " + deque.peekLast());
  }

  /** デックの状態を見やすい形式で表示する */
  private void printState(String label, DequeSnapshot snapshot) {
    System.out.println(label);
    System.out.println("  要素(front -> rear): " + snapshot.elements());
    System.out.println("  size: " + snapshot.size());
    System.out.println("  first: " + snapshot.firstValue());
    System.out.println("  last: " + snapshot.lastValue());
  }
}

package io.github.seiya_matsuoka.linearsearchhashing.runner;

import io.github.seiya_matsuoka.linearsearchhashing.RunnerOptions;
import io.github.seiya_matsuoka.linearsearchhashing.datastructures.LinkedListBasics;
import io.github.seiya_matsuoka.linearsearchhashing.datastructures.LinkedListBasics.DoublyLinkedList;
import io.github.seiya_matsuoka.linearsearchhashing.datastructures.LinkedListBasics.DoublyLinkedListSnapshot;
import io.github.seiya_matsuoka.linearsearchhashing.datastructures.LinkedListBasics.SinglyLinkedList;
import io.github.seiya_matsuoka.linearsearchhashing.datastructures.LinkedListBasics.SinglyLinkedListSnapshot;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * 連結リストトピックを実行する runner
 *
 * <p>このクラスの役割は、学習用入力の決定、実行順の組み立て、結果表示。
 *
 * <p>連結リストそのものの実装と、ノード参照のつなぎ替えは {@link LinkedListBasics} にまとめている。
 */
public final class LinkedListRunner implements TopicRunner {

  private static final int[] DEFAULT_INPUT = {10, 20, 30, 40};
  private static final int DEFAULT_TARGET = 30;
  private static final int PREPEND_VALUE = 5;
  private static final int APPEND_VALUE = 99;
  private static final int INSERT_VALUE = 77;
  private static final int INSERT_INDEX = 2;
  private static final int REMOVE_INDEX = 3;

  @Override
  public void run(RunnerOptions options) {
    // 入力値は --input → --size → 既定値 の順で決定する。
    int[] input = resolveInput(options);

    // target は contains / indexOf の確認対象として使う。
    int target = resolveTarget(options);

    System.out.println("=== 連結リストの基礎 ===");
    System.out.println("入力値: " + formatArray(input));
    System.out.println("確認対象値: " + target);
    System.out.println();

    if (options.getTarget() != null) {
      System.out.println("--target を使用して contains / indexOf の確認対象値を変更している");
      System.out.println();
    }

    // 単方向連結リストと双方向連結リストを順に確認する。
    // 単方向では next のみ、双方向では next / prev の両方を見る。
    runSinglyLinkedListDemo(input, target, options.isTrace());
    System.out.println();
    runDoublyLinkedListDemo(input, options.isTrace());
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
   * カンマ区切り文字列を int 配列へ変換する
   *
   * <p>任意入力を試しやすくするための補助処理。
   */
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

  /** contains / indexOf に使う対象値を決定する */
  private int resolveTarget(RunnerOptions options) {
    if (options.getTarget() == null || options.getTarget().isBlank()) {
      return DEFAULT_TARGET;
    }
    return Integer.parseInt(options.getTarget());
  }

  /** 入力配列を表示用文字列へ変換する */
  private String formatArray(int[] values) {
    return Arrays.stream(values)
        .mapToObj(String::valueOf)
        .collect(Collectors.joining(", ", "[", "]"));
  }

  /**
   * 単方向連結リストの基本操作を順に実行する
   *
   * <p>ここでは、単方向連結リストの学習で見たい操作を固定順で実行する。
   *
   * <p>実際にノード参照をつなぎ替える本体処理は {@link LinkedListBasics.SinglyLinkedList} 側にある。
   */
  private void runSinglyLinkedListDemo(int[] input, int target, boolean trace) {
    System.out.println("[単方向連結リスト]");

    // ここが学習対象となるデータ構造の生成部分。
    // append を使って末尾へ順に連結し、初期状態を構築する。
    SinglyLinkedList list = LinkedListBasics.buildSingly(input);
    printSinglyState("初期状態", list.snapshot());

    if (trace) {
      System.out.println("  trace: append で末尾にノードを順に連結して初期構築を行う");
    }

    // 先頭追加
    // 新しいノードを作り、head の前へ差し込む。
    list.prepend(PREPEND_VALUE);
    printSinglyState("先頭に " + PREPEND_VALUE + " を追加", list.snapshot());
    if (trace) {
      System.out.println("  trace: 新しいノードの next を現在の head へ向けてから head を差し替える");
    }

    // 末尾追加
    // tail を持っているため、末尾ノードへ直接つなげられる。
    list.append(APPEND_VALUE);
    printSinglyState("末尾に " + APPEND_VALUE + " を追加", list.snapshot());
    if (trace) {
      System.out.println("  trace: tail の next を新しいノードへ向け、tail 自体も更新する");
    }

    // 中間挿入
    // 挿入位置の1つ前まで順にたどり、参照のつなぎ先を差し替える。
    list.insertAt(INSERT_INDEX, INSERT_VALUE);
    printSinglyState(INSERT_INDEX + " 番目に " + INSERT_VALUE + " を挿入", list.snapshot());
    if (trace) {
      System.out.println("  trace: 挿入位置の1つ前まで順にたどり、参照のつなぎ先を差し替える");
    }

    // 中間削除
    // 削除対象の1つ前のノードが持つ next を付け替える。
    int removedValue = list.removeAt(REMOVE_INDEX);
    printSinglyState(REMOVE_INDEX + " 番目を削除（削除値: " + removedValue + "）", list.snapshot());
    if (trace) {
      System.out.println("  trace: 削除対象の1つ前の next を付け替え、対象ノードを飛ばす");
    }

    // contains / indexOf は、先頭から順にたどる走査の確認として出力する。
    boolean contains = list.contains(target);
    int index = list.indexOf(target);
    System.out.println("contains(" + target + "): " + contains);
    System.out.println("indexOf(" + target + "): " + index);
  }

  /**
   * 双方向連結リストの基本操作を順に実行する
   *
   * <p>単方向連結リストとの違いとして、prev を持つ点と、後ろからもたどれる点を確認する。
   */
  private void runDoublyLinkedListDemo(int[] input, boolean trace) {
    System.out.println("[双方向連結リスト]");

    // ここが学習対象となるデータ構造の生成部分。
    DoublyLinkedList list = LinkedListBasics.buildDoubly(input);
    printDoublyState("初期状態", list.snapshot());

    if (trace) {
      System.out.println("  trace: next に加えて prev も持つため、後ろからもたどれる");
    }

    // 先頭追加
    // 新旧の先頭ノードの prev / next を相互に更新する。
    list.prepend(PREPEND_VALUE);
    printDoublyState("先頭に " + PREPEND_VALUE + " を追加", list.snapshot());
    if (trace) {
      System.out.println("  trace: 新しい先頭ノードと旧 head の prev / next を相互に更新する");
    }

    // 末尾追加
    // 旧 tail と新しいノードを prev / next で接続する。
    list.append(APPEND_VALUE);
    printDoublyState("末尾に " + APPEND_VALUE + " を追加", list.snapshot());
    if (trace) {
      System.out.println("  trace: 旧 tail と新しいノードを prev / next で接続する");
    }

    // 中間削除
    // 前後ノードの参照をつなぎ替え、中間ノードを外す。
    int removedValue = list.removeAt(REMOVE_INDEX);
    printDoublyState(REMOVE_INDEX + " 番目を削除（削除値: " + removedValue + "）", list.snapshot());
    if (trace) {
      System.out.println("  trace: 前後ノードの参照をつなぎ替えることで中間ノードを外す");
    }
  }

  /**
   * 単方向連結リストの状態を見やすい形式で表示する
   *
   * <p>ここは出力整形用の補助処理。連結リストの本体処理ではない。
   */
  private void printSinglyState(String label, SinglyLinkedListSnapshot snapshot) {
    System.out.println(label);
    System.out.println("  要素: " + snapshot.elements());
    System.out.println("  size: " + snapshot.size());
    System.out.println("  first: " + snapshot.firstValue());
    System.out.println("  last: " + snapshot.lastValue());
  }

  /** 双方向連結リストの状態を見やすい形式で表示する */
  private void printDoublyState(String label, DoublyLinkedListSnapshot snapshot) {
    System.out.println(label);
    System.out.println("  forward: " + snapshot.forwardElements());
    System.out.println("  backward: " + snapshot.backwardElements());
    System.out.println("  size: " + snapshot.size());
    System.out.println("  first: " + snapshot.firstValue());
    System.out.println("  last: " + snapshot.lastValue());
  }
}

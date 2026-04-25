package io.github.seiya_matsuoka.linearsearchhashing.datastructures;

import java.util.ArrayList;
import java.util.List;

/**
 * キューの基礎を確認するためのクラス
 *
 * <p>このクラスは今回の学習テーマの中心となる部分。
 *
 * <p>キューの FIFO の性質と、enqueue / dequeue / peek の基本動作を自前実装で確認できる形にしている。
 */
public final class QueueBasics {

  private QueueBasics() {
    // インスタンス化しないユーティリティクラス
  }

  /** キューの状態をまとめるスナップショット */
  public record QueueSnapshot(
      List<Integer> elements, int size, Integer frontValue, Integer rearValue) {}

  /**
   * 配列ベースのキュー
   *
   * <p>ここで見たいのは、front / rear を使って先頭から取り出し、末尾へ追加する流れ。
   *
   * <p>実装は循環配列を使い、先頭へ詰め直さずに進める形にしている。
   */
  public static final class ArrayQueue {
    private int[] elements = new int[8];
    private int front;
    private int size;

    /**
     * 値をキューの末尾へ追加する
     *
     * <p>ここがキューの基本操作の1つ。 rear に相当する位置へ値を追加する。
     */
    public void enqueue(int value) {
      ensureCapacity();

      int rearIndex = physicalIndex(size);

      // ここがキュー本体の更新部分。
      // 末尾位置へ値を入れ、その後 size を増やす。
      elements[rearIndex] = value;
      size++;
    }

    /**
     * キューの先頭から値を取り出す
     *
     * <p>ここが FIFO を確認しやすい部分。 front の値を返し、front を 1 つ進める。
     */
    public int dequeue() {
      validateNotEmpty();

      int value = elements[front];

      // ここがキュー本体の更新部分。
      // 先頭値を取り出した後に front を進め、size を減らす。
      elements[front] = 0;
      front = (front + 1) % elements.length;
      size--;
      return value;
    }

    /** キューの先頭値を見る */
    public int peek() {
      validateNotEmpty();
      return elements[front];
    }

    public boolean isEmpty() {
      return size == 0;
    }

    public int size() {
      return size;
    }

    /**
     * キューの内容を先頭から末尾へ List 化する
     *
     * <p>これは状態確認をしやすくするための補助処理。 キューのコア処理ではない。
     */
    public List<Integer> toList() {
      List<Integer> values = new ArrayList<>();
      for (int i = 0; i < size; i++) {
        values.add(elements[physicalIndex(i)]);
      }
      return values;
    }

    /** 学習用の状態確認をまとめて返す */
    public QueueSnapshot snapshot() {
      return new QueueSnapshot(
          toList(),
          size,
          size == 0 ? null : elements[front],
          size == 0 ? null : elements[physicalIndex(size - 1)]);
    }

    /**
     * 論理的な位置を内部配列上の位置へ変換する補助処理
     *
     * <p>ここは循環配列を扱うための補助実装。 学習テーマの本体は enqueue / dequeue の流れ。
     */
    private int physicalIndex(int logicalOffset) {
      return (front + logicalOffset) % elements.length;
    }

    /** 内部配列の容量不足を解消する補助処理 */
    private void ensureCapacity() {
      if (size < elements.length) {
        return;
      }

      int[] expanded = new int[elements.length * 2];
      for (int i = 0; i < size; i++) {
        expanded[i] = elements[physicalIndex(i)];
      }
      elements = expanded;
      front = 0;
    }

    /** 空キュー操作を防ぐ補助処理 */
    private void validateNotEmpty() {
      if (isEmpty()) {
        throw new IllegalStateException("空のキューに対して操作はできない");
      }
    }
  }

  /** 入力配列からキューを構築する */
  public static ArrayQueue buildQueue(int[] values) {
    ArrayQueue queue = new ArrayQueue();
    for (int value : values) {
      queue.enqueue(value);
    }
    return queue;
  }
}

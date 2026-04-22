package io.github.seiya_matsuoka.linearsearchhashing.datastructures;

import java.util.ArrayList;
import java.util.List;

/**
 * スタックの基礎を確認するためのクラス
 *
 * <p>このクラスは今回の学習テーマの中心となる部分。
 *
 * <p>スタックの LIFO の性質と、push / pop / peek の基本動作を自前実装で確認できる形にしている。
 */
public final class StackBasics {

  private StackBasics() {
    // インスタンス化しないユーティリティクラス
  }

  /**
   * スタックの状態をまとめるスナップショット
   *
   * <p>runner 側で状態を表示しやすくするための補助データ。
   */
  public record StackSnapshot(List<Integer> elements, int size, Integer topValue) {}

  /**
   * 配列ベースのスタック
   *
   * <p>ここで見たいのは、top を基準に push / pop を行う流れ。
   *
   * <p>実装は学習用の最小構成とし、自動拡張可能な配列を内部で使う。
   */
  public static final class ArrayStack {
    private int[] elements = new int[8];
    private int size;

    /**
     * 値をスタックの先頭へ積む
     *
     * <p>ここがスタックの基本操作の1つ。 top に相当する末尾位置へ値を追加する。
     */
    public void push(int value) {
      ensureCapacity();

      // ここがスタック本体の更新部分。
      // size が示す次の空き位置へ値を入れ、その後 size を増やす。
      elements[size] = value;
      size++;
    }

    /**
     * スタックの先頭から値を取り出す
     *
     * <p>ここが LIFO を確認しやすい部分。 末尾位置の値を返し、size を 1 つ減らす。
     */
    public int pop() {
      validateNotEmpty();

      int topIndex = size - 1;
      int value = elements[topIndex];

      // ここがスタック本体の更新部分。
      // 末尾値を取り出した後に size を 1 つ戻し、不要値をクリアする。
      elements[topIndex] = 0;
      size--;
      return value;
    }

    /**
     * スタックの先頭値を見る
     *
     * <p>peek は状態を変えずに top を確認するための操作。
     */
    public int peek() {
      validateNotEmpty();
      return elements[size - 1];
    }

    public boolean isEmpty() {
      return size == 0;
    }

    public int size() {
      return size;
    }

    /**
     * スタックの内容を下から上へ List 化する
     *
     * <p>これは状態確認をしやすくするための補助処理。 スタックのコア処理ではない。
     */
    public List<Integer> toList() {
      List<Integer> values = new ArrayList<>();
      for (int i = 0; i < size; i++) {
        values.add(elements[i]);
      }
      return values;
    }

    /** 学習用の状態確認をまとめて返す */
    public StackSnapshot snapshot() {
      return new StackSnapshot(toList(), size, size == 0 ? null : elements[size - 1]);
    }

    /**
     * 内部配列の容量不足を解消する補助処理
     *
     * <p>ここは学習テーマの本体ではなく、入力件数が増えても試せるようにするための補助実装。
     */
    private void ensureCapacity() {
      if (size < elements.length) {
        return;
      }

      int[] expanded = new int[elements.length * 2];
      System.arraycopy(elements, 0, expanded, 0, elements.length);
      elements = expanded;
    }

    /** 空スタック操作を防ぐ補助処理 */
    private void validateNotEmpty() {
      if (isEmpty()) {
        throw new IllegalStateException("空のスタックに対して操作はできない");
      }
    }
  }

  /**
   * 入力配列からスタックを構築する
   *
   * <p>runner 側から呼び出しやすくするための補助処理。
   */
  public static ArrayStack buildStack(int[] values) {
    ArrayStack stack = new ArrayStack();
    for (int value : values) {
      stack.push(value);
    }
    return stack;
  }
}

package io.github.seiya_matsuoka.linearsearchhashing.datastructures;

import java.util.ArrayList;
import java.util.List;

/**
 * デックの基礎を確認するためのクラス
 *
 * <p>このクラスは今回の学習テーマの中心となる部分。
 *
 * <p>デックの両端操作と、addFirst / addLast / removeFirst / removeLast の基本動作を自前実装で確認できる形にしている。
 */
public final class DequeBasics {

  private DequeBasics() {
    // インスタンス化しないユーティリティクラス
  }

  /** デックの状態をまとめるスナップショット */
  public record DequeSnapshot(
      List<Integer> elements, int size, Integer firstValue, Integer lastValue) {}

  /**
   * 双方向連結リストベースのデック
   *
   * <p>ここで見たいのは、先頭側・末尾側のどちらからでも追加と削除を行える点。
   *
   * <p>ノード同士は prev / next でつながっている。
   */
  public static final class LinkedDeque {
    private DequeNode head;
    private DequeNode tail;
    private int size;

    /**
     * 先頭側へ値を追加する
     *
     * <p>ここがデックの基本操作の1つ。
     *
     * <p>新しいノードを head の前へ差し込み、head を更新する。
     */
    public void addFirst(int value) {
      DequeNode newNode = new DequeNode(value);
      if (head == null) {
        head = newNode;
        tail = newNode;
        size = 1;
        return;
      }

      // ここがデック本体の接続更新部分。
      newNode.next = head;
      head.prev = newNode;
      head = newNode;
      size++;
    }

    /** 末尾側へ値を追加する */
    public void addLast(int value) {
      DequeNode newNode = new DequeNode(value);
      if (tail == null) {
        head = newNode;
        tail = newNode;
        size = 1;
        return;
      }

      // ここがデック本体の接続更新部分。
      tail.next = newNode;
      newNode.prev = tail;
      tail = newNode;
      size++;
    }

    /** 先頭側から値を取り出す */
    public int removeFirst() {
      validateNotEmpty();
      int value = head.value;

      // ここがデック本体の更新部分。
      // head を次ノードへ進め、旧 head を構造から外す。
      head = head.next;
      size--;
      if (head == null) {
        tail = null;
      } else {
        head.prev = null;
      }
      return value;
    }

    /** 末尾側から値を取り出す */
    public int removeLast() {
      validateNotEmpty();
      int value = tail.value;

      // ここがデック本体の更新部分。
      // tail を前ノードへ戻し、旧 tail を構造から外す。
      tail = tail.prev;
      size--;
      if (tail == null) {
        head = null;
      } else {
        tail.next = null;
      }
      return value;
    }

    /** 先頭値を見る */
    public Integer peekFirst() {
      return head == null ? null : head.value;
    }

    /** 末尾値を見る */
    public Integer peekLast() {
      return tail == null ? null : tail.value;
    }

    public int size() {
      return size;
    }

    /**
     * デックの内容を先頭から末尾へ List 化する
     *
     * <p>これは状態確認をしやすくするための補助処理。 デックのコア処理ではない。
     */
    public List<Integer> toList() {
      List<Integer> values = new ArrayList<>();
      DequeNode current = head;
      while (current != null) {
        values.add(current.value);
        current = current.next;
      }
      return values;
    }

    /** 学習用の状態確認をまとめて返す */
    public DequeSnapshot snapshot() {
      return new DequeSnapshot(toList(), size, peekFirst(), peekLast());
    }

    /** 空デック操作を防ぐ補助処理 */
    private void validateNotEmpty() {
      if (size == 0) {
        throw new IllegalStateException("空のデックに対して操作はできない");
      }
    }
  }

  /** 入力配列からデックを構築する */
  public static LinkedDeque buildDeque(int[] values) {
    LinkedDeque deque = new LinkedDeque();
    for (int value : values) {
      deque.addLast(value);
    }
    return deque;
  }

  /**
   * デック用ノード
   *
   * <p>ここは両端操作を実現するための内部表現。
   *
   * <p>学習の中心は add / remove 時の参照更新にある。
   */
  private static final class DequeNode {
    private final int value;
    private DequeNode prev;
    private DequeNode next;

    private DequeNode(int value) {
      this.value = value;
    }
  }
}

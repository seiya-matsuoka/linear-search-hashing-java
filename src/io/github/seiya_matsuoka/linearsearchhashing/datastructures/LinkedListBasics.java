package io.github.seiya_matsuoka.linearsearchhashing.datastructures;

import java.util.ArrayList;
import java.util.List;

/**
 * 連結リストの基礎を確認するためのクラス
 *
 * <p>このクラスは今回の学習テーマの中心となる部分。
 *
 * <p>Java 標準ライブラリの LinkedList をそのまま使わず、 ノード同士が参照でつながる仕組みを自前実装で確認できる形にしている。
 */
public final class LinkedListBasics {

  private LinkedListBasics() {
    // インスタンス化しないユーティリティクラス
  }

  /**
   * 単方向連結リストの状態をまとめるスナップショット
   *
   * <p>runner 側で状態を表示しやすくするための補助データ。
   */
  public record SinglyLinkedListSnapshot(
      List<Integer> elements, int size, Integer firstValue, Integer lastValue) {}

  /** 双方向連結リストの状態をまとめるスナップショット */
  public record DoublyLinkedListSnapshot(
      List<Integer> forwardElements,
      List<Integer> backwardElements,
      int size,
      Integer firstValue,
      Integer lastValue) {}

  /**
   * 単方向連結リスト
   *
   * <p>各ノードは value と next を持つ。 ここで見たいのは、ノードを参照でつなぎながら構造を保つ流れ。
   */
  public static final class SinglyLinkedList {
    private SinglyNode head;
    private SinglyNode tail;
    private int size;

    /**
     * 末尾に値を追加する
     *
     * <p>ここが単方向連結リストの基本操作の1つ。 tail を持っているため、末尾ノードまで毎回たどらずに追加できる。
     */
    public void append(int value) {
      SinglyNode newNode = new SinglyNode(value);
      if (head == null) {
        head = newNode;
        tail = newNode;
        size = 1;
        return;
      }

      // ここが参照のつなぎ替え本体。
      // 旧 tail の next を新しいノードへ向け、tail 自体も更新する。
      tail.next = newNode;
      tail = newNode;
      size++;
    }

    /**
     * 先頭に値を追加する
     *
     * <p>先頭追加では、新しいノードの next を現在の head に向けてから head を差し替える。
     */
    public void prepend(int value) {
      SinglyNode newNode = new SinglyNode(value);

      // ここが参照のつなぎ替え本体。
      newNode.next = head;
      head = newNode;
      if (tail == null) {
        tail = newNode;
      }
      size++;
    }

    /**
     * 指定位置に値を挿入する
     *
     * <p>ここが連結リストらしさを特に確認しやすい部分。
     *
     * <p>挿入位置の1つ前まで順にたどり、参照のつなぎ先を差し替えることで 新しいノードを中間へ差し込む。
     */
    public void insertAt(int index, int value) {
      validateInsertIndex(index);

      if (index == 0) {
        prepend(value);
        return;
      }
      if (index == size) {
        append(value);
        return;
      }

      // ここから下が中間挿入の本体。
      // previous.next が指していた先を newNode へ付け替え、
      // newNode.next には旧 next を保持する。
      SinglyNode previous = nodeAt(index - 1);
      SinglyNode newNode = new SinglyNode(value);
      newNode.next = previous.next;
      previous.next = newNode;
      size++;
    }

    /**
     * 指定位置の要素を削除する
     *
     * <p>削除も連結リストの重要な確認ポイント。
     *
     * <p>削除対象の1つ前のノードが持つ next を付け替え、対象ノードを飛ばす。
     */
    public int removeAt(int index) {
      validateAccessIndex(index);

      if (index == 0) {
        int removedValue = head.value;
        head = head.next;
        size--;
        if (size == 0) {
          tail = null;
        }
        return removedValue;
      }

      // ここから下が中間削除の本体。
      // previous.next を削除対象の次ノードへ付け替える。
      SinglyNode previous = nodeAt(index - 1);
      SinglyNode removedNode = previous.next;
      previous.next = removedNode.next;
      if (removedNode == tail) {
        tail = previous;
      }
      size--;
      return removedNode.value;
    }

    /**
     * 指定値を含むかを確認する
     *
     * <p>連結リストではランダムアクセスできないため、先頭から順にたどって確認する。
     */
    public boolean contains(int target) {
      SinglyNode current = head;
      while (current != null) {
        if (current.value == target) {
          return true;
        }
        current = current.next;
      }
      return false;
    }

    /** 指定値が最初に現れる位置を返す */
    public int indexOf(int target) {
      int index = 0;
      SinglyNode current = head;
      while (current != null) {
        if (current.value == target) {
          return index;
        }
        current = current.next;
        index++;
      }
      return -1;
    }

    /**
     * 連結リストの内容を先頭から順に List 化する
     *
     * <p>これは状態確認をしやすくするための補助処理。 連結リストのコア処理ではない。
     */
    public List<Integer> toList() {
      List<Integer> values = new ArrayList<>();
      SinglyNode current = head;
      while (current != null) {
        values.add(current.value);
        current = current.next;
      }
      return values;
    }

    public int size() {
      return size;
    }

    public Integer firstValue() {
      return head == null ? null : head.value;
    }

    public Integer lastValue() {
      return tail == null ? null : tail.value;
    }

    /** 学習用の状態確認をまとめて返す */
    public SinglyLinkedListSnapshot snapshot() {
      return new SinglyLinkedListSnapshot(toList(), size(), firstValue(), lastValue());
    }

    /**
     * 指定位置までノードを順にたどって取得する
     *
     * <p>ここが配列との違いを特に確認しやすい部分。
     *
     * <p>index が分かっていても、その位置へ直接ジャンプできず、先頭から順に進む必要がある。
     */
    private SinglyNode nodeAt(int index) {
      SinglyNode current = head;
      for (int i = 0; i < index; i++) {
        current = current.next;
      }
      return current;
    }

    /** 挿入位置の妥当性を確認する補助処理 */
    private void validateInsertIndex(int index) {
      if (index < 0 || index > size) {
        throw new IllegalArgumentException("挿入位置が範囲外: " + index);
      }
    }

    /** 参照位置の妥当性を確認する補助処理 */
    private void validateAccessIndex(int index) {
      if (index < 0 || index >= size) {
        throw new IllegalArgumentException("参照位置が範囲外: " + index);
      }
    }
  }

  /**
   * 双方向連結リスト
   *
   * <p>各ノードは next に加えて prev も持つ。 前後どちらにもたどれる点が単方向連結リストとの違い。
   */
  public static final class DoublyLinkedList {
    private DoublyNode head;
    private DoublyNode tail;
    private int size;

    /**
     * 末尾に値を追加する
     *
     * <p>旧 tail と新しいノードを prev / next で接続する。
     */
    public void append(int value) {
      DoublyNode newNode = new DoublyNode(value);
      if (head == null) {
        head = newNode;
        tail = newNode;
        size = 1;
        return;
      }

      // ここが双方向連結リストの接続本体。
      tail.next = newNode;
      newNode.prev = tail;
      tail = newNode;
      size++;
    }

    /**
     * 先頭に値を追加する
     *
     * <p>新旧の先頭ノードを prev / next で相互に接続する。
     */
    public void prepend(int value) {
      DoublyNode newNode = new DoublyNode(value);
      if (head == null) {
        head = newNode;
        tail = newNode;
        size = 1;
        return;
      }

      // ここが双方向連結リストの接続本体。
      newNode.next = head;
      head.prev = newNode;
      head = newNode;
      size++;
    }

    /**
     * 指定位置の要素を削除する
     *
     * <p>双方向連結リストでは前後ノードの両方を更新する。 中間削除では、削除対象の前後を直接つなぎ替える。
     */
    public int removeAt(int index) {
      validateAccessIndex(index);

      if (index == 0) {
        int removedValue = head.value;
        head = head.next;
        size--;
        if (head == null) {
          tail = null;
        } else {
          head.prev = null;
        }
        return removedValue;
      }

      if (index == size - 1) {
        int removedValue = tail.value;
        tail = tail.prev;
        tail.next = null;
        size--;
        return removedValue;
      }

      // ここが中間削除の本体。
      // current の前後ノードを直接つなぎ替える。
      DoublyNode current = nodeAt(index);
      current.prev.next = current.next;
      current.next.prev = current.prev;
      size--;
      return current.value;
    }

    /**
     * 先頭から順に List 化する
     *
     * <p>状態表示用の補助処理。
     */
    public List<Integer> toForwardList() {
      List<Integer> values = new ArrayList<>();
      DoublyNode current = head;
      while (current != null) {
        values.add(current.value);
        current = current.next;
      }
      return values;
    }

    /**
     * 末尾から逆順に List 化する
     *
     * <p>prev を使って後ろからたどれることを確認するための補助処理。
     */
    public List<Integer> toBackwardList() {
      List<Integer> values = new ArrayList<>();
      DoublyNode current = tail;
      while (current != null) {
        values.add(current.value);
        current = current.prev;
      }
      return values;
    }

    public int size() {
      return size;
    }

    public Integer firstValue() {
      return head == null ? null : head.value;
    }

    public Integer lastValue() {
      return tail == null ? null : tail.value;
    }

    /** 学習用の状態確認をまとめて返す */
    public DoublyLinkedListSnapshot snapshot() {
      return new DoublyLinkedListSnapshot(
          toForwardList(), toBackwardList(), size(), firstValue(), lastValue());
    }

    /**
     * 指定位置のノードを取得する
     *
     * <p>双方向連結リストでは prev を持つため、前後どちらからたどるかを工夫できる。 今回は学習の見通しを優先し、単純に先頭から順にたどる。
     */
    private DoublyNode nodeAt(int index) {
      DoublyNode current = head;
      for (int i = 0; i < index; i++) {
        current = current.next;
      }
      return current;
    }

    /** 参照位置の妥当性を確認する補助処理 */
    private void validateAccessIndex(int index) {
      if (index < 0 || index >= size) {
        throw new IllegalArgumentException("参照位置が範囲外: " + index);
      }
    }
  }

  /**
   * 入力配列から単方向連結リストを構築する
   *
   * <p>学習用の初期状態を作るための補助処理。
   */
  public static SinglyLinkedList buildSingly(int[] values) {
    SinglyLinkedList list = new SinglyLinkedList();
    for (int value : values) {
      list.append(value);
    }
    return list;
  }

  /**
   * 入力配列から双方向連結リストを構築する
   *
   * <p>学習用の初期状態を作るための補助処理。
   */
  public static DoublyLinkedList buildDoubly(int[] values) {
    DoublyLinkedList list = new DoublyLinkedList();
    for (int value : values) {
      list.append(value);
    }
    return list;
  }

  /**
   * 単方向連結リスト用ノード
   *
   * <p>value と next のみを持つ最小構成。 連結リストの仕組みそのものを表す中心的な型。
   */
  private static final class SinglyNode {
    private final int value;
    private SinglyNode next;

    private SinglyNode(int value) {
      this.value = value;
    }
  }

  /**
   * 双方向連結リスト用ノード
   *
   * <p>value に加えて next / prev を持つ。
   */
  private static final class DoublyNode {
    private final int value;
    private DoublyNode next;
    private DoublyNode prev;

    private DoublyNode(int value) {
      this.value = value;
    }
  }
}

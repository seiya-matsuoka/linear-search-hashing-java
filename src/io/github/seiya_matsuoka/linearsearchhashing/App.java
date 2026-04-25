package io.github.seiya_matsuoka.linearsearchhashing;

import io.github.seiya_matsuoka.linearsearchhashing.runner.BinarySearchRunner;
import io.github.seiya_matsuoka.linearsearchhashing.runner.DequeRunner;
import io.github.seiya_matsuoka.linearsearchhashing.runner.HashTableRunner;
import io.github.seiya_matsuoka.linearsearchhashing.runner.LinearSearchRunner;
import io.github.seiya_matsuoka.linearsearchhashing.runner.LinkedListRunner;
import io.github.seiya_matsuoka.linearsearchhashing.runner.QueueRunner;
import io.github.seiya_matsuoka.linearsearchhashing.runner.SetMapRunner;
import io.github.seiya_matsuoka.linearsearchhashing.runner.StackRunner;
import io.github.seiya_matsuoka.linearsearchhashing.runner.TopicRunner;

/**
 * リポジトリ全体の共通エントリーポイント。
 *
 * <p>このクラスでは、コマンドライン引数を読み取り、実行対象のトピックに対応する runner へ処理を振り分ける。
 *
 * <p>アルゴリズムやデータ構造そのものの処理本体は持たず、ルーティング役に徹する構成とする。
 */
public final class App {

  private App() {
    // インスタンス化しないユーティリティクラス
  }

  public static void main(String[] args) {
    // 実行時オプションを最初にまとめて読み取る。
    RunnerOptions options = RunnerOptions.parse(args);

    if (!options.getUnknownOptions().isEmpty()) {
      System.out.println(
          "未対応のオプションを検出したため無視して続行する: " + String.join(", ", options.getUnknownOptions()));
      System.out.println();
    }

    // topic が未指定の場合は、何を実行するか判断できないため使い方を表示する。
    if (options.getTopic() == null || options.getTopic().isBlank()) {
      printUsage();
      return;
    }

    // topic 名から、対応する runner を決定する。
    TopicRunner runner = createRunner(options.getTopic());
    if (runner == null) {
      System.out.println("未対応の topic を指定しているため実行できない: " + options.getTopic());
      System.out.println();
      printUsage();
      return;
    }

    runner.run(options);
  }

  /** topic 名に応じて実行対象の runner を生成する。 */
  private static TopicRunner createRunner(String topic) {
    return switch (topic) {
      case "linked-list" -> new LinkedListRunner();
      case "stack" -> new StackRunner();
      case "queue" -> new QueueRunner();
      case "deque" -> new DequeRunner();
      case "linear-search" -> new LinearSearchRunner();
      case "binary-search" -> new BinarySearchRunner();
      case "hash-table" -> new HashTableRunner();
      case "set-map" -> new SetMapRunner();
      default -> null;
    };
  }

  /** 利用可能な topic とオプションを表示する。 */
  private static void printUsage() {
    System.out.println("使用方法");
    System.out.println(
        "  java -cp out io.github.seiya_matsuoka.linearsearchhashing.App --topic <topic>"
            + " [options]");
    System.out.println();
    System.out.println("利用可能な topic");
    System.out.println("  linked-list    連結リストの基礎");
    System.out.println("  stack          スタックの基礎");
    System.out.println("  queue          キューの基礎");
    System.out.println("  deque          デックの基礎");
    System.out.println("  linear-search  線形探索の基礎");
    System.out.println("  binary-search  二分探索の基礎");
    System.out.println("  hash-table     ハッシュテーブルの基礎");
    System.out.println("  set-map        Set / Map を使った探索・判定・集計");
    System.out.println();
    System.out.println("利用可能なオプション");
    System.out.println("  --topic <name>    実行対象のトピックを指定する");
    System.out.println("  --input <value>   任意の入力値を指定する。例: 10,20,30,40");
    System.out.println("  --trace           処理途中の流れを表示する");
    System.out.println("  --target <value>  探索対象値などを指定する");
    System.out.println("  --size <number>   入力サイズを指定して自動生成する");
  }
}

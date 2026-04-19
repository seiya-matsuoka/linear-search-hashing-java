package io.github.seiya_matsuoka.linearsearchhashing;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 実行時オプションを保持するクラス。
 *
 * <p>引数解析自体は簡易実装とし、外部ライブラリは使用しない。
 */
public final class RunnerOptions {
  private final String topic;
  private final String input;
  private final boolean trace;
  private final String target;
  private final Integer size;
  private final List<String> unknownOptions;

  private RunnerOptions(
      String topic,
      String input,
      boolean trace,
      String target,
      Integer size,
      List<String> unknownOptions) {
    this.topic = topic;
    this.input = input;
    this.trace = trace;
    this.target = target;
    this.size = size;
    this.unknownOptions = List.copyOf(unknownOptions);
  }

  /** 実行対象の topic 名を返す。 */
  public String getTopic() {
    return topic;
  }

  /** 直接指定された入力値を返す。 */
  public String getInput() {
    return input;
  }

  /** trace 表示の有無を返す。 */
  public boolean isTrace() {
    return trace;
  }

  /** target 用に指定された値を返す。 */
  public String getTarget() {
    return target;
  }

  /** size 用に指定された値を返す。 */
  public Integer getSize() {
    return size;
  }

  /** オプションに指定されたが未対応な値を返す。 */
  public List<String> getUnknownOptions() {
    return Collections.unmodifiableList(unknownOptions);
  }

  /**
   * String 配列から必要なオプションを簡易解析する。
   *
   * <p>学習では、引数解析そのものが主題ではない。 そのため、for ループで順に読み取るシンプルな実装にしている。
   */
  public static RunnerOptions parse(String[] args) {
    String topic = null;
    String input = null;
    boolean trace = false;
    String target = null;
    Integer size = null;
    List<String> unknown = new ArrayList<>();

    for (int i = 0; i < args.length; i++) {
      String arg = args[i];

      switch (arg) {
        // 実行したい学習トピック名を読み取る。
        case "--topic" -> {
          if (i + 1 < args.length) {
            topic = args[++i];
          }
        }
        // 入力値を直接指定したい場合に使用する。
        case "--input" -> {
          if (i + 1 < args.length) {
            input = args[++i];
          }
        }
        // 処理途中の流れを表示したい場合に使用する。
        case "--trace" -> trace = true;
        // 探索対象や比較対象の値を指定したい場合に使用する。
        case "--target" -> {
          if (i + 1 < args.length) {
            target = args[++i];
          }
        }
        // 大きめの入力データを自動生成したい場合のサイズ指定とする。
        case "--size" -> {
          if (i + 1 < args.length) {
            try {
              size = Integer.parseInt(args[++i]);
            } catch (NumberFormatException e) {
              unknown.add("--size=" + args[i]);
            }
          }
        }
        // 未対応のオプションを集約する。
        default -> unknown.add(arg);
      }
    }

    return new RunnerOptions(topic, input, trace, target, size, unknown);
  }
}

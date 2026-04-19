package io.github.seiya_matsuoka.linearsearchhashing.runner;

import io.github.seiya_matsuoka.linearsearchhashing.RunnerOptions;

/**
 * 各トピックの runner が実装する共通インターフェース。
 *
 * <p>App からはこの型で扱うことで、topic ごとの実行処理を統一した形で呼び出せるようにする。
 */
public interface TopicRunner {
  /** topic に対応する学習用デモを実行する。 */
  void run(RunnerOptions options);
}

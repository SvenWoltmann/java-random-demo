package eu.happycoders.random.sequence;

import java.util.concurrent.ThreadLocalRandom;

public class SequenceRepetitionFinderRunner {
  public static void main(String[] args) {
    new SequenceRepetitionFinder(ThreadLocalRandom.current(), 1L << 48, 100).run();
  }
}

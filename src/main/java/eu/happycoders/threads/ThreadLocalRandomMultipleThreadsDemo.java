package eu.happycoders.threads;

import java.util.Locale;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadLocalRandom;

public class ThreadLocalRandomMultipleThreadsDemo {

  private static final int NUMBER_OF_NUMBERS = 100_000_000;

  public static void main(String[] args) {
    for (int i = 0; i < 10; i++) {
      System.out.printf("Round %d%n", i + 1);
      testSingleThreaded("single thread");
      testWithMultipleThreads(2);
      testWithMultipleThreads(4);
    }
  }

  private static void testWithMultipleThreads(int numberOfThreads) {
    CountDownLatch startLatch = new CountDownLatch(1);
    CountDownLatch stopLatch = new CountDownLatch(numberOfThreads);
    for (int i = 0; i < numberOfThreads; i++) {
      new Thread(
              () -> {
                try {
                  startLatch.await();
                  testSingleThreaded(numberOfThreads + " threads");
                  stopLatch.countDown();
                } catch (InterruptedException e) {
                  // let thread die
                }
              })
          .start();
    }
    startLatch.countDown();
    try {
      stopLatch.await();
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
    }
  }

  private static void testSingleThreaded(String description) {
    Random r = ThreadLocalRandom.current();
    long blackhole = 0;
    long time = System.currentTimeMillis();

    for (int i = 0; i < NUMBER_OF_NUMBERS; i++) {
      blackhole += r.nextInt();
    }

    time = System.currentTimeMillis() - time;
    System.out.printf(
        Locale.US,
        "time for %,d numbers / %s = %,d ms; blackhole = %d%n",
        NUMBER_OF_NUMBERS,
        description,
        time,
        blackhole);
  }
}

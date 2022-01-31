package eu.happycoders.random.sequence;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;

/**
 * Finds repetitions of number pairs in the random number sequence used by java.util.{@link
 * java.util.Random}.
 *
 * @author <a href="sven@happycoders.eu>Sven Woltmann</a>
 */
public class RandomIntegerPairRepetitionFinder {

  private static final int SEED_NOISE_BITS = 16;
  private static final int NUMBER_OF_POSSIBLE_SEEDS = 1 << SEED_NOISE_BITS;

  private static final long multiplier = 0x5DEECE66DL;
  private static final long addend = 0xBL;
  private static final long mask = (1L << 48) - 1;

  private final List<IntegerPair> integerPairsFoundTwice = new ArrayList<>();

  private long startTime;

  public static void main(String[] args) {
    new RandomIntegerPairRepetitionFinder().run();
  }

  private void run() {
    startTime = System.currentTimeMillis();
    int firstNumber = Integer.MAX_VALUE;
    do {
      firstNumber++;
      findPairRepetition(firstNumber);
      if ((firstNumber & 0x3ff) == 0x3ff) {
        printStats(firstNumber);
      }
    } while (firstNumber < Integer.MAX_VALUE);

    System.out.println("FINISHED.");
  }

  private void printStats(int firstNumber) {
    long elementsChecked = (long) firstNumber - Integer.MIN_VALUE + 1;

    double completionRatio = (double) elementsChecked / 0x100000000L;
    long elapsedTime = System.currentTimeMillis() - startTime;
    long totalTimeEstimated = (long) (elapsedTime / completionRatio);
    long remainingTimeEstimated = totalTimeEstimated - elapsedTime;

    System.out.printf(
        Locale.US,
        "elements checked: %,d; integer pairs found twice: %,d %s - completion: %.4f %% - "
            + "elapsed time: %,.1f s - total time est.: %,.1f s - remaining time est.: %,.1f s (= %,.1f h)%n",
        elementsChecked,
        integerPairsFoundTwice.size(),
        integerPairsFoundTwice,
        completionRatio * 100.0,
        elapsedTime / 1_000.0,
        totalTimeEstimated / 1_000.0,
        remainingTimeEstimated / 1_000.0,
        remainingTimeEstimated / 3_600_000.0);
  }

  // Total estimated time:
  // ---------------------
  // Creating a new HashSet with default capacity inside findPairRepetition(): 13,564,658.8 s
  // Creating a new HashSet with a capacity of 65,536 inside findPairRepetition(): 9,737,299.8 s
  // Creating a new HashSet with a capacity of 131,072 inside findPairRepetition(): 4,661,406.2 s
  // Creating a new HashSet with a capacity of 262,144 inside findPairRepetition(): 4,207,725.8 s
  // Using a shared HashSet field and clearing it inside findPairRepetition(): 5,754,616.2 s

  private void findPairRepetition(int firstNumber) {
    HashSet<Integer> nextInts = new HashSet<>(262144);

    long firstNumberSeedBase = createSeedBase(firstNumber);

    for (int noise = 0; noise < NUMBER_OF_POSSIBLE_SEEDS; noise++) {
      long seed = firstNumberSeedBase | noise;
      int nextInt = getNextInt(seed);
      if (nextInts.contains(nextInt)) {
        System.out.printf("Integer pair found twice: %,d => %,d%n", firstNumber, nextInt);
        integerPairsFoundTwice.add(new IntegerPair(firstNumber, nextInt));
      }
      nextInts.add(nextInt);
    }
  }

  private long createSeedBase(int firstNumber) {
    return Integer.toUnsignedLong(firstNumber) << SEED_NOISE_BITS;
  }

  private int getNextInt(long oldseed) {
    long nextseed = (oldseed * multiplier + addend) & mask;
    return (int) (nextseed >>> 16);
  }

  record IntegerPair(int first, int second) {
    @Override
    public String toString() {
      return String.format(Locale.US, "(%,d, %,d)", first, second);
    }
  }
}

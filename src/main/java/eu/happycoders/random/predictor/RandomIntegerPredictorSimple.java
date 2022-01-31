package eu.happycoders.random.predictor;

/**
 * Predicts pseudo-random numbers generated with java.util.{@link java.util.Random}.
 *
 * <p>Simplified implementation that checks only two numbers.
 *
 * @author <a href="sven@happycoders.eu>Sven Woltmann</a>
 */
public class RandomIntegerPredictorSimple {

  private static final int SEED_NOISE_BITS = 16;
  private static final int NUMBER_OF_POSSIBLE_SEEDS = 1 << SEED_NOISE_BITS;

  private static final long multiplier = 0x5DEECE66DL;
  private static final long addend = 0xBL;
  private static final long mask = (1L << 48) - 1;

  private final int[] givenNumbers;

  public RandomIntegerPredictorSimple(int... givenNumbers) {
    if (givenNumbers.length != 2) {
      throw new IllegalArgumentException("Please specify exactly two numbers.");
    }
    this.givenNumbers = givenNumbers;
  }

  public int[] predict(int numberOfPredictions) {
    long seed = getSeedMatchingForSequence();

    // Skip the given numbers
    for (int i = 0; i < givenNumbers.length; i++) {
      seed = getNextSeed(seed);
    }

    // Get the predictions
    int[] predictions = new int[numberOfPredictions];
    for (int i = 0; i < numberOfPredictions; i++) {
      predictions[i] = (int) (seed >>> 16);
      seed = getNextSeed(seed);
    }

    return predictions;
  }

  private long getSeedMatchingForSequence() {
    long firstNumberSeedBase = Integer.toUnsignedLong(givenNumbers[0]) << SEED_NOISE_BITS;

    for (int noise = 0; noise < NUMBER_OF_POSSIBLE_SEEDS; noise++) {
      long seed = firstNumberSeedBase | noise;
      long nextSeed = getNextSeed(seed);
      int nextInt = (int) (nextSeed >>> 16);
      if (nextInt == givenNumbers[1]) {
        return seed;
      }
    }

    throw new IllegalArgumentException(
        "Found no matching seed; please verify your input sequence.");
  }

  private long getNextSeed(long seed) {
    return (seed * multiplier + addend) & mask;
  }
}

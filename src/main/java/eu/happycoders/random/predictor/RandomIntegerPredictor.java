package eu.happycoders.random.predictor;

/**
 * Predicts pseudo-random numbers generated with java.util.{@link java.util.Random}.
 *
 * @author <a href="sven@happycoders.eu>Sven Woltmann</a>
 */
public class RandomIntegerPredictor {

  private static final int SEED_NOISE_BITS = 16;
  private static final int NUMBER_OF_POSSIBLE_SEEDS = 1 << SEED_NOISE_BITS;

  private static final long multiplier = 0x5DEECE66DL;
  private static final long addend = 0xBL;
  private static final long mask = (1L << 48) - 1;

  private final int[] givenNumbers;

  public RandomIntegerPredictor(int... givenNumbers) {
    if (givenNumbers.length < 2) {
      throw new IllegalArgumentException("Please specify at least two numbers.");
    }
    this.givenNumbers = givenNumbers;
  }

  public int[] predict(int numberOfPredictions) {
    long seed = getSeedMatchingForSequence();

    // Skip the given numbers
    for (int i = 0; i < givenNumbers.length; i++) {
      seed = nextSeed(seed);
    }

    // Get the predictions
    int[] predictions = new int[numberOfPredictions];
    for (int i = 0; i < numberOfPredictions; i++) {
      predictions[i] = intFromSeed(seed);
      seed = nextSeed(seed);
    }

    return predictions;
  }

  private long getSeedMatchingForSequence() {
    long firstNumberSeedBase = calculateSeedBase(givenNumbers[0]);

    Long matchingSeed = null;
    for (int noise = 0; noise < NUMBER_OF_POSSIBLE_SEEDS; noise++) {
      long seed = firstNumberSeedBase | noise;

      if (sequenceMatchesForSeed(seed)) {
        if (matchingSeed != null) {
          throw new IllegalArgumentException(
              "Found two matching seeds; please add one more number to the input.");
        }
        matchingSeed = seed;
      }
    }

    if (matchingSeed == null) {
      throw new IllegalArgumentException(
          "Found no matching seed; please verify your input sequence.");
    }

    return matchingSeed;
  }

  private long calculateSeedBase(int number) {
    return Integer.toUnsignedLong(number) << SEED_NOISE_BITS;
  }

  private boolean sequenceMatchesForSeed(long seed) {
    for (int i = 1; i < givenNumbers.length; i++) {
      seed = nextSeed(seed);
      int nextInt = intFromSeed(seed);
      if (nextInt != givenNumbers[i]) {
        return false;
      }
    }
    return true;
  }

  private long nextSeed(long seed) {
    return (seed * multiplier + addend) & mask;
  }

  private int intFromSeed(long seed) {
    return (int) (seed >>> 16);
  }
}

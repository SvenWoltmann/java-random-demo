package eu.happycoders.random.sequence;

import java.util.Arrays;
import java.util.Locale;
import java.util.random.RandomGenerator;

public class SequenceRepetitionFinder {
  public enum Result {
    FOUND_SAME_SEQUENCE_AFTER_ITERATING_OVER_FULL_RANDOM_NUMBER_SEQUENCE,
    FOUND_SAME_SEQUENCE_EARLY
  }

  private final RandomGenerator random;
  private final long randomSequenceLength;
  private final int[] storedSequence;

  private long startTime;

  public SequenceRepetitionFinder(
      RandomGenerator random, long randomSequenceLength, int storedSequenceLength) {
    if (storedSequenceLength > randomSequenceLength)
      throw new IllegalArgumentException(
          "storedSequenceLength must not be longer than randomSequenceLength");

    this.random = random;
    this.randomSequenceLength = randomSequenceLength;
    this.storedSequence = new int[storedSequenceLength];
  }

  public Result run() {
    startTime = System.currentTimeMillis();

    storeInitialSequence();

    int firstNumberInStoredSequence = storedSequence[0];
    long sequencePosition = storedSequence.length;
    while (true) {
      int number = random.nextInt();
      if (number != firstNumberInStoredSequence) {
        sequencePosition++;
        continue;
      }

      int matchLength = countMatchingNumbers();
      printMatchingSequence(matchLength, sequencePosition);
      if (matchLength == storedSequence.length) {
        return getResultForMatchAt(sequencePosition);
      }
      sequencePosition += matchLength;

      // In countMatchingNumbers, one more random number was generated, which we ignore.
      // Handling that number would be complicated and the probability is extremely low that it
      // would start a new sequence
      sequencePosition++;
    }
  }

  private void storeInitialSequence() {
    for (int i = 0; i < storedSequence.length; i++) {
      storedSequence[i] = random.nextInt();
    }
  }

  /**
   * Compares the next ints from the random number generator with the stored sequence, starting at
   * the second number (the first was already compared)
   *
   * @return the number of matching numbers; at least 1; at most <code>storedSequence.length</code>
   */
  private int countMatchingNumbers() {
    for (int i = 1; i < storedSequence.length; i++) {
      int number = random.nextInt();
      if (storedSequence[i] != number) {
        return i;
      }
    }

    // No mismatch found -> all match
    return storedSequence.length;
  }

  private void printMatchingSequence(int matchLength, long sequencePosition) {
    double completionRatio = (double) sequencePosition / randomSequenceLength;
    long elapsedTime = System.currentTimeMillis() - startTime;
    long totalTimeEstimated = (long) (elapsedTime / completionRatio);
    long remainingTimeEstimated = totalTimeEstimated - elapsedTime;

    System.out.printf(
        Locale.US,
        "Found sequence of %d matching number(s) at position %,d of %,d: "
            + "%s - completion: %.4f %% - elapsed time: %,.1f s - total time est.: %,.1f s - remaining time est.: %,.1f s (= %,.1f h)%n",
        matchLength,
        sequencePosition,
        randomSequenceLength,
        Arrays.toString(Arrays.copyOf(storedSequence, matchLength)),
        completionRatio * 100.0,
        elapsedTime / 1_000.0,
        totalTimeEstimated / 1_000.0,
        remainingTimeEstimated / 1_000.0,
        remainingTimeEstimated / 3_600_000.0);
  }

  private Result getResultForMatchAt(long sequencePosition) {
    if (sequencePosition % randomSequenceLength == 0) {
      System.out.println(
          "Found same sequence after iterating over the full random number sequence.");
      return Result.FOUND_SAME_SEQUENCE_AFTER_ITERATING_OVER_FULL_RANDOM_NUMBER_SEQUENCE;
    } else {
      System.out.println(
          "Found same sequence *before* iterating over the full random number sequence; "
              + "please increase size of stored sequence.");
      return Result.FOUND_SAME_SEQUENCE_EARLY;
    }
  }
}

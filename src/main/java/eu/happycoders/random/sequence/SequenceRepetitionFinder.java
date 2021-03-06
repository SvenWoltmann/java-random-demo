package eu.happycoders.random.sequence;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import java.util.random.RandomGenerator;

public class SequenceRepetitionFinder {
  public enum Result {
    FOUND_SAME_SEQUENCE_AFTER_ITERATING_OVER_FULL_RANDOM_NUMBER_SEQUENCE,
    FOUND_SAME_SEQUENCE_EARLY
  }

  private final RandomGenerator random;
  private final long randomSequenceLength;
  private final int[] storedSequence;

  private final List<Long> foundSequenceStarts = new ArrayList<>();
  private final Map<Integer, AtomicLong> matchLengthCounters = new HashMap<>();

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
    long sequencePosition = storedSequence.length - 1;

    int number;
    while (true) {
      // First loop ... without comparing sequences to avoid extra check (costs ~70 hours in total)
      while (true) {
        sequencePosition++;
        number = random.nextInt();

        if (number == firstNumberInStoredSequence) {
          startComparingNewSequence(sequencePosition);
          break;
        }
      }

      Result result = compareWithAllSequencesCurrentlyBeingCompared(number, sequencePosition);
      if (result != null) return result;

      // Second loop ... with comparing sequences
      do {
        sequencePosition++;
        number = random.nextInt();

        if (number == firstNumberInStoredSequence) {
          startComparingNewSequence(sequencePosition);
        }

        result = compareWithAllSequencesCurrentlyBeingCompared(number, sequencePosition);
        if (result != null) return result;

        // No more sequences being checked? Go back to fast loop!
      } while (!foundSequenceStarts.isEmpty());
    }
  }

  private void storeInitialSequence() {
    for (int i = 0; i < storedSequence.length; i++) {
      storedSequence[i] = random.nextInt();
    }
  }

  private void startComparingNewSequence(long sequencePosition) {
    foundSequenceStarts.add(sequencePosition);
  }

  private Result compareWithAllSequencesCurrentlyBeingCompared(int number, long sequencePosition) {
    for (Iterator<Long> iterator = foundSequenceStarts.iterator(); iterator.hasNext(); ) {
      long foundSequenceStart = iterator.next();
      int foundSequencePos = (int) (sequencePosition - foundSequenceStart);

      // Ignore sequence just started
      if (foundSequencePos == 0) {
        continue;
      }

      boolean match = number == storedSequence[foundSequencePos];
      if (!match || foundSequencePos == storedSequence.length - 1) {
        long sequenceStartPosition = sequencePosition - foundSequencePos;
        int matchLength = match ? foundSequencePos + 1 : foundSequencePos;
        printMatchingSequence(matchLength, sequenceStartPosition);
        matchLengthCounters.computeIfAbsent(matchLength, key -> new AtomicLong()).incrementAndGet();
        iterator.remove();

        if (match) {
          return getResultForMatchAt(sequenceStartPosition);
        }
      }
    }
    return null;
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

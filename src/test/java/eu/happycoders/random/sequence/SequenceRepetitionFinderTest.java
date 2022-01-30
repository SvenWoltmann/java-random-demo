package eu.happycoders.random.sequence;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import eu.happycoders.random.sequence.SequenceRepetitionFinder.Result;
import java.util.random.RandomGenerator;
import org.junit.jupiter.api.Test;

class SequenceRepetitionFinderTest {

  @Test
  void storeSequenceMustNotBeLongerThanRandomSequence() {
    RandomGenerator random = mock(RandomGenerator.class);
    assertThatExceptionOfType(IllegalArgumentException.class)
        .isThrownBy(() -> new SequenceRepetitionFinder(random, 10, 11));
  }

  @Test
  void sameSequenceIsDetectedWhenStoredSequenceHasSameLengthAsRandomSequence() {
    RandomGenerator random = mock(RandomGenerator.class);
    when(random.nextInt())
        .thenReturn(
            1, 2, 3, 4, 5, 6, 7, 8, 9, 10, //
            1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
        .thenThrow(new IllegalStateException("Sequence traversed twice"));

    SequenceRepetitionFinder sequenceRepetitionFinder =
        new SequenceRepetitionFinder(random, 10, 10);
    Result result = sequenceRepetitionFinder.run();

    assertThat(result)
        .isEqualTo(Result.FOUND_SAME_SEQUENCE_AFTER_ITERATING_OVER_FULL_RANDOM_NUMBER_SEQUENCE);
  }

  @Test
  void sameSequenceIsDetectedWhenStoredSequenceIsShorterThanRandomSequence() {
    RandomGenerator random = mock(RandomGenerator.class);
    when(random.nextInt())
        .thenReturn(
            1, 2, 3, 4, 5, 6, 7, 8, 9, 10, //
            1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
        .thenThrow(new IllegalStateException("Sequence traversed twice"));

    SequenceRepetitionFinder sequenceRepetitionFinder = new SequenceRepetitionFinder(random, 10, 5);
    Result result = sequenceRepetitionFinder.run();

    assertThat(result)
        .isEqualTo(Result.FOUND_SAME_SEQUENCE_AFTER_ITERATING_OVER_FULL_RANDOM_NUMBER_SEQUENCE);
  }

  @Test
  void sameSequenceIsDetectedCorrectlyWhenPartsOfTheSequenceAreRepeated() {
    RandomGenerator random = mock(RandomGenerator.class);
    when(random.nextInt())
        .thenReturn(
            1, 2, 3, 4, 5, 6, 1, 2, 3, 7, 8, 9, //
            1, 2, 3, 4, 5, 6, 1, 2, 3, 7, 8, 9)
        .thenThrow(new IllegalStateException("Sequence traversed twice"));

    SequenceRepetitionFinder sequenceRepetitionFinder = new SequenceRepetitionFinder(random, 12, 4);
    Result result = sequenceRepetitionFinder.run();

    assertThat(result)
        .isEqualTo(Result.FOUND_SAME_SEQUENCE_AFTER_ITERATING_OVER_FULL_RANDOM_NUMBER_SEQUENCE);
  }

  @Test
  void earlySameSequenceIsDetected() {
    RandomGenerator random = mock(RandomGenerator.class);
    when(random.nextInt())
        .thenReturn(
            1, 2, 3, 4, 5, 6, 1, 2, 3, 7, 8, 9, //
            1, 2, 3, 4, 5, 6, 1, 2, 3, 7, 8, 9)
        .thenThrow(new IllegalStateException("Sequence traversed twice"));

    SequenceRepetitionFinder sequenceRepetitionFinder = new SequenceRepetitionFinder(random, 12, 3);
    Result result = sequenceRepetitionFinder.run();

    assertThat(result).isEqualTo(Result.FOUND_SAME_SEQUENCE_EARLY);
  }

  @Test
  void sequenceIsDetectedAfterAllButOneMatch() {
    RandomGenerator random = mock(RandomGenerator.class);
    when(random.nextInt())
        .thenReturn(
            1, 2, 3, 4, 5, 6, 1, 2, 3, //
            1, 2, 3, 4, 5, 6, 1, 2, 3)
        .thenThrow(new IllegalStateException("Sequence traversed twice"));

    SequenceRepetitionFinder sequenceRepetitionFinder = new SequenceRepetitionFinder(random, 9, 4);
    Result result = sequenceRepetitionFinder.run();

    assertThat(result)
        .isEqualTo(Result.FOUND_SAME_SEQUENCE_AFTER_ITERATING_OVER_FULL_RANDOM_NUMBER_SEQUENCE);
  }

  @Test
  void sequenceIsDetectedAfterHalfMatch() {
    RandomGenerator random = mock(RandomGenerator.class);
    when(random.nextInt())
        .thenReturn(
            1, 1, 2, 2, 5, 6, 7, 8, 1, 1, //
            1, 1, 2, 2, 5, 6, 7, 8, 1, 1)
        .thenThrow(new IllegalStateException("Sequence traversed twice"));

    SequenceRepetitionFinder sequenceRepetitionFinder = new SequenceRepetitionFinder(random, 10, 4);
    Result result = sequenceRepetitionFinder.run();

    assertThat(result)
        .isEqualTo(Result.FOUND_SAME_SEQUENCE_AFTER_ITERATING_OVER_FULL_RANDOM_NUMBER_SEQUENCE);
  }
}

package eu.happycoders.random.predictor;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import org.junit.jupiter.api.Test;

class RandomIntegerPredictorSimpleTest {

  @Test
  void predictsSequence1() {
    RandomIntegerPredictorSimple predictor =
        new RandomIntegerPredictorSimple(1_568_757_050, 1_047_012_071);
    int[] predict = predictor.predict(10);
    assertThat(predict)
        .containsExactly(
            -491_646_049,
            670_726_983,
            -476_980_395,
            -1_839_411_938,
            -46_646_292,
            1_930_750_600,
            338_924_384,
            1_625_903_649,
            132_043_008,
            -242_760_411);
  }

  @Test
  void predictsSequence2() {
    RandomIntegerPredictorSimple predictor =
        new RandomIntegerPredictorSimple(1_461_184_574, -2_007_849_466);
    int[] predict = predictor.predict(5);
    assertThat(predict)
        .containsExactly(-737_247_132, 753_421_446, 811_546_579, 1_098_632_800, -498_492_950);
  }

  @Test
  void throwsWhenNonExistingSequenceIsSpecified() {
    RandomIntegerPredictorSimple predictor = new RandomIntegerPredictorSimple(1, 2);
    assertThatExceptionOfType(IllegalArgumentException.class)
        .isThrownBy(() -> predictor.predict(10));
  }

  @Test
  void throwsWhenGivenSequenceIsTooShort() {
    assertThatExceptionOfType(IllegalArgumentException.class)
        .isThrownBy(() -> new RandomIntegerPredictorSimple(1));
  }

  @Test
  void throwsWhenGivenSequenceIsTooLong() {
    assertThatExceptionOfType(IllegalArgumentException.class)
        .isThrownBy(() -> new RandomIntegerPredictorSimple(1, 2, 3));
  }
}

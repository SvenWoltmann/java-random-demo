package eu.happycoders.random.predictor;

import java.util.Locale;
import java.util.Random;

/**
 * Little demo for {@link RandomIntegerPredictor}. Creates two random numbers, then prints 10
 * predictions, then prints 10 more random numbers.
 *
 * @author <a href="sven@happycoders.eu>Sven Woltmann</a>
 */
public class RandomIntegerPredictorDemo {
  private static final int NUMBER_OF_PREDICTIONS = 10;

  public static void main(String[] args) {
    Random random = new Random();
    int[] givenNumbers = createTwoRandomNumbers(random);
    predict(givenNumbers);
    printNextActualRandomNumbers(random);
  }

  private static int[] createTwoRandomNumbers(Random random) {
    int first = random.nextInt();
    int second = random.nextInt();

    System.out.println("Two random numbers:");
    printRandomNumber(first);
    printRandomNumber(second);
    System.out.println();

    return new int[] {first, second};
  }

  private static void predict(int[] givenNumbers) {
    System.out.printf("Predicting %d random numbers:%n", NUMBER_OF_PREDICTIONS);
    int[] predicted = new RandomIntegerPredictor(givenNumbers).predict(NUMBER_OF_PREDICTIONS);
    for (int i : predicted) {
      printRandomNumber(i);
    }
    System.out.println();
  }

  private static void printNextActualRandomNumbers(Random random) {
    System.out.println("Next *actual* random numbers:");
    for (int i = 0; i < NUMBER_OF_PREDICTIONS; i++) {
      printRandomNumber(random.nextInt());
    }
  }

  private static void printRandomNumber(int first) {
    System.out.printf(Locale.US, "%,d%n", first);
  }
}

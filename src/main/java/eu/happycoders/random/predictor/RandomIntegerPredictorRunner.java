package eu.happycoders.random.predictor;

import java.util.Arrays;
import java.util.Locale;

/**
 * Command-line runner for {@link RandomIntegerPredictor}.
 *
 * @author <a href="sven@happycoders.eu>Sven Woltmann</a>
 */
public class RandomIntegerPredictorRunner {

  public static final String HELP =
      "Call with at least 3 parameters: [number of predictions, first number, second number, ...]";

  public static void main(String[] args) {
    PredictorInput input = parseInput(args);
    if (input == null) {
      return;
    }
    System.out.printf("Given numbers: %s%n", Arrays.toString(input.givenNumbers));

    System.out.printf("Predicting %d random numbers...%n", input.numberOfPredictions);
    int[] predictedNumbers =
        new RandomIntegerPredictor(input.givenNumbers).predict(input.numberOfPredictions);

    for (int predictedNumber : predictedNumbers) {
      System.out.printf(Locale.US, "%,d%n", predictedNumber);
    }
  }

  private static PredictorInput parseInput(String[] args) {
    if (args.length < 3) {
      System.err.println(HELP);
      return null;
    }

    try {
      int numberOfPredictions = Integer.parseInt(args[0]);
      int numberOfGivenNumbers = args.length - 1;
      int[] givenNumbers = new int[numberOfGivenNumbers];
      for (int i = 0; i < numberOfGivenNumbers; i++) {
        givenNumbers[i] = Integer.parseInt(args[i + 1]);
      }

      return new PredictorInput(numberOfPredictions, givenNumbers);
    } catch (NumberFormatException ex) {
      System.err.println(HELP);
      return null;
    }
  }

  private static void printResult(int[] predictedNumbers) {
    System.out.println("Prediction of the next random numbers:");
    for (int predictedNumber : predictedNumbers) {
      System.out.printf(Locale.US, "%,d%n", predictedNumber);
    }
  }

  private static record PredictorInput(int numberOfPredictions, int[] givenNumbers) {}
}

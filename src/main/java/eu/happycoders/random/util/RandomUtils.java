package eu.happycoders.random.util;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class RandomUtils {
  public static int nextInt(Random random, int origin, int bound) {
    if (origin >= bound) {
      throw new IllegalArgumentException();
    }
    return origin + random.nextInt(bound - origin);
  }

  public static String randomLowerCaseString(int length) {
    StringBuilder sb = new StringBuilder();
    Random r = ThreadLocalRandom.current();
    for (int i = 0; i < length; i++) {
      char c = (char) ('a' + r.nextInt(26));
      sb.append(c);
    }
    return sb.toString();
  }

  private static final String ALPHANUMERIC_WITH_SPACE_ALPHABET =
      " 0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

  public static String randomAlphanumericalWithSpaceString(int length) {
    return randomString(length, ALPHANUMERIC_WITH_SPACE_ALPHABET);
  }

  public static String randomString(int length, String alphabet) {
    StringBuilder sb = new StringBuilder();
    Random r = ThreadLocalRandom.current();
    for (int i = 0; i < length; i++) {
      int pos = r.nextInt(alphabet.length());
      char c = alphabet.charAt(pos);
      sb.append(c);
    }
    return sb.toString();
  }

  public static String randomStringWithStream(int length, String alphabet) {
    return ThreadLocalRandom.current()
        .ints(length, 0, alphabet.length())
        .map(alphabet::charAt)
        .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
        .toString();
  }
}

package eu.happycoders.random.seed;

import java.util.Random;

public class SetSeedExample {
  public static void main(String[] args) {
    Random random = new Random(42);
    for (int i = 0; i < 5; i++) {
      System.out.println(random.nextInt(100));
    }

    random.setSeed(42);
    for (int i = 0; i < 5; i++) {
      System.out.println(random.nextInt(100));
    }
  }
}

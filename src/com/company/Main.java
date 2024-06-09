package com.company;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        Map<Integer, String> primeNumbers = new TreeMap<>();

        PrimeNumberWriter writer1 = new PrimeNumberWriter(primeNumbers, "Thread1.txt");
        PrimeNumberWriter writer2 = new PrimeNumberWriter(primeNumbers, "Thread2.txt");

        try {
            Files.deleteIfExists(Path.of("Result.txt"));
            Files.deleteIfExists(Path.of("Thread1.txt"));
            Files.deleteIfExists(Path.of("Thread2.txt"));
            } catch (IOException e) {
            e.printStackTrace();
        }

        Thread thread1 = new Thread(writer1);
        Thread thread2 = new Thread(writer2);

        thread1.start();
        thread2.start();

        try {
            thread1.join();
            thread2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Prime numbers: " + primeNumbers);
    }
}

class PrimeNumberWriter implements Runnable {
    private Map<Integer, String> primeNumbers;
    private String filename;

    public PrimeNumberWriter(Map<Integer, String> primeNumbers, String filename) {
        this.primeNumbers = primeNumbers;
        this.filename = filename;
    }

    @Override
    public void run() {
        int limit = 1000000;

        for (int i = 2; i <= limit; i++) {
            if (isPrime(i)) {
                synchronized (primeNumbers) {
                    if (!primeNumbers.containsKey(i)) {
                        primeNumbers.put(i, Thread.currentThread().getName());
                        try (FileWriter commonWriter = new FileWriter("Result.txt", true);
                             FileWriter selfWriter = new FileWriter(filename, true)
                        ) {
                            commonWriter.write(i + " ");
                            selfWriter.write(i + " ");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }

    private boolean isPrime(int number) {
        if (number <= 1) {
            return false;
        }
        for (int i = 2; i <= Math.sqrt(number); i++) {
            if (number % i == 0) {
                return false;
            }
        }
        return true;
    }
}
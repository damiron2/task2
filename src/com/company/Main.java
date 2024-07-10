package com.company;

import java.io.FileWriter;
import java.io.IOException;


//public class Main {
//    public static void main(String[] args) {
//        Map<Integer, String> primeNumbers = new TreeMap<>();
//
//        PrimeNumberWriter writer1 = new PrimeNumberWriter(primeNumbers, "Thread1.txt");
//        PrimeNumberWriter writer2 = new PrimeNumberWriter(primeNumbers, "Thread2.txt");
//
//        try {
//            Files.deleteIfExists(Path.of("Result.txt"));
//            Files.deleteIfExists(Path.of("Thread1.txt"));
//            Files.deleteIfExists(Path.of("Thread2.txt"));
//            } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        Thread thread1 = new Thread(writer1);
//        Thread thread2 = new Thread(writer2);
//
//        thread1.start();
//        thread2.start();
//
//        try {
//            thread1.join();
//            thread2.join();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//
//        System.out.println("Prime numbers: " + primeNumbers);
//    }
//}
//
//class PrimeNumberWriter implements Runnable {
//    private Map<Integer, String> primeNumbers;
//    private String filename;
//
//    public PrimeNumberWriter(Map<Integer, String> primeNumbers, String filename) {
//        this.primeNumbers = primeNumbers;
//        this.filename = filename;
//    }
//
//    @Override
//    public void run() {
//        int limit = 1000000;
//
//        for (int i = 2; i <= limit; i++) {
//            if (isPrime(i)) {
//                synchronized (primeNumbers) {
//                    if (!primeNumbers.containsKey(i)) {
//                        primeNumbers.put(i, Thread.currentThread().getName());
//                        try (FileWriter commonWriter = new FileWriter("Result.txt", true);
//                             FileWriter selfWriter = new FileWriter(filename, true)
//                        ) {
//                            commonWriter.write(i + " ");
//                            selfWriter.write(i + " ");
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }
//            }
//        }
//    }
//
//    private boolean isPrime(int number) {
//        if (number <= 1) {
//            return false;
//        }
//        for (int i = 2; i <= Math.sqrt(number); i++) {
//            if (number % i == 0) {
//                return false;
//            }
//        }
//        return true;
//    }
//}

public class Main {
    private static int n = 0;
    private static final Object lock = new Object();

    public static void main(String[] args) throws InterruptedException {

        Thread thread1 = new Thread(() -> primeFinder());

        Thread thread2 = new Thread(() -> primeFinder());


        thread1.start();
        thread2.start();

        thread1.join();
        thread2.join();

    }

    public static boolean isPrime(int number) {
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

    public static void primeFinder() {
        for (int i = 0; i < 100000; i++) {
            synchronized (lock) {
                if (i > n && isPrime(i)) {
                    n = i;
                    System.out.println(Thread.currentThread().getName() + " " + i);
                    writeToFile(i, Thread.currentThread().getName());
                }
            }
        }
    }

    public static void writeToFile(int n, String threadName) {
        String commonPath ="Result.txt";
        String threadPath = threadName.equals("Thread-0") ? "Thread0.txt" : "Thread1.txt";

        try (FileWriter writer1 = new FileWriter(commonPath,true);
             FileWriter writer2 = new FileWriter(threadPath,true);
        ){
            writer1.write(n+" ");
            writer2.write(n+" ");
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
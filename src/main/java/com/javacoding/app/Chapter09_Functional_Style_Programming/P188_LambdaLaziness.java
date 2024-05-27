package com.javacoding.app.Chapter09_Functional_Style_Programming;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class P188_LambdaLaziness {

    public static void main(String[] args) {

        Supplier<Integer> supplier = () -> Counter.count();
        Consumer<Integer> consumer = c -> {
            c = c + Counter.count();
            System.out.println("Consumer: " + c);
        }; // 2

        System.out.println("Counter: " + Counter.c); // 0
        System.out.println("Supplier: " + supplier.get()); // 0
                                                           // count()가 호출되었음에도 0을 반환. return c++ 에서는 현재값을 return 후
                                                           // 사후 증분시킴.
        System.out.println("Counter: " + Counter.c); // 1

        consumer.accept(Counter.c);
        // consumer의 식에서 c = c + Counter.count()가 호출되었지만 count()가 호출되었음에도 1을 반환(사후 증분).
        // c = 1 + 1 = 2

        System.out.println("Counter: " + Counter.c); // 2
        System.out.println("Supplier: " + supplier.get()); // 2
        System.out.println("Counter: " + Counter.c); // 3
    }

    static class Counter {

        static int c;

        public static int count() {
            System.out.println("Incrementing c from " + c + " to " + (c + 1));
            return c++;
        }
    }
}

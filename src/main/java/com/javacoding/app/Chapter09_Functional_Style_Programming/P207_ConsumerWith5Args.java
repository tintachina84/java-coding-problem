package com.javacoding.app.Chapter09_Functional_Style_Programming;

public class P207_ConsumerWith5Args {

    public static void main(String[] args) {
        FiveConsumer<Double, Double, Double, Double, Double> pl4c = (a, b, c, d, x) -> Logistics.pl4(a, b, c, d, x);

        pl4c.accept(4.19, -1.10, 12.65, 0.03, 40.3);
    }

    public final class Logistics {

        private Logistics() {
            throw new AssertionError("Cannot be instantiated");
        }

        public static void pl4(Double a, Double b, Double c, Double d, Double x) {

            System.out.println(d + ((a - d) / (1 + (Math.pow(x / c, b)))));
        }
    }

    @FunctionalInterface
    public interface FiveConsumer<T1, T2, T3, T4, T5> {

        void accept(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5);
    }

}

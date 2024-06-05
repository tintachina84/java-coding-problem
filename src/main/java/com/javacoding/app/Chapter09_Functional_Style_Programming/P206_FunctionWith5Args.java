package com.javacoding.app.Chapter09_Functional_Style_Programming;

import java.util.ArrayList;
import java.util.List;

public class P206_FunctionWith5Args {

    public static void main(String[] args) {
        FiveFunction<Double, Double, Double, Double, Double, Double> pl4 = (a, b, c, d, x) -> d
                + ((a - d) / (1 + (Math.pow(x / c, b))));

        List<Double> allX = List.of(40.3, 100.0, 250.2, 400.1, 600.6, 800.4, 1150.4, 1400.6);
        List<Double> allY = Logistics.compute(4.19, -1.10, 12.65, 0.03, allX, pl4);

        System.out.println(allY);

        PL4 pl4_1 = Logistics.create(4.19, -1.10, 12.65, 0.03, 40.3, PL4::new);
        PL4 pl4_2 = Logistics.create(4.19, -1.10, 12.65, 0.03, 100.0, PL4::new);
        PL4 pl4_3 = Logistics.create(4.19, -1.10, 12.65, 0.03, 250.2, PL4::new);
        PL4 pl4_4 = Logistics.create(4.19, -1.10, 12.65, 0.03, 400.1, PL4::new);
        PL4 pl4_5 = Logistics.create(4.19, -1.10, 12.65, 0.03, 600.6, PL4::new);
        PL4 pl4_6 = Logistics.create(4.19, -1.10, 12.65, 0.03, 800.4, PL4::new);
        PL4 pl4_7 = Logistics.create(4.19, -1.10, 12.65, 0.03, 1150.4, PL4::new);
        PL4 pl4_8 = Logistics.create(4.19, -1.10, 12.65, 0.03, 1400.6, PL4::new);

        System.out.println();
        System.out.println(pl4_1.compute());
        System.out.println(pl4_2.compute());
        System.out.println(pl4_3.compute());
        System.out.println(pl4_4.compute());
        System.out.println(pl4_5.compute());
        System.out.println(pl4_6.compute());
        System.out.println(pl4_7.compute());
        System.out.println(pl4_8.compute());
    }

    public final class Logistics {

        private Logistics() {
            throw new AssertionError("Cannot be instantiated");
        }

        public static <T1, T2, T3, T4, X, R> List<R> compute(T1 t1, T2 t2, T3 t3, T4 t4, List<X> allX,
                FiveFunction<T1, T2, T3, T4, X, R> f) {

            List<R> allY = new ArrayList<>();

            for (X x : allX) {
                allY.add(f.apply(t1, t2, t3, t4, x));
            }

            return allY;
        }

        public static <T1, T2, T3, T4, X, R> R create(T1 t1, T2 t2, T3 t3, T4 t4, X x,
                FiveFunction<T1, T2, T3, T4, X, R> f) {

            return f.apply(t1, t2, t3, t4, x);
        }
    }

    @FunctionalInterface
    public interface FiveFunction<T1, T2, T3, T4, T5, R> {

        R apply(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5);
    }

    static class PL4 {

        private final double a;
        private final double b;
        private final double c;
        private final double d;
        private final double x;

        public PL4(double a, double b, double c, double d, double x) {
            this.a = a;
            this.b = b;
            this.c = c;
            this.d = d;
            this.x = x;
        }

        public double getA() {
            return a;
        }

        public double getB() {
            return b;
        }

        public double getC() {
            return c;
        }

        public double getD() {
            return d;
        }

        public double getX() {
            return x;
        }

        public double compute() {
            return d + ((a - d) / (1 + (Math.pow(x / c, b))));
        }

        @Override
        public int hashCode() {
            int hash = 7;
            hash = 19 * hash + (int) (Double.doubleToLongBits(this.a) ^ (Double.doubleToLongBits(this.a) >>> 32));
            hash = 19 * hash + (int) (Double.doubleToLongBits(this.b) ^ (Double.doubleToLongBits(this.b) >>> 32));
            hash = 19 * hash + (int) (Double.doubleToLongBits(this.c) ^ (Double.doubleToLongBits(this.c) >>> 32));
            hash = 19 * hash + (int) (Double.doubleToLongBits(this.d) ^ (Double.doubleToLongBits(this.d) >>> 32));
            hash = 19 * hash + (int) (Double.doubleToLongBits(this.x) ^ (Double.doubleToLongBits(this.x) >>> 32));
            return hash;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final PL4 other = (PL4) obj;
            if (Double.doubleToLongBits(this.a) != Double.doubleToLongBits(other.a)) {
                return false;
            }
            if (Double.doubleToLongBits(this.b) != Double.doubleToLongBits(other.b)) {
                return false;
            }
            if (Double.doubleToLongBits(this.c) != Double.doubleToLongBits(other.c)) {
                return false;
            }
            if (Double.doubleToLongBits(this.d) != Double.doubleToLongBits(other.d)) {
                return false;
            }
            return Double.doubleToLongBits(this.x) == Double.doubleToLongBits(other.x);
        }

        @Override
        public String toString() {
            return "PL4{" + "a=" + a + ", b=" + b + ", c=" + c + ", d=" + d + ", x=" + x + '}';
        }
    }

}

package com.javacoding.app.Chapter09_Functional_Style_Programming;

import java.util.function.BiFunction;
import java.util.function.Function;

// 부분적으로 함수 적용하기
public class P208_FunctionApplyOnly {

    public static void main(String[] args) {
        // (a+b+c)^2 = a^2+b^2+c^2+2ab+2bc+2ca
        TriFunction<Double, Double, Double, Double> abc2 = (a, b, c) -> Math.pow(a, 2) + Math.pow(b, 2) + Math.pow(c, 2)
                + 2.0 * a * b + 2 * b * c + 2 * c * a;

        System.out.println("abc2 (1): " + abc2.apply(1.0, 2.0, 1.0));
        System.out.println("abc2 (2): " + abc2.apply(1.0, 2.0, 2.0));
        System.out.println("abc2 (3): " + abc2.apply(1.0, 2.0, 3.0));

        Function<Double, Double> abc2Only1 = abc2.applyOnly(1.0, 2.0);

        System.out.println();
        System.out.println("abc2Only1 (1): " + abc2Only1.apply(1.0));
        System.out.println("abc2Only1 (2): " + abc2Only1.apply(2.0));
        System.out.println("abc2Only1 (3): " + abc2Only1.apply(3.0));

        BiFunction<Double, Double, Double> abc2Only2 = abc2.applyOnly(1.0);

        System.out.println();
        System.out.println("abc2Only2 (1): " + abc2Only2.apply(2.0, 3.0));
        System.out.println("abc2Only2 (2): " + abc2Only2.apply(1.0, 2.0));
        System.out.println("abc2Only2 (3): " + abc2Only2.apply(3.0, 2.0));
    }

    // 인수의 일부만 적용하여 다른 함수를 반환하는 함수
    @FunctionalInterface
    public interface TriFunction<T1, T2, T3, R> {

        R apply(T1 t1, T2 t2, T3 t3);

        default BiFunction<T2, T3, R> applyOnly(T1 t1) {
            return (t2, t3) -> apply(t1, t2, t3);
        }

        default Function<T3, R> applyOnly(T1 t1, T2 t2) {
            return (t3) -> apply(t1, t2, t3);
        }
    }
}

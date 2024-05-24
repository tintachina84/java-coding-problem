package com.javacoding.app.Chapter09_Functional_Style_Programming;

import java.util.List;
import static java.util.stream.Collectors.toList;

public class P185_MapMulti1 {

    /**
     * mapMulti()로 작업하기
     * mapMulti()는 스트림의 각 요소에 대해 여러 값을 생성할 수 있는 메서드이다.
     * mapMulti()는 Stream<T>를 반환하며, 이 스트림은 여러 요소를 생성할 수 있다.
     * mapMulti()는 BiConsumer<Consumer<T>, T>를 인수로 받는다.
     * BiConsumer는 두 개의 인수를 받아들이는 함수형 인터페이스이다.
     * 첫 번째 인수는 Consumer<T>이며, 이 인터페이스는 T 타입의 요소를 받아들인다.
     * 두 번째 인수는 T 타입의 요소이다.
     * mapMulti()는 스트림의 각 요소에 대해 BiConsumer를 호출한다.
     * BiConsumer는 스트림의 각 요소에 대해 여러 값을 생성할 수 있다.
     */
    public static void main(String[] args) {
        List<Integer> integers = List.of(3, 2, 5, 6, 7, 8);

        // filter()와 map()의 조합을 사용하여 짝수 정수를 필터링하고 그 값을 두 배로 늘리는 다음 고전적인 예제
        List<Integer> evenDoubledClassic = integers.stream()
                .filter(i -> i % 2 == 0)
                .map(i -> i * 2)
                .collect(toList());
        System.out.println(evenDoubledClassic);

        // 위와 동일한 결과
        // 두 개의 중간 작업을 사용하는 대신 mapMulti() 하나만 사용했기 때문에 코드가 더 간결해졌다.
        // filter() 역할은 if 문으로 대체되었으며 map() 역할은 accept() 메서드에서 수행.
        List<Integer> evenDoubledMM1 = integers.stream()
                .<Integer>mapMulti((i, consumer) -> {
                    if (i % 2 == 0) {
                        consumer.accept(i * 2);
                    }
                }).collect(toList());
        System.out.println(evenDoubledMM1);

        // 반면에 Double/Long/IntStream 대신 Stream<T>이 필요할 때마다 여전히 mapToObj() 또는 boxed()를
        // 사용해야 한다.
        List<Integer> evenDoubledMM2 = integers.stream()
                .mapMultiToInt((i, consumer) -> {
                    if (i % 2 == 0) {
                        consumer.accept(i * 2);
                    }
                })
                .mapToObj(i -> i) // or, .boxed()
                .collect(toList());
        System.out.println(evenDoubledMM2);

        // 기본 유형(double, long 및 int)의 경우 각각 DoubleStream, LongStream 및 IntStream을 반환하는
        // mapMultiToDouble(), mapMultiToLong() 및 mapMultiToInt()가 있다. 예를 들어 짝수 정수의 합을
        // 계산하려는 경우 mapMultiToInt()를 사용하는 것이 mapMulti()보다 더 나은 선택이다.
        // 왜냐하면 유형 감시를 건너뛰고 기본 int로만 작업할 수 있기 때문이다.
        int evenDoubledAndSumMM = integers.stream()
                .mapMultiToInt((i, consumer) -> {
                    if (i % 2 == 0) {
                        consumer.accept(i * 2);
                    }
                })
                .sum();
        System.out.println(evenDoubledAndSumMM);
    }
}

package com.javacoding.app.Chapter09_Functional_Style_Programming;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class P204_StreamDistinctBy {

    public static void main(String[] args) {
        List<Car> cars = List.of(
                new Car("Chevrolet", "diesel", 350),
                new Car("Dacia", "diesel", 100),
                new Car("Lexus", "gasoline", 300),
                new Car("Chevrolet", "electric", 150),
                new Car("Mercedes", "electric", 150),
                new Car("Chevrolet", "diesel", 250),
                new Car("Lexus", "gasoline", 300),
                new Car("Ford", "electric", 80),
                new Car("Chevrolet", "diesel", 450),
                new Car("Mercedes", "electric", 200),
                new Car("Chevrolet", "gasoline", 350),
                new Car("Lexus", "diesel", 300));

        // equals()에 기반한 기본적인 distinct
        cars.stream()
                .distinct()
                .forEach(System.out::println);

        System.out.println();

        // 중복된 브랜드 제거
        cars.stream()
                .collect(Collectors.toMap(Car::getBrand, Function.identity(), (c1, c2) -> c1))
                .values()
                .forEach(System.out::println);

        System.out.println();

        // 위와 같은 결과를 얻는 distinctByKeyV1(Function) 사용, null도 잘 작동함.
        cars.stream()
                .collect(Streams.distinctByKeyV1(Car::getBrand))
                .values()
                .forEach(System.out::println);

        System.out.println();

        cars.stream()
                .collect(Streams.distinctByKeyV1(Car::getFuel))
                .values()
                .forEach(System.out::println);

        System.out.println();

        // null 제외 시킴
        cars.stream()
                .filter(Streams.distinctByKeyV2(Car::getBrand))
                .forEach(System.out::println);

        System.out.println();

        // null 제외 시킴
        cars.stream()
                .filter(Streams.distinctByKeyV3(Car::getFuel))
                .forEach(System.out::println);
    }

    public final class Streams {

        private Streams() {
            throw new AssertionError("Cannot be instantiated");
        }

        public static <K, T> Collector<T, ?, Map<K, T>> distinctByKeyV1(
                Function<? super T, ? extends K> function) {

            return Collectors.toMap(function, Function.identity(), (t1, t2) -> t1);
        }

        public static <T> Predicate<T> distinctByKeyV2(Function<? super T, ?> function) {

            Map<Object, Boolean> seen = new ConcurrentHashMap<>();

            return t -> seen.putIfAbsent(function.apply(t), Boolean.TRUE) == null;
        }

        public static <T> Predicate<T> distinctByKeyV3(Function<? super T, ?> function) {

            Set<Object> seen = ConcurrentHashMap.newKeySet();

            return t -> seen.add(function.apply(t));
        }
    }

    static class Car {

        private final String brand;
        private final String fuel;
        private final int horsepower;

        public Car(String brand, String fuel, int horsepower) {
            this.brand = brand;
            this.fuel = fuel;
            this.horsepower = horsepower;
        }

        public String getBrand() {
            return brand;
        }

        public String getFuel() {
            return fuel;
        }

        public int getHorsepower() {
            return horsepower;
        }

        @Override
        public int hashCode() {
            int hash = 5;
            hash = 17 * hash + Objects.hashCode(this.brand);
            hash = 17 * hash + Objects.hashCode(this.fuel);
            hash = 17 * hash + this.horsepower;
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
            final Car other = (Car) obj;
            if (this.horsepower != other.horsepower) {
                return false;
            }
            if (!Objects.equals(this.brand, other.brand)) {
                return false;
            }
            return Objects.equals(this.fuel, other.fuel);
        }

        @Override
        public String toString() {
            return "Car{" + "brand=" + brand + ", fuel=" + fuel + ", horsepower=" + horsepower + '}';
        }
    }

}

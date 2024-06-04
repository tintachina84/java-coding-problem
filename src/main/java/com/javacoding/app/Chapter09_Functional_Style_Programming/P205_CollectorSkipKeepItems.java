package com.javacoding.app.Chapter09_Functional_Style_Programming;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class P205_CollectorSkipKeepItems {

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

        List<Car> first5CarsLimit = cars.stream()
                .limit(5)
                .collect(Collectors.toList());

        System.out.println("\nFirst 5 cars: " + first5CarsLimit);

        List<Car> first5Cars = cars.stream()
                .collect(MyCollectors.toUnmodifiableListKeep(5));

        System.out.println("\nFirst 5 cars: " + first5Cars);

        List<Car> last5CarsSkip = cars.stream()
                .skip(5)
                .collect(Collectors.toList());

        System.out.println("\nLast 5 cars: " + last5CarsSkip);

        List<Car> last5Cars1 = cars.stream()
                .collect(MyCollectors.toUnmodifiableListSkip(5));

        System.out.println("\nLast 5 cars: " + last5Cars1);

        List<Car> last5Cars2 = cars.stream()
                .collect(MyCollectors.toUnmodifiableListSkipOptimized(5));

        System.out.println("\nLast 5 cars: " + last5Cars2);
    }

    public final class MyCollectors {

        private MyCollectors() {
            throw new AssertionError("Cannot be instantiated");
        }

        public static <T> Collector<T, List<T>, List<T>> toUnmodifiableListKeep(int max) {

            return Collector.of(ArrayList::new, // Supplier: 수집을 시작할 때 새로운 ArrayList를 제공
                    (list, value) -> {
                        if (list.size() < max) {
                            list.add(value);
                        }
                    }, // Accumulator: 리스트의 크기가 max보다 작을 때만 요소를 리스트에 추가
                    (left, right) -> {
                        left.addAll(right);
                        return left;
                    }, // Combiner: 병렬 스트림 실행 시 두 리스트를 병합
                    Collections::unmodifiableList); // Finisher: 최종 리스트를 수정 불가능 리스트로 반환
        }

        public static <T> Collector<T, List<T>, List<T>> toUnmodifiableListSkip(int index) {

            return Collector.of(ArrayList::new, // Supplier: 수집을 시작할 때 새로운 ArrayList를 제공
                    (list, value) -> {
                        if (list.size() >= index) {
                            list.add(value);
                        } else {
                            list.add(null);
                        }
                    }, // Accumulator: 리스트의 크기가 index보다 크거나 같으면 요소를 추가하고, 그렇지 않으면 null을 추가
                    (left, right) -> {
                        left.addAll(right);
                        return left;
                    }, // Combiner: 병렬 스트림 실행 시 두 리스트를 병합
                    list -> Collections.unmodifiableList(list.subList(index, list.size())));
        } // Finisher: 리스트에서 특정 인덱스부터 끝까지의 부분 리스트를 수정 불가능 리스트로 반환

        public static <T> Collector<T, ?, List<T>> toUnmodifiableListSkipOptimized(int index) {

            class Sublist {

                int index;
                List<T> list = new ArrayList<>();
            }

            return Collector.of(Sublist::new, // Supplier: 새로운 Sublist 객체를 제공
                    (sublist, value) -> {
                        if (sublist.index >= index) {
                            sublist.list.add(value);
                        } else {
                            sublist.index++;
                        }
                    }, // Accumulator: 주어진 인덱스가 index 이상인 경우에만 리스트에 요소를 추가, 그렇지 않으면 인덱스를 증가
                    (left, right) -> {
                        left.list.addAll(right.list);
                        left.index = left.index + right.index;
                        return left;
                    }, // Combiner: 두 Sublist 객체를 병합
                    sublist -> Collections.unmodifiableList(sublist.list)); // Finisher: 최종 리스트를 수정 불가능 리스트로 반환
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

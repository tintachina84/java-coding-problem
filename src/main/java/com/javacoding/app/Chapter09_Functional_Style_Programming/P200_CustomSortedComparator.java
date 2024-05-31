package com.javacoding.app.Chapter09_Functional_Style_Programming;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;

public class P200_CustomSortedComparator {

    public static void main(String[] args) {
        Map<Integer, Car> cars = Map.of(
                1, new Car("Dacia", "diesel", 350),
                2, new Car("Lexus", "gasoline", 350),
                3, new Car("Chevrolet", "electric", 150),
                4, new Car("Mercedes", "gasoline", 150),
                5, new Car("Chevrolet", "diesel", 250),
                6, new Car("Ford", "electric", 80),
                7, new Car("Chevrolet", "diesel", 450),
                8, new Car("Mercedes", "electric", 200),
                9, new Car("Chevrolet", "gasoline", 350),
                10, new Car("Lexus", "diesel", 300));

        // c1과 c2의 value의 horsepower가 같으면 key를 기준으로 정렬하고, 그렇지 않으면 horsepower를 기준으로 정렬.
        // [7(450), 1(350), 2(350), 9(350), 10(300), 5(250), 8(200), 3(150), 4(150),
        // 6(80)]
        List<String> result1 = cars.entrySet().stream()
                .sorted((c1, c2) -> c2.getValue().getHorsepower() == c1.getValue().getHorsepower()
                        ? c1.getKey().compareTo(c2.getKey())
                        : Integer.valueOf(c2.getValue().getHorsepower())
                                .compareTo(c1.getValue().getHorsepower()))
                .map(c -> c.getKey() + "(" + c.getValue().getHorsepower() + ")")
                .toList();

        // [7(450), 1(350), 2(350), 9(350), 10(300), 5(250), 8(200), 3(150), 4(150),
        // 6(80)]
        List<String> result2 = cars.entrySet().stream()
                .sorted(Entry.<Integer, Car>comparingByValue(
                        Comparator.comparingInt(Car::getHorsepower).reversed()) // horsepower를 기준으로 내림차순 정렬
                        .thenComparing(Entry.comparingByKey())) // 그 후 key를 기준으로 오름차순 정렬
                .map(c -> c.getKey() + "(" + c.getValue().getHorsepower() + ")")
                .toList();

        System.out.println(result1);
        System.out.println(result2);
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

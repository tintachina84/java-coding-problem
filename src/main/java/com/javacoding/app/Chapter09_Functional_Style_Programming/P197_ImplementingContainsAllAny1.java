package com.javacoding.app.Chapter09_Functional_Style_Programming;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class P197_ImplementingContainsAllAny1 {

    public static void main(String[] args) {
        List<Car> cars = Arrays.asList(new Car("Dacia", "diesel", 100),
                new Car("Lexus", "gasoline", 300), new Car("Chevrolet", "electric", 150),
                new Car("Mercedes", "gasoline", 150), new Car("Chevrolet", "diesel", 250),
                new Car("Ford", "electric", 80), new Car("Chevrolet", "diesel", 450),
                new Car("Mercedes", "electric", 200), new Car("Chevrolet", "gasoline", 350),
                new Car("Lexus", "diesel", 300), new Car("Ford", "electric", 200));

        Car car1 = new Car("Lexus", "diesel", 300);
        Car car2 = new Car("Ford", "electric", 80);
        Car car3 = new Car("Chevrolet", "electric", 150);

        List<Car> cars123 = List.of(car1, car2, car3);

        System.out.println();
        boolean f11 = Streams.from(cars.stream())
                .filter(car -> car.getBrand().equals("Mercedes")).contains(car1);
        boolean f12 = Streams.from(cars.stream()).containsAll(car1, car2, car3);
        boolean f13 = Streams.from(cars.stream()).containsAll(cars123);
        boolean f14 = Streams.from(cars.stream()).containsAll(cars123.stream());
        boolean f15 = Streams.from(cars123.stream()).containsAll(cars.stream());

        System.out.println("f11: " + f11);
        System.out.println("f12: " + f12);
        System.out.println("f13: " + f13);
        System.out.println("f14: " + f14);
        System.out.println("f15: " + f15);

        System.out.println();

        boolean f21 = Streams.from(cars.stream()).containsAny(car1, car2, car3);
        boolean f22 = Streams.from(cars.stream()).containsAny(cars123);
        boolean f23 = Streams.from(cars.stream()).containsAny(cars123.stream());
        boolean f24 = Streams.from(cars123.stream()).containsAny(cars.stream());

        System.out.println("f21: " + f21);
        System.out.println("f22: " + f22);
        System.out.println("f23: " + f23);
        System.out.println("f24: " + f24);

        System.out.println();

        Car car4 = new Car("Mercedes", "electric", 200);
        boolean isit = Streams.from(cars.stream())
                .filter(car -> car.getBrand().equals("Mercedes"))
                .distinct() // 스트림에서 중복된 요소를 제거. equals와 hashCode 메소드를 적절히 오버라이드하고 있어야 한다.
                // dropWhile(...)
                // 스트림에서 연료가 "gasoline"인 차량을 스트림의 시작부터 만나지 않을 때까지 제거.
                // 즉, 처음으로 연료가 "gasoline"이 아닌 차량을 만날 때까지 차량을 제거.
                .dropWhile(car -> car.getFuel().equals("gasoline"))
                .contains(car4);

        System.out.println("Car4? " + isit);
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

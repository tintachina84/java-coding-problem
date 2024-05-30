package com.javacoding.app.Chapter09_Functional_Style_Programming;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;

public class P195_BuildCustomPredicateMap {

    public static void main(String[] args) {
        List<Car> cars = Arrays.asList(new Car("Dacia", "diesel", 100),
                new Car("Lexus", "gasoline", 300), new Car("Chevrolet", "electric", 150),
                new Car("Mercedes", "gasoline", 150), new Car("Chevrolet", "diesel", 250),
                new Car("Ford", "electric", 80), new Car("Chevrolet", "diesel", 450),
                new Car("Mercedes", "electric", 200), new Car("Chevrolet", "gasoline", 350),
                new Car("Lexus", "diesel", 300), new Car("Ford", "electric", 200));

        Map<String, String> filtersMap = Map.of(
                "brand", "Chevrolet",
                "fuel", "diesel");

        Predicate<Car> filterPredicate = t -> true;
        for (String key : filtersMap.keySet()) {
            filterPredicate = filterPredicate.and(PredicateBuilder.EQUALS
                    .toPredicate(PredicateBuilder.getFieldByName(Car.class, key), filtersMap.get(key)));
        }

        cars.stream()
                .filter(filterPredicate)
                .forEach(System.out::println);
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

    enum PredicateBuilder {

        EQUALS(String::equals);

        private final BiPredicate<String, String> predicate;

        private PredicateBuilder(BiPredicate<String, String> predicate) {
            this.predicate = predicate;
        }

        public <T> Predicate<T> toPredicate(Function<T, String> getter, String u) {
            return obj -> this.predicate.test(getter.apply(obj), u);
        }

        public static <T> Function<T, String> getFieldByName(Class<T> cls, String field) {
            return object -> {
                try {
                    Field f = cls.getDeclaredField(field);
                    f.setAccessible(true);

                    return (String) f.get(object);
                } catch (IllegalAccessException | IllegalArgumentException
                        | NoSuchFieldException | SecurityException e) {
                    throw new RuntimeException(e);
                }
            };
        }
    }
}

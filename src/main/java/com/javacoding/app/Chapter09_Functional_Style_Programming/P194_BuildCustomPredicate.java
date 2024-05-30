package com.javacoding.app.Chapter09_Functional_Style_Programming;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;

public class P194_BuildCustomPredicate {

    public static void main(String[] args) {
        List<Car> cars = Arrays.asList(new Car("Dacia", "diesel", 100),
                new Car("Lexus", "gasoline", 300), new Car("Chevrolet", "electric", 150),
                new Car("Mercedes", "gasoline", 150), new Car("Chevrolet", "diesel", 250),
                new Car("Ford", "electric", 80), new Car("Chevrolet", "diesel", 450),
                new Car("Mercedes", "electric", 200), new Car("Chevrolet", "gasoline", 350),
                new Car("Lexus", "diesel", 300), new Car("Ford", "electric", 200));

        Predicate<Car> gtPredicate = PredicateBuilder.GT
                .toPredicate(PredicateBuilder.getFieldByName(Car.class, "horsepower"), 300);

        cars.stream()
                .filter(gtPredicate)
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

        GT((t, u) -> t > u),
        LT((t, u) -> t < u),
        GE((t, u) -> t >= u),
        LE((t, u) -> t <= u),
        EQ((t, u) -> t.intValue() == u.intValue()),
        NOT_EQ((t, u) -> t.intValue() != u.intValue());

        private final BiPredicate<Integer, Integer> predicate;

        private PredicateBuilder(BiPredicate<Integer, Integer> predicate) {
            this.predicate = predicate;
        }

        public <T> Predicate<T> toPredicate(Function<T, Integer> getter, int u) {
            return obj -> this.predicate.test(getter.apply(obj), u);
        }

        public static <T> Function<T, Integer> getFieldByName(Class<T> cls, String field) {
            return object -> {
                try {
                    Field f = cls.getDeclaredField(field);
                    f.setAccessible(true);

                    return (Integer) f.get(object);
                } catch (IllegalAccessException | IllegalArgumentException
                        | NoSuchFieldException | SecurityException e) {
                    throw new RuntimeException(e);
                }
            };
        }
    }
}

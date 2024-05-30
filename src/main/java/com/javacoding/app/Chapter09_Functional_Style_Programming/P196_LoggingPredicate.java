package com.javacoding.app.Chapter09_Functional_Style_Programming;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class P196_LoggingPredicate {

    public static void main(String[] args) {
        List<Car> cars = Arrays.asList(new Car("Dacia", "diesel", 100),
                new Car("Lexus", "gasoline", 300), new Car("Chevrolet", "electric", 150),
                new Car("Mercedes", "gasoline", 150), new Car("Chevrolet", "diesel", 250),
                new Car("Ford", "electric", 80), new Car("Chevrolet", "diesel", 450),
                new Car("Mercedes", "electric", 200), new Car("Chevrolet", "gasoline", 350),
                new Car("Lexus", "diesel", 300), new Car("Ford", "electric", 200));

        LogPredicate<Car> predicate = car -> car.getFuel().equals("electric");

        cars.stream()
                .filter(t -> predicate.testAndLog(t, "electric"))
                .forEach(System.out::println);

        System.out.println();

        cars.stream()
                .filter(Predicates.testAndLog(car -> car.getFuel().equals("electric"), "electric"))
                .forEach(System.out::println);
    }

    public final class Predicates {

        private static final Logger logger = LoggerFactory.getLogger(LogPredicate.class);

        private Predicates() {
            throw new AssertionError("Cannot be instantiated");
        }

        public static <T> Predicate<T> testAndLog(Predicate<? super T> predicate, String val) {

            return t -> {
                boolean result = predicate.test(t);

                if (!result) {
                    logger.warn(predicate + " don't match '" + val + "'");
                }

                return result;
            };
        }
    }

    @FunctionalInterface
    public interface LogPredicate<T> extends Predicate<T> {

        Logger logger = LoggerFactory.getLogger(LogPredicate.class);

        default boolean testAndLog(T t, String val) {

            boolean result = this.test(t);

            if (!result) {
                logger.warn(t + " don't match '" + val + "'");
            }

            return result;
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

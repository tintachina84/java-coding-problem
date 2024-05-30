package com.javacoding.app.Chapter09_Functional_Style_Programming;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class P191_ApplyMultiplePredicate {

    public static void main(String[] args) {
        List<Car> cars = Arrays.asList(new Car("Dacia", "diesel", 100),
                new Car("Lexus", "gasoline", 300), new Car("Chevrolet", "electric", 150),
                new Car("Mercedes", "gasoline", 150), new Car("Chevrolet", "diesel", 250),
                new Car("Ford", "electric", 80), new Car("Chevrolet", "diesel", 450),
                new Car("Mercedes", "electric", 200), new Car("Chevrolet", "gasoline", 350),
                new Car("Lexus", "diesel", 300), new Car("Ford", "electric", 200));

        // 쉐보레만 필터링 하는 Predicate
        Predicate<Car> pChevrolets = car -> car.getBrand().equals("Chevrolet");

        List<Car> chevrolets = cars.stream()
                .filter(pChevrolets)
                .collect(Collectors.toList());
        System.out.println("Chevrolets:\n" + chevrolets);

        // 쉐보레가 아닌 것만 필터링 하는 Predicate
        Predicate<Car> pNotChevrolets1 = car -> !car.getBrand().equals("Chevrolet");
        Predicate<Car> pNotChevrolets2 = Predicate.not(pChevrolets);
        Predicate<Car> pNotChevrolets3 = pChevrolets.negate();

        List<Car> notChevrolets1 = cars.stream()
                .filter(pNotChevrolets1)
                .collect(Collectors.toList());

        List<Car> notChevrolets2 = cars.stream()
                .filter(pNotChevrolets2)
                .collect(Collectors.toList());

        List<Car> notChevrolets3 = cars.stream()
                .filter(pNotChevrolets3)
                .collect(Collectors.toList());

        System.out.println("\nAll non-chevrolet cars (1):\n" + notChevrolets1);
        System.out.println("\nAll non-chevrolet cars (2):\n" + notChevrolets2);
        System.out.println("\nAll non-chevrolet cars (3):\n" + notChevrolets3);

        // 쉐보레가 아닌 것 중에서 150마력 이상인 것만 필터링 하는 Predicate
        Predicate<Car> pHorsepower = car -> car.getHorsepower() >= 150;

        List<Car> notChevrolets150_1 = cars.stream()
                .filter(pChevrolets.negate())
                .filter(pHorsepower)
                .collect(Collectors.toList());

        List<Car> notChevrolets150_2 = cars.stream()
                .filter(pChevrolets.negate().and(pHorsepower))
                .collect(Collectors.toList());

        System.out.println("\nAll non-chevrolet cars with at least 150 horsepower (1):\n" + notChevrolets150_1);
        System.out.println("\nAll non-chevrolet cars with at least 150 horsepower (2):\n" + notChevrolets150_2);

        // 전기차인 것만 필터링 하는 Predicate
        Predicate<Car> pElectric = car -> car.getFuel().equals("electric");

        // 쉐보레이거나 전기차인 것만 필터링 하는 Predicate
        List<Car> chevroletsOrElectric = cars.stream()
                .filter(pChevrolets.or(pElectric))
                .collect(Collectors.toList());

        System.out.println("\nAll chevrolets or electric cars:\n" + chevroletsOrElectric);

        /* Predicate 병합 */
        // and
        Predicate<Car> pLexus = car -> car.getBrand().equals("Lexus");
        Predicate<Car> pDiesel = car -> car.getFuel().equals("diesel");
        Predicate<Car> p250 = car -> car.getHorsepower() > 250;

        Predicate<Car> predicateAnd = Predicates.asOneAnd(pLexus, pDiesel, p250);

        List<Car> lexusDiesel250And = cars.stream()
                .filter(predicateAnd)
                .collect(Collectors.toList());

        System.out.println("\nLexus, diesel with more than 250 housepower:\n" + lexusDiesel250And);

        // or
        Predicate<Car> pGasoline = car -> car.getFuel().equals("gasoline");

        Predicate<Car> predicateOr = Predicates.asOneOr(pDiesel, pGasoline);

        List<Car> dieselGasolineOr = cars.stream()
                .filter(predicateOr)
                .collect(Collectors.toList());

        System.out.println("\nAll diesel or gasoline car:\n" + dieselGasolineOr);

        // and-or combo
        Predicate<Car> p100 = car -> car.getHorsepower() >= 100;
        Predicate<Car> p200 = car -> car.getHorsepower() <= 200;

        Predicate<Car> p300 = car -> car.getHorsepower() >= 300;
        Predicate<Car> p400 = car -> car.getHorsepower() <= 400;

        Predicate<Car> pCombo = Predicates.asOneOr(
                Predicates.asOneAnd(p100, p200), Predicates.asOneAnd(p300, p400));

        List<Car> comboAndOr = cars.stream()
                .filter(pCombo)
                .collect(Collectors.toList());

        System.out.println("\nAll cars having horsepower between 100 and 200 or 300 and 400:\n" + comboAndOr);
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

    @SuppressWarnings("unchecked")
    static class Predicates {

        private Predicates() {
            throw new AssertionError("Cannot be instantiated");
        }

        // 입력된 모든 Predicate를 AND 연산으로 결합. 즉, 반환된 Predicate는 모든 입력 Predicate가 참일 때만 참을
        // 반환. reduce 메소드를 사용하여 모든 Predicate를 결합하며, 초기값으로 항상 참을 반환하는 Predicate를 사용.
        public static <T> Predicate<T> asOneAnd(Predicate<T>... predicates) {
            Predicate<T> theOneAnd = Stream.of(predicates).reduce(p -> true, Predicate::and);

            return theOneAnd;
        }

        // 입력된 모든 Predicate를 OR 연산으로 결합. 즉, 반환된 Predicate는 입력 Predicate 중 하나라도 참이면 참을
        // 반환. reduce 메소드를 사용하여 모든 Predicate를 결합하며, 초기값으로 항상 거짓을 반환하는 Predicate를 사용.
        public static <T> Predicate<T> asOneOr(Predicate<T>... predicates) {
            Predicate<T> theOneOr = Stream.of(predicates).reduce(p -> false, Predicate::or);

            return theOneOr;
        }
    }
}

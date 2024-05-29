package com.javacoding.app.Chapter09_Functional_Style_Programming;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;

public class P189_LambdaLazinessFunctional {

    public static void main(String[] args) {

        ApplicationDependency app1 = new ApplicationDependency(1, "app-1");
        ApplicationDependency app2 = new ApplicationDependency(2, "app-2");

        DependencyManager dm = new DependencyManager();
        dm.processDependencies(app1);
        dm.processDependencies(app2);
        dm.processDependencies(app1);
        dm.processDependencies(app2);
    }

    static class DependencyManager {

        private Map<Long, String> apps = new HashMap<>();

        public void processDependencies(ApplicationDependency appd) {

            System.out.println();
            System.out.println("Processing app: " + appd.getName());
            System.out.println("Dependencies: " + appd.getDependencies());

            apps.put(appd.getId(), appd.getDependencies());
        }
    }

    static class ApplicationDependency {

        private final long id;
        private final String name;
        private final Supplier<String> dependencies = Memoize.supplier(this::downloadDependencies);

        public ApplicationDependency(long id, String name) {
            this.id = id;
            this.name = name;
        }

        public long getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public String getDependencies() {
            return dependencies.get();
        }

        private String downloadDependencies() {
            return "list of dependencies downloaded from repository " + Math.random();
        }
    }

    // Memoize 클래스는 supplier라는 정적 메소드를 가지고 있다. 이 메소드는 Supplier<T>를 입력으로 받아, 그 결과를
    // 캐시하는 FSupplier<T>를 반환한다.
    static class Memoize {

        private final static Object UNDEFINED = new Object();

        public static <T> FSupplier<T> supplier(final Supplier<T> supplier) {

            // AtomicReference<Object> 타입의 cache를 생성하고, 초기값으로 UNDEFINED를 설정.
            // 이 cache는 Supplier<T>의 결과를 저장하는 데 사용된다.
            AtomicReference<Object> cache = new AtomicReference<>(UNDEFINED);

            return () -> {
                Object value = cache.get();
                if (value == UNDEFINED) {
                    synchronized (cache) {
                        if (cache.get() == UNDEFINED) {

                            System.out.println("Caching: " + supplier.get());
                            value = supplier.get();
                            cache.set(value);
                        }
                    }
                }

                return (T) value;
            };
        }
    }

    // FSupplier<T>는 함수형 인터페이스로, 입력 없이 결과를 반환하는 함수를 나타낸다.
    @FunctionalInterface
    public interface FSupplier<R> extends Supplier<R> {

    }
}

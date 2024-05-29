package com.javacoding.app.Chapter09_Functional_Style_Programming;

import java.util.HashMap;
import java.util.Map;

public class P189_LambdaLazinessDysFunctional {

    public static void main(String[] args) {
        ApplicationDependency app1 = new ApplicationDependency(1, "app-1");
        ApplicationDependency app2 = new ApplicationDependency(2, "app-2");

        DependencyManager dm = new DependencyManager();
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
        private String dependencies;

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
            return dependencies;
        }

        private void downloadDependencies() {

            dependencies = "list of dependencies downloaded from repository " + Math.random();
        }
    }
}

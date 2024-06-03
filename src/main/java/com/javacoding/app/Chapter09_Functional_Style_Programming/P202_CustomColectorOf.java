package com.javacoding.app.Chapter09_Functional_Style_Programming;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Queue;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collector;

public class P202_CustomColectorOf {

    public static void main(String[] args) {
        Map<Integer, Car> cars = Map.of(
                1, new Car("Dacia", "diesel", 100),
                2, new Car("Lexus", "gasoline", 300),
                3, new Car("Chevrolet", "electric", 150),
                4, new Car("Mercedes", "gasoline", 150),
                5, new Car("Chevrolet", "diesel", 250),
                6, new Car("Ford", "electric", 80),
                7, new Car("Chevrolet", "diesel", 450),
                8, new Car("Mercedes", "electric", 200),
                9, new Car("Chevrolet", "gasoline", 350),
                10, new Car("Lexus", "diesel", 300));

        TreeSet<String> electricBrands = cars.values().stream()
                .filter(c -> "electric".equals(c.getFuel()))
                .map(c -> c.getBrand())
                .collect(MyCollectors.toTreeSet());

        System.out.println("Electric brands: " + electricBrands);

        LinkedHashSet<Integer> hpSorted = cars.values().stream()
                .map(c -> c.getHorsepower())
                .sorted()
                .collect(MyCollectors.toLinkedHashSet());

        System.out.println("Sorted horsepower: " + hpSorted);

        LinkedHashSet<Integer> excludeHp200 = cars.values().stream()
                .map(c -> c.getHorsepower())
                .sorted()
                .collect(MyCollectors.exclude(c -> c > 200, MyCollectors.toLinkedHashSet()));

        System.out.println("Sorted horsepower less than 200: " + excludeHp200);

        Vehicle mazda = new Car("Mazda", "diesel", 155);
        Vehicle ferrari = new Car("Ferrari", "gasoline", 500);

        Vehicle hov = new Submersible("HOV", 3000);
        Vehicle rov = new Submersible("ROV", 7000);

        List<Vehicle> vehicles = List.of(mazda, hov, ferrari, rov);

        List<Car> onlyCars = vehicles.stream()
                .collect(MyCollectors.toType(Car.class, ArrayList::new));

        Set<Submersible> onlySubmersible = vehicles.stream()
                .collect(MyCollectors.toType(Submersible.class, HashSet::new));

        System.out.println("Only cars:" + onlyCars);
        System.out.println("Only submersible:" + onlySubmersible);

        SplayTree st = cars.values().stream()
                .map(c -> c.getHorsepower())
                .collect(MyCollectors.toSplayTree());

        System.out.println("SplayTree:");
        st.print(SplayTree.TraversalOrder.IN);
    }

    public final class MyCollectors {

        private MyCollectors() {
            throw new AssertionError("Cannot be instantiated");
        }

        public static <T> Collector<T, TreeSet<T>, TreeSet<T>> toTreeSet() {

            return Collector.of(TreeSet::new, TreeSet::add,
                    (left, right) -> {
                        left.addAll(right);

                        return left;
                    }, Collector.Characteristics.IDENTITY_FINISH);
        }

        public static <T> Collector<T, LinkedHashSet<T>, LinkedHashSet<T>> toLinkedHashSet() {

            return Collector.of(LinkedHashSet::new, HashSet::add,
                    (left, right) -> {
                        left.addAll(right);

                        return left;
                    }, Collector.Characteristics.IDENTITY_FINISH);
        }

        // T는 스트림의 요소 유형(수집할 요소)
        // A는 수집 프로세스 중에 사용된 객체 유형
        // R은 수집 과정 이후의 객체 유형(최종 결과)
        public static <T, A, R> Collector<T, A, R> exclude(
                Predicate<T> predicate, Collector<T, A, R> collector) {
            return Collector.of(
                    collector.supplier(), // 새로운 비어 있는 변경 가능한 결과 컨테이너 생성
                    (l, r) -> {
                        if (predicate.negate().test(r)) {
                            collector.accumulator().accept(l, r);
                        }
                    }, // 변경 가능한 결과 컨테이너에 새 데이터 요소 통합
                    collector.combiner(), // 두 개의 변경 가능한 결과 컨테이너를 하나로 결합
                    collector.finisher(), // 최종 결과를 얻기 위해 변경 가능한 결과 컨테이너에서 선택적 최종 변환을 수행
                    collector.characteristics().toArray(Collector.Characteristics[]::new));
        }

        public static <T, A extends T, R extends Collection<A>> Collector<T, ?, R> toType(Class<A> type,
                Supplier<R> supplier) {
            // 인자로 전달되는 Supplier<R>은 () -> new ArrayList<>() 와 같은 람다식 또는 ArrayList::new와 같은
            // 메서드 참조로 전달될 수 있다.
            return Collector.of(supplier,
                    (R r, T t) -> {
                        if (type.isInstance(t)) {
                            r.add(type.cast(t));
                        }
                    },
                    (R left, R right) -> {
                        left.addAll(right);

                        return left;
                    },
                    Collector.Characteristics.IDENTITY_FINISH);
        }

        public static Collector<Integer, SplayTree, SplayTree> toSplayTree() {

            return Collector.of(SplayTree::new, SplayTree::insert,
                    (left, right) -> {
                        left.insertAll(right);

                        return left;
                    }, Collector.Characteristics.IDENTITY_FINISH);
        }
    }

    static class SplayTree {

        private Node root;
        private int count;

        private final class Node {

            private Node left;
            private Node right;
            private Node parent;
            private int data;

            public Node() {
                this(0, null, null, null);
            }

            public Node(int data) {
                this(data, null, null, null);
            }

            public Node(int data, Node left, Node right, Node parent) {
                this.left = left;
                this.right = right;
                this.parent = parent;
                this.data = data;
            }
        }

        public SplayTree() {
            root = null;
        }

        // 이진 검색 트리에 새로운 노드를 삽입하는 기능을 수행.
        // 이진 검색 트리는 각 노드의 왼쪽 하위 트리에는 해당 노드보다 작은 키를,
        // 오른쪽 하위 트리에는 해당 노드보다 큰 키를 가진 노드들이 위치하는 트리 구조.
        public void insert(int data) {

            // 루트 노드에서 시작하여 새 노드가 삽입될 위치를 찾기 위해 트리를 탐색.
            // yNode는 xNode의 부모 노드를 추적.
            Node xNode = root;
            Node yNode = null;

            // 트리를 탐색하면서 새 노드가 삽입될 위치를 찾는다.
            while (xNode != null) {
                yNode = xNode;
                if (data > yNode.data) {
                    xNode = xNode.right;
                } else {
                    xNode = xNode.left;
                }
            }

            xNode = new Node();
            xNode.data = data;
            xNode.parent = yNode;

            if (yNode == null) {
                root = xNode;
            } else if (data > yNode.data) {
                yNode.right = xNode;
            } else {
                yNode.left = xNode;
            }

            splay(xNode);

            count++;
        }

        public void insertAll(SplayTree other) {
            insertAll(other.root);
        }

        private void insertAll(Node node) {
            if (node != null) {
                insertAll(node.left);
                insert(node.data);
                insertAll(node.right);
            }
        }

        private void splay(Node node) {

            while (node.parent != null) { // 현재 노드가 루트가 될 때까지 반복합니다.

                Node parentNode = node.parent; // 현재 노드의 부모 노드를 저장합니다.
                Node grandpaNode = parentNode.parent; // 현재 노드의 조부모 노드를 저장합니다.

                if (grandpaNode == null) { // 부모 노드가 루트 노드인 경우
                    if (node == parentNode.left) { // 현재 노드가 부모 노드의 왼쪽 자식인 경우
                        leftChildToParent(node, parentNode); // 회전하여 현재 노드를 루트로 만듭니다.
                    } else { // 현재 노드가 부모 노드의 오른쪽 자식인 경우
                        rightChildToParent(node, parentNode); // 회전하여 현재 노드를 루트로 만듭니다.
                    }
                } else { // 부모 노드 위에 조부모 노드가 있는 경우
                    if (node == parentNode.left) { // 현재 노드가 부모 노드의 왼쪽 자식인 경우
                        if (parentNode == grandpaNode.left) { // 부모 노드가 조부모 노드의 왼쪽 자식인 경우
                            leftChildToParent(parentNode, grandpaNode); // 회전하여 부모 노드를 조부모 노드 위치로 올립니다.
                            leftChildToParent(node, parentNode); // 회전하여 현재 노드를 부모 노드 위치로 올립니다.
                        } else { // 부모 노드가 조부모 노드의 오른쪽 자식인 경우
                            leftChildToParent(node, node.parent); // 회전하여 현재 노드를 부모 노드 위치로 올립니다.
                            rightChildToParent(node, node.parent); // 다시 회전하여 현재 노드를 조부모 노드 위치로 올립니다.
                        }
                    } else { // 현재 노드가 부모 노드의 오른쪽 자식인 경우
                        if (parentNode == grandpaNode.left) { // 부모 노드가 조부모 노드의 왼쪽 자식인 경우
                            rightChildToParent(node, node.parent); // 회전하여 현재 노드를 부모 노드 위치로 올립니다.
                            leftChildToParent(node, node.parent); // 다시 회전하여 현재 노드를 조부모 노드 위치로 올립니다.
                        } else { // 부모 노드가 조부모 노드의 오른쪽 자식인 경우
                            rightChildToParent(parentNode, grandpaNode); // 회전하여 부모 노드를 조부모 노드 위치로 올립니다.
                            rightChildToParent(node, parentNode); // 회전하여 현재 노드를 부모 노드 위치로 올립니다.
                        }
                    }
                }
            }

            root = node; // 최종적으로 현재 노드를 루트로 설정합니다.
        }

        private void leftChildToParent(Node xNode, Node yNode) {

            if (xNode == null || yNode == null || yNode.left != xNode || xNode.parent != yNode) {
                throw new IllegalStateException(
                        "Something is not working properly while transforming the left child into parent");
            }

            if (yNode.parent != null) {
                if (yNode == yNode.parent.left) {
                    yNode.parent.left = xNode;
                } else {
                    yNode.parent.right = xNode;
                }
            }
            if (xNode.right != null) {
                xNode.right.parent = yNode;
            }

            xNode.parent = yNode.parent;
            yNode.parent = xNode;
            yNode.left = xNode.right;
            xNode.right = yNode;
        }

        private void rightChildToParent(Node xNode, Node yNode) {

            if ((xNode == null) || (yNode == null) || (yNode.right != xNode) || (xNode.parent != yNode)) {
                throw new IllegalStateException(
                        "Something is not working properly while transforming the right child into parent");
            }

            if (yNode.parent != null) {
                if (yNode == yNode.parent.left) {
                    yNode.parent.left = xNode;
                } else {
                    yNode.parent.right = xNode;
                }
            }

            if (xNode.left != null) {
                xNode.left.parent = yNode;
            }

            xNode.parent = yNode.parent;
            yNode.parent = xNode;
            yNode.right = xNode.left;
            xNode.left = yNode;
        }

        public boolean search(int data) {

            return searchNode(data) != null;
        }

        private Node searchNode(int data) {

            Node previousNode = null;
            Node rootNode = root;

            while (rootNode != null) {
                previousNode = rootNode;
                if (data > rootNode.data) {
                    rootNode = rootNode.right;
                } else if (data < rootNode.data) {
                    rootNode = rootNode.left;
                } else if (data == rootNode.data) {
                    splay(rootNode);
                    return rootNode;
                }
            }

            if (previousNode != null) {
                splay(previousNode);
                return null;
            }

            return null;
        }

        public void delete(int data) {

            Node node = searchNode(data);
            delete(node);
        }

        private void delete(Node node) {

            if (node == null) {
                return;
            }

            splay(node);

            if ((node.left != null) && (node.right != null)) {
                Node min = node.left;
                while (min.right != null) {
                    min = min.right;
                }

                min.right = node.right;
                node.right.parent = min;
                node.left.parent = null;
                root = node.left;
            } else if (node.right != null) {
                node.right.parent = null;
                root = node.right;
            } else if (node.left != null) {
                node.left.parent = null;
                root = node.left;
            } else {
                root = null;
            }

            node.parent = null;
            node.left = null;
            node.right = null;
            node = null;

            count--;
        }

        public boolean isEmpty() {
            return root == null;
        }

        public int count() {
            return count;
        }

        public void clear() {
            root = null;
            count = 0;
        }

        public enum TraversalOrder {
            PRE,
            IN,
            POST,
            LEVEL
        }

        public void print(TraversalOrder to) {

            if (isEmpty()) {
                System.out.println("empty");
                return;
            }

            switch (to) {
                // DFS
                case IN ->
                    printInOrder(root);
                case PRE ->
                    printPreOrder(root);
                case POST ->
                    printPostOrder(root);
                // BFS
                case LEVEL ->
                    printLevelOrder(root);
            }
        }

        private void printInOrder(Node node) {
            if (node != null) {
                printInOrder(node.left);
                System.out.print(" " + node.data);
                printInOrder(node.right);
            }
        }

        private void printPreOrder(Node node) {
            if (node != null) {
                System.out.print(" " + node.data);
                printPreOrder(node.left);
                printPreOrder(node.right);
            }
        }

        private void printPostOrder(Node node) {
            if (node != null) {
                printPostOrder(node.left);
                printPostOrder(node.right);
                System.out.print(" " + node.data);
            }
        }

        private void printLevelOrder(Node node) {

            Queue<Node> queue = new ArrayDeque<>();

            queue.add(node);

            while (!queue.isEmpty()) {

                Node current = queue.poll();

                System.out.print(" " + current.data);

                if (current.left != null) {
                    queue.add(current.left);
                }

                if (current.right != null) {
                    queue.add(current.right);
                }
            }
        }
    }

    static class Car implements Vehicle {

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

    static class Submersible implements Vehicle {

        private final String type;
        private final double maxdepth;

        public Submersible(String type, double maxdepth) {
            this.type = type;
            this.maxdepth = maxdepth;
        }

        public String getType() {
            return type;
        }

        public double getMaxdepth() {
            return maxdepth;
        }

        @Override
        public int hashCode() {
            int hash = 7;
            hash = 43 * hash + Objects.hashCode(this.type);
            hash = 43 * hash
                    + (int) (Double.doubleToLongBits(this.maxdepth) ^ (Double.doubleToLongBits(this.maxdepth) >>> 32));
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
            final Submersible other = (Submersible) obj;
            if (Double.doubleToLongBits(this.maxdepth) != Double.doubleToLongBits(other.maxdepth)) {
                return false;
            }
            return Objects.equals(this.type, other.type);
        }

        @Override
        public String toString() {
            return "Submersible{" + "type=" + type + ", maxdepth=" + maxdepth + '}';
        }
    }

    public interface Vehicle {
    }
}

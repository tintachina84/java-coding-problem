package com.javacoding.app.Chapter09_Functional_Style_Programming;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class P185_MapMulti2 {

    public static void main(String[] args) {
        Book b1 = new Book("Book1", LocalDate.of(2000, 3, 12));
        Book b2 = new Book("Book2", LocalDate.of(2002, 3, 11));
        Book b3 = new Book("Book3", LocalDate.of(2004, 11, 24));
        Book b4 = new Book("Book4", LocalDate.of(2002, 6, 10));
        Book b5 = new Book("Book5", LocalDate.of(2009, 5, 7));
        Book b6 = new Book("Book6", LocalDate.of(2007, 2, 17));
        Book b7 = new Book("Book7", LocalDate.of(1995, 10, 27));
        Book b8 = new Book("Book8", LocalDate.of(2001, 10, 17));
        Book b9 = new Book("Book9", LocalDate.of(2004, 8, 10));
        Book b10 = new Book("Book10", LocalDate.of(2008, 1, 4));

        Author a1 = new Author("Joana Nimar", List.of(b1, b2, b3));
        Author a2 = new Author("Olivia Goy", List.of(b4, b5));
        Author a3 = new Author("Marcel Joel", List.of(b6));
        Author a4 = new Author("Alexender Tohn", List.of(b7, b8, b9, b10));

        List<Author> authors = new ArrayList<>();
        authors.add(a1);
        authors.add(a2);
        authors.add(a3);
        authors.add(a4);

        // 함수형 프로그래밍에서 일대다 모델을 플랫 Bookshelf 모델에 매핑하는 flatMap()을 사용하는 고전적인 시나리오
        // flatMap()의 문제점은 각 작성자에 대해 새로운 중간 스트림을 생성해야 하며(작성자가 많은 경우 성능 저하가 될 수 있음)
        // 그 후에만 map() 작업을 적용할 수 있다는 것
        List<Bookshelf> bookshelfClassic = authors.stream()
                .flatMap(
                        author -> author.getBooks()
                                .stream()
                                .map(book -> new Bookshelf(author.getName(), book.getTitle())))
                .collect(Collectors.toList());

        System.out.println(bookshelfClassic);
        System.out.println();

        // mapMulti()를 사용하면 이러한 중간 스트림이 필요하지 않으며 매핑이 간단
        List<Bookshelf> bookshelfMM = authors.stream()
                .<Bookshelf>mapMulti((author, consumer) -> {
                    for (Book book : author.getBooks()) {
                        consumer.accept(new Bookshelf(author.getName(), book.getTitle()));
                    }
                })
                .collect(Collectors.toList());

        System.out.println(bookshelfMM);
        System.out.println();

        // mapMulti()는 각 스트림 요소를 작은(아마도 0) 수의 요소로 대체할 때 유용하다.
        List<Bookshelf> bookshelfGt2005Classic = authors.stream()
                .flatMap(
                        author -> author.getBooks()
                                .stream()
                                .filter(book -> book.getPublished().getYear() > 2005)
                                .map(book -> new Bookshelf(author.getName(), book.getTitle())))
                .collect(Collectors.toList());

        System.out.println();
        System.out.println(bookshelfGt2005Classic);

        // 아래 예가 flatMap()을 사용하는 것보다 낫다. 중간 작업 수를 줄이고(더 이상 filter() 호출 없음)
        // 중간 스트림을 방지하기 때문이다. 읽기에도 더 쉽다.
        List<Bookshelf> bookshelfGt2005MM1 = authors.stream()
                .<Bookshelf>mapMulti((author, consumer) -> {
                    for (Book book : author.getBooks()) {
                        if (book.getPublished().getYear() > 2005) {
                            consumer.accept(new Bookshelf(author.getName(), book.getTitle()));
                        }
                    }
                })
                .collect(Collectors.toList());

        System.out.println();
        System.out.println(bookshelfGt2005MM1);

        // mapMulti()는 결과 요소를 스트림 형태로 반환하는 것보다 명령형 접근 방식을 사용하는 것이 더 쉬울 때 유용하다.
        List<Bookshelf> bookshelfGt2005MM2 = authors.stream()
                .<Bookshelf>mapMulti(Author::bookshelfGt2005)
                .collect(Collectors.toList());

        System.out.println();
        System.out.println(bookshelfGt2005MM2);
    }
}

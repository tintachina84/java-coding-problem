package com.javacoding.app.Chapter09_Functional_Style_Programming;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import static java.util.Map.entry;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.mapping;
import static java.util.stream.Collectors.toList;

import java.util.Collection;

// 매핑을 위한 스트리밍 커스텀 코드
public class P186_StreamMapCustomMap {

    // 이 목록에서 각 태그(키)에 대해 게시물 목록(값)을 포함하는 Map<String, List<Integer>>를 추출하는 것이 목표
    public static void main(String[] args) {

        List<Post> posts = List.of(
                new Post(1, "Running jOOQ", "#database #sql #rdbms"),
                new Post(2, "I/O files in Java", "#io #storage #rdbms"),
                new Post(3, "Hibernate Course", "#jpa #database #rdbms"),
                new Post(4, "Hooking Java Sockets", "#io #network"),
                new Post(5, "Analysing JDBC transactions", "#jdbc #rdbms"));

        // 함수형 프로그래밍에서 이 작업을 수행하는 것은 flatMap() 및 groupingBy()를 통해 수행할 수 있다. 간단히 말해서
        // flatMap()은 중첩된 Stream<Stream<R>> 모델을 평면화하는 데 유용한 반면, groupingBy()는 일부 논리 또는
        // 속성을 기준으로 맵의 데이터를 그룹화하는 데 유용한 수집기다.
        // 각 Post에 대해 allTags()를 통해 List<String>을 중첩하는 List<Post>가 있기 때문에 flatMap()이
        // 필요하다(따라서 단순히 stream()을 호출하면 Stream<Stream<R>>을 반환). 병합한 후 각 태그를
        // Map.Entry<String, Integer>로 래핑한다. 마지막으로 이러한 항목을 태그별로 다음과 같이 맵으로 그룹화한다.
        Map<String, List<Integer>> result = posts.stream()
                .flatMap(post -> Post.allTags(post).stream()
                        .map(t -> entry(t, post.getId())))
                .collect(groupingBy(Entry::getKey,
                        mapping(Entry::getValue, toList())));

        System.out.println(result);

        // mapMulti()를 사용한 버전
        // 위의 코드에서 중간 작업, 스트림으로 쓰인 map()을 사용하는 과정을 아낄 수 있었다.
        Map<String, List<Integer>> resultMulti = posts
                .stream().<Map.Entry<String, Integer>>mapMulti((post, consumer) -> {
                    for (String tag : Post.allTags(post)) {
                        consumer.accept(entry(tag, post.getId()));
                    }
                })
                .collect(groupingBy(Entry::getKey,
                        mapping(Entry::getValue, toList())));

        System.out.println(resultMulti);
    }
}

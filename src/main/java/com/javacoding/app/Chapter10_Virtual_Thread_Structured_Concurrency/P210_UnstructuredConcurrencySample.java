package com.javacoding.app.Chapter10_Virtual_Thread_Structured_Concurrency;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

// 구조화 되지 않은 스레드 예시
public class P210_UnstructuredConcurrencySample {

    private static final Logger logger = Logger.getLogger(P210_UnstructuredConcurrencySample.class.getName());
    private static final ExecutorService executor = Executors.newFixedThreadPool(2);

    public static void main(String[] args) throws InterruptedException, ExecutionException {

        System.setProperty("java.util.logging.SimpleFormatter.format",
                "[%1$tT] [%4$-7s] %5$s %n");

        buildTestingTeam();
    }

    public static TestingTeam buildTestingTeam() throws InterruptedException {

        // STOP 1: buildTestingTeam()을 실행하는 스레드가 중단되면 어떻게 다음 스레드를 쉽게 중단할 수 있나?
        Future<String> future1 = futureTester(1);
        Future<String> future2 = futureTester(2); // Integer.MAX_VALUE
        Future<String> future3 = futureTester(3);

        try {
            // STOP 2: 세번의 get() 호출, 현재 스레드는 다른 스레드가 완료될 때까지 대기. 그 스레드를 쉽게 관찰할 수 있나?
            String tester1 = future1.get();
            String tester2 = future2.get();
            String tester3 = future3.get();

            logger.info(tester1);
            logger.info(tester2);
            logger.info(tester3);

            return new TestingTeam(tester1, tester2, tester3);

        } catch (ExecutionException ex) {
            // STOP 3: ExecutionException이 발생하면 세 개의 Future 인스턴스 중 하나가 실패했음을 알 수 있지만,
            // 나머지 두 개는 쉽게 취소할 수 있나? 아니면 그대로 유지되나? 이로 인해 예상 결과의 심각한 불일치, 메모리 누수 등이 발생
            throw new RuntimeException(ex);
        } finally {
            // STOP 4: 여기가 이 일을 하기에 적합한 장소인가? 이것이 스레드 풀을 쉽게 종료할 수 있는 방법인가?
            shutdownExecutor(executor);
        }
    }

    // STOP 5: 이전 코드 줄을 발견하지 못했다면 이 실행 프로그램이 어떻게/어디에서 종료되었는지 자문해 보는 것이 타당
    public static Future<String> futureTester(int id) {

        return executor.submit(() -> fetchTester(id));
    }

    public static String fetchTester(int id) throws IOException, InterruptedException {

        HttpClient client = HttpClient.newHttpClient();

        HttpRequest requestGet = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create("https://reqres.in/api/users/" + id))
                .build();

        HttpResponse<String> responseGet = client.send(
                requestGet, HttpResponse.BodyHandlers.ofString());

        if (responseGet.statusCode() == 200) {
            return responseGet.body();
        }

        throw new UserNotFoundException("Code: " + responseGet.statusCode());
    }

    private static boolean shutdownExecutor(ExecutorService executor) {
        executor.shutdown();
        try {
            if (!executor.awaitTermination(1000, TimeUnit.MILLISECONDS)) {
                executor.shutdownNow();

                return executor.awaitTermination(1000, TimeUnit.MILLISECONDS);
            }

            return true;
        } catch (InterruptedException ex) {
            executor.shutdownNow();
            Thread.currentThread().interrupt();
            logger.severe(() -> "Exception: " + ex);
        }
        return false;
    }

    static class UserNotFoundException extends RuntimeException {

        public UserNotFoundException(String message) {
            super(message);
        }
    }

    public record TestingTeam(String... testers) {
    }
}

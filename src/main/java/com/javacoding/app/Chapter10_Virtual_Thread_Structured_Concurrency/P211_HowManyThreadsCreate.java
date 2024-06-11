package com.javacoding.app.Chapter10_Virtual_Thread_Structured_Concurrency;

import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.LockSupport;

public class P211_HowManyThreadsCreate {

    // 버추얼 스레드 소개
    public static void main(String[] args) {
        // 플랫폼 스레드 생성
        // 주의, 이 코드는 단순히 멈출 수도 있거나 에러를 발생시킬 수 있다:
        // OutOfMemoryError
        /*
         * AtomicLong counterOSThreads = new AtomicLong();
         * while (true) {
         * Thread.ofPlatform().start(() -> {
         * long currentOSThreadNr = counterOSThreads.incrementAndGet();
         * System.out.println("Thread: " + currentOSThreadNr);
         * LockSupport.park();
         * });
         * }
         */

        /*
         * AtomicLong counterOSThreads = new AtomicLong();
         * while (true) {
         * new Thread(() -> {
         * long currentOSThreadNr = counterOSThreads.incrementAndGet();
         * System.out.println("Thread: " + currentOSThreadNr);
         * LockSupport.park();
         * }).start();
         * }
         */

        // 버추얼 스레드 생성
        // 주의, 이 코드는 긴 시간 동안 실행될 수 있다(1000만개 이상의 버추얼 스레드)
        AtomicLong counterOSThreads = new AtomicLong();
        while (true) {
            Thread.startVirtualThread(() -> {
                long currentOSThreadNr = counterOSThreads.incrementAndGet();
                System.out.println("Virtual thread: " + currentOSThreadNr);
                LockSupport.park();
            });
        }

    }
}

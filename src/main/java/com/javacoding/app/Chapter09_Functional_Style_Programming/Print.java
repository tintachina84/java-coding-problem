package com.javacoding.app.Chapter09_Functional_Style_Programming;

public class Print {

    public void start() {

        // static 메소드 참조의 경우 동작의 차이가 없이 생성자를 호출하지 않고 동일하게 동작.
        // Runnable p1 = Printer::printNoReset;
        // Runnable p2 = () -> Printer.printNoReset();

        // run() 메소드를 호출하지 않더라도 Printer 생성자 즉시 호출.
        // p1.run()을 호출하더라도 Printer 생성자 호출하지 않음.
        Runnable p1 = new Printer()::printReset;
        // p2는 lambda이기 때문에 lazy 호출, run() 메서드를 호출할 때까지 Printer 생성자 호출하지 않음.
        // p2.run()을 호출하면 매번 Printer 생성자 호출.
        Runnable p2 = () -> new Printer().printReset();

        System.out.println("p1:");
        p1.run();
        System.out.println("p1:");
        p1.run();
        System.out.println("p2:");
        p2.run();
        System.out.println("p2:");
        p2.run();
        System.out.println("p1:");
        p1.run();
        System.out.println("p2:");
        p2.run();
    }
}

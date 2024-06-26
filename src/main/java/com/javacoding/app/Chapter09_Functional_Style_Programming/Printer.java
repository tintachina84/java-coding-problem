package com.javacoding.app.Chapter09_Functional_Style_Programming;

public class Printer {

    Printer() {
        System.out.println("Reset printer ...");
    }

    public static void printNoReset() {
        System.out.println("Printing (no reset) ..." + Printer.class.hashCode());
    }

    public void printReset() {
        System.out.println("Printing (with reset) ..." + Printer.class.hashCode());
    }
}

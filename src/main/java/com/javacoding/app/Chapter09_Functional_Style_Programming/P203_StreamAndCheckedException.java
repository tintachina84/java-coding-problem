package com.javacoding.app.Chapter09_Functional_Style_Programming;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.util.List;

public class P203_StreamAndCheckedException {

    static void readFile(Path path) throws IOException {

        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(new FileInputStream(path.toFile())))) {
            String line;
            while ((line = br.readLine()) != null) {
                System.out.println(line);
            }
        }
    }

    static void readFiles(List<Path> paths) throws IOException {

        paths.forEach(p -> {
            try {
                readFile(p);
            } catch (IOException e) {
                // throw new RuntimeException(e);
                // Exceptions.throwChecked(e);
                // IOException과 같은 Checked Exception은 던질 수 없다. 컴파일 되지 않음.
                // 다음과 같이 컴파일러에 대한 확인된 예외를 숨기면 Checked Exceptino 던지기 가능.
                Exceptions.throwChecked(new IOException("Some files are corrupted", e));
            }
        });
    }

    public static void main(String[] args) {

        List<Path> paths = List.of(
                Path.of("/learning/packt/JavaModernChallengeFE.pdf"),
                Path.of("/learning/packt/JavaModernChallengeSE.pdf"),
                Path.of("/learning/packt/jOOQmasterclass.pdf"));

        try {
            readFiles(paths);
        } catch (IOException e) {
            System.out.println(e + " \n " + e.getCause());
        }
    }

    public final class Exceptions {

        private Exceptions() {
            throw new AssertionError("Cannot be instantiated");
        }

        public static void throwChecked(Throwable t) {
            Exceptions.<RuntimeException>throwIt(t);
        }

        @SuppressWarnings({ "unchecked" })
        private static <X extends Throwable> void throwIt(Throwable t) throws X {
            throw (X) t;
        }
    }

}

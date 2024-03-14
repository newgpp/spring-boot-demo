package com.felix.config;

import org.junit.Test;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class LineReadWriteTest {

    @Test
    public void read_line_should_success() {
        //given
        InputStream inputStream = LineReadWriteTest.class.getClassLoader().getResourceAsStream("ids.csv");

        //when
        List<String> lines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
                lines.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        //then
        String collect = String.join(",", lines);
        System.out.println(collect.getBytes(StandardCharsets.UTF_8).length);
    }

    @Test
    public void write_line_should_success() throws Exception {
        //given
        try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("out.csv")))) {
            out.println("aaa1123132");
            out.println("1123132");
            out.println("1123132");
            out.println("1123132");
            out.println("1123132");
            out.println("1123132");
            out.flush();
        }
    }
}

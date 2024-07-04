package com.felix;

import org.apache.commons.io.IOUtils;
import org.apache.commons.io.LineIterator;
import org.junit.Test;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class LineReadWriteTest {

    /**
     * 一次读取所有行
     */
    @Test
    public void read_lines_all() {
        //given
        InputStream inputStream = LineReadWriteTest.class.getClassLoader().getResourceAsStream("sample_lines.txt");
        //when
        List<String> lines = IOUtils.readLines(inputStream, StandardCharsets.UTF_8);
        //then
        for (String line : lines) {
            System.out.println(line);
        }
        IOUtils.closeQuietly(inputStream);
    }

    /**
     * 一次输出所有行
     */
    @Test
    public void write_lines_all() {
        //given
        List<Integer> lines = Stream.iterate(1, x -> x + 1).limit(100).collect(Collectors.toList());
        try (FileOutputStream fileOutputStream = new FileOutputStream("out.text")) {
            //then
            IOUtils.writeLines(lines, null, fileOutputStream, StandardCharsets.UTF_8);
            IOUtils.closeQuietly(fileOutputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 逐行读取文本
     */
    @Test
    public void read_lines_iterator() {
        //given
        InputStream inputStream = LineReadWriteTest.class.getClassLoader().getResourceAsStream("sample_lines.txt");
        //when
        LineIterator iterator = IOUtils.lineIterator(inputStream, StandardCharsets.UTF_8);
        //then
        while (iterator.hasNext()) {
            String s = iterator.nextLine();
            System.out.println(s);
        }
        IOUtils.closeQuietly(inputStream);
    }

    /**
     * 逐行输出文本
     */
    @Test
    public void write_line_iterator() {
        //given
        List<Integer> lines = Stream.iterate(1, x -> x + 1).limit(100).collect(Collectors.toList());
        try (FileOutputStream fileOutputStream = new FileOutputStream("out.text")) {
            for (Integer line : lines) {
                String str = line.toString();
                IOUtils.write(str + "\n", fileOutputStream, StandardCharsets.UTF_8);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 读取整个文本
     */
    @Test
    public void read_text_all() {
        //given
        StringWriter writer = new StringWriter();
        try (InputStream inputStream = LineReadWriteTest.class.getClassLoader().getResourceAsStream("sample_json.json")) {
            //when
            IOUtils.copy(inputStream, writer, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        //then
        String text = writer.toString();
        System.out.println(text);
    }

    /**
     * 输出整个文本
     */
    @Test
    public void write_text_all() {
        //given
        String str = "{\n" +
                "  \"barId\": 1804814949930646612,\n" +
                "  \"berthSize\": 5,\n" +
                "  \"equipmentId\": 1804814949664808452,\n" +
                "  \"berthCodeStr\": \"07079047、07079041、07079043、07079045、07079049\",\n" +
                "  \"equipmentCode\": \"HW012210201533010208\",\n" +
                "  \"equipmentName\": \"兰花路（永成路）10208\",\n" +
                "  \"equipmentType\": 7,\n" +
                "  \"equipmentState\": 1\n" +
                "}";
        try (FileOutputStream fileOutputStream = new FileOutputStream("out.json")) {
            //then
            IOUtils.write(str, fileOutputStream, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


}

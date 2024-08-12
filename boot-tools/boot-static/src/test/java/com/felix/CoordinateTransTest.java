package com.felix;

import com.felix.utils.Coordtransform;
import org.apache.commons.io.IOUtils;
import org.junit.Test;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class CoordinateTransTest {

    @Test
    public void test_t(){
        //given
        double lon = 113.232975d;
        double lat = 23.396378d;
        //when
        Double[] doubles = Coordtransform.WGS84ToGCJ02(lon, lat);
        //then
        System.out.println(doubles[0] + "," + doubles[1]);
    }

    @Test
    public void csv_84_to_102(){
        //given
        InputStream inputStream = CoordinateTransTest.class.getClassLoader().getResourceAsStream("lizhiji.csv");
        //when
        List<String> lines = IOUtils.readLines(inputStream, StandardCharsets.UTF_8);
        //then
        List<String> coordinates = new ArrayList<>();
        for (String line : lines) {
            String[] arr = line.split(",");
            String a = arr[0].replaceAll("左侧", "").replaceAll("\\.", "");
            String b = arr[1].substring(0, 9);
            String c = arr[2].substring(0, 10);
            coordinates.add(a + "," + b + "," + c);
        }

        try (FileOutputStream fileOutputStream = new FileOutputStream("lizhiji-out.csv")) {
            //then
            IOUtils.writeLines(coordinates, null, fileOutputStream, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        IOUtils.closeQuietly(inputStream);
    }


}

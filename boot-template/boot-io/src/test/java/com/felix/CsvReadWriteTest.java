package com.felix;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.junit.Test;

import java.io.*;
import java.util.List;

public class CsvReadWriteTest {

    /**
     * 读取整个CSV
     */
    @Test
    public void read_csv_all() {
        //given
        InputStream inputStream = CsvReadWriteTest.class.getClassLoader().getResourceAsStream("sample_csv.csv");
        CSVFormat format = CSVFormat.DEFAULT.builder()
                .setHeader("parkRecordId", "entryTime", "exitTime", "should", "money")
                .setDelimiter(",")
                .setSkipHeaderRecord(true)
                .build();
        try {
            //when
            CSVParser parser = new CSVParser(new InputStreamReader(inputStream), format);
            //then
            List<CSVRecord> records = parser.getRecords();
            for (CSVRecord record : records) {
                String parkRecordId = record.get("parkRecordId");
                String entryTime = record.get("entryTime");
                String exitTime = record.get("exitTime");
                String should = record.get("should");
                String money = record.get("money");
                System.out.println(parkRecordId + "---" + entryTime + "---" + exitTime + "---" + should + "---" + money);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 生成csv
     */
    @Test
    public void write_csv_all() {
        //given
        CSVFormat format = CSVFormat.DEFAULT.builder()
                .setHeader("parkRecordId", "entryTime", "exitTime", "should", "money")
                .setDelimiter(",")
                .build();
        try (FileOutputStream fileOutputStream = new FileOutputStream("out.csv");
             OutputStreamWriter writer = new OutputStreamWriter(fileOutputStream)) {
            //when
            CSVPrinter csvPrinter = new CSVPrinter(writer, format);
            //then
            csvPrinter.printRecord("1811678351384497510", "2017-09-29 16:59:53", "2017-09-29 17:20:02", "2.0", "0.0");
            csvPrinter.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }


}

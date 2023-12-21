package com.felix.mysql;

import com.alibaba.fastjson2.JSON;
import com.felix.utils.IdUtils;
import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Stream;


public class DataInitTest {

    private static final String jdbcUrl = "jdbc:mysql://192.168.159.111:3306/oper_db?useUnicode=true&characterEncoding=UTF-8&autoReconnect=true&serverTimezone=Asia/Shanghai";
    private static final String driverClass = "com.mysql.cj.jdbc.Driver";
    private static final String userName = "root";
    private static final String password = "123456";

    private static QueryRunner queryRunner;

    @Before
    public void init() {
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setUrl(jdbcUrl);
        dataSource.setDriverClassName(driverClass);
        dataSource.setUsername(userName);
        dataSource.setPassword(password);
        queryRunner = new QueryRunner(dataSource);
    }

    @Test
    public void query_oper_log_should_success() {
        //given
        String sql = "select * from sys_oper_log where id in (1, 2)";
        //when
        try {
            List<Map<String, Object>> list = queryRunner.query(sql, new MapListHandler());
            //then
            for (Map<String, Object> line : list) {
                System.out.println(JSON.toJSONString(line));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void insert_oper_log_should_success() {
        //given
        String sql = "insert into sys_oper_log(oper_id, title, business_type, method, request_method, operator_type, oper_name, dept_name, oper_url, oper_ip, oper_location, oper_param, status, error_msg, oper_time) values" +
                "(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        Object[] titles = {"用户", "订单", "物流", "支付"};
        Object[] business_types = {0, 1, 2, 3};
        Object[] methods = {"查询用户信息", "取消订单", "退货", "查询物流"};
        Object[] request_methods = {"GET", "POST", "PUT"};
        Object[] operator_types = {0, 1, 2};
        Object[] oper_names = {"小红", "铁公鸡", "小甜甜", "旺财", "花花", "轮胎", "小灰机"};
        Object[] dept_names = {"客服部", "物流部", "开发部", "商务部", "文旅部"};
        Object[] oper_urls = {"/v1/user", "/v1/order", "/v1/pay", "/v1/express"};
        Object[] oper_ips = {"192.168.0.1", "172.16.0.1", "192.168.255.255", "172.31.255.255", "10.0.0.1", "10.255.255.255"};
        Object[] oper_locations = {"河北省", "中国香港", "上海市", "陕西省", "云南省", "浙江省", "四川省", "安徽省"};
        Object[] oper_params = {"page=0&pageSize=0&companyName=&bearSide=", "userId=1&msgType=0&page=1&pageSize=10&startTime=2023-12-19%2013%3A53%3A11&t=1702965187202",
                "[1,2]", "{\"name\":\"felix\", \"orderId\":123}", "", "{}", "userId=1&unread=0", "userId=1&carId=&page=1&pageSize=15&startTime=2023-12-19%2013%3A53%3A11&t=1702965187202"};
        Object[] statuss = {0, 1};
        Object[] error_msgs = {"", null, "系统错误", "Error:NullPointerException", "success"};
        //when
        Stream.iterate(0, x -> x + 1).limit(5).forEach(x -> {
            try {
                Object[] params = new Object[]{IdUtils.nextSnowflakeId(), randomGet(titles), randomGet(business_types), randomGet(methods), randomGet(request_methods), randomGet(operator_types)
                        , randomGet(oper_names), randomGet(dept_names), randomGet(oper_urls), randomGet(oper_ips), randomGet(oper_locations), randomGet(oper_params), randomGet(statuss)
                        , randomGet(error_msgs), dateRandom("2023-12-01 00:00:00", "2023-12-21 00:00:00")};
                Object insert = queryRunner.insert(sql, new ScalarHandler<>(), params);
                System.out.println(insert);
                //then
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    @Test
    public void insert_t_order_should_success() {
        //given
        String sql = "insert into t_order (order_no, customer_id, order_time, order_status, order_amount, goods_id) values "
                + "(?, ?, ?, ?, ?, ?)";

        Object[] order_statuses = {0, 1, 2, 3};
        Object[] goods_ids = Stream.iterate(0, x -> x + 1).limit(100).toArray();
        Object[] customer_ids = Stream.iterate(1000, x -> x + 1).limit(10).toArray();

        //when
        Stream.iterate(0, x -> x + 1).limit(5).forEach(x -> {
            try {
                long order_no = IdUtils.nextSnowflakeId();
                Object customer_id = randomGet(customer_ids);
                String order_time = dateRandom("2023-12-01 00:00:00", "2023-12-21 00:00:00");
                Object order_status = randomGet(order_statuses);
                String order_amount = moneyRandom("10", "500");
                Object goods_id = randomGet(goods_ids);
                Object[] params = new Object[]{order_no, customer_id, order_time, order_status, order_amount, goods_id};
                Object insert = queryRunner.insert(sql, new ScalarHandler<>(), params);
                System.out.println(insert);
                //then
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    private Object randomGet(Object[] arr) {
        int i = new Random().nextInt(Integer.MAX_VALUE);
        int index = i % arr.length;
        return arr[index];
    }

    private Long rangeRandom(long min, long max) {
        long bound = max - min;
        int i = new Random().nextInt((int) bound);
        return min + i;
    }

    private String dateRandom(String start, String end) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long min = 0;
        long max = 0;
        try {
            min = format.parse(start).getTime();
            max = format.parse(end).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Long l = rangeRandom(min, max);
        Date date = new Date(l);
        return format.format(date);
    }


    private String moneyRandom(String min, String max) {
        String lMin = new BigDecimal(min).multiply(new BigDecimal(100)).setScale(0, RoundingMode.HALF_UP).toPlainString();
        String lMax = new BigDecimal(max).multiply(new BigDecimal(100)).setScale(0, RoundingMode.HALF_UP).toPlainString();
        Long num = rangeRandom(Long.parseLong(lMin), Long.parseLong(lMax));
        return new BigDecimal(num).divide(new BigDecimal(100), 2, RoundingMode.HALF_UP).toPlainString();
    }
}

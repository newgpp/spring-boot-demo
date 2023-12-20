package com.felix.mysql;

import com.alibaba.fastjson2.JSON;
import com.felix.utils.IdUtils;
import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import org.junit.Before;
import org.junit.Test;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Stream;

/**
 * @author felix
 * <p>
 * CREATE DATABASE IF NOT EXISTS `oper_db`;
 * <p>
 * CREATE TABLE IF NOT EXISTS `sys_oper_log` (
 * `id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
 * `oper_id` BIGINT(20) NULL COMMENT '日志主键',
 * `title` VARCHAR(50) NULL COMMENT '模块标题',
 * `business_type` INT(2) NULL DEFAULT '0' COMMENT '业务类型（0其它 1新增 2修改 3删除）',
 * `method` VARCHAR(100) NULL COMMENT '方法名称',
 * `request_method` VARCHAR(10) NULL COMMENT '请求方式',
 * `operator_type` INT(1) NULL DEFAULT '0' COMMENT '操作类别（0其它 1后台用户 2手机端用户）',
 * `oper_name` VARCHAR(50) NULL COMMENT '操作人员',
 * `dept_name` VARCHAR(50) NULL COMMENT '部门名称',
 * `oper_url` VARCHAR(255) NULL COMMENT '请求URL',
 * `oper_ip` VARCHAR(50) NULL COMMENT '主机地址',
 * `oper_location` VARCHAR(255) NULL COMMENT '操作地点',
 * `oper_param` VARCHAR(2000) NULL COMMENT '请求参数',
 * `status` INT(1) NULL DEFAULT '0' COMMENT '操作状态（0正常 1异常）',
 * `error_msg` VARCHAR(2000) NULL COMMENT '错误消息',
 * `oper_time` DATETIME NULL COMMENT '操作时间',
 * `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
 * PRIMARY KEY (`id`)
 * ) COMMENT '操作日志记录';
 */
public class DbUtilsTest {

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

    private Object randomGet(Object[] arr) {
        int i = new Random().nextInt(Integer.MAX_VALUE);
        int index = i % arr.length;
        return arr[index];
    }

    @Test
    public void insert_data_should_success() {
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
        Object[] oper_times = {"2023-12-19 00:10:09", "2023-09-20 17:59:58", "2023-10-19 00:10:09", "2023-03-19 12:05:09", "2023-04-20 14:19:23", "2023-07-10 18:09:46"};
        //when
        Stream.iterate(0, x -> x + 1).limit(10).forEach(x -> {
            try {
                Object[] params = new Object[]{IdUtils.nextSnowflakeId(), randomGet(titles), randomGet(business_types), randomGet(methods), randomGet(request_methods), randomGet(operator_types)
                        , randomGet(oper_names), randomGet(dept_names), randomGet(oper_urls), randomGet(oper_ips), randomGet(oper_locations), randomGet(oper_params), randomGet(statuss)
                        , randomGet(error_msgs), randomGet(oper_times)};
                Object insert = queryRunner.insert(sql, new ScalarHandler<>(), params);
                System.out.println(insert);
                //then
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    @Test
    public void query_list_should_success() {
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

}

package com.game.util;

import org.junit.Test;

import java.sql.Connection;

import static org.junit.Assert.*;

public class MyConnectionPoolTest {

    @Test
    public void testGetAndReleaseConnection() throws Exception {
        MyConnectionPool pool = new MyConnectionPool(
                "com.mysql.cj.jdbc.Driver",
                "jdbc:mysql://localhost:3306/chess?useSSL=false&serverTimezone=Asia/Shanghai&allowPublicKeyRetrieval=true",
                "root",
                "123456",
                3
        );
        Connection conn = pool.getConnection();
        assertNotNull(conn);
        pool.releaseConnection(conn);
        pool.shutdown();
    }

    @Test(expected = java.sql.SQLException.class)
    public void testMaxConnectionLimit() throws Exception {
        MyConnectionPool pool = new MyConnectionPool(
                "com.mysql.cj.jdbc.Driver",
                "jdbc:mysql://localhost:3306/chess?useSSL=false&serverTimezone=Asia/Shanghai&allowPublicKeyRetrieval=true",
                "root",
                "123456",
                1
        );
        Connection c1 = pool.getConnection();
        // 第二次获取会阻塞或抛异常（超时）
        pool.getConnection();
    }
}

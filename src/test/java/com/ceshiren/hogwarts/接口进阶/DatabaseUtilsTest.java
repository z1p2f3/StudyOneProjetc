package com.ceshiren.hogwarts.接口进阶;

import com.ceshiren.hogwarts.DatabaseUtils;
import org.junit.jupiter.api.Test;


import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DatabaseUtilsTest {

    static final String DB_URL = "jdbc:mysql://192.168.10.108:44284/test?useSSL=false";
    static final String USER = "root";
    static final String PASS = "root";
    static final String QUERY = "select * from case_user where overdue_days > 1";

    @Test

    void getResultSetBySQL() throws SQLException {
        // 初始化数据库工具类
        DatabaseUtils databaseUtils = new DatabaseUtils(DB_URL, USER, PASS);
        String res_account_number = databaseUtils.getResultSetBySQL(QUERY,"account_number");
        String res_overdue_days = databaseUtils.getResultSetBySQL(QUERY,"overdue_days");

        assertEquals("9876543210",res_account_number);
        assertEquals("9",res_overdue_days);
    }
}
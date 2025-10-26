package com.ceshiren.hogwarts;

import java.sql.*;

public class DatabaseUtils {
    Statement statement;
    Connection conn;

    public DatabaseUtils(String DB_URL, String USER, String PASS) {
        try {
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            statement = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public String getResultSetBySQL(String sql,String key) {
        // 格式转换将ResultSet 转换为 标准的json格式，方便完成断言。
        try {
            ResultSet resultSet = statement.executeQuery(sql);
            resultSet.first();

            return resultSet.getString(key);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


}
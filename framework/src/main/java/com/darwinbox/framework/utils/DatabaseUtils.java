package com.darwinbox.framework.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class DatabaseUtils {

    Connection conn;

    public void loadDriver(String name) throws Exception  {
        Class.forName(name);
    }

    public Connection getConnection(String url, String username, String password) throws Exception  {
        conn = DriverManager.getConnection(url,username ,password );
        return conn;
    }

    public int executeUpdate(String query ) throws Exception {

        Statement stmt = conn.createStatement();

        int n = stmt.executeUpdate(query);

        return n;

    }

    public ResultSet executeQuery(String query) throws Exception {

        Statement stmt = conn.createStatement();

        ResultSet rs = stmt.executeQuery(query);

        return rs;
    }
}

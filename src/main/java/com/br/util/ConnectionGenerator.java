package com.br.util;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConnectionGenerator {


    public static Connection gen(DataSource ds) throws SQLException {
        return ds.getConnection();
    }

    public static Connection gen(String conStr,String usr,String password) throws SQLException {

        Connection conn = null;
        Properties connectionProps = new Properties();
        connectionProps.put("user", usr);
        connectionProps.put("password",password);

        conn = DriverManager.getConnection(
                conStr,
                connectionProps);
        return conn;
    }
}

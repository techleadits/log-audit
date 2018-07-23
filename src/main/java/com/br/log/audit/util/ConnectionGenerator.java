package com.br.log.audit.util;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConnectionGenerator {

    private static  Properties connectionProps=null ;
    private static  String conStr=null ;
    private static DataSource ds=null;
    public static Connection gen(DataSource ds) throws SQLException {
        ConnectionGenerator.ds=ds;
        return ds.getConnection();
    }

    public static Connection gen(String conStr,String usr,String password) throws SQLException {

        connectionProps = new Properties();
        connectionProps.put("user", usr);
        connectionProps.put("password",password);

        ConnectionGenerator.conStr=conStr;
        return getConnection();
    }

    public static Connection getConnection() throws SQLException{
        if(connectionProps!=null){
            return  DriverManager.getConnection(
                    conStr,
                    connectionProps);
        }else if(ds!=null){
            return ds.getConnection();
        }else{
            throw new SQLException("no connection string configured. Use ConnectionGenerator.gen(String conStr,String usr,String password) to configure a connection string for this library");
        }
    }

}

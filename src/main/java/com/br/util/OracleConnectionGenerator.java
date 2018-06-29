package com.br.util;

import oracle.jdbc.driver.OracleConnection;

import java.sql.Connection;

public class OracleConnectionGenerator extends  ConnectionGenerator {

    public static OracleConnection gen(Connection con){
        return (OracleConnection)con;
    }

}

package com.br.log.audit;

import com.br.log.audit.lib.Log;
import com.br.log.audit.lib.LogService;
import com.br.log.audit.util.ConnectionGenerator;

import javax.sql.DataSource;
import java.sql.SQLException;

public class LogFactory {
    public static void main(String args[]){

        try {
            configureConnection(args[0],args[1],args[2]);
            Log dbLogger = getLog("hello",1,"TEST.LOG.AUDIT.HELLO");

            dbLogger.log("world");
            System.out.println(dbLogger.toString());
        } catch (SQLException e) {
            System.out.println("err");
            e.printStackTrace();
        }

    }

    /**
     * configure a connection to be made via simple ConnectionDriver (no connection pool controlling).
     * @param conStr connection string
     * @param user database user
     * @param password user password
     */
    public static void configureConnection(String conStr,String user,String password) throws SQLException{
        ConnectionGenerator.gen(conStr,user,password);
    }

    /**
     * use connection with an alredy controlled connection pool.
     * @param ds am alredy ccreated data source
     */
    public static void configureConnection(DataSource ds) throws SQLException{
        ConnectionGenerator.gen(ds);
    }



    public static Log getLog(String message, int priority, String context) {
        try {
            return getLogThrowable(message,priority,context);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new Log();
    }

    public static Log getLogThrowable(String message, int priority, String context) throws SQLException {
        LogService service = new LogService();
        return service.logFactory(message,priority,context);
    }
}

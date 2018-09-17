package com.br.log.audit;

import com.br.log.audit.lib.Log;
import com.br.log.audit.lib.LogService;
import com.br.log.audit.test.TestClass;
import com.br.log.audit.util.ConnectionGenerator;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.time.LocalDateTime;

import io.reactivex.Flowable;
import io.reactivex.Observable;

public class LogFactory {
    private class Test{
        String teste="salvando";
        String teste2="classe";
    }
    public static void main(String args[]){
        Flowable.just("Hello world").subscribe(System.out::println);
        try {
            System.out.println("iniciando "+LocalDateTime.now().toString());
            configureConnection(args[0],args[1],args[2]);
            Log dbLogger = getLog("hello",1,"TEST.LOG.AUDIT.HELLO");

            dbLogger.add("string","teste");

            dbLogger.add("number",12);

            dbLogger.add("xml","<parametro>12</parametro>");
            dbLogger.add("data",LocalDateTime.now());



            dbLogger.log(new TestClass());
            dbLogger.log("testando exceção", new Exception("erro teste", new Exception("erro interno do erro teste")));


            dbLogger.log("world");

            // testa mapeamento de objetos

        } catch (SQLException e) {
            System.out.println("err");
            e.printStackTrace();
        }
        System.out.println("terminou thread principal! "+LocalDateTime.now().toString());
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

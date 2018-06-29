package com.br.lib;

import java.sql.*;
import java.time.LocalDateTime;

import com.br.util.ConnectionGenerator;

import oracle.jdbc.OracleCallableStatement;
import oracle.jdbc.OracleConnection;


public class LogRepository  {

    private OracleConnection con;
    public LogRepository(String conStr, String usr, String password) throws SQLException {
        this.con=(OracleConnection)ConnectionGenerator.gen(conStr,usr,password);
    }


    public Log log(Log log)  throws SQLException{
      
        String saida=customLogExec(log);
        log.setReference(saida);
        return log;
		
    }

    public Log parameter(Parameter parameter)  throws SQLException{
        
        String saida=customParameterExec(parameter);
        parameter.getLog().setReference(saida);
        return parameter.getLog();
		
    }

    

    private String customLogExec(Log log) throws SQLException {
        //OracleConnection connection=null;
        Clob clob=null;
   
        String block=
        "declare"+
        "    t_log clob:=?;"+
        "begin"+
        "    LOG_AUDIT.PKG_LOG_I.P_LOG(?,?,?,t_log);"+
        "    ? := t_log;"+
        "end;";
        try{
            try(OracleConnection connection=this.con){
                try ( OracleCallableStatement statement=(OracleCallableStatement)connection.prepareCall(block)){
                
                    clob = connection.createClob();
                    clob.setString(1, log.getReference());
                    statement.setClob(1, clob);
                    

                    statement.setString(2,log.getMensaem());
                    statement.setInt(3,log.getPriority());
                    statement.setString(4,log.getContext());


                    statement.registerOutParameter(5,java.sql.Types.CLOB);

                    
        
                
                    statement.execute();
        
                    String saida= statement.getString(5);
                
                    return saida;
                } catch (SQLException e) {
                    throw e;
                }finally{
        
                    if(clob!=null){
                        try {
                            clob.free();
                        
                        } catch (Exception e) {

                        }
                    }
                }
            }
        }catch (SQLException e) {

            throw e;
        }
    }
    


    private String customParameterExec(Parameter param) throws SQLException{
        //OracleConnection connection=null;
        Clob clob=null;
        Clob clobParam=null;
   
        String block=
        "declare"+
        "    t_log clob:=?;"+
        "begin"+
        "    log_audit.pkg_log_i.p_add_"+param.getDataType().toString()+"(?,?,t_log);"+
        "    ? := t_log;"+
        "end;";

        try{
            try(OracleConnection connection=this.con){
                try ( OracleCallableStatement statement=(OracleCallableStatement)connection.prepareCall(block)){
                
                    clob = connection.createClob();
                    clob.setString(1, param.getLog().getReference());
                    statement.setClob(1, clob);

                    statement.setString(2,param.getName());
                    switch(param.getDataType()){
                        case Clob:
                        case Xml:
                            clobParam = connection.createClob();
                            clobParam.setString(3, param.getValue().toString());
                            statement.setClob(3, clobParam);
                            break;
                        case Date:
                            Timestamp sqlDate = Timestamp.valueOf( (LocalDateTime)param.getValue() );
                            statement.setTimestamp(3, sqlDate );
                            break;
                        case Double:
                            statement.setDouble(3,(Double)param.getValue());
                            break;
                        case Float:
                            statement.setFloat(3,(Float)param.getValue());
                            break;
                        case Int:
                            statement.setInt(3,(Integer)param.getValue());
                            break;
                    }
                    

                    
                    statement.registerOutParameter(4,java.sql.Types.CLOB);
        
                
                    statement.execute();
        
                    String saida= statement.getString(4);
                
                    return saida;
                } catch (SQLException e) {

                    throw e;
                }finally{
        
                    if(clob!=null){
                        try {
                            clob.free();
                        } catch (Exception e) {}
                    }

                    if(clobParam!=null){
                        try {
                            clobParam.free();
                        } catch (Exception e) {}
                    }
                }
            }
        }catch (SQLException e) {

            throw e;
        }
    }
}



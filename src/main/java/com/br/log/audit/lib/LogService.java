package com.br.log.audit.lib;


import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.logging.Level;
import java.util.logging.Logger;



public class LogService {

	private static final Logger logger = Logger.getLogger(LogService.class.getName());

	public LogService() throws SQLException {
		repository= new LogRepository();
		logger.setLevel(Level.ALL);
	}
	public LogService(Connection con){
		repository= new LogRepository(con);
		logger.setLevel(Level.ALL);
	}
	private LogRepository repository;


	public Log logFactory(Integer priority, String context){
		Log log=new Log(priority,context);
		log.setService(this);
		return log;
	}

	public Log logFactory(String mensagem,Integer priority,String context){
		Log log=logFactory(priority,context);
		log(mensagem,log);
		return log;
	}


	protected void log(String mensagem,Log reference){
		try{
			Log log=new Log(mensagem,reference);
		
			Log newlog=repository.log(log);

			reference.setReference(newlog.getReference());
			reference.setMensagem(newlog.getMensaem());
		}catch(Exception e){
			logger.severe(mensagem);
			buildErrMessage(e,0);
		}
	}

	protected void logError(String mensagem,Throwable e,Log reference){
		log(mensagem,reference);
	
		StringBuilder strBuilder= new StringBuilder();
		for(int i=0; i<e.getStackTrace().length;i++){
			String formatted = String.format("%04d", i);
			strBuilder.append("<err id=\""+i+"\">"+e.getStackTrace()[i].toString()+"</err>");
		}
		add("stacktrace","<stacktrace>"+strBuilder.toString()+"</stacktrace>",reference);

		addError(reference,e,0);
	}

	private void addError(Log reference,Throwable e,int count){
		
		log(e.getClass().getName(),reference);
		add("order",count,reference);
		if(e.getMessage()!=null){
			add("message",e.getMessage(),reference);
		}
		if(e.getCause()!=null){
			if(count<20){
				addError(reference, e.getCause(),count+1);
			}else{
				log("mais de 20 exceções registradas",reference);
			}
		}
	}

	private void buildErrMessage(Throwable e,int count){

		logger.severe(e.getClass().getName());


		if(e.getMessage()!=null){
			logger.severe(e.getMessage());
		}
		if(e.getCause()!=null){
			if(count<20){
				buildErrMessage( e.getCause(),count+1);
			}else{
				logger.severe("mais de 20 exceções registradas");
			}
		}
	}
	protected void add(Parameter parameter, Log reference){
		try{
			Log newlog=repository.parameter(parameter);
			reference.setReference(newlog.getReference());
			reference.setMensagem(newlog.getMensaem());
		}catch(Exception e){
			logger.severe("add param-> "+parameter.getName());
			buildErrMessage(e,0);
		}
	}

	protected void add(String name,String value,Log reference) {
		Parameter parameter = new Parameter(name,reference);
		parameter.setValue(value);
		add(parameter,reference);
	}
	protected void add(String name,Integer value,Log reference) {
		Parameter parameter = new Parameter(name,reference);
		parameter.setValue(value);
		add(parameter,reference);
	}
	protected void add(String name,float value,Log reference) {
		Parameter parameter = new Parameter(name,reference);
		parameter.setValue(value);
		add(parameter,reference);
	}
	protected void add(String name,Double value,Log reference) {
		Parameter parameter = new Parameter(name,reference);
		parameter.setValue(value);
		add(parameter,reference);
	}

	protected void add(String name,Number value,Log reference) {
		Parameter parameter = new Parameter(name,reference);
		parameter.setValue(value);
		add(parameter,reference);
	}

	protected void add(String name,Boolean value,Log reference) {
		Parameter parameter = new Parameter(name,reference);
		parameter.setValue(value);
		add(parameter,reference);
	}

	protected void add(String name,LocalDateTime value,Log reference) {
		Parameter parameter = new Parameter(name,reference);
		parameter.setValue(value);
		add(parameter,reference);
	}
}
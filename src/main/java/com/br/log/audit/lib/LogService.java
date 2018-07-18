package com.br.log.audit.lib;


import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


@SuppressWarnings({"rawtypes"})

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
			strBuilder.append("<err id=\""+i+"\">"+toXmlString(e.getStackTrace()[i].toString())+"</err>");
		}
		add("stacktrace","<stacktrace>"+strBuilder.toString()+"</stacktrace>",reference);

		addError(reference,e,0);
	}

	private static String toXmlString(String xmlText){
		xmlText=xmlText.replaceAll("<", "&lt;");
		xmlText=xmlText.replaceAll(">", "&gt;");
		xmlText=xmlText.replaceAll("\"", "&quot;");
		xmlText=xmlText.replaceAll("'", "&apos;");
		xmlText=xmlText.replaceAll("&", "&amp;");
		return xmlText;
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


	protected void addObjectFields(Object obj, Log reference){
		ArrayList<Class> list=new ArrayList<Class>();
		list.add(obj.getClass());
        addObjectFields(obj,reference,list);
	}
	
	private void addObjectFields(Object obj, Log reference,ArrayList<Class> previousClasses){
		
        for (Field f : obj.getClass().getDeclaredFields()) {
            String property=f.getName();
            try{
                String methodName = "get" + property.substring(0, 1).toUpperCase() + property.substring(1, property.length());
                java.lang.reflect.Method method = obj.getClass().getMethod(methodName);
                Object returnValue = method.invoke(obj);
                if(returnValue!=null){
					Parameter parameter= new Parameter(property,reference);
					if(parameter.setValueUnknowType(returnValue)){
						this.add(parameter,reference);
					}else if(existsInList(previousClasses,returnValue.getClass())){
						parameter.setValue("LogService recursivity protection. Will not navigate again in this object");
						this.add(parameter,reference);
					}
					else if(returnValue.getClass().isAssignableFrom(Iterable.class)){
						for(Object item :(Iterable)returnValue){
						
							addObjectFields(item,reference,addAndClone(previousClasses,item.getClass()));
						}
					}else{
						addObjectFields(returnValue,reference,addAndClone(previousClasses,returnValue.getClass()));
					}
                }
            }
            catch(Exception e){
				reference.log("Log service error", e);
            }
		}
	}
	@SuppressWarnings("unchecked")
	private static ArrayList<Class> addAndClone(ArrayList<Class> list,Class clazz){
		ArrayList<Class> newList=(ArrayList<Class>)list.clone();
		newList.add(clazz);
		return newList;
	}
	private static Boolean existsInList(List<Class> list,Class clazz){
		return clazz!=null&& list.indexOf(clazz)>=0;
	}
}
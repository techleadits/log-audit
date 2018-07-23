package com.br.log.audit.lib;

import java.time.LocalDateTime;


public class Log{

    LogService service=null;
    public Log(){}
    protected Log(int priority,String context){
        this("",priority,context,null);
      }
      
    protected Log(String mensagem,Log reference){
      this(mensagem,reference.priority,reference.context,reference.reference);
    }
    protected Log(String mensagem,int priority,String context,String reference){
        this.mensaem=mensagem;
        this.priority=priority;
        
        if(context!=null)
            this.context=context;
        if(reference!=null)
            this.reference=reference;
    }

    @Override
    public String toString() {
        return this.reference;
    }

    //necessary to make easyer service calls
    protected void setService(LogService service) {
        this.service = service;
    }
    String mensaem;
    int priority;
    String context="DEFAULT";
    String reference="<log />";



    /**
     * @return the context
     */
    public String getContext() {
        return context;
    }
    /**
     * @return the mensaem
     */
    public String getMensaem() {
        return mensaem;
    }
    /**
     * @return the priority
     */
    public int getPriority() {
        return priority;
    }
    /**
     * @return the reference
     */
    public String getReference() {
        return reference;
    }
    /**
     * @param context the context to set
     */
    protected void setContext(String context) {
        this.context = context;
    }
    /**
     * @param mensaem the mensaem to set
     */
    protected void setMensagem(String mensaem) {
        this.mensaem = mensaem;
    }
    /**
     * @param priority the priority to set
     */
    protected void setPriority(int priority) {
        this.priority = priority;
    }
    /**
     * @param reference the reference to set
     */
    protected void setReference(String reference) {
        this.reference = reference;
    }

    protected void log(){
        service.log(this.getMensaem(),this);
   }

    public void log(String mensagem){
         this.setMensagem(mensagem);
         log();
    }
    public void log(Object obj) {
		service.log(obj,this);
    }

	public void log(String mensagem,Throwable e){
        service.logError(mensagem,e,this);
	}


    
    public void add(String name,String value) {
		service.add(name,value,this);
	} 
	public void add(String name,Integer value) {
		service.add(name,value,this);
	} 
	public void add(String name,float value) {
		service.add(name,value,this);
	} 
	public void add(String name,Double value) {
		service.add(name,value,this);
	}

	public void add(String name,Number value) {
		service.add(name,value,this);
	}

	public void add(String name,Boolean value) {
		service.add(name,value,this);
	}
	
	public void add(String name,LocalDateTime value) {
		service.add(name,value,this);
    }
   

}
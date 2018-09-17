package com.br.log.audit.lib;

import java.time.LocalDateTime;
import java.util.LinkedList;


public class Log{

    LogService service=null;
    public Log(){this(1,"DEFAULT");}
    protected Log(int priority,String context){
        this("log",priority,context,null);
      }
      
    protected Log(String mensagem,Log reference){
      this(mensagem,reference.priority,reference.context,reference.reference);
    }
    protected Log(String mensagem,int priority,String context,String reference){
        createRunner();
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
     * @param mensagem the mensaem to set
     */
    protected void setMensagem(String mensagem) {
        this.mensaem = mensagem;
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
        addOperation(new ToDo(this.getMensaem()));
   }

    public void log(String mensagem){
         this.setMensagem(mensagem);
         log();
    }
    public void log(Object obj) {
        addOperation(new ToDo(obj));
    }

	public void log(String mensagem,Throwable e){

        addOperation(  new ToDo(mensagem,e));

	}


    
    public void add(String name,String value) {
        Parameter parameter = new Parameter(name,this);
        parameter.setValue(value);
        addOperation( new ToDo(parameter));
	} 
	public void add(String name,Integer value) {
        Parameter parameter = new Parameter(name,this);
        parameter.setValue(value);
        addOperation( new ToDo(parameter));
	} 
	public void add(String name,float value) {
        Parameter parameter = new Parameter(name,this);
        parameter.setValue(value);
        addOperation( new ToDo(parameter));
	} 
	public void add(String name,Double value) {
        Parameter parameter = new Parameter(name,this);
        parameter.setValue(value);
        addOperation( new ToDo(parameter));
	}

	public void add(String name,Number value) {
        Parameter parameter = new Parameter(name,this);
        parameter.setValue(value);

        addOperation( new ToDo(parameter));
	}

	public void add(String name,Boolean value) {
        Parameter parameter = new Parameter(name,this);
        parameter.setValue(value);
        addOperation( new ToDo(parameter));
	}
	
	public void add(String name,LocalDateTime value) {
        Parameter parameter = new Parameter(name,this);
        parameter.setValue(value);
        addOperation( new ToDo(parameter));

    }

    private void addOperation(ToDo todo){
        operations.addLast(todo);
        run();
    }

    LinkedList<ToDo> operations= new LinkedList<ToDo>();


    private static Boolean pauseAll =false;
    private static Boolean pause =false;
    private Thread runner=null;

    private void createRunner(){

        this.runner=new Thread(() -> {
            //Do whatever
            while (operations.size() > 0) {
                try {
                    System.out.println(operations.size());
                    ToDo toDo = operations.poll();


                    if (toDo.getParam() != null) {
                        System.out.println("Salvando operação parameter"+LocalDateTime.now().toString());
                        this.service.add(toDo.param, this);
                    } else if (toDo.log != null) {
                        if (toDo.getE() != null) {
                            System.out.println("Salvando operação exception"+LocalDateTime.now().toString());
                            this.service.logError(toDo.getLog(), toDo.getE(), this);
                        }  else {
                            System.out.println("Salvando operação log"+LocalDateTime.now().toString());
                            this.service.log(toDo.getLog(), this);
                        }
                    }else if (toDo.getGenericObjs() != null) {
                        System.out.println("Salvando operação objeto "+LocalDateTime.now().toString());
                        this.service.logObject(toDo.getGenericObjs(), this);
                    }


                    try {
                        Thread.sleep(1);
                        while (pause||pauseAll) {
                            Thread.sleep(10);
                        }
                    } catch (InterruptedException ex) {
                    }

                } catch (Exception e) {
                    this.service.logError("erro crítico",e, this);
                    e.printStackTrace();
                }
            }
        });
    }

    public void run (){
        if(!runner.isAlive() || runner.isInterrupted()){
            this.runner.start();
        }
    }

    /***
     *
     * @return if all logs are paused or not
     */
    public static Boolean getPauseAll() {
        return pauseAll;
    }

    /***
     *
     * @return when true, pause all log saving operations
     */
    public static void setPauseAll(Boolean pause) {
        Log.pauseAll = pause;
    }



    /***
     *
     * @return if this log is paused or not
     */
    public  Boolean getPause() {
        return pauseAll;
    }

    /***
     *
     * @return when true, pause this log saving operations
     */
    public  void setPause(Boolean pause) {
        Log.pauseAll = pause;
    }
}


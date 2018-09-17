package com.br.log.audit.lib;


 class ToDo {

     public ToDo(Parameter param) {
         this.param = param;
     }

     public ToDo(String log) {
         this.log = log;
     }

     public ToDo(String log, Throwable e) {
         this.log = log;
         this.e = e;
     }




     public ToDo(Object genericObjs) {
         this.genericObjs = genericObjs;
     }

     String log;
     Throwable e;
    Object genericObjs;

    Parameter param;

     public String getLog() {
         return log;
     }

     public void setLog(String log) {
         this.log = log;
     }

     public Throwable getE() {
         return e;
     }

     public void setE(Throwable e) {
         this.e = e;
     }

     public Object getGenericObjs() {
         return genericObjs;
     }

     public void setGenericObjs(Object genericObjs) {
         this.genericObjs = genericObjs;
     }

     public Parameter getParam() {
         return param;
     }

     public void setParam(Parameter param) {
         this.param = param;
     }
 }

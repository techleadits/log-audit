package com.br.log.audit.lib;

import java.io.InputStream;
import java.time.LocalDateTime;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;

import org.apache.commons.io.IOUtils;
import org.w3c.dom.Document;

public class Parameter{
    
    public Parameter(Log log){this.log=log;}
    public Parameter(String name,Log log){this(log);this.name=name; }
    String name;
    Object value;
    DataType dataType;
    Log log;


    public Log getLog() {
        return log;
    }
    // public void setValue(Object value){
    //     if(value!=null){
    //         if(value.getClass().isAssignableFrom(Number.class)){
    //             setValue((Number)value);
    //         }else if(){

    //         }else{
    //             setValue(value.toString());
    //         }
    //     }
    // }

    public void setValue(String value){

        if(value!=null){
        
            String strippedvalue=value.toLowerCase().trim();
            try{
                String source = value;
                InputStream in = IOUtils.toInputStream(source, "UTF-8");

                DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                Document doc = dBuilder.parse(in);
                this.dataType=DataType.Xml;
                this.value=value;
            }catch(Exception e){
            }

            if(this.dataType==null){
                if(strippedvalue.equals("true")||strippedvalue.equals("false")){
                    setValue(Boolean.valueOf(strippedvalue));
                }else{
                    this.dataType=DataType.Clob;
                    this.value=value;
                }
            }
        }
    }

    public void setValue(int value){
        setValue(new Integer(value));
    }

    public void setValue(Boolean value){
        if(value!=null&&value){
            setValue(1);
        }else{
            setValue(0);
        }
    }
    public void setValue(Double value){
        if(value!=null){
        
            this.dataType=DataType.Double;
            this.value=value;
        }
    }
    public void setValue(Float value){
        if(value!=null){
        
            this.dataType=DataType.Float;
            this.value=value;
        }
    }

    

    public void setValue(Number value){
        if(value!=null){
        
        this.dataType=DataType.Double;
        this.value=value.doubleValue();
        }
    }

    public void setValue(LocalDateTime value){
        if(value!=null){
        this.dataType=DataType.Date;
        this.value=value;
        }
    }

    public void setName(String name) {
        this.name=name;
    }
    public String getName() {
        return name;
    }

    /**
     * @return the value
     */
    public Object getValue() {
        return value;
    }

    public DataType getDataType() {
        return dataType;
    }

    public enum DataType{
        Xml(1,"xml"),
        
        Int(2,"number"),
        Double(2,"number"),
        Float(2,"number"),

        Date(3,"date"),
        Clob(4,"clob");

        String name;
        int id;
        DataType(int id,String name){
            this.name=name;
            this.id=id;
        }

        /**
         * @return the id
         */
        public int getId() {
            return id;
        }

        @Override
        public String toString() {
            return name.toString();
        }
    }
}
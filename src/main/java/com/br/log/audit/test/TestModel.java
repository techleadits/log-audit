package com.br.log.audit.test;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import lombok.Data;

@Data public class TestModel{
    Date data;
    Integer number;

    List<SubTestList> itens = new LinkedList<SubTestList>();
   
    public static TestModel factory(){
        TestModel test = new TestModel();
        
        test.addList("test1",null);
        test.addList("test2",null);
        test.addList("test3",null);
        test.setData(new Date());
        test.setNumber(2);
        
        return test;
    }

    public void addList(String name,SubTestModel testModel){
        SubTestList subTestList=new SubTestList();
            subTestList.setFatherClass(this);
            subTestList.setName(name);
            subTestList.setHierarchy(testModel);
            itens.add(subTestList);
    }
   
}
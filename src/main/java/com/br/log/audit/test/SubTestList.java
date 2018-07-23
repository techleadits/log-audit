package com.br.log.audit.test;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper=false)
@Data public class SubTestList extends SubTestModel{
    TestModel fatherClass;
}


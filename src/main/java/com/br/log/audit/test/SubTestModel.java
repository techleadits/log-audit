package com.br.log.audit.test;

import lombok.Data;

@Data public class SubTestModel{
    SubTestModel hierarchy;
    String name;
}
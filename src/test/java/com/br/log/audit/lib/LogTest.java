package com.br.log.audit.lib;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.*;

import org.junit.Test;

import java.sql.SQLException;

public class LogTest {


    @Test
    public void testErrorLogging() {
      
        new Exception("erro", new Exception( new SQLException("sub erro", new Exception("fim"))));

        assertThat("", containsString(""));
    }
}

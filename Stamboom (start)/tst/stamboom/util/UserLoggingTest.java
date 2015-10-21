/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stamboom.util;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Hein
 */
public class UserLoggingTest {
    
    public UserLoggingTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of logAction method, of class UserLogging.
     */
    @Test
    public void testLogAction() {
        System.out.println("logAction");
        String header = "GUI Error";
        String message = "De GUI is slecht ontworpen.";
        String expResult = "GUI Error: De GUI is slecht ontworpen.";
        String result = UserLogging.logAction(header, message);
        assertEquals(expResult, result);
    }
    
}

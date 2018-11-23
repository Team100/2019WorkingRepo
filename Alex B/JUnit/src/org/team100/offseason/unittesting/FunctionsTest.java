package org.team100.offseason.unittesting;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Array;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
/*
 * NOTE: This project utilizes JUnit 5 _NOT_ Groovy JUnit, despire IntelliJ Defaulting to Groovy.
 */
class FunctionsTest {
    /**
     * Unit testing for the Add Numbers function
     */
    @org.junit.jupiter.api.Test
    void add() {
        assertEquals(3, Functions.add(1,2));
        assertEquals(5, Functions.add(3,2));
        assertEquals(7, Functions.add(3,4));//Should fail
    }

    /**
     * Unit testing for the Subtract Numbers function
     */
    @org.junit.jupiter.api.Test
    void subtract() {
        assertEquals(5, Functions.subtract(10,5));
        assertEquals(1,Functions.subtract(3,2));
        assertEquals(-5, Functions.subtract(5,10));

    }

    /**
     * Unit testing for boolean returns
     */
    @org.junit.jupiter.api.Test
    void aGreater(){
        assertTrue(Functions.aGreater(3,2));
        assertFalse(Functions.aGreater(2,3));
        assertFalse(Functions.aGreater(10,12));
    }

    /**
     * Inline unit tests (likely would'nt do in the real world)
     * Primarily to prove that you can do more than just asserts in a Unit Test function
     */
    @org.junit.jupiter.api.Test
    void arrayComparisons(){
        String[] a = {"a","b","c"};
        String[] b = {"a","b","c"};



        assertArrayEquals(a,b);
        assertArrayEquals(b,a);

        ArrayList<String> c = new ArrayList<>();
        ArrayList<String> d = new ArrayList<>();

        for( String entry : a ){
            c.add(entry);
            d.add(entry);
            System.out.println(entry); // Prints data to console on run
        }
        assertLinesMatch(c,d);
        assertLinesMatch(d,c);

    }



}
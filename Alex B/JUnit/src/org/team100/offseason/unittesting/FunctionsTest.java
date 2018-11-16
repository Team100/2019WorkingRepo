package org.team100.offseason.unittesting;

import static org.junit.jupiter.api.Assertions.*;

class FunctionsTest {

    @org.junit.jupiter.api.Test
    void add() {
        assertEquals(3, Functions.add(1,2));
        assertEquals(5, Functions.add(3,2));
        assertEquals(7, Functions.add(3,4));//Should fail
    }

    @org.junit.jupiter.api.Test
    void subtract() {
        assertEquals(5, Functions.subtract(10,5));
        assertEquals(1,Functions.subtract(3,2));
        assertEquals(-5, Functions.subtract(5,10));
    }


}
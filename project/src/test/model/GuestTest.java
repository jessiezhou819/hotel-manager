package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class GuestTest {
    private Guest testGuest;

    @BeforeEach
    void runBefore() {
        testGuest = new Guest("Joe");
    }

    @Test
    void testConstructor() {
        assertEquals("Joe", testGuest.getName());
        assertEquals(0, testGuest.getDayStaying());
        assertEquals(0, testGuest.getBill());
        assertTrue(testGuest.getRoomType());
        assertEquals(0, testGuest.getMiniBarItems().size());
    }

    @Test
    void testConstructorDoubleBed() {
        testGuest.setName("Jess");
        testGuest.setDoubleBed();
        assertEquals("Jess", testGuest.getName());
        assertEquals(0, testGuest.getDayStaying());
        assertEquals(0, testGuest.getBill());
        assertFalse(testGuest.getRoomType());
        assertEquals(0, testGuest.getMiniBarItems().size());
    }

    @Test
    void testSetBillSingleBed() {
        testGuest.setDayStaying(2);
        testGuest.setBill();
        assertEquals(400, testGuest.getBill());
    }

    @Test
    void testSetBillDoubleBed() {
        testGuest.setDoubleBed();
        testGuest.setDayStaying(1);
        testGuest.setBill();
        assertEquals(250, testGuest.getBill());
    }

    @Test
    void testAddMiniBarSingleItem() {
        testGuest.addMiniBarItem("soda");
        assertEquals(1, testGuest.getMiniBarItems().size());
        assertEquals("soda", testGuest.getMiniBarItems().get(0));
    }

    @Test
    void testAddMiniBarMultipleItems() {
        testGuest.addMiniBarItem("soda");
        testGuest.addMiniBarItem("snack");
        testGuest.addMiniBarItem("soda");
        assertEquals(3, testGuest.getMiniBarItems().size());

        assertEquals("soda", testGuest.getMiniBarItems().get(0));
        assertEquals("snack", testGuest.getMiniBarItems().get(1));
        assertEquals("soda", testGuest.getMiniBarItems().get(2));
    }

    @Test
    void testAddToBillEmptyItem() {
        assertEquals(0, testGuest.addToBill());
    }

    @Test
    void testAddToBillInvalidInput() {
        testGuest.addMiniBarItem("banana");
        assertEquals(0, testGuest.addToBill());
    }

    @Test
    void testAddToBillOnevalidInput() {
        testGuest.addMiniBarItem("banana");
        testGuest.addMiniBarItem("snack");
        assertEquals(4, testGuest.addToBill());
    }

    @Test
    void testAddToBillOneSoda() {
        testGuest.addMiniBarItem("soda");
        assertEquals(2.5, testGuest.addToBill());
    }

    @Test
    void testAddToBillOneSnack() {
        testGuest.addMiniBarItem("snack");
        assertEquals(4, testGuest.addToBill());
    }

    @Test
    void testAddToBillMultipleItems() {
        testGuest.addMiniBarItem("snack");
        testGuest.addMiniBarItem("soda");
        testGuest.addMiniBarItem("soda");
        assertEquals(9, testGuest.addToBill());
    }

    @Test
    void testToString() {
        assertTrue( testGuest.toString().contains(". Name: Joe | Days Staying: 0 | Charge: $0"));
    }
}

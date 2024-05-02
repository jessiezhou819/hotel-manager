package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class RegistrationHistoryTest {
    private RegistrationHistory testHistory;
    private Guest g1;
    private Guest g2;

    @BeforeEach
    void runBefore() {
        testHistory = new RegistrationHistory();
        g1 = new Guest("Jess");
        g2 = new Guest("Joey");
    }

    @Test
    void testConstructor() {
        assertEquals(0, testHistory.getGuestCount());
        assertEquals(0, testHistory.getCurrentRevenue());
        assertEquals(0, testHistory.getCheckOutRevenue());
    }

    @Test
    void testCheckInGuest() {
        testHistory.checkInGuest("Joe", "3", "Single Bed");
        assertEquals(1, testHistory.getGuestCount());
        assertTrue(testHistory.getGuests().get(0).getRoomType());
        assertEquals("Joe", testHistory.getGuests().get(0).getName());
    }

    @Test
    void testCheckInGuest2() {
        testHistory.checkInGuest("Joe", "3", "Double Bed");
        assertEquals(1, testHistory.getGuestCount());
        assertFalse(testHistory.getGuests().get(0).getRoomType());
        assertEquals("Joe", testHistory.getGuests().get(0).getName());
    }

    @Test
    void testAddGuestOnce() {
        testHistory.addGuest(g1);
        assertEquals(1, testHistory.getGuestCount());
        assertEquals(g1, testHistory.getGuests().get(0));
    }

    @Test
    void testAddMultipleGuests() {
        testHistory.addGuest(g1);
        testHistory.addGuest(g2);
        assertEquals(2, testHistory.getGuestCount());
        assertEquals(g1, testHistory.getGuests().get(0));
        assertEquals(g2, testHistory.getGuests().get(1));
    }

    @Test
    void testRemoveOneGuest() {
        testHistory.addGuest(g1);
        testHistory.addGuest(g2);

        testHistory.removeGuest(g1);
        assertEquals(1, testHistory.getGuestCount());
        assertEquals(g2, testHistory.getGuests().get(0));
    }

    @Test
    void testRemoveMultipleGuests() {
        testHistory.addGuest(g1);
        testHistory.addGuest(g2);

        testHistory.removeGuest(g1);
        testHistory.removeGuest(g2);
        assertEquals(0, testHistory.getGuestCount());
    }

    @Test
    void testGetCurrentRevenueNoGuest() {
        assertEquals(0, testHistory.getCurrentRevenue());
    }

    @Test
    void testGetCurrentRevenueOneGuest() {
        testHistory.addCheckOutRevenue(0);
        testHistory.addGuest(g1);
        g1.setDayStaying(1);
        g1.setBill();
        assertEquals(200, testHistory.getCurrentRevenue());
    }

    @Test
    void testGetCurrentRevenueMultipleGuests() {
        testHistory.addCheckOutRevenue(200);
        testHistory.addGuest(g1);
        g1.setDoubleBed();
        g1.setDayStaying(1);
        g1.setBill();
        testHistory.addGuest(g2);
        g2.setDayStaying(2);
        g2.setBill();
        assertEquals(850, testHistory.getCurrentRevenue());
    }
}

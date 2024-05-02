package persistence;

import model.Guest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

// Code influenced by the JsonSerializationDemo (https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo)
public class JsonTest {
    protected void checkGuest(String name, Boolean isSingleBed, double bill,
                              List<String> miniBar, int days, Guest guest) {
        assertEquals(name, guest.getName());
        assertEquals(isSingleBed, guest.getRoomType());
        assertEquals(bill, guest.getBill());
        assertEquals(miniBar, guest.getMiniBarItems());
        assertEquals(days, guest.getDayStaying());
    }
}

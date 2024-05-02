package persistence;

import model.Guest;
import model.RegistrationHistory;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

// Code influenced by the JsonSerializationDemo (https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo)
public class JsonReaderTest extends JsonTest {

    @Test
    void testReaderNonExistentFile() {
        JsonReader reader = new JsonReader("./data/noSuchFile.json");
        try {
            RegistrationHistory rh = reader.read();
            fail("IOException expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    void testReaderEmptyWorkRoom() {
        JsonReader reader = new JsonReader("./data/testReaderEmptyRegistration.json");
        try {
            RegistrationHistory rh = reader.read();
            assertEquals(0, rh.getCheckOutRevenue());
            assertEquals(0, rh.getCurrentRevenue());
            assertEquals(0,rh.getGuests().size());
        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }

    @Test
    void testReaderGeneralWorkRoom() {
        JsonReader reader = new JsonReader("./data/testReaderGeneralRegistration.json");
        try {
            RegistrationHistory rh = reader.read();
            assertEquals(0, rh.getCheckOutRevenue());
            assertEquals(1159, rh.getCurrentRevenue());
            List<Guest> guests = rh.getGuests();
            assertEquals(2, guests.size());
            List<String> miniBarItems = List.of("soda");
            checkGuest("Jessie", true, 402.5, miniBarItems, 2, guests.get(0));
            miniBarItems = List.of("soda", "snack");
            checkGuest("Joe", false, 756.5, miniBarItems, 3, guests.get(1));
        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }

}

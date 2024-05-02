package persistence;

import model.Guest;
import model.RegistrationHistory;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

// Code influenced by the JsonSerializationDemo (https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo)
class JsonWriterTest extends JsonTest {

    @Test
    void testWriterInvalidFile() {
        try {
            RegistrationHistory rh = new RegistrationHistory();
            JsonWriter writer = new JsonWriter("./data/my\0illegal:fileName.json");
            writer.open();
            fail("IOException was expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    void testWriterEmptyWorkroom() {
        try {
            RegistrationHistory rh = new RegistrationHistory();
            JsonWriter writer = new JsonWriter("./data/testWriterEmptyRegistration.json");
            writer.open();
            writer.write(rh);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterEmptyRegistration.json");
            rh = reader.read();
            assertEquals(0, rh.getCheckOutRevenue());
            assertEquals(0, rh.getCurrentRevenue());
            assertEquals(0,rh.getGuests().size());
        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }

    @Test
    void testWriterGeneralWorkroom() {
        try {
            RegistrationHistory rh = new RegistrationHistory();
            rh.addGuest(getGuest1());
            rh.addGuest(getGuest2());
            rh.setCurrentRevenue(1159);
            JsonWriter writer = new JsonWriter("./data/testWriterGeneralRegistration.json");
            writer.open();
            writer.write(rh);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterGeneralRegistration.json");
            rh = reader.read();
            assertEquals(0, rh.getCheckOutRevenue());
            assertEquals(1159, rh.getCurrentRevenue());
            List<Guest> guests = rh.getGuests();
            assertEquals(2,guests.size());
            List<String> miniBarItems = List.of("soda");
            checkGuest("Jessie", true, 402.5, miniBarItems, 2, guests.get(0));
            miniBarItems = List.of("soda", "snack");
            checkGuest("Joe", false, 756.5, miniBarItems, 3, guests.get(1));

        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }

    Guest getGuest1() {
        Guest g1 = new Guest("Jessie");
        g1.addMiniBarItem("soda");
        g1.setDayStaying(2);
        g1.setBill();
        g1.addToBill();
        return g1;
    }

    Guest getGuest2() {
        Guest g2 = new Guest("Joe");
        g2.addMiniBarItem("soda");
        g2.addMiniBarItem("snack");
        g2.setDoubleBed();
        g2.setDayStaying(3);
        g2.setBill();
        g2.addToBill();
        return g2;
    }
}
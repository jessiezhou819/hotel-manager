package persistence;

import model.Guest;
import model.RegistrationHistory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

import org.json.*;

// Represents a reader that reads registrationHistory from JSON data stored in file
// Code influenced by the JsonSerializationDemo (https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo)
public class JsonReader {
    private String source;

    // EFFECTS: constructs reader to read from source file
    public JsonReader(String source) {
        this.source = source;
    }

    // EFFECTS: reads workroom from file and returns it;
    // throws IOException if an error occurs reading data from file
    public RegistrationHistory read() throws IOException {
        String jsonData = readFile(source);
        JSONObject jsonObject = new JSONObject(jsonData);
        return parseRegistrationHistory(jsonObject);
    }

    // EFFECTS: reads source file as string and returns it
    private String readFile(String source) throws IOException {
        StringBuilder contentBuilder = new StringBuilder();

        try (Stream<String> stream = Files.lines(Paths.get(source), StandardCharsets.UTF_8)) {
            stream.forEach(s -> contentBuilder.append(s));
        }

        return contentBuilder.toString();
    }

    // EFFECTS: parses registration from JSON object and returns it
    private RegistrationHistory parseRegistrationHistory(JSONObject jsonObject) {
        RegistrationHistory rh = new RegistrationHistory();
        addGuests(rh, jsonObject);
        rh.setCurrentRevenue(jsonObject.getDouble("currentRevenue"));
        rh.setCheckOutRevenue(jsonObject.getDouble("checkOutRevenue"));
        return rh;
    }

    // MODIFIES: rh
    // EFFECTS: parses guests from JSON object and adds them to registration
    private void addGuests(RegistrationHistory rh, JSONObject jsonObject) {
        JSONArray jsonArray = jsonObject.getJSONArray("guests");
        for (Object json : jsonArray) {
            JSONObject nextGuest = (JSONObject) json;
            addGuest(rh, nextGuest);
        }
    }

    // MODIFIES: rh
    // EFFECTS: parses guest from JSON object and adds it to registration
    private void addGuest(RegistrationHistory rh, JSONObject jsonObject) {
        String name = jsonObject.getString("name");
        Guest guest = new Guest(name);
        guest.setDayStaying(jsonObject.getInt("dayStaying"));
        if (!(jsonObject.getBoolean("isSingleBed"))) {
            guest.setDoubleBed();
        }
        guest.setBill();
        JSONArray miniBarItemsArray = jsonObject.getJSONArray("miniBarItems");
        for (int i = 0; i < miniBarItemsArray.length(); i++) {
            guest.addMiniBarItem(miniBarItemsArray.getString(i));
        }
        guest.addToBill();
        rh.addGuest(guest);
    }
}

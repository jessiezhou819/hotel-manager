package model;

import org.json.JSONArray;
import org.json.JSONObject;
import persistence.Writable;

import java.util.ArrayList;
import java.util.List;

// represents hotel's registration history having a list of different guests, the current revenue
// and a record of revenue from guests that have checked-out
// Code influenced by the JsonSerializationDemo (https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo)
public class RegistrationHistory implements Writable {
    private List<Guest> guests;            // a history of guests
    private double currentRevenue;         // current revenue
    private double checkOutRevenue;        // revenue from check-out

    // EFFECTS: constructs a registration history with an empty list of guests
    //          with current revenue and check out revenue set to 0
    public RegistrationHistory() {
        this.guests = new ArrayList<>();
        this.currentRevenue = 0;
        this.checkOutRevenue = 0;
    }

    public void setCheckOutRevenue(double checkOutRevenue) {
        this.checkOutRevenue = checkOutRevenue;
    }

    public void setCurrentRevenue(double currentRevenue) {
        this.currentRevenue = currentRevenue;
    }

    public List<Guest> getGuests() {
        return guests;
    }

    // EFFECTS: returns the total count of current guests
    public int getGuestCount() {
        return this.guests.size();
    }

    public double getCheckOutRevenue() {
        return checkOutRevenue;
    }

    // MODIFIES: this
    // EFFECTS: add a guest to history
    public void addGuest(Guest g) {
        this.guests.add(g);
    }

    // EFFECTS: check-in a guest to history based on input from text fields
    public void checkInGuest(String name, String days, String roomType) {
        Guest guest = new Guest(name);
        if (roomType.equals("Double Bed")) {
            guest.setDoubleBed();
        }
        int length = Integer.parseInt(days);
        guest.setDayStaying(length);
        guest.setBill();

        addGuest(guest);

        EventLog.getInstance().logEvent(new Event("New Guest " + guest.getName()
                + " is added to Registration History."));
    }

    // MODIFIES: this
    // EFFECTS: remove a guest from history during check-out
    public void removeGuest(Guest g) {
        this.guests.remove(g);
        EventLog.getInstance().logEvent(new Event("Guest " + g.getName()
                + " is removed from Registration History."));
    }

    // MODIFIES: this
    // EFFECTS: record total revenue obtained from guests that checked out
    public void addCheckOutRevenue(double r) {
        checkOutRevenue += r;
    }

    // EFFECTS: calculate the bill charged for each guest and return the sum
    //          ; in other words, get hotel's total current revenue
    public double getCurrentRevenue() {
        currentRevenue = getCheckOutRevenue();
        for (Guest next: guests) {
            currentRevenue += next.getBill();
        }
        return currentRevenue;
    }

    // EFFECTS: returns registration list as a JSON object
    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("guests", guestsToJson());
        json.put("currentRevenue", getCurrentRevenue());
        json.put("checkOutRevenue", checkOutRevenue);
        return json;
    }

    // EFFECTS: returns guests in this registration list as a JSON array
    private JSONArray guestsToJson() {
        JSONArray jsonArray = new JSONArray();

        for (Guest g : guests) {
            jsonArray.put(g.toJson());
        }

        return jsonArray;
    }
}

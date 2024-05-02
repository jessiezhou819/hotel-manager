package model;

import org.json.JSONArray;
import org.json.JSONObject;
import persistence.Writable;

import java.util.ArrayList;
import java.util.List;

// Represents a guest having a name, length of staying (in days), current bill (in dollars);
// the selected room type (true if it's single room; false otherwise)
// and a list of purchases from the mini bar
// Code influenced by the JsonSerializationDemo (https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo)
public class Guest implements Writable {
    private String name;                   // the guest name
    private int dayStaying;                // days staying
    private double bill;                   // the current bill of the guest
    private Boolean isSingleBed;           // if guests chooses single bed
    private List<String> miniBarItems;     // any purchases from mini bar

    // REQUIRES: guest name has a non-zero length
    // EFFECTS: name is set to guest name; dayStaying is set to 0;
    //          isSingleBed is set to true, miniBarItems is now an
    //          empty list; current bill is set to 0
    public Guest(String name) {
        this.name = name;
        this.dayStaying = 0;
        this.isSingleBed = true;
        this.miniBarItems = new ArrayList<>();
        this.bill = 0;
    }

    public String getName() {
        return name;
    }

    public int getDayStaying() {
        return dayStaying;
    }

    public double getBill() {
        return bill;
    }

    public boolean getRoomType() {
        return isSingleBed;
    }

    public List<String> getMiniBarItems() {
        return miniBarItems;
    }

    // REQUIRES: name has non-zero length
    // MODIFIES: this
    // EFFECTS: update guest name
    public void setName(String name) {
        this.name = name;
    }

    // REQUIRES: days > 0
    // MODIFIES: this
    // EFFECTS: update guest's length of staying at hotel
    public void setDayStaying(int days) {
        this.dayStaying = days;
    }

    // MODIFIES: this
    // EFFECTS: adds an item to a list of mini bar items
    public void addMiniBarItem(String item) {
        this.miniBarItems.add(item);
    }

    // MODIFIES: this
    // EFFECTS: changes the default room type single bed to double bed
    public void setDoubleBed() {
        this.isSingleBed = false;
    }

    // MODIFIES: this
    // EFFECTS: calculate current bill based on chosen room type;
    //          $200/day for single bed, $250/day for double bed
    public void setBill() {
        if (isSingleBed) {
            bill = 200 * getDayStaying();
        } else {
            bill = 250 * getDayStaying();
        }
    }

     // MODIFIES: this
     // EFFECTS: price is calculated for each item purchased from
     //          mini bar; each soda is $2.5, each snack is $4;
     //          total price (in dollars) is returned
    public double addToBill() {
        List<String> items = getMiniBarItems();
        double price = 0;
        if (!items.isEmpty()) {
            for (String item: items) {
                if (item.equals("soda")) {
                    price += 2.5;
                } else if (item.equals("snack")) {
                    price += 4;
                }
            }
        }
        this.bill += price;
        return price;
    }

    // EFFECTS: returns a string representation of guest
    @Override
    public String toString() {
        String billStr = String.format("%.2f", bill);  // get bill to 2 decimal places as a string
        return ". Name: " + name + " | Days Staying: " + dayStaying
                + " | Charge: $" + billStr;
    }

    // EFFECTS: returns guest as a JSON object
    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("name", name);
        json.put("dayStaying", dayStaying);
        json.put("isSingleBed", isSingleBed);
        JSONArray miniBarItemsArray = new JSONArray(miniBarItems);
        json.put("miniBarItems", miniBarItemsArray);
        json.put("bill", bill);
        return json;
    }
}


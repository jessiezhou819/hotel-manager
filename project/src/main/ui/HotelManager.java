package ui;

import model.Guest;
import model.RegistrationHistory;
import persistence.JsonReader;
import persistence.JsonWriter;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

// Hotel Manager Application
public class HotelManager {
    private static final String JSON_STORE = "./data/registration.json";
    private Scanner input;
    private Guest guest;
    private RegistrationHistory history;
    private JsonWriter jsonWriter;
    private JsonReader jsonReader;

    // EFFECTS: runs the hotel manager application
    public HotelManager() {
        input = new Scanner(System.in);
        history = new RegistrationHistory();
        jsonWriter = new JsonWriter(JSON_STORE);
        jsonReader = new JsonReader(JSON_STORE);
        runManager();
    }

    // MODIFIES: this
    // EFFECTS: processes user input
    private void runManager() {
        boolean keepGoing = true;
        String command = null;

        init();

        while (keepGoing) {

            displayMenu();
            command = input.next();
            command = command.toLowerCase();

            if (command.equals("q")) {
                keepGoing = false;
            } else {
                processCommand(command);
            }
        }
        System.out.println("\nGoodbye!");
    }

    // MODIFIES: this
    // EFFECTS: processes user command
    private void processCommand(String command) {
        if (command.equals("c")) {
            doCheckIn();
        } else if (command.equals("m")) {
            doManageRegistration();
        } else if (command.equals("s")) {
            saveRegistration();
        } else if (command.equals("l")) {
            loadRegistration();
        } else {
            System.out.println("Selection not valid...");
        }
    }

    // MODIFIES: this
    // EFFECTS: initializes hotel management program
    private void init() {
        history = new RegistrationHistory();
        input = new Scanner(System.in);
        input.useDelimiter("\n");
    }

    // EFFECTS: displays menu of options to user
    private void displayMenu() {
        System.out.println("\nSelect from:");
        System.out.println("\tc -> new guest check-in");
        System.out.println("\tm -> manage (view) current registrations");
        System.out.println("\ts -> save registration history to file");
        System.out.println("\tl -> load registration history from file");
        System.out.println("\tq -> quit");
    }

    // EFFECTS: displays mini bar menu
    private void displayMiniBarItem() {
        System.out.println("\nEnter one of the option (You can add unlimited items before you click 'n'):");
        System.out.println("\tsoda -> purchased a soda");
        System.out.println("\tsnack -> purchased a snack");
        System.out.println("\tn -> no more");
    }

    // MODIFIES: this
    // EFFECTS: run through a guest's check-in procedure, do not allow duplicates
    private void doCheckIn() {
        guest = new Guest("Someone");

        selectRoomType();
        System.out.print("Enter guest name: ");
        String name = input.next();
        while (isInList(name)) {
            System.out.println("This name has been registered. Please try again:");
            name = input.next();
        }
        guest.setName(name);

        System.out.print("Enter your length of staying (in days): ");
        while (! (input.hasNextInt())) {
            System.out.print("Please enter an integer: ");
            input.next();
        }
        int days = input.nextInt();
        guest.setDayStaying(days);
        guest.setBill();
        history.addGuest(guest);
        printInfo(guest);
    }

    // MODIFIES: this
    // EFFECTS: run through guest check-out procedure
    private void doCheckOut() {
        Guest guest = findGuest();
        while (guest == null) {
            System.out.println("Cannot find guest... Please try again:");
            guest = findGuest();
        }
        printLeaveMessage(guest);
        history.addCheckOutRevenue(guest.getBill());
        history.removeGuest(guest);
    }

    // MODIFIES: this
    // EFFECTS: add items (snack or soda) purchased from mini bar and update bill
    private void doMiniBarPurchase() {
        boolean stillMore = true;
        String bought = null;
        Guest guest = findGuest();

        while (guest == null) {
            System.out.println("Cannot find name. Please try again:");
            guest = findGuest();
        }

        while (stillMore) {
            displayMiniBarItem();
            bought = input.next().toLowerCase();

            if (bought.equals("n")) {
                stillMore = false;
            } else if (bought.equals("soda") || bought.equals("snack")) {
                guest.addMiniBarItem(bought);
            } else {
                System.out.println("Can't find item...");
            }
        }
        System.out.println("\nYou have made the following purchases:");
        for (String str : guest.getMiniBarItems()) {
            System.out.println(str);
        }
        System.out.println("With a total charge of $" + guest.addToBill() + " due during check-out.");
    }

    // EFFECTS: display a list of guest with their information summary;
    //          go back to main page if there's no current guest
    public void doManageRegistration() {
        printSummaryInfo(history);
        System.out.println("-----------------------------------------------------------");
        for (int i = 0; i < history.getGuestCount(); i++) {
            System.out.println("Guest " + String.format("%d", i + 1) + history.getGuests().get(i));
        }
        System.out.println("-----------------------------------------------------------");

        if (! (history.getGuestCount() == 0)) {
            selectAction();
        } else {
            System.out.println("There's no current guest!");
        }
    }

    // EFFECTS: search for guest by asking for the guest name, return that guest
    private Guest findGuest() {
        System.out.print("Enter guest name (used for check-in): ");
        String name = input.next();
        for (Guest next: history.getGuests()) {
            if (next.getName().equals(name)) {
                return next;
            }
        }
        return null;
    }

    // EFFECT: check if a guest is already present in registration history
    private boolean isInList(String name) {
        for (Guest g: history.getGuests()) {
            if (g.getName().equals(name)) {
                return true;
            }
        }
        return false;
    }

    // MODIFIES: this
    // EFFECTS: assign a guest to their selected room type
    private void selectRoomType() {
        String selection = "";

        while (!(selection.equals("1") || selection.equals("2"))) {
            System.out.println("1 for single-bed ($200)");
            System.out.println("2 for double-bed ($250)");
            selection = input.next();
            selection = selection.toLowerCase();
        }
        if (selection.equals("2")) {
            guest.setDoubleBed();
        }
    }

    // EFFECTS: determine if there are any further user modifications of the registration list
    private void selectAction() {
        String selection = "";

        while (!(selection.equals("c") || selection.equals("a")) && !selection.equals("q")) {
            System.out.println("\nc for check-out");
            System.out.println("a for adding mini bar purchases");
            System.out.println("q for going back");
            selection = input.next();
            selection = selection.toLowerCase();
        }

        if (selection.equals("c")) {
            doCheckOut();
        } else if (selection.equals("a")) {
            doMiniBarPurchase();
        }
    }

    // EFFECTS: prints guest info on the screen after guest is checked in
    private void printInfo(Guest guest) {
        String dayStr = String.format("%d", guest.getDayStaying());
        String billStr = String.format("%.2f", guest.getBill());
        System.out.println("\nWELCOME! Guest Name: " + guest.getName() + " | Days Staying: " + dayStr
                + " | Current Charge: $" + billStr);
    }

    // EFFECTS: prints guest payment summary
    private void printLeaveMessage(Guest guest) {
        String billStr = String.format("%.2f", guest.getBill());
        System.out.println("\nTHANK YOU " + guest.getName() + "! Your total payment is $" + billStr + ".");
    }

    // EFFECTS: prints current revenue and guest count when viewing registration history
    private void printSummaryInfo(RegistrationHistory history) {
        String revenue = String.format("%.2f", history.getCurrentRevenue());
        String guestCount = String.format("%d", history.getGuestCount());
        System.out.println("\nCurrent Revenue: " + revenue + " | Current Guest Count: " + guestCount);
    }

    // EFFECTS: saves the registration to file
    private void saveRegistration() {
        try {
            jsonWriter.open();
            jsonWriter.write(history);
            jsonWriter.close();
            System.out.println("Saved registration list  to " + JSON_STORE);
        } catch (FileNotFoundException e) {
            System.out.println("Unable to write to file: " + JSON_STORE);
        }
    }

    // MODIFIES: this
    // EFFECTS: loads registration from file
    private void loadRegistration() {
        try {
            history = jsonReader.read();
            System.out.println("Loaded registration list from " + JSON_STORE);
        } catch (IOException e) {
            System.out.println("Unable to read from file: " + JSON_STORE);
        }
    }
}

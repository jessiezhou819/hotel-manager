package ui;

import ui.exception.DuplicateNameException;
import model.Guest;
import model.RegistrationHistory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

// GridLayout referenced https://docs.oracle.com/en/java/javase/21/docs/api/java.desktop/java/awt/GridLayout.html
// JTextField referenced https://www.geeksforgeeks.org/java-swing-jtextfield/
// JComboBox referenced https://www.javatpoint.com/java-jcombobox

// Represents a window that manages the input of guest information
public class CheckInWindow extends JFrame {

    private RegistrationHistory history;
    private JTextField nameField;
    private JTextField daysField;
    private JComboBox<String> roomTypeComboBox;

    // EFFECTS: constructs a check in window with labels and input fields
    public CheckInWindow(RegistrationHistory rh) {
        super("Check-in");
        history = rh;
        setSize(500, 300);

        JPanel panel = new JPanel(new GridLayout(4,2,10,10));
        addQuestions(panel);
        addCheckInButton(panel);

        add(panel);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    // EFFECTS: add questions to ask and provide fields for input,
    //          put them onto the panel
    public void addQuestions(JPanel panel) {
        JLabel nameLabel = new JLabel("Guest's Name:");
        nameField = new JTextField();

        JLabel lengthOfStayLabel = new JLabel("Days staying (in integer):");
        daysField = new JTextField();

        JLabel roomTypeLabel = new JLabel("Room Type:");
        String[] roomTypes = {"Single Bed", "Double Bed"};
        roomTypeComboBox = new JComboBox<>(roomTypes);

        panel.add(nameLabel);
        panel.add(nameField);
        panel.add(lengthOfStayLabel);
        panel.add(daysField);
        panel.add(roomTypeLabel);
        panel.add(roomTypeComboBox);
        addCheckInIcon(panel);
    }

    // EFFECTS: create a button that when clicked can
    //          record the guest's info
    //          or display pop-up messages if user provided invalid info
    public void addCheckInButton(JPanel panel) {
        JButton checkInButton = new JButton("Check In");
        checkInButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    doCheckIn();
                } catch (DuplicateNameException exception) {
                    showErrorMsg("Name already registered");
                } catch (Exception exception) {
                    showErrorMsg("Invalid information provided");
                }
            }
        });
        panel.add(checkInButton);
    }

    // EFFECTS: add an icon to panel
    public void addCheckInIcon(JPanel panel) {
        ImageIcon hotelIcon = new ImageIcon("src/main/ui/images/cart.png");
        Image scaledImage = hotelIcon.getImage().getScaledInstance(45, 45, Image.SCALE_SMOOTH);
        ImageIcon scaledIcon = new ImageIcon(scaledImage);
        JLabel iconLabel = new JLabel(scaledIcon);
        panel.add(iconLabel);
    }

    // REQUIRES: string is not empty
    // EFFECTS: display the given error message in a pop-up window
    public void showErrorMsg(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Error", JOptionPane.INFORMATION_MESSAGE);
    }

    // MODIFIES: this
    // EFFECTS: get guest info from fields and construct a new guest;
    //          throws exception if a guest is added multiple times
    //          display full info in a pop-up message
    public void doCheckIn() throws Exception {
        String guestName = nameField.getText();
        String lengthOfStay = daysField.getText();
        String roomType = (String) roomTypeComboBox.getSelectedItem();

        for (Guest g: history.getGuests()) {
            if (g.getName().equals(guestName)) {
                throw new DuplicateNameException();
            }
        }

        history.checkInGuest(guestName, lengthOfStay, roomType);

        String message = "Guest's Name: " + guestName
                + "\nLength of Stay: " + lengthOfStay
                + "\nRoom Type: " + roomType;

        JOptionPane.showMessageDialog(this, message, "Check-in Information", JOptionPane.INFORMATION_MESSAGE);

        dispose();
    }

}

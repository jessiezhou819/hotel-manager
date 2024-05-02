package ui;

import model.EventLog;
import model.Event;
import model.RegistrationHistory;
import persistence.JsonReader;
import persistence.JsonWriter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileNotFoundException;
import java.io.IOException;


// JFrame referenced https://www.youtube.com/watch?v=jBOpJ8giqhE
// Font referenced https://stackoverflow.com/questions/25054203/how-to-set-font-weight-in-java-for-swing-components
// Button referenced ButtonDemo https://docs.oracle.com/javase/tutorial/uiswing/examples/components/index.html
// Listener referenced https://docs.oracle.com/javase/tutorial/uiswing/events/actionlistener.html
// Icon referenced https://stackoverflow.com/questions/1614772/how-to-change-jframe-icon?lq=1
// JOptionPane referenced https://docs.oracle.com/javase/8/docs/api/javax/swing/JOptionPane.html

// Represents a hotel management application
public class HotelManagerApp extends JFrame {
    private static final String JSON_STORE = "./data/registration.json";

    private RegistrationHistory registrationHistory;
    private JsonWriter jsonWriter;
    private JsonReader jsonReader;

    // EFFECTS: constructs a Hotel Manager APP
    public HotelManagerApp() {
        super("Hotel Manager");

        jsonReader = new JsonReader(JSON_STORE);
        jsonWriter = new JsonWriter(JSON_STORE);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 500);
        setResizable(false);

        popUpSetUp();
        createWelcomePanel();
        createButtonPanel();

        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    // EFFECTS: display the pop-up windows when the application starts/ends
    public void popUpSetUp() {
        loadDataPopUp();

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                saveDataPopUp();
            }
        });
    }

    // EFFECTS: constructs the welcome panel with icon and title
    public void createWelcomePanel() {
        JPanel welcomePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        welcomePanel.setPreferredSize(new Dimension(300, 150));
        welcomePanel.setBorder(BorderFactory.createEmptyBorder(50,5,5,5));

        ImageIcon hotelIcon = new ImageIcon("src/main/ui/images/hotelIcon.png");
        Image scaledImage = hotelIcon.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH);
        ImageIcon scaledIcon = new ImageIcon(scaledImage);
        JLabel iconLabel = new JLabel(scaledIcon);

        JLabel welcomeLabel = new JLabel("Welcome to Hotel Manager", JLabel.CENTER);
        welcomeLabel.setFont(new Font("Consolas", Font.BOLD, 30));

        welcomePanel.add(iconLabel);
        welcomePanel.add(welcomeLabel);
        add(welcomePanel, BorderLayout.NORTH);
    }

    // EFFECTS: constructs a button panel below welcome panel that displays two buttons
    public void createButtonPanel() {
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));

        buttonPanel.add(setCheckInButton());
        buttonPanel.add(setManageButton());

        add(buttonPanel, BorderLayout.CENTER);
    }

    // EFFECTS: designs the check-in button that initiates a new window
    public JButton setCheckInButton() {
        JButton checkInButton = new JButton("Guest Check-in");
        checkInButton.setPreferredSize(new Dimension(600,100));
        checkInButton.add(getCheckInIcon());
        checkInButton.setFont(new Font("Palatino", Font.PLAIN, 20));

        checkInButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openCheckInWindow();
            }
        });

        return checkInButton;
    }

    // EFFECTS: designs the manage button that initiates a new window
    public JButton setManageButton() {
        JButton manageButton = new JButton("Manage Registration");
        manageButton.setPreferredSize(new Dimension(600,100));
        manageButton.add(getHistoryIcon());
        manageButton.setFont(new Font("Palatino", Font.PLAIN, 20));

        manageButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openHistoryWindow(registrationHistory);
            }
        });

        return manageButton;
    }

    // EFFECTS: an icon to be put on the check-in button
    public JLabel getCheckInIcon() {
        ImageIcon hotelIcon = new ImageIcon("src/main/ui/images/checkIn.png");
        Image scaledImage = hotelIcon.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
        ImageIcon scaledIcon = new ImageIcon(scaledImage);
        JLabel iconLabel = new JLabel(scaledIcon);
        return iconLabel;
    }

    // EFFECTS: an icon to be put on the manage button
    public JLabel getHistoryIcon() {
        ImageIcon hotelIcon = new ImageIcon("src/main/ui/images/list.png");
        Image scaledImage = hotelIcon.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
        ImageIcon scaledIcon = new ImageIcon(scaledImage);
        JLabel iconLabel = new JLabel(scaledIcon);
        return iconLabel;
    }

    // EFFECTS: open the check in window when button is clicked
    public void openCheckInWindow() {
        CheckInWindow checkInFrame = new CheckInWindow(registrationHistory);
        checkInFrame.setVisible(true);
    }

    // EFFECTS: open the history window when button is clicked
    public void openHistoryWindow(RegistrationHistory registrationHistory) {
        HistoryWindow historyFrame = new HistoryWindow(registrationHistory);
        historyFrame.setVisible(true);
    }

    // EFFECTS: constructs the pop-up window asking whether to save data;
    //          if yes, write the data to json file
    //          if no, close window and end application
    //          if select cancel, return to main page
    public void saveDataPopUp() {
        int choice = JOptionPane.showConfirmDialog(this,
                "Would you like to save current registrations?",
                "Save",
                JOptionPane.YES_NO_CANCEL_OPTION);

        if (choice == JOptionPane.YES_OPTION) {
            try {
                jsonWriter.open();
                jsonWriter.write(registrationHistory);
                jsonWriter.close();
                System.out.println("Saved registration history to " + JSON_STORE);
            } catch (FileNotFoundException e) {
                System.out.println("Unable to write to file: " + JSON_STORE);
            }
            dispose();
        } else if (choice == JOptionPane.NO_OPTION) {
            dispose();
        }
        printLog(EventLog.getInstance());
    }

    // EFFECT: print the event line by line when program ends
    public void printLog(EventLog el) {
        for (Event next : el) {
            System.out.println("\n" + next);
        }
        repaint();
    }

    // EFFECTS: constructed the pop-up window asking whether to load data from file;
    //          if yes, load; if no, start a new application
    public void loadDataPopUp() {
        int choice = JOptionPane.showConfirmDialog(this,
                "Would you like to load previous registration?",
                "Load",
                JOptionPane.YES_NO_OPTION);

        if (choice == JOptionPane.YES_OPTION) {
            try {
                registrationHistory = jsonReader.read();
                System.out.println("Loaded registration list from " + JSON_STORE);
            } catch (IOException e) {
                System.out.println("Unable to read from file: " + JSON_STORE);
                registrationHistory = new RegistrationHistory();
            }
        } else {
            registrationHistory = new RegistrationHistory();
        }
    }

}

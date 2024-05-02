package ui;

import model.Guest;
import model.RegistrationHistory;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

// referenced ListDemo (https://docs.oracle.com/javase/tutorial/uiswing/examples/components/index.html)

// Represents a window that displays the guest registration list when Manage Registration button is clicked
public class HistoryWindow extends JFrame
        implements ListSelectionListener {
    private RegistrationHistory history;
    private JList list;
    private DefaultListModel listModel;

    private JButton checkOutButton;
    private JButton viewInfoButton;

    // EFFECTS: constructs a window with registration info to be displayed
    public HistoryWindow(RegistrationHistory rh) {
        super("Registration History");
        setSize(500, 300);
        setResizable(false);
        this.history = rh;

        addScrollPane();
        addLowerPanel();
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    // EFFECTS: add a scroll pane showing a list of guests
    public void addScrollPane() {
        listModel = new DefaultListModel();
        for (Guest g: history.getGuests()) {
            listModel.addElement(g.getName());
        }
        list = new JList(listModel);
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.setSelectedIndex(0);
        list.addListSelectionListener(this);
        list.setVisibleRowCount(5);
        JScrollPane listScrollPane = new JScrollPane(list);
        add(listScrollPane, BorderLayout.CENTER);
    }

    // EFFECTS: add a panel consists of buttons related to different actions
    public void addLowerPanel() {
        addCheckOutButton();
        addViewInfoButton();

        JPanel lowerPanel = new JPanel(new BorderLayout());
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel revenueLabel = new JLabel("Total Revenue: " + history.getCurrentRevenue());

        buttonsPanel.add(checkOutButton);
        buttonsPanel.add(viewInfoButton);

        lowerPanel.add(buttonsPanel, BorderLayout.LINE_START);
        lowerPanel.add(revenueLabel, BorderLayout.LINE_END);

        lowerPanel.setBorder(BorderFactory.createEmptyBorder(2, 5, 2, 10));
        add(lowerPanel, BorderLayout.PAGE_END);
    }

    // EFFECTS: designing the check-out button
    public void addCheckOutButton() {
        checkOutButton = new JButton("Check-out");
        checkOutButton.setActionCommand("Check-out");
        checkOutButton.addActionListener(new HistoryWindow.CheckOutListener());
    }

    // EFFECTS: designing the check-out button
    public void addViewInfoButton() {
        viewInfoButton = new JButton("View Info");
        viewInfoButton.setActionCommand("View Info");
        viewInfoButton.addActionListener(new HistoryWindow.ViewInfoListener());
    }

//    public void addPurchaseButton() {
//        purchaseButton = new JButton("Add Purchase");
//        purchaseButton.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                MiniBarWindow miniBarFrame = new MiniBarWindow();
//                miniBarFrame.setVisible(true);
//            }
//        });
//    }

    // Represents an event listener that is called when view-info button is clicked
    class ViewInfoListener implements ActionListener {

        // EFFECTS: displays in a separate pop up window the information of the selected guest
        public void actionPerformed(ActionEvent e) {
            int size = listModel.getSize();

            if (size > 0) {
                int index = list.getSelectedIndex();
                if (index != -1) {
                    Guest guest = history.getGuests().get(index);
                    String roomtype = "";
                    if (guest.getRoomType()) {
                        roomtype = "Single Bed";
                    } else {
                        roomtype = "Double Bed";
                    }
                    String message = "Guest's Name: " + guest.getName()
                            + "\nLength of Stay: " + guest.getDayStaying()
                            + "\nRoom Type: " + roomtype;

                    JOptionPane.showMessageDialog(HistoryWindow.this, message,
                            "Guest Profile", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        }
    }

    // represents an event listener that that is called when check-out button is clicked
    class CheckOutListener implements ActionListener {

        // MODIFIES: this
        // EFFECTS: remove the selected guest when the check-out button is clicked
        //          if no guest is selected set default to last row
        public void actionPerformed(ActionEvent e) {
            int size = listModel.getSize();

            if (size > 0) {
                int index = list.getSelectedIndex();
                if (index != -1) {
                    Guest selectedGuest = history.getGuests().get(index);
                    history.addCheckOutRevenue(selectedGuest.getBill());
                    history.removeGuest(selectedGuest);

                    listModel.remove(index);

                    if (index == listModel.getSize()) {
                        index--;
                    }

                    list.setSelectedIndex(index);
                    list.ensureIndexIsVisible(index);
                }
            }

            // Update the button state after the removal
            checkOutButton.setEnabled(listModel.getSize() > 0);
        }
    }

    // EFFECTS: enable/disable button based on the count of guests
    public void valueChanged(ListSelectionEvent e) {
        if (e.getValueIsAdjusting() == false) {

            if (list.getSelectedIndex() == -1) {
                checkOutButton.setEnabled(false);

            } else {
                checkOutButton.setEnabled(true);
            }
        }
    }
}

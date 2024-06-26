package risiko.local.ui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

import risiko.local.domain.Risiko;
import risiko.local.persistance.Exceptions;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class PlayerInitializationWindow extends JFrame {

    private List<String> playerNames;
    private JTable playerTable;
    private DefaultTableModel tableModel;
    private boolean gameLoaded;
    Exceptions Exceptions = new Exceptions();
    public PlayerInitializationWindow() {
        playerNames = new ArrayList<>();

        setTitle("Player Initialization");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(44, 62, 80));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));

        String[] columnNames = { "Current Players:", "Options" };
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 1; // Only the "Delete" column is editable
            }
        };
        playerTable = new JTable(tableModel);

        // Add a custom cell renderer for the "Delete" column to display buttons
        playerTable.getColumnModel().getColumn(1).setCellRenderer(new ButtonRenderer());
        // Add a custom editor for the "Delete" column to handle button clicks
        playerTable.getColumnModel().getColumn(1).setCellEditor(new ButtonEditor(new JCheckBox()));
        // Set the preferred width for the "Name" column to have more space
        playerTable.getColumnModel().getColumn(0).setPreferredWidth(250);
        // Set the preferred width for the "Delete" column to push it to the right
        playerTable.getColumnModel().getColumn(1).setPreferredWidth(100);

        JScrollPane scrollPane = new JScrollPane(playerTable);
        scrollPane.setBackground(new Color(44, 62, 80));
        scrollPane.setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // Panel for name input and "Add" button
        JPanel inputPanel = new JPanel(new BorderLayout());
        inputPanel.setBackground(new Color(44, 62, 80)); // Set background color
        JTextField playerNameField = new JTextField();
        playerNameField.setFont(new Font("Arial", Font.PLAIN, 15)); // Set font
        playerNameField.setForeground(Color.BLACK); // Set font color to black
        playerNameField.setCaretColor(Color.BLACK); // Set caret color to black
        playerNameField.setBorder(BorderFactory.createMatteBorder(1, 1, 0, 1, new Color(44, 62, 80))); // Add padding
                                                                                                       // (top, left,
                                                                                                       // bottom, right)
        inputPanel.add(playerNameField, BorderLayout.CENTER);

        mainPanel.add(inputPanel, BorderLayout.NORTH);

        // Panel for "Continue" and "Add player" button
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(new Color(44, 62, 80)); // Set background color
        JButton continueButton = new JButton("Continue");
        continueButton.setBackground(new Color(34, 153, 84)); // Set button background color
        continueButton.setForeground(Color.WHITE); // Set button text color
        continueButton.setFont(new Font("Arial", Font.PLAIN, 18)); // Set button font
        continueButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (playerNames.size() < 2) {
                    //JOptionPane.showMessageDialog(PlayerInitializationWindow.this, "Please add at least two players.");
                    Exceptions.showErrorDialog("du musst mindestens 2 spieler hinzufügen");
                    return;
                }
                
                List<String> playerNamesList = readPlayerNamesFromTable();
                Risiko risiko = new Risiko();
                startMainGame(playerNamesList, risiko, false);
            }
        });

        JButton addButton = new JButton("Add Player");
        addButton.setBackground(new Color(34, 153, 84)); // Set button background color
        addButton.setForeground(Color.WHITE); // Set button text color
        addButton.setFont(new Font("Arial", Font.PLAIN, 18)); // Set button font
        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String playerName = playerNameField.getText().trim();
                if (!playerName.isEmpty()) {
                    playerNames.add(playerName);
                    tableModel.addRow(new Object[] { playerName });
                    playerNameField.setText("");
                }
            }
        });
        buttonPanel.add(addButton, BorderLayout.WEST);
        buttonPanel.add(continueButton, BorderLayout.EAST);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);

        setLocationRelativeTo(null); // Center the window on the screen
        setVisible(true);
    }

    private List<String> readPlayerNamesFromTable() {
        playerNames.clear(); // Clear the list before adding player names from the table
        int rowCount = tableModel.getRowCount();
        for (int i = 0; i < rowCount; i++) {
            String playerName = (String) tableModel.getValueAt(i, 0); // Get data from the first column
            playerNames.add(playerName);
        }
        return playerNames;
    }

    private void startMainGame(List<String> playerNames, Risiko risiko, boolean isLoaded) {
        MainGame mainGame = new MainGame(playerNames, risiko, isLoaded);
        dispose(); // Close the PlayerInitializationWindow after starting the MainGame
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new PlayerInitializationWindow();
            }
        });
    }

    private class ButtonRenderer extends JButton implements TableCellRenderer {
        public ButtonRenderer() {
            setOpaque(true);
            setBorderPainted(false);
            setFocusPainted(false);
            setFont(new Font("Arial", Font.PLAIN, 14));
            setHorizontalAlignment(SwingConstants.CENTER); // Align the button to the right
            setPreferredSize(new Dimension(80, 30));
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
                int row, int column) {
            setText("Delete");
            setBackground(new Color(220, 20, 60)); // Set button background color
            setForeground(Color.WHITE);
            return this;
        }
    }

    // Custom cell editor for handling "Delete" button clicks
    private class ButtonEditor extends DefaultCellEditor {
        private JButton button;
        private String playerName;

        public ButtonEditor(JCheckBox checkBox) {
            super(checkBox);
            button = new JButton();
            button.setOpaque(true);
            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    playerNames.remove(playerName);
                    updateTableData();
                }
            });
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row,
                int column) {
            playerName = (String) table.getValueAt(row, 0);
            button.setText("Delete");
            button.setBackground(new Color(220, 20, 60));
            button.setForeground(Color.WHITE);
            return button;
        }
    }

    // Method to update the table data after deleting a player
    private void updateTableData() {
        tableModel.setRowCount(0);
        for (String playerName : playerNames) {
            tableModel.addRow(new Object[] { playerName });
        }
    }

}
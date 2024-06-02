package risiko.local.ui;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import risiko.local.domain.Risiko;
import risiko.local.entities.Spieler;
import java.util.ArrayList;
import java.util.List;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import risiko.local.persistance.Exceptions;
import risiko.local.entities.Land;

public class MainGame extends JFrame {
    private boolean colorMapVisible = true;
    private JTable playerTable;
    private DefaultTableModel playerTableModel;

    private JTable landInfoTable;
    private DefaultTableModel landInfoTableModel;

    private JTable landTable;
    private DefaultTableModel landTableModel;

    private JPanel rightPanel;
    private JPanel bottomPanel;
    private JLabel showPhase;
    private JPanel mainPanel;

    private Spieler currentPlayer;
    private Risiko risiko;
    private List<Spieler> spielerListe = new ArrayList<>();
    private int turn;
    private int anfangsPhase = 0;
    private int playersFinishedDistribution = 0;
    private int scaleHeight = 600;
    private int scaleWidth = (int) (scaleHeight * 1.7778);
    private Land selectedLand;
    private int beginningDistribution = 0;
    private Risiko game;
    private String currentPhase;
    private boolean isSelectingAttackingCountry = true;
    private boolean isSelectingDefendingCountry = false; // Initialize as false initially
    private Land attackingCountry;
    private Land defendingCountry;
    Exceptions Exceptions = new Exceptions();

    public MainGame(List<String> playerNames) {
        risiko = new Risiko(); // Use the class-level property instead of declaring a new local variable

        // Initialize players in the Risiko class using the playerNames list
        for (String playerName : playerNames) {
            risiko.spielerHinzufuegen(playerName);
            // System.out.println("spielerHinzufuegen called with: " + playerName);
        }

        spielerListe = risiko.getSpielerListe();

        risiko.startGame(risiko);

        initializeGUI(spielerListe);
    }

    private void initializeGUI(List<Spieler> spielerListe) {
        setTitle("Phaseninformation");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // JMenuBar
        JMenuBar menuBar = new JMenuBar();

        // JMenu for game actions
        JMenu gameMenu = new JMenu("Game Actions");

        // JMenuItem Save
        JMenuItem saveMenuItem = new JMenuItem("Save Game");
        saveMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                if (fileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
                    File file = fileChooser.getSelectedFile();
                    // saveLoadGameManager.saveGame(game, file.getPath());
                }
            }
        });

        // JMenuItem Load
        JMenuItem loadMenuItem = new JMenuItem("Load Game");
        loadMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                    File file = fileChooser.getSelectedFile();
                    // game = saveLoadGameManager.loadGame(file.getPath());
                    if (game == null) {
                        // handle the error in loading
                    }
                }
            }
        });

        // add menu items to the game menu
        gameMenu.add(saveMenuItem);
        gameMenu.add(loadMenuItem);

        // add game menu to the menu bar
        menuBar.add(gameMenu);

        // set menu bar in the frame
        setJMenuBar(menuBar);

        setSize(800, 800);
        setVisible(true);

        // Create the rightPanel first
        JPanel rightPanel = createRightPanel();

        // Initialize the landInfoTable and country table
        initializeLandInfoTable();
        initializeLandTable(); // Creates an empty table initially

        JPanel bottomPanel = createBottomPanel();

        // Initialize the player table
        initializePlayerTable();

        // Create the main panel
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);
        mainPanel.add(rightPanel, BorderLayout.EAST);
        JLayeredPane middlePanel = null;
        try {
            middlePanel = loadAndInitializeImages();
        } catch (IOException e) {
            Exceptions.showErrorDialog("Bild konnte nicht geladen werden");
        }
        mainPanel.add(middlePanel, BorderLayout.CENTER);
        // Set up the window
        setContentPane(mainPanel);
        pack();
        setLocationRelativeTo(null); // Center the window on the screen
        setVisible(true);

        // Initialize player information with the first player
        if (!spielerListe.isEmpty()) {
            currentPlayer = spielerListe.get(0);
            updatePlayerInfo(currentPlayer);
        }
    }

    private JLayeredPane loadAndInitializeImages() throws IOException {
        BufferedImage colorImage = ImageIO.read(new File("bilder/Color_Map.png"));
        BufferedImage image = ImageIO.read(new File("bilder/Risiko_Karte_1920x10803.png"));

        // Scale the images to the desired dimensions
        BufferedImage scaledColorImage = scaleImage(colorImage, scaleWidth, scaleHeight);
        BufferedImage scaledImage = scaleImage(image, scaleWidth, scaleHeight);

        // Create the ImageIcon for each image
        ImageIcon colorScaledImageIcon = new ImageIcon(scaledColorImage);
        ImageIcon scaledImageIcon = new ImageIcon(scaledImage);

        // Create the JLabels for each image
        JLabel colorImageLabel = new JLabel(colorScaledImageIcon);
        JLabel imageLabel = new JLabel(scaledImageIcon);

        // Create a layered pane and add the image labels with appropriate layer
        // positions
        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setPreferredSize(new Dimension(scaleWidth, scaleHeight));

        // Set a transparent background for the layered pane
        layeredPane.setOpaque(false);

        // Add the images to the layered pane with desired layer positions
        layeredPane.add(imageLabel, Integer.valueOf(0)); // Add the scaled image label above the background
        layeredPane.add(colorImageLabel, Integer.valueOf(1)); // Add the color-based map label to the top layer
        // Make the color map label invisible (transparent)
        colorImageLabel.setOpaque(false);
        colorImageLabel.setIcon(null);
        // Position the images in the center of the layered pane (adjusting only
        // colorImageLabel now)
        int x = (scaleWidth - colorScaledImageIcon.getIconWidth()) / 2;
        int y = (scaleHeight - colorScaledImageIcon.getIconHeight()) / 2;
        colorImageLabel.setBounds(x, y, colorScaledImageIcon.getIconWidth(), colorScaledImageIcon.getIconHeight());
        imageLabel.setBounds(0, 0, scaleWidth, scaleHeight);

        imageLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                int x = e.getX();
                int y = e.getY();
                Color clickedColor = getColorAtPixel(x, y, scaledColorImage);
                // showColorInfo(clickedColor);
                String colour = getColorName(clickedColor);
                selectedLand = risiko.getLandByColour(colour);
                // Update the land information in the table
                updateLandInfo(selectedLand, risiko.getSpielerListe());
            }
        });
        // Add the layered pane to the main panel
        return layeredPane;
    }

    private void updateLandInfo(Land currentLand, List<Spieler> spieler) {
        if (currentLand == null) {
            // If the selected land is null, clear the table or show a message indicating no
            // land is selected.
            // You can implement this based on your application's requirements.
            // For example, you can set all the values in the table to an empty string.
            for (int i = 0; i < landInfoTableModel.getRowCount(); i++) {
                landInfoTableModel.setValueAt("", i, 1);
            }
        } else {
            String landName = currentLand.getName();
            int landArmies = currentLand.getArmee();

            String landOccupant = null;
            for (Spieler player : spieler) {
                if (currentLand.getEingenommenVon() == player.getSpielerID()) {
                    landOccupant = player.getSpielerName();
                    break;
                }
            }

            // Check if the landInfoTable already has rows, if yes, update the existing row
            // data
            if (landInfoTableModel.getRowCount() >= 1) {
                landInfoTableModel.setValueAt(landName, 0, 1); // Update land name
                landInfoTableModel.setValueAt(landArmies, 1, 1); // Update land armies count
                landInfoTableModel.setValueAt(landOccupant, 2, 1); // Update land occupant
            } else {
                // If no rows exist, add a new row with land information
                landInfoTableModel.addRow(new Object[] { landName, landArmies, landOccupant });
            }
        }
    }

    private JPanel createRightPanel() {

        rightPanel = new JPanel(new BorderLayout());
        rightPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Add a component listener to handle window resizing
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                // ... (other code for image scaling remains the same) ...
            }
        });

        return rightPanel;
    }

    private JPanel createBottomPanel() {
        // Create the bottom panel
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        bottomPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Button to trigger phase change and show current phase
        JLabel showPhase = new JLabel(currentPhase);
        showPhase.setText(risiko.getPhase());
        bottomPanel.add(showPhase);

        // ActionListener for the "Phase Change" button
        JButton phaseChangeButton = new JButton("Phase Change");
        phaseChangeButton.addActionListener(e -> {

            if (zusatzArmeenVorhanden(spielerListe)) { // true if no more spare Armies
                JOptionPane.showMessageDialog(this, "Yeah broda next phase came to be");
                risiko.nextPhase(); // change phase

                currentPlayer = risiko.currentPlayer(); // update current player

                updateTables(currentPlayer); // update tables
                updatePhase();
                showPhase.setText(currentPhase);
            } else {
                JOptionPane.showMessageDialog(this, "Please distribute your forces first."); // incase there are spare
                                                                                             // forces left
                return;
            }

        });
        bottomPanel.add(phaseChangeButton); // Add the phase change button to the bottom panel

        // Show "Angreifen" button only during the "ANGREIFEN" phase

        JButton actionButton = new JButton("Action");
        actionButton.addActionListener(e -> {
            updatePhase();
            if ("Verteilenphase".equals(currentPhase) | "AnfangsVerteilenphase".equals(currentPhase)) {
                if (selectedLand != null && risiko.deinLand(currentPlayer, selectedLand)) {
                    // Prompt the user for the amount of troops
                    String input = JOptionPane.showInputDialog(this,
                            "Enter the number of troops to distribute: \n You have "
                                    + currentPlayer.getZusatzSoldaten() + " spare Armies!",
                            "Distribute Troops", JOptionPane.PLAIN_MESSAGE);

                    // Check if the user clicked "Cancel" or closed the dialog
                    if (input != null) {
                        try {
                            int amount = Integer.parseInt(input);
                            risiko.verteilen(currentPlayer, selectedLand, amount);
                            updateTables(currentPlayer);

                        } catch (NumberFormatException ex) {
                            // Show an error message if the input is not a valid integer
                            JOptionPane.showMessageDialog(this, "Invalid input. Please enter a valid number.",
                                    "Invalid Input", JOptionPane.ERROR_MESSAGE);
                        }
                        if (currentPlayer.getZusatzSoldaten() == 0
                                && beginningDistribution != spielerListe.size() - 1) { // comparing
                                                                                       // beginningDistrubution so that
                                                                                       // after the Last player
                                                                                       // noPlayerchanges occur, so that
                                                                                       // PhaseChange changes to the
                                                                                       // right player.
                            beginningDistribution++;
                            turnChange(); // nextPlayer
                        }
                    }
                } else {
                    // Show an error message if no Land object is selected
                    JOptionPane.showMessageDialog(this, "Please select a valid Land first.", "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            } else if ("Angreifenphase".equals(currentPhase)) {
                if (isSelectingAttackingCountry) {
                    JOptionPane.showMessageDialog(this, "Choose from where you'd like to attack.");
                    // Validate if the selected country is a valid attacking country for the current player
                    if (selectedLand != null && risiko.deinLand(currentPlayer, selectedLand) /* && selectedLand.hasNeighboringEnemies(currentPlayer) */) {
                        // Save the selected attacking country
                        attackingCountry = selectedLand;
                        
                        // Prompt the player to choose a target country
                        
                        isSelectingAttackingCountry = false; // Set the flag to false to switch to selecting the target country.
                        isSelectingDefendingCountry = true; // Set the flag to true to indicate that we are now selecting the defending country.
                        selectedLand = null;
                        
                    } else {
                        // Show an error message if the selected country is not a valid attacking country
                        JOptionPane.showMessageDialog(this, "Please select a valid attacking country.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } else if (isSelectingDefendingCountry) {
                    JOptionPane.showMessageDialog(this, "Choose a target country to attack.");
                    // Assuming you have a previously selected attackingCountry variable.
                    // Validate if the selected country is a valid target country for the attack
                    if (selectedLand != null && risiko.sindNachbar(attackingCountry, selectedLand) && !risiko.deinLand(currentPlayer, selectedLand)) {
                        // Perform the attack logic here
                        risiko.angreifen(currentPlayer, attackingCountry, selectedLand);
                        JOptionPane.showMessageDialog(this, "Attack happened");
                        // Reset the defendingCountry variable for future attacks
                        defendingCountry = null;
                        isSelectingDefendingCountry = false; // Set the flag back to false for future attacks.
                        isSelectingAttackingCountry = true; // Set the flag to true to indicate that we are now selecting the attacking country for the next attack.
                        selectedLand = null;
                    } else {
                        // Show an error message if the selected country is not a valid target country
                        JOptionPane.showMessageDialog(this, "Please select a valid target country to attack.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }

            else if ("Verschiebenphase".equals(currentPhase)) {

            }

        });

        bottomPanel.add(actionButton);

        bottomPanel.add(new JScrollPane(landInfoTable));
        landInfoTable.setPreferredScrollableViewportSize(new Dimension(landInfoTable.getPreferredSize().width, 70)); // Set
                                                                                                                     // preferred
                                                                                                                     // height

        return bottomPanel;
    }

    private void updatePhase() {
        currentPhase = risiko.getCurrentPhase();
    }

    private void updateTables(Spieler currentPlayer) {
        updatePlayerInfo(currentPlayer);
        updateLandInfo(selectedLand, spielerListe);
        updateLandTable(risiko.getCountriesOwned(currentPlayer));
    }

    private void initializeLandInfoTable() {
        // Define the data for the rows
        String[] landProperties = { "Land Name", "Land Armies", "Land Occupant" };

        // Create the landInfoTable model with empty data for now
        Object[][] tableData = new Object[3][2];
        for (int i = 0; i < 3; i++) {
            tableData[i][0] = landProperties[i];
            tableData[i][1] = ""; // Placeholder for land information
        }

        // Create the custom table model for landInfoTable
        DefaultTableModel customLandInfoTableModel = new DefaultTableModel(tableData, new Object[] { "", "" }) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make the table uneditable
            }
        };

        // Set the customLandInfoTableModel as the landInfoTableModel for the class
        landInfoTableModel = customLandInfoTableModel;

        // Create the JTable with the custom model
        landInfoTable = new JTable(landInfoTableModel);

        // Set preferred column widths (optional, adjust as needed)
        landInfoTable.getColumnModel().getColumn(0).setPreferredWidth(150);
        landInfoTable.getColumnModel().getColumn(1).setPreferredWidth(150);
    }

    private boolean zusatzArmeenVorhanden(List<Spieler> spielerListe) {
        for (Spieler spieler : spielerListe) {
            if (spieler.getZusatzSoldaten() != 0) {
                return false;
            }
        }
        return true;
    }

    private void initializeLandTable() {
        // Define the data for the rows in the land table (country table)
        String[] landProperties = { "Land Name", "Land Armies" };

        // Create the landTableModel with empty data for now
        Object[][] tableData = new Object[0][2];

        // Create the custom table model for the land table (country table)
        DefaultTableModel customLandTableModel = new DefaultTableModel(tableData, landProperties) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make the table uneditable
            }
        };

        // Set the customLandTableModel as the landTableModel for the class
        landTableModel = customLandTableModel;

        // Create the JTable with the custom model
        landTable = new JTable(landTableModel);

        // Set preferred column widths (optional, adjust as needed)
        landTable.getColumnModel().getColumn(0).setPreferredWidth(150);
        landTable.getColumnModel().getColumn(1).setPreferredWidth(150);
    }

    private void initializePlayerTable() {
        // Define the data for the three rows
        String[] rowNames = { "Player Name", "Conquered Countries", "Mission", "Zusatzarmee" };

        // Create the player table model with the data for the three rows
        Object[][] tableData = new Object[4][4];
        for (int i = 0; i < 4; i++) {
            tableData[i][0] = rowNames[i];
            tableData[i][1] = ""; // Placeholder for player information
            tableData[i][2] = "";
            tableData[i][3] = "";
        }

        // Create the custom table model for playerTable
        DefaultTableModel customPlayerTableModel = new DefaultTableModel(tableData, new Object[] { "", "" }) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make the table uneditable
            }
        };

        // Set the customPlayerTableModel as the playerTableModel for the class
        playerTableModel = customPlayerTableModel;

        // Create the JTable with the custom model
        playerTable = new JTable(playerTableModel);

        // Set preferred column widths (optional, adjust as needed)
        playerTable.getColumnModel().getColumn(0).setPreferredWidth(150);
        playerTable.getColumnModel().getColumn(1).setPreferredWidth(300);

        DefaultTableCellRenderer customRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                    boolean hasFocus, int row, int column) {
                Component component = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row,
                        column);
                // Set the desired font here
                component.setFont(new Font("Arial", Font.PLAIN, 14));
                return component;
            }
        };

        playerTable.setDefaultRenderer(Object.class, customRenderer);

        // Initialize the country table
        initializeLandTable();

        // Create a GridLayout for the rightPanel with 2 rows and 1 column
        rightPanel.setLayout(new GridLayout(2, 1));

        // Add the player table to the second row of the rightPanel
        JPanel playerTablePanel = new JPanel(new BorderLayout());
        playerTablePanel.add(new JScrollPane(playerTable), BorderLayout.CENTER);
        rightPanel.add(playerTablePanel);

        // Add the country table to the first row of the rightPanel
        JPanel landTablePanel = new JPanel(new BorderLayout());
        landTablePanel.add(new JScrollPane(landTable), BorderLayout.CENTER);
        rightPanel.add(landTablePanel);

    }

    private void updatePlayerInfo(Spieler currentPlayer) {
        String playerName = currentPlayer.getName();
        int conqueredCountries = risiko.spielerCountriesOnwedCount(currentPlayer); // Use the sl object to access
                                                                                   // SpielLogik method
        String mission = currentPlayer.getMission().toString();
        int zusatzArmee = risiko.getZusatzArmee(currentPlayer);
        // Check if the playerTable already has rows, if yes, update the existing row
        // data
        if (playerTableModel.getRowCount() >= 1) {
            playerTableModel.setValueAt(playerName, 0, 1); // Update player name
            playerTableModel.setValueAt(conqueredCountries, 1, 1); // Update conquered countries count
            playerTableModel.setValueAt(mission, 2, 1); // Update mission
            playerTableModel.setValueAt(zusatzArmee, 3, 1);
        } else {
            // If no rows exist, add a new row with player information
            playerTableModel.addRow(new Object[] { playerName, conqueredCountries, mission });
        }

        updateLandTable(risiko.getCountriesOwned(currentPlayer));
    }

    private void updateLandTable(List<Land> countries) {
        // Clear the existing data in the land table (country table)
        landTableModel.setRowCount(0);

        // Populate the land table (country table) with the current player's countries
        for (Land country : countries) {
            String countryName = country.getName();
            int armies = country.getArmee();

            // Add a new row with country information
            landTableModel.addRow(new Object[] { countryName, armies });
        }
    }

    public void phaseChange() {

    }

    public void turnChange() {
        risiko.nextPlayer();
        int newIndex = risiko.getSpielerIndex();

        currentPlayer = spielerListe.get(newIndex); // Get the next player from the list

        // Update player informa tion on the GUI for the new current player
        updatePlayerInfo(currentPlayer);

        // Update the country table with the current player's countries
        updateLandTable(risiko.getCountriesOwned(currentPlayer));
        selectedLand = null;
    }

    private BufferedImage scaleImage(BufferedImage originalImage, int width, int height) {
        BufferedImage scaledImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = scaledImage.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2d.drawImage(originalImage, 0, 0, width, height, null);
        g2d.dispose();
        return scaledImage;
    }

    private Color getColorAtPixel(int x, int y, BufferedImage image) {
        // Make sure the clicked coordinates are within the image bounds
        if (x >= 0 && x < image.getWidth() && y >= 0 && y < image.getHeight()) {
            // Get the RGB value of the pixel at the clicked coordinates
            int rgb = image.getRGB(x, y);

            // Create a Color object from the RGB value
            return new Color(rgb);
        }
        return null; // Return null if the clicked coordinates are out of bounds
    }

    private String getColorName(Color color) {
        // Compare the color RGB values with the predefined colors and return the
        // corresponding color code
        if (color.equals(Color.RED)) {
            return "#FF0000";
        } else if (color.equals(Color.GREEN)) {
            return "#00FF00";
        } else if (color.equals(Color.BLUE)) {
            return "#0000FF";
        } else if (color.equals(Color.YELLOW)) {
            return "#FFFF00";
        } else if (color.equals(Color.CYAN)) {
            return "#00FFFF";
        } else if (color.equals(Color.MAGENTA)) {
            return "#FF00FF";
        } else if (color.equals(Color.WHITE)) {
            return "#FFFFFF";
        } else if (color.equals(Color.BLACK)) {
            return "#000000";
        } else if (color.equals(Color.GRAY)) {
            return "#808080";
        } else if (color.equals(new Color(165, 42, 42))) {
            return "#A52A2A"; // Brown
        } else if (color.equals(Color.ORANGE)) {
            return "#FFA500";
        } else if (color.equals(Color.PINK)) {
            return "#FFC0CB";
        } else if (color.equals(new Color(128, 0, 128))) {
            return "#800080"; // Purple
        } else if (color.equals(new Color(255, 215, 0))) {
            return "#FFD700"; // Gold
        } else if (color.equals(new Color(165, 255, 8))) {
            return "#A5FF08";
        } else if (color.equals(new Color(255, 130, 8))) {
            return "#FF8208";
        } else if (color.equals(new Color(255, 8, 101))) {
            return "#FF0865";
        } else if (color.equals(new Color(12, 112, 61))) {
            return "#0C703D";
        } else if (color.equals(Color.LIGHT_GRAY)) {
            return "#C0C0C0";
        } else if (color.equals(new Color(0, 0, 128))) {
            return "#000080"; // Navy
        } else if (color.equals(new Color(0, 128, 128))) {
            return "#008080"; // Teal
        } else if (color.equals(new Color(128, 128, 0))) {
            return "#808000"; // Olive
        } else if (color.equals(new Color(128, 0, 0))) {
            return "#800000"; // Maroon
        } else if (color.equals(new Color(0, 255, 0))) {
            return "#00FF00";
        } else if (color.equals(Color.CYAN)) {
            return "#00FFFF";
        } else if (color.equals(Color.MAGENTA)) {
            return "#FF00FF";
        } else if (color.equals(new Color(75, 0, 130))) {
            return "#4B0082"; // Indigo
        } else if (color.equals(new Color(230, 230, 250))) {
            return "#E6E6FA"; // Lavender
        } else if (color.equals(new Color(255, 127, 80))) {
            return "#FF7F50"; // Coral
        } else if (color.equals(new Color(64, 224, 208))) {
            return "#40E0D0"; // Turquoise
        } else if (color.equals(new Color(240, 230, 140))) {
            return "#F0E68C"; // Khaki
        } else if (color.equals(new Color(218, 112, 214))) {
            return "#DA70D6"; // Orchid
        } else if (color.equals(new Color(250, 128, 114))) {
            return "#FA8072"; // Salmon
        } else if (color.equals(new Color(245, 245, 220))) {
            return "#F5F5DC"; // Beige
        } else if (color.equals(new Color(135, 206, 235))) {
            return "#87CEEB"; // Sky Blue
        } else if (color.equals(new Color(0, 100, 0))) {
            return "#006400"; // Dark Green
        } else if (color.equals(new Color(0, 0, 139))) {
            return "#00008B"; // Dark Blue
        } else if (color.equals(new Color(139, 0, 0))) {
            return "#8B0000"; // Dark Red
        } else if (color.equals(new Color(169, 169, 169))) {
            return "#A9A9A9"; // Dark Gray
        } else if (color.equals(new Color(91, 58, 41))) {
            return "#5B3A29"; // Dark Brown
        } else if (color.equals(new Color(144, 238, 144))) {
            return "#90EE90"; // Light Green
        } else if (color.equals(new Color(173, 216, 230))) {
            return "#ADD8E6"; // Light Blue
        } else if (color.equals(new Color(255, 182, 193))) {
            return "#FFB6C1"; // Light Pink
        } else if (color.equals(new Color(230, 230, 250))) {
            return "#E6E6FA"; // Light Purple
        } else if (color.equals(new Color(211, 211, 211))) {
            return "#D3D3D3"; // Light Gray
        } else if (color.equals(new Color(255, 255, 224))) {
            return "#FFFFE0"; // Light Yellow
        } else if (color.equals(new Color(255, 192, 203))) {
            return "#FFC0CB";
        } else if (color.equals(new Color(255, 165, 0))) {
            return "#FFA500";
        } else if (color.equals(new Color(127, 85, 0))) {
            return "#7F5500";
        } else {
            return "Custom Color";
        }
    }

    public static void main(String[] args) {
        MainMenuWindow mainMenu = new MainMenuWindow();
    }
}
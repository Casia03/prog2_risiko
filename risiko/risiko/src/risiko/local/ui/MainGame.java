package risiko.local.ui;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import risiko.local.domain.Risiko;
import risiko.local.entities.Spieler;
import risiko.local.entities.Turn.Phase;

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
    private JTable spielerTable;
    private DefaultTableModel spielerTableModel;

    private JTable landInfoTable;
    private DefaultTableModel landInfoTableModel;

    private JTable landTable;
    private DefaultTableModel landTableModel;

    private JPanel rightPanel;
    private JPanel bottomPanel;
    private JLabel showPhase;
    private JPanel mainPanel;

    private Spieler currentSpieler;
    private Risiko risiko;
    private List<Spieler> spielerListe = new ArrayList<>();
    private int turn;
    private int scaleHeight = 700;
    private int scaleWidth = (int) (scaleHeight * 1.7778);
    private int ausgewaehltesLand;
    private int beginningDistribution = 0;
    private Risiko game;
    private Phase currentPhase;
    private boolean isSelectingAttackingCountry = true;
    private boolean isSelectingDefendingCountry = false; // Initialize as false initially
    private int attackingCountry;
    private int i = 0;
    Exceptions Exceptions = new Exceptions();

    public MainGame(List<String> spielerNameListe) {
        risiko = new Risiko(); // Use the class-level property instead of declaring a new local variable

        // Initialize spielers in the Risiko class using the spielerNames list
        for (String spielerName : spielerNameListe) {
            risiko.spielerHinzufuegen(spielerName);
            // System.out.println("spielerHinzufuegen called with: " + spielerName);
        }
        //15112512
        //penis
        risiko.newGame(risiko);
        
        spielerListe = risiko.getSpielerListe();

        

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

        setSize(scaleWidth, scaleHeight);
        setVisible(true);

        // Create the rightPanel first
        JPanel rightPanel = createRightPanel();

        // Initialize the landInfoTable and country table
        initializeLandInfoTable();
        initializeLandTable(); // Creates an empty table initially

        JPanel bottomPanel = createBottomPanel();

        // Initialize the spieler table
        initializeSpielerTable();

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

        // Initialize spieler information with the first spieler
        if (!spielerListe.isEmpty()) {
            currentSpieler = spielerListe.get(0);
            updateSpielerInfo(currentSpieler);
        }
    }

    private JLayeredPane loadAndInitializeImages() throws IOException {
        BufferedImage colorImage = ImageIO.read(new File("risiko\\risiko\\src\\risiko\\local\\bilder\\Color_Map.png"));
        BufferedImage image = ImageIO.read(new File("risiko\\risiko\\src\\risiko\\local\\bilder\\Risiko_Karte_1920x10803.png"));
    
        // Scale the images to the desired dimensions
        BufferedImage scaledColorImage = scaleImage(colorImage, scaleWidth, scaleHeight);
        BufferedImage scaledImage = scaleImage(image, scaleWidth, scaleHeight);
    
        // Create the ImageIcon for each image
        ImageIcon colorScaledImageIcon = new ImageIcon(scaledColorImage);
        ImageIcon scaledImageIcon = new ImageIcon(scaledImage);
    
        // Create the JLabels for each image
        JLabel colorImageLabel = new JLabel(colorScaledImageIcon);
        JLabel imageLabel = new JLabel(scaledImageIcon);
        // Add a JLabel to display the selected image

        JLabel selectedImageLabel = new JLabel();
    
        // Create a layered pane and add the image labels with appropriate layer positions
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

        layeredPane.addComponentListener((new ComponentAdapter(){
            public void componentResized(ComponentEvent componentEvent){
                Rectangle window = layeredPane.getBounds();

                int windowWidth = window.width;  // Annehmen, dass scaleWidth die Fensterbreite ist
                int windowHeight = window.height;  // Annehmen, dass scaleHeight die Fensterhöhe ist

                int imageWidth = colorScaledImageIcon.getIconWidth();
                int imageHeight = colorScaledImageIcon.getIconHeight();

                float widthRatio = (float) windowWidth / imageWidth;
                float heightRatio = (float) windowHeight / imageHeight;
                float scaleFactor = Math.min(widthRatio, heightRatio);  // Um das Bild proportional zu skalieren

                int newImageWidth = (int) (imageWidth * scaleFactor);
                int newImageHeight = (int) (imageHeight * scaleFactor);

                int x = (windowWidth - newImageWidth) / 2;
                int y = (windowHeight - newImageHeight) / 2;

                // FARBENKARTE ZUR KLICK ERKENNUNG
                colorImageLabel.setBounds(0, 0, scaleWidth, scaleHeight);

                // SPIELKARTE 
                imageLabel.setBounds(0, 0, scaleWidth, scaleHeight);
                
                // LAND HIGHLIGHT
                selectedImageLabel.setBounds(0, 0, scaleWidth, scaleHeight);
                
            }
        }));
        
        
        //displayPlayerCountries(selectedImageLabel);
        layeredPane.add(selectedImageLabel, Integer.valueOf(2)); // Add it above other layers
        
        imageLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                // X Y position der maus werden abgelesen
                int x = e.getX();
                int y = e.getY();
                // Die Farbe (unsichbare farb karte) an der Stelle wo gekiclt wurde wird abgelesen
                Color clickedColor = getColorAtPixel(x, y, scaledColorImage);
                String colour = getColorName(clickedColor);
                ausgewaehltesLand = risiko.getLandByColour(colour);
                // Update the land information in the table
                updateLandInfo(risiko.getLand(ausgewaehltesLand), risiko.getSpielerListe());
    
                try {
                    // Load the image corresponding to the selected land
                    int[] eigeneLaender = risiko.getEigeneLaenderId();
                    Color tint = Color.BLACK;

                    for (int land : eigeneLaender) {
                            if (land == 0) {
                                continue; // Skip if land ID is zero
                            }
                            // Determine the tint color
                            if (ausgewaehltesLand == land) {
                                tint = Color.GREEN; // Highlight selected country in red
                                // exit = true; // Set exit to true to break the loop after processing
                                break;
                            } else {
                                tint = Color.RED; // Tint other player countries in green
                                
                            }
                        }
                        String imagePath = String.format("risiko\\risiko\\src\\risiko\\local\\bilder\\42\\%d.png", ausgewaehltesLand);
                        BufferedImage selectedImage = ImageIO.read(new File(imagePath));
    
                        // Scale the image if necessary
                        BufferedImage scaledSelectedImage = scaleImage(selectedImage, scaleWidth, scaleHeight);
    
                        // Tint the image
                        BufferedImage tintedImage = tintImage(scaledSelectedImage, tint);
    
                        // Update the label with the tinted image
                        ImageIcon selectedImageIcon = new ImageIcon(tintedImage);
                        selectedImageLabel.setIcon(selectedImageIcon);
                        selectedImageLabel.setVisible(true); // Ensure the label is visible
                    

                } catch (IOException ex) {
                    ex.printStackTrace();
                    // Handle the exception, e.g., by showing a default image or an error message
                }
            }
        });
        displayPlayerCountries(selectedImageLabel);
        // Add the layered pane to the main panel
        return layeredPane;
    }

    public BufferedImage tintImage(BufferedImage image, Color color) {
        int width = image.getWidth();
        int height = image.getHeight();
        
        BufferedImage tintedImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        
        Graphics2D g = tintedImg.createGraphics();
        g.drawImage(image, 0, 0, null);
        g.setComposite(AlphaComposite.SrcAtop);
        g.setColor(color);
        g.fillRect(0, 0, width, height);
        g.dispose();
        
        return tintedImg;
    }
        
    // Method to load and display the player's countries
    private void displayPlayerCountries(JLabel selectedImageLabel) {
        try {
            // Clear previously displayed images
            selectedImageLabel.removeAll();

            // Load images corresponding to the player's countries
            int[] laender = risiko.getEigeneLaenderId();
            
            for (int landId : laender) {
                if(landId != 0){
                    String imagePath = String.format("risiko\\risiko\\src\\risiko\\local\\bilder\\42\\%d.png", landId);
                    File imageFile = new File(imagePath);

                    if (!imageFile.exists()) {
                        System.err.println("Image file not found: " + imagePath);
                        continue; // Skip if image file does not exist
                    }

                    BufferedImage selectedImage = ImageIO.read(imageFile);
                    BufferedImage scaledSelectedImage = scaleImage(selectedImage, scaleWidth, scaleHeight);

                    // Tint the image with a specific color (example: green color)
                    Color tint = Color.GREEN;
                    BufferedImage tintedImage = tintImage(scaledSelectedImage, tint);

                    // Create ImageIcon and JLabel for the tinted image
                    ImageIcon selectedImageIcon = new ImageIcon(tintedImage);
                    JLabel selectedImageJLabel = new JLabel(selectedImageIcon);

                    // Add the tinted image JLabel to the selectedImageLabel
                    selectedImageLabel.add(selectedImageJLabel);
                }
            }

            // Ensure the selectedImageLabel is visible and repaint
            selectedImageLabel.setVisible(true);
            selectedImageLabel.revalidate();
            selectedImageLabel.repaint();

        } catch (IOException ex) {
            ex.printStackTrace();
            // Handle the exception, e.g., by showing a default image or an error message
        }
    }

    private void updateLandInfo(Land currentLand, List<Spieler> spielerListe) {
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
            for (Spieler spieler : spielerListe) {
                if (currentLand.getEingenommenVon() == spieler.getSpielerID()) {
                    landOccupant = spieler.getSpielerName();
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

    private JPanel createBottomPanel(){
        // Create the bottom panel
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        bottomPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
    
        // Create and configure phase change button
        JButton phaseChangeButton = new JButton("Phase Change");
        phaseChangeButton.addActionListener(e -> {
            if (zusatzArmeenVorhanden(spielerListe)) {
                JOptionPane.showMessageDialog(this, "Yeah broda next phase came to be");
                risiko.nextPhase(); // change phase
                currentSpieler = risiko.getJetzigerSpieler(); // update current spieler
                updateTables(currentSpieler); // update tables
                updatePhase(); // update phase 
            } else {
                JOptionPane.showMessageDialog(this, "Please distribute your forces first.");
            }
        });
        bottomPanel.add(phaseChangeButton); // Add the phase change button to the bottom panel


        // Show "Angreifen" button only during the "ANGREIFEN" phase

        JButton actionButton = new JButton(risiko.getPhase().toString());
        actionButton.addActionListener(e -> {
            updatePhase();
                int[] eigeneLaender = risiko.getEigeneLaenderId();
                boolean canProceed = false;
                switch(currentPhase){
                    case ERSTVERTEILEN:

                    while(risiko.jetzigerSpielerHatZusatzarmee()){
                        for(int land : eigeneLaender){
                            if(ausgewaehltesLand == land){
                                canProceed = true;
                                break;
                            }
                        }
                        if(canProceed){
                            boolean validInput = false; // Flag to check if input is valid
                            while (!validInput) {
                                String input = JOptionPane.showInputDialog(this,
                                        "Enter the number of troops to distribute: \n You have " + currentSpieler.getZusatzArmee() + " spare Armies!",
                                        "Distribute Troops", JOptionPane.PLAIN_MESSAGE);

                                // Check if the input dialog was closed or canceled
                                if (input == null || input.isEmpty()) {
                                    // Handle cancel or close window action as valid input (no action needed)
                                    break; // Exit the loop if canceled or closed window
                                }

                                try {
                                    // Validate and parse the input as an integer
                                    int armeeAnzahl = Exceptions.readInt(input, 1, currentSpieler.getZusatzArmee());

                                    // If valid, distribute the troops
                                    risiko.verteilen(ausgewaehltesLand, armeeAnzahl);
                                    updateTables(currentSpieler);
                                    validInput = true; // Set the flag to true to exit the loop
                                } catch (NumberFormatException ex) {
                                    // Handle invalid input that cannot be parsed to an integer
                                    Exceptions.showErrorDialog("Invalid input. Please enter a valid number.");
                                } catch (IllegalArgumentException ex) {
                                    // Display error for invalid number range
                                    Exceptions.showErrorDialog("Invalid input. " + ex.getMessage());
                                } catch (Exception ex) {
                                    // Handle any other unexpected exceptions
                                    Exceptions.showErrorDialog("An unexpected error occurred: " + ex.getMessage());
                                }
                            }
                        }
                    }

                    int result = JOptionPane.showConfirmDialog(null,"Du hast keine Zusatzarmee mehr. Willst auf den nächsten Spieler ändern?", 
                        "Frage", 
                        JOptionPane.YES_NO_OPTION, 
                        JOptionPane.QUESTION_MESSAGE);

                    if (result == JOptionPane.YES_OPTION) {
                        risiko.nextPlayer(); // Naechster spieler
                        currentSpieler = risiko.getJetzigerSpieler();
                        updateTables(currentSpieler);
                        i += 1;
                        if (i == risiko.getAnzahlSpieler()) { // wenn alle spieler zusatzarmee verteilt haben dann gehts in die naechste phase
                            JOptionPane.showConfirmDialog(null, "Alle Spieler haben ihre zusatzarmee verteilt! Der Erste Spieler wird in die Angreifephase weitergeleitet.", 
                            "Info",  
                            JOptionPane.INFORMATION_MESSAGE);
                            risiko.nextPhase();
                        }
                    }
                
                
                break;

                case VERTEILEN:
                    String input = JOptionPane.showInputDialog(this,
                        "Enter the number of troops to distribute: \n You have " + currentSpieler.getZusatzArmee() + " spare Armies!",
                        "Distribute Troops", JOptionPane.PLAIN_MESSAGE);
                    if (input != null) {
                        try {
                            int armeeAnzahl = Integer.parseInt(input);
                            risiko.verteilen(ausgewaehltesLand, armeeAnzahl);
                            updateTables(currentSpieler);
                        } catch (NumberFormatException ex) {
                            JOptionPane.showMessageDialog(this, "Invalid input. Please enter a valid number.", "Invalid Input",
                                    JOptionPane.ERROR_MESSAGE);
                        }
                        if (currentSpieler.getZusatzArmee() == 0 && beginningDistribution != spielerListe.size() - 1) {
                            beginningDistribution++;
                            phaseChange();
                        }
                    }
                    
                    break;
                
                case ANGREIFFEN:
                    if (isSelectingAttackingCountry) {
                        JOptionPane.showMessageDialog(this, "Choose from where you'd like to attack.");
                        if (risiko.getLand(ausgewaehltesLand) != null && risiko.istDeinLand(ausgewaehltesLand)) {
                            attackingCountry = ausgewaehltesLand;
                            isSelectingAttackingCountry = false;
                            isSelectingDefendingCountry = true;
                        } else {
                            JOptionPane.showMessageDialog(this, "Please select a valid attacking country.", "Error",
                                    JOptionPane.ERROR_MESSAGE);
                        }
                    } else if (isSelectingDefendingCountry) {
                        JOptionPane.showMessageDialog(this, "Choose a target country to attack.");
                        input = JOptionPane.showInputDialog(this,
                                "Enter the number of troops to attack: \n You have " + currentSpieler.getZusatzArmee() + " spare Armies! You can use up to three at a time",
                                "Attack number", JOptionPane.PLAIN_MESSAGE);
                        if (input != null) {
                            if (risiko.getLand(ausgewaehltesLand) != null && risiko.sindNachbar(attackingCountry, ausgewaehltesLand)
                                    && !risiko.istDeinLand(ausgewaehltesLand)) {
                                try {
                                    int armeeAnzahl = Integer.parseInt(input);
                                    risiko.angreifen(attackingCountry, ausgewaehltesLand, armeeAnzahl);
                                    JOptionPane.showMessageDialog(this, "Attack happened");
                                    isSelectingDefendingCountry = false;
                                    isSelectingAttackingCountry = true;
                                } catch (NumberFormatException ex) {
                                    JOptionPane.showMessageDialog(this, "Invalid input. Please enter a valid number.", "Invalid Input",
                                            JOptionPane.ERROR_MESSAGE);
                                }
                            } else {
                                JOptionPane.showMessageDialog(this, "Please select a valid target country to attack.", "Error",
                                        JOptionPane.ERROR_MESSAGE);
                            }
                        }
                    }
                    break;
                case VERSCHIEBEN:
                    break;
            }

        });
        bottomPanel.add(actionButton); // Add the action button to the bottom panel

    bottomPanel.add(new JScrollPane(landInfoTable));
    landInfoTable.setPreferredScrollableViewportSize(new Dimension(landInfoTable.getPreferredSize().width, 70)); // Set preferred height

    // Maiks button
    JButton highlightButton = new JButton("Eigene Länder aufleuchten lassen");
    highlightButton.addActionListener(e -> highlightOwnCountries());
    bottomPanel.add(highlightButton); // Add button to the bottom panel

    return bottomPanel;
}

private void highlightOwnCountries() {
    int[] eigeneLaender = risiko.getEigeneLaenderId();

    try {
        // Create a panel to hold all the highlighted country images
        JPanel displayPanel = new JPanel();
        displayPanel.setLayout(new GridLayout(0, 7)); // Adjust layout as needed

        for (int land : eigeneLaender) {
            if (land == 0) {
                continue; // Skip if land ID is zero
            }

            String imagePath = String.format("risiko/risiko/src/risiko/local/bilder/42/%d.png", land);
            BufferedImage landImage = ImageIO.read(new File(imagePath));

            // Scale the image if necessary
            BufferedImage scaledLandImage = scaleImage(landImage, scaleWidth, scaleHeight);

            // Tint the image in green
            BufferedImage tintedImage = tintImage(scaledLandImage, Color.GREEN);

            // Update the label with the tinted image
            ImageIcon landImageIcon = new ImageIcon(tintedImage);
            JLabel selectedImageLabel = new JLabel();
            selectedImageLabel.setIcon(landImageIcon);
            selectedImageLabel.setVisible(true); // Ensure the label is visible

            // Add the label to the display panel
            displayPanel.add(selectedImageLabel);
        }
        JLabel selectedImageLabel = new JLabel();
        // Update the main display panel or frame with the new display panel
        selectedImageLabel.removeAll(); // Clear previous contents
        selectedImageLabel.add(displayPanel);
        selectedImageLabel.revalidate();
        selectedImageLabel.repaint();

    } catch (IOException ex) {
        ex.printStackTrace();
        // Handle the exception, e.g., by showing a default image or an error message
    }
}
    
    
        // return bottomPanel;
    private void updatePhase() {
        currentPhase = risiko.getPhase();
    }

    private void updateTables(Spieler currentSpieler) {
        updateSpielerInfo(currentSpieler);
        updateLandInfo(risiko.getLand(ausgewaehltesLand), spielerListe);
        updateLandTable(risiko.getEigeneLaender());
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
            if (spieler.getZusatzArmee() != 0) {
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
        landTable.getColumnModel().getColumn(0).setPreferredWidth(50);
        landTable.getColumnModel().getColumn(1).setPreferredWidth(150);
    }

    private void initializeSpielerTable() {
        // Define the data for the three rows
        String[] rowNames = { "Spieler Name", "Conquered Countries", "Mission", "Zusatzarmee" };

        // Create the spieler table model with the data for the three rows
        Object[][] tableData = new Object[4][4];
        for (int i = 0; i < 4; i++) {
            tableData[i][0] = rowNames[i];
            tableData[i][1] = ""; // Placeholder for spieler information
            tableData[i][2] = "";
            tableData[i][3] = "";
        }

        // Create the custom table model for spielerTable
        DefaultTableModel customSpielerTableModel = new DefaultTableModel(tableData, new Object[] { "", "" }) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make the table uneditable
            }
        };

        // Set the customSpielerTableModel as the spielerTableModel for the class
        spielerTableModel = customSpielerTableModel;

        // Create the JTable with the custom model
        spielerTable = new JTable(spielerTableModel);

        // Set preferred column widths (optional, adjust as needed)
        spielerTable.getColumnModel().getColumn(0).setPreferredWidth(150);
        spielerTable.getColumnModel().getColumn(1).setPreferredWidth(300);

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

        spielerTable.setDefaultRenderer(Object.class, customRenderer);

        // Initialize the country table
        initializeLandTable();

        // Create a GridLayout for the rightPanel with 2 rows and 1 column
        rightPanel.setLayout(new GridLayout(2, 1));

        // Add the spieler table to the second row of the rightPanel
        JPanel spielerTablePanel = new JPanel(new BorderLayout());
        spielerTablePanel.add(new JScrollPane(spielerTable), BorderLayout.CENTER);
        rightPanel.add(spielerTablePanel);

        // Add the country table to the first row of the rightPanel
        JPanel landTablePanel = new JPanel(new BorderLayout());
        landTablePanel.add(new JScrollPane(landTable), BorderLayout.CENTER);
        rightPanel.add(landTablePanel);

    }

    private void updateSpielerInfo(Spieler currentSpieler) {
        String spielerName = currentSpieler.getSpielerName();
        int conqueredCountries = risiko.getSpielerLaenderAnzahl(); // Use the sl object to access
                                                                                   // SpielLogik method
        String mission = risiko.getJetzigerSpielerMission();
        int zusatzArmee = risiko.getZusatzArmee();
       
        // Check if the spielerTable already has rows, if yes, update the existing row
        // data
        if (spielerTableModel.getRowCount() >= 1) {
            spielerTableModel.setValueAt(spielerName, 0, 1); // Update spieler name
            spielerTableModel.setValueAt(conqueredCountries, 1, 1); // Update conquered countries count
            spielerTableModel.setValueAt(mission, 2, 1); // Update mission
            spielerTableModel.setValueAt(zusatzArmee, 3, 1);
           
        } else {
            // If no rows exist, add a new row with spieler information
            spielerTableModel.addRow(new Object[] { spielerName, conqueredCountries, mission });
        }

        updateLandTable(risiko.getEigeneLaender());
    }

    private void updateLandTable(List<Land> countries) {
        // Clear the existing data in the land table (country table)
        landTableModel.setRowCount(0);

        // Populate the land table (country table) with the current spieler's countries
        for (Land country : countries) {
            String countryName = country.getName();
            int armies = country.getArmee();

            // Add a new row with country information
            landTableModel.addRow(new Object[] { countryName, armies });
        }
    }

    public void phaseChange() {
        risiko.nextPhase();
        // risiko.nextSpieler();
        int newIndex = risiko.getSpielerID();

        currentSpieler = spielerListe.get(newIndex); // Get the next spieler from the list

        // Update spieler informa tion on the GUI for the new current spieler
        updateSpielerInfo(currentSpieler);

        // Update the country table with the current spieler's countries
        updateLandTable(risiko.getEigeneLaender());
        // !!!!! ausgewaehltesLand = null; !!!!!
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
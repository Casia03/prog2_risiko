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
import java.util.Arrays;
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
    private JLayeredPane layeredPane = new JLayeredPane();
    private JPanel rightPanel;
    private JPanel bottomPanel;
    private JLabel showPhase;
    private JPanel mainPanel;

    private Spieler currentSpieler;
    private Risiko risiko;
    private List<Spieler> spielerListe = new ArrayList<>();
    private int turn;
    private int scaleHeight = 600;
    private int scaleWidth = (int) (scaleHeight * 1.7778);
    private int ausgewaehltesLand;
    private int beginningDistribution = 0;
    private Risiko game;
    private Phase currentPhase;
    private boolean isSelectingAttackingCountry = false;
    private boolean isSelectingDefendingCountry = false;
    private boolean isSelectingVerschiebeVonCountry = false;
    private boolean isSelectingVerschiebeNachCountry = false;
    private boolean hatEinheitskarteBekommen = false;
    private int attackingCountry = 0;
    private int defendingCountry = 0;
    private int i = 0;
    Exceptions Exceptions = new Exceptions();

    public MainGame(List<String> spielerNameListe, Risiko risiko, boolean isLoaded) {
        this.risiko = risiko; // Use the class-level property instead of declaring a new local variable

        if (!isLoaded) { // Neues Spiel Falls nicht geladen
            // Initialize spielers in the Risiko class using the spielerNames list
            for (String spielerName : spielerNameListe) {
                risiko.spielerHinzufuegen(spielerName);
                // System.out.println("spielerHinzufuegen called with: " + spielerName);
            }
            risiko.newGame(risiko);
            currentPhase = risiko.getPhase();

            spielerListe = risiko.getSpielerListe();

            initializeGUI(spielerListe);
        } else { // Geladenes Spiel
            currentPhase = risiko.getPhase();

            spielerListe = risiko.getSpielerListe();

            updateCurrentPlayer();
            updatePhase();

            ausgewaehltesLand = 1;

            initializeGUI(spielerListe);
            updateTables(currentSpieler);
            displayPlayerCountries(layeredPane);
        }

    }

    private void initializeGUI(List<Spieler> spielerListe) {
        setTitle("RISIKO");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // JMenuBar
        JMenuBar menuBar = new JMenuBar();

        // JMenu for game actions
        JMenu gameMenu = new JMenu("Menu");

        // JMenuItem Save
        JMenuItem saveMenuItem = new JMenuItem("Save Game");
        saveMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                risiko.save(risiko);
                JOptionPane.showMessageDialog(null, "Spiel gespeichert");

            }
        });

        // JMenuItem Load
        JMenuItem loadMenuItem = new JMenuItem("Load Game");
        loadMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                    File file = fileChooser.getSelectedFile();
                    risiko.load(risiko);
                    if (game == null) {
                        // handle the error in loading
                    }
                }
            }
        });

        // add menu items to the game menu
        gameMenu.add(saveMenuItem);
        // gameMenu.add(loadMenuItem);

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
            middlePanel = loadAndInitializeImages(layeredPane);
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

    private JLayeredPane loadAndInitializeImages(JLayeredPane layeredPane) throws IOException {
        BufferedImage colorImage = ImageIO.read(new File("risiko\\risiko\\src\\risiko\\local\\bilder\\Color_Map.png"));
        BufferedImage image = ImageIO
                .read(new File("risiko\\risiko\\src\\risiko\\local\\bilder\\Risiko_Karte_1920x10803.png"));

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

        // Create a layered pane and add the image labels with appropriate layer
        // positions

        layeredPane.setPreferredSize(new Dimension(scaleWidth, scaleHeight));

        // Set a transparent background for the layered pane
        layeredPane.setOpaque(false);

        // Add the images to the layered pane with desired layer positions
        layeredPane.add(imageLabel, Integer.valueOf(0)); // Add the scaled image label above the background
        layeredPane.add(colorImageLabel, Integer.valueOf(1)); // Add the color-based map label to the top layer
        // Make the color map label invisible (transparent)
        colorImageLabel.setOpaque(false);
        colorImageLabel.setIcon(null);

        // FARBENKARTE ZUR KLICK ERKENNUNG
        colorImageLabel.setBounds(0, 0, scaleWidth, scaleHeight);

        // SPIELKARTE
        imageLabel.setBounds(0, 0, scaleWidth, scaleHeight);

        // displayPlayerCountries(layeredPane);
        initializeMouseClickListener(imageLabel, scaledColorImage, selectedImageLabel);

        layeredPane.add(selectedImageLabel, Integer.valueOf(3));
        displayPlayerCountries(layeredPane);
        // Add the layered pane to the main panel
        return layeredPane;
    }

    private void initializeMouseClickListener(JLabel imageLabel, BufferedImage scaledColorImage,
            JLabel selectedImageLabel) {
        imageLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                handleMapClick(e, scaledColorImage, selectedImageLabel);
            }
        });
    }

    private void handleMapClick(java.awt.event.MouseEvent e, BufferedImage scaledColorImage,
            JLabel selectedImageLabel) {
        // X Y position der maus werden abgelesen
        int x = e.getX();
        int y = e.getY();
        // Die Farbe (unsichbare farb karte) an der Stelle wo gekiclt wurde wird
        // abgelesen
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
                    break;
                } else {
                    tint = Color.RED; // Tint other player countries in green
                }
            }

            String imagePath = String.format("risiko\\risiko\\src\\risiko\\local\\bilder\\42\\%d.png",
                    ausgewaehltesLand);
            BufferedImage selectedImage = ImageIO.read(new File(imagePath));
            // Scale the image if necessary
            BufferedImage scaledSelectedImage = scaleImage(selectedImage, scaleWidth, scaleHeight);
            // Tint the image
            BufferedImage tintedImage = tintImage(scaledSelectedImage, tint);
            // Update the label with the tinted image
            ImageIcon selectedImageIcon = new ImageIcon(tintedImage);
            selectedImageLabel.setIcon(selectedImageIcon);
            selectedImageLabel.setBounds(0, 0, scaleWidth, scaleHeight);
            selectedImageLabel.setVisible(true);

            switch (currentPhase) {
                case ERSTVERTEILEN:
                case VERTEILEN:
                    break;
                case ANGREIFFEN:
                    if (ausgewaehltesLand != 0) {
                        displayNeighbourCountries(layeredPane, ausgewaehltesLand, Color.RED);
                    }
                    break;
                case VERSCHIEBEN:
                    if (ausgewaehltesLand != 0) {
                        displayNeighbourCountries(layeredPane, ausgewaehltesLand, Color.CYAN);
                    }
                    break;
            }

        } catch (IOException ex) {
            ex.printStackTrace();
            // Handle the exception, e.g., by showing a default image or an error message
        }
    }

    private void clearHighlightedCountry(JLayeredPane layeredPane) {
        Component[] components = layeredPane.getComponentsInLayer(3);
        for (Component comp : components) {
            comp.setVisible(false);
        }
        Component[] component1 = layeredPane.getComponentsInLayer(4);
        for (Component comp : component1) {
            comp.setVisible(false);
        }
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
    private void displayPlayerCountries(JLayeredPane layeredPane) {

        try {
            // Remove all existing components from layer 2
            Component[] playerCountries = layeredPane.getComponentsInLayer(2);
            for (Component comp : playerCountries) {
                layeredPane.remove(comp);
            }
            // Load images corresponding to the player's countries
            int[] laender = risiko.getEigeneLaenderId();

            for (int landId : laender) {
                if (landId != 0) {
                    // System.out.println(landId);
                    String imagePath = String.format("risiko\\risiko\\src\\risiko\\local\\bilder\\42\\%d.png", landId);
                    File imageFile = new File(imagePath);

                    if (!imageFile.exists()) {
                        System.err.println("Image file not found: " + imagePath);
                        continue; // Skip if image file does not exist
                    }

                    BufferedImage selectedImage = ImageIO.read(imageFile);
                    BufferedImage scaledSelectedImage = scaleImage(selectedImage, scaleWidth, scaleHeight);

                    // Tint the image with a specific color (example: green color)
                    Color tint = Color.GRAY;
                    BufferedImage tintedImage = tintImage(scaledSelectedImage, tint);

                    // Create ImageIcon and JLabel for the tinted image
                    ImageIcon selectedImageIcon = new ImageIcon(tintedImage);
                    JLabel selectedImageJLabel = new JLabel(selectedImageIcon);
                    selectedImageJLabel.setBounds(0, 0, scaleWidth, scaleHeight);
                    // selectedImageJLabel.setVisible(true);

                    layeredPane.add(selectedImageJLabel, Integer.valueOf(2));
                }
            }

        } catch (IOException ex) {
            ex.printStackTrace();
            // Handle the exception, e.g., by showing a default image or an error message
        }

    }

    private void displayNeighbourCountries(JLayeredPane layeredPane, int ausgewaehltesLand, Color color) {
        try {
            // Remove all existing components from layer 2
            Component[] components = layeredPane.getComponentsInLayer(4);
            for (Component comp : components) {
                layeredPane.remove(comp);
            }
            // Load images corresponding to the player's countries
            int[] nachbarLaender;
            switch (currentPhase) {
                case ANGREIFFEN:
                    nachbarLaender = risiko.getAlleGegnerNachbarsIntArray(ausgewaehltesLand);
                    break;
                case VERSCHIEBEN:
                    nachbarLaender = risiko.getAlleEigeneNachbarsIntArray(ausgewaehltesLand);
                    break;
                default:
                    nachbarLaender = new int[0];
                    break;
            }

            if (!risiko.istDeinLand(ausgewaehltesLand)) {

            } else if (nachbarLaender == null) {
                // WENN KEINE GEGNER LAENDER
            } else {
                for (int i = 0; i < nachbarLaender.length; i++) {

                    if (nachbarLaender[i] != 0) {
                        String imagePath = String.format("risiko\\risiko\\src\\risiko\\local\\bilder\\42\\%d.png",
                                nachbarLaender[i]);
                        File imageFile = new File(imagePath);

                        if (!imageFile.exists()) {
                            System.err.println("Image file not found: " + imagePath);
                            continue; // Skip if image file does not exist
                        }

                        BufferedImage selectedImage = ImageIO.read(imageFile);
                        BufferedImage scaledSelectedImage = scaleImage(selectedImage, scaleWidth, scaleHeight);

                        // Tint the image with a specific color (example: green color)
                        Color tint = color;
                        BufferedImage tintedImage = tintImage(scaledSelectedImage, tint);

                        // Create ImageIcon and JLabel for the tinted image
                        ImageIcon selectedImageIcon = new ImageIcon(tintedImage);
                        JLabel selectedImageJLabel = new JLabel(selectedImageIcon);
                        selectedImageJLabel.setBounds(0, 0, scaleWidth, scaleHeight);
                        selectedImageJLabel.setVisible(true);
                        // selectedImageJLabel.setVisible(true);

                        layeredPane.add(selectedImageJLabel, Integer.valueOf(4)); // 4 FUR DEN LAYER, NICHT AENDERN
                    }
                }
            }

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

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
            }
        });

        return rightPanel;
    }

    private JPanel createBottomPanel() {
        // Create the bottom panel
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        bottomPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Create and configure phase change button
        JButton phaseChangeButton = new JButton("Phase Change");
        phaseChangeButton.addActionListener(e -> handlePhaseChange());
        bottomPanel.add(phaseChangeButton); // Add the phase change button to the bottom panel

        JButton infoButton = new JButton("Information");
        infoButton.addActionListener(e -> showPhaseInformation());
        bottomPanel.add(infoButton);
        // Show "Angreifen " button only during the "ANGREIFEN" phase

        JButton actionButton = new JButton("Action");
        actionButton.addActionListener(e -> handleActionButtonPress());
        bottomPanel.add(actionButton); // Add the action button to the bottom panel

        bottomPanel.add(new JScrollPane(landInfoTable));
        landInfoTable.setPreferredScrollableViewportSize(new Dimension(landInfoTable.getPreferredSize().width, 70)); // Set
                                                                                                                     // preferred
                                                                                                                     // height
        return bottomPanel;
    }

    private boolean falschAusgewähltesLand() {
        int[] meineLaender = risiko.getEigeneLaenderId();
        for (int land : meineLaender) {
            if (ausgewaehltesLand == land) {
                return true;
            }
        }
        return false;
    }

    private void nextPlayer() {
        risiko.nextPlayer(); // Naechster spieler
        updateCurrentPlayer();
        displayPlayerCountries(layeredPane);

    }

    private void updateCurrentPlayer() {
        currentSpieler = risiko.getJetzigerSpieler();
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
        String[] rowNames = { "Spieler Name", "Conquered Countries", "Mission", "Zusatzarmee", "Phase",
                "Einheitskarten" };

        // Create the spieler table model with the data for the three rows
        Object[][] tableData = new Object[6][6];
        for (int i = 0; i < 6; i++) {
            tableData[i][0] = rowNames[i];
            tableData[i][1] = ""; // Placeholder for spieler information
            tableData[i][2] = "";
            tableData[i][3] = "";
            tableData[i][4] = "";
            tableData[i][5] = "";
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
        String currentphase = risiko.getPhase().toString();
        int[] einheitsKarten = risiko.getEinheitskarten();

        // Check if the spielerTable already has rows, if yes, update the existing row
        // data
        if (spielerTableModel.getRowCount() >= 1) {
            spielerTableModel.setValueAt(spielerName, 0, 1); // Update spieler name
            spielerTableModel.setValueAt(conqueredCountries, 1, 1); // Update conquered countries count
            spielerTableModel.setValueAt(mission, 2, 1); // Update mission
            spielerTableModel.setValueAt(zusatzArmee, 3, 1);
            spielerTableModel.setValueAt(currentphase, 4, 1);
            spielerTableModel.setValueAt(Arrays.toString(einheitsKarten), 5, 1);

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

    //
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

    // Methode für Phase change button um zu prüfen ob man die Phase wechseln kann
    // und abzufragen ob man die Phase wechseln möchte?
    private void handlePhaseChange() {
        int result;
        switch (currentPhase) {
            case ERSTVERTEILEN:
                JOptionPane.showMessageDialog(null,
                        "Du kannst nicht diese Phase überspringen solange du noch zusatzarmeen besitzt",
                        "Error", JOptionPane.ERROR_MESSAGE);
                break;
            case VERTEILEN:
                hatEinheitskarteBekommen = false;
                if (risiko.getZusatzArmee() != 0) {
                    JOptionPane.showMessageDialog(null,
                            "Du kannst nicht diese Phase überspringen solange du noch zusatzarmeen besitzt",
                            "Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(null,
                            "Du wirst an die Angreifephase weitergeleitet",
                            "Info", JOptionPane.INFORMATION_MESSAGE);

                    risiko.nextPhase();
                    updatePhase();
                    updateTables(currentSpieler);
                    clearHighlightedCountry(layeredPane);

                }
                break;
            case ANGREIFFEN:
                result = JOptionPane.showConfirmDialog(null,
                        "Hallo " + currentSpieler.getSpielerName() + "\nWillst du die Angreifephase überspringen?",
                        "Frage", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

                if (result == JOptionPane.YES_OPTION) {
                    isSelectingAttackingCountry = false;
                    isSelectingDefendingCountry = false;
                    risiko.nextPhase();
                    updatePhase();
                    updateTables(currentSpieler);
                    clearHighlightedCountry(layeredPane);

                }
                break;
            case VERSCHIEBEN:
                hatEinheitskarteBekommen = false;
                result = JOptionPane.showConfirmDialog(null,
                        "Hallo " + currentSpieler.getSpielerName()
                                + "\nWillst du die Verschiebephase überspringen?",
                        "Frage", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

                if (result == JOptionPane.YES_OPTION) {
                    // risiko updated auf den naechsten spieler selbst nach der Verschiebephase.
                    clearHighlightedCountry(layeredPane);
                    risiko.nextPhase();
                    updateCurrentPlayer();
                    updatePhase();
                    einheitenkartenAustauschen();
                    risiko.addBonusArmee(); // Addieren der Bonusarmee zu dem naechsten Spieler
                    updateTables(currentSpieler);
                    displayPlayerCountries(layeredPane);

                }
                break;
            default:
                break;
        }
    }

    // Methode für Info Button um die richtige Info für die richtige Phase
    // auszugeben?

    private void einheitenkartenAustauschen() {

        JFrame frame = new JFrame("Einheitenkarten Tauschen");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(600, 200);
        frame.setLayout(new GridLayout(1, 5));

        JButton button1 = new JButton("3 INFANTERIE");
        JButton button2 = new JButton("3 KAVALLERIE");
        JButton button3 = new JButton("3 ARTILLERIE");
        JButton button4 = new JButton("3 unterschidliche");

        // Add action listeners to the buttons
        button1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (risiko.darfTauschen()) {
                    risiko.tauscheDreiGleicheKartenEin(1);
                    updateTables(currentSpieler);
                    JOptionPane.showMessageDialog(null,
                            "Einheitenkarten erfolgreich eingetauscht",
                            "Info", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    Exceptions.showErrorDialog("Nicht genügend Einheitenkarten");
                }

            }
        });

        button2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (risiko.darfTauschen()) {
                    risiko.tauscheDreiGleicheKartenEin(2);
                    updateTables(currentSpieler);
                    JOptionPane.showMessageDialog(null,
                            "Einheitenkarten erfolgreich eingetauscht",
                            "Info", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    Exceptions.showErrorDialog("Nicht genügend Einheitenkarten");
                }
            }
        });

        button3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (risiko.darfTauschen()) {
                    risiko.tauscheDreiGleicheKartenEin(3);
                    updateTables(currentSpieler);
                    JOptionPane.showMessageDialog(null,
                            "Einheitenkarten erfolgreich eingetauscht",
                            "Info", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    Exceptions.showErrorDialog("Nicht genügend Einheitenkarten");
                }
            }
        });

        button4.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (risiko.darfTauschen()) {
                    risiko.tauscheDreiGUnterschiedlicheKartenEin();
                    updateTables(currentSpieler);
                    JOptionPane.showMessageDialog(null,
                            "Einheitenkarten erfolgreich eingetauscht",
                            "Info", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    Exceptions.showErrorDialog("Nicht genügend Einheitenkarten");
                }
            }
        });

        // Add the buttons to the frame
        frame.add(button1);
        frame.add(button2);
        frame.add(button3);
        frame.add(button4);

        // Display the frame
        frame.setVisible(true);
    }

    private void showPhaseInformation() {
        currentPhase = risiko.getPhase();
        switch (currentPhase) {
            case ERSTVERTEILEN:
                break;
            case VERTEILEN:
                JOptionPane.showMessageDialog(null,
                        "In der Verteilphase musst du durch Klicken ein deiner Länder auswählen.\n"
                                + "Du erkennst, ob das ausgewählte Land dein ist, an dem grünen Highlight,\n"
                                + "oder daran, dass das ausgewählte Land in der Liste deiner Länder ist.");
                break;
            case ANGREIFFEN:
                JOptionPane.showMessageDialog(null,
                        "In der Angriffsphase musst du durch Klicken des Angriff-Buttons\n"
                                + "das vorher selektierte Land als Angriffsland festlegen. Dann das gleiche für ein gegnerisches Land,\n"
                                + "danach die Anzahl der zum Angreifen benötigten Truppen. Der Angriff ist danach abgeschlossen.");
                break;
            case VERSCHIEBEN:
                JOptionPane.showMessageDialog(null, "In der Verschiebephase bla bla bla.");
                break;
            default:
                JOptionPane.showMessageDialog(null, "Unbekannte Phase.");
                break;
        }
    }

    private void handleActionButtonPress() {
        boolean canProceed;
        switch (currentPhase) {
            case ERSTVERTEILEN:
                boolean richtigesLand = falschAusgewähltesLand();
                canProceed = false;
                if (ausgewaehltesLand == 0 || !richtigesLand) {
                    Exceptions.showErrorDialog("Du musst zuerst dein Land Auswählen");
                } else {
                    while (risiko.getZusatzArmee() != 0) {
                        richtigesLand = falschAusgewähltesLand();
                        if (richtigesLand) {
                            canProceed = true;
                        } else {
                            break;
                        }
                        if (canProceed) {
                            String input;

                            input = JOptionPane.showInputDialog(this,
                                    "Gib die Anzahl der Einheiten ein, die du plazieren möchtest \n Du Hast noch "
                                            + currentSpieler.getZusatzArmee() + " Einheiten!",
                                    "Distribute Troops", JOptionPane.PLAIN_MESSAGE);

                            // Check if the input dialog was closed or canceled
                            if (input == null || input.isEmpty()) {
                                // Handle cancel or close window action as valid input (no action needed)
                                canProceed = false;
                                break; // Exit the loop if canceled or closed window
                            }

                            try {
                                // Validate and parse the input as an integer
                                int armeeAnzahl = Exceptions.readInt(input, 1, currentSpieler.getZusatzArmee());

                                // If valid, distribute the troops
                                risiko.verteilen(ausgewaehltesLand, armeeAnzahl);
                                updateTables(currentSpieler);
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

                        if (risiko.getZusatzArmee() == 0) {
                            JOptionPane.showMessageDialog(null,
                                    "Du hast keine Zusatzarmee mehr. Es wird auf den Naechsten Spieler Geaendert");
                            i += 1;
                            nextPlayer();
                            clearHighlightedCountry(layeredPane);
                            currentSpieler = risiko.getJetzigerSpieler();
                            updateTables(currentSpieler);
                            canProceed = false;
                            // Wenn alle Spieler an die Erstverteilephase Teilgenommen haben, dann Wird der
                            // Erste Spieler in die Angriffsphase weitergeleitet
                            if (i == risiko.getAnzahlSpieler()) {

                                JOptionPane.showConfirmDialog(null,
                                        "Alle Spieler haben ihre zusatzarmee verteilt! Der Erste Spieler wird in die Angreifephase weitergeleitet.",
                                        "Info",
                                        JOptionPane.INFORMATION_MESSAGE);
                                risiko.nextPhase();
                                updatePhase();
                                updateTables(currentSpieler);
                            }
                        }
                    }
                }
                break;

            case VERTEILEN:
                // risiko.save(risiko);
                updateCurrentPlayer();
                updateTables(currentSpieler);
                clearHighlightedCountry(layeredPane);
                displayPlayerCountries(layeredPane);
                richtigesLand = falschAusgewähltesLand();
                String input;

                if (ausgewaehltesLand == 0 || !richtigesLand) {
                    Exceptions.showErrorDialog("Du musst zuerst dein Land Auswählen");
                } else if (currentSpieler.getZusatzArmee() == 0) {
                    Exceptions.showErrorDialog("Keine Zusatzarmeen über.");
                } else {

                    input = JOptionPane.showInputDialog(this,
                            "Enter the number of troops to distribute: \n You have "
                                    + currentSpieler.getZusatzArmee() + " spare Armies!",
                            "Distribute Troops", JOptionPane.PLAIN_MESSAGE);

                    // Check if the input dialog was closed or canceled
                    if (input == null || input.isEmpty()) { // eine error message die sagt was man nicht tuhen soll
                        // Handle cancel or close window action as valid input (no action needed)
                        break; // Exit the loop if canceled or closed window
                    }

                    try {
                        // Validate and parse the input as an integer
                        int armeeAnzahl = Exceptions.readInt(input, 1, currentSpieler.getZusatzArmee());

                        // If valid, distribute the troops
                        risiko.verteilen(ausgewaehltesLand, armeeAnzahl);
                        updateTables(currentSpieler);
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
                break;

            case ANGREIFFEN:
                updateTables(currentSpieler);
                displayPlayerCountries(layeredPane);

                // Falls keine Angriffsbereeite Laender, dann naechste phase
                if (risiko.getAngreiffeBereiteLaender().length == 0) {
                    JOptionPane.showMessageDialog(null,
                            "Du Hast keine Angriffsbereite Laender, wechselung zur Verschiebephase");
                    risiko.nextPhase();
                    updatePhase();
                    break;
                } else {
                    isSelectingAttackingCountry = true;
                }
                // ABLAUF::
                // Wenn man die Angreifephase NICHT überspringt, dann wird
                // isSelectingAttackingCountry = true gesetzt,
                // nach dem man auf ein seine Laender klickt, muss mann auf dem Angreifen button
                // klicken, dann wird man gefragt ob das
                // Ausgewaehlte land als Angreifeland festgelegt werden soll.

                // Selektierung des Angrifflandes
                if (isSelectingAttackingCountry && !isSelectingDefendingCountry) {
                    try { // try catch fur falsche Auswahl von Land

                        if (risiko.getLand(ausgewaehltesLand) != null && risiko.istDeinLand(ausgewaehltesLand)
                                && risiko.getLandArmee(ausgewaehltesLand) > 1
                                && risiko.getAngriffGegnerLaender(ausgewaehltesLand).length != 0) {

                            int result = JOptionPane.showConfirmDialog(null, // Bestaetigung des Angiffslandes
                                    "Du Möchtest von " + risiko.getLandName(ausgewaehltesLand) + " Angreifen?",
                                    "Frage", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

                            if (result == JOptionPane.YES_OPTION) {
                                attackingCountry = ausgewaehltesLand;
                                isSelectingDefendingCountry = true; // Angriffsland Wurde selektiert
                                JOptionPane.showMessageDialog(this, "Choose a target country to attack.");
                            }

                        } else {
                            JOptionPane.showMessageDialog(this, "Please select a valid attacking country.", "Error",
                                    JOptionPane.ERROR_MESSAGE);
                        }
                    } catch (Exception ex) {
                        // Handle any other unexpected exceptions
                        Exceptions.showErrorDialog("An unexpected error occurred: " + ex.getMessage());
                    }

                }
                // Auswahl des Verteidigungslandes
                else if (isSelectingDefendingCountry) { // Diese bedingungen könnte man eventuell als eigene exceptions
                                                        // catchen
                    // Wenn das selektierte Land dein ist, break
                    if (risiko.istDeinLand(ausgewaehltesLand)) {
                        JOptionPane.showMessageDialog(null,
                                "Du kannst dein eigenes Land nicht angreifen.",
                                "Fehler", JOptionPane.ERROR_MESSAGE);
                        break;
                    }
                    // Wenn das selektierte Land kein nachbar vom angreifenden Land ist
                    else if (risiko.sindNachbar(attackingCountry, ausgewaehltesLand) == false) {
                        JOptionPane.showMessageDialog(null,
                                risiko.getLandName(ausgewaehltesLand) + " ist  kein Nachbar von "
                                        + risiko.getLandName(attackingCountry),
                                "Fehler", JOptionPane.ERROR_MESSAGE);
                        break;
                    }

                    // Wenn alles gut, weiter gehts
                    else {

                        int result = JOptionPane.showConfirmDialog(null,
                                "Du Möchtest das Land " + risiko.getLandName(ausgewaehltesLand) + " Angreifen?",
                                "Frage", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

                        // Wenn verteidigungsLand bestaetigt wurde, dann weiter gehts
                        if (result == JOptionPane.YES_OPTION) {

                            defendingCountry = ausgewaehltesLand;

                            input = JOptionPane.showInputDialog(this,
                                    "Gib die Anzahl der Einheiten ein, mit dem du Angreiffen möchtest \n Du hast  "
                                            + risiko.getLandArmee(attackingCountry)
                                            + " Einheiten! Du kannst mit bis zu "
                                            + risiko.getMaxAttackNumber(attackingCountry) + " Angreiffen",
                                    "Attack number", JOptionPane.PLAIN_MESSAGE);

                            if (input == null || risiko.getLandArmee(attackingCountry) - Integer.parseInt(input) < 1) {
                                JOptionPane.showMessageDialog(null,
                                        "Ungültige Eingabe, bitte nochmal versuchen",
                                        "Invalid Input",
                                        JOptionPane.ERROR_MESSAGE);
                                break;
                            } else {
                                // Eingabe von
                                try {
                                    Exceptions.readIntAngreifen(input, 1,risiko.getMaxAttackNumber(attackingCountry)); // Eingabe prüffen
                                    int armeeAnzahl = Integer.parseInt(input);
                                    // Angriffsergebniss
                                    JOptionPane.showMessageDialog(null, "Attack passiert, Resultat: \n"
                                            + risiko.angreifen(attackingCountry, defendingCountry,
                                                    armeeAnzahl));

                                    // Wiederholung der Attacke nach bedarf
                                    while (!risiko.landHatKeineArmee(defendingCountry)
                                            || risiko.getLandArmee(attackingCountry) <= 1) {
                                        result = JOptionPane.showConfirmDialog(null,
                                                "Moechtest du nochmal Eingreifen?", "Frage",
                                                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                                        if (result == JOptionPane.NO_OPTION) {
                                            break;
                                        } else {
                                            input = JOptionPane.showInputDialog(this,
                                                    "Enter the number of troops to attack: \n You have "
                                                            + risiko.getLandArmee(attackingCountry)
                                                            + " Armies! You can use up to "
                                                            + risiko.getMaxAttackNumber(attackingCountry)
                                                            + " at a time",
                                                    "Attack number", JOptionPane.PLAIN_MESSAGE);
                                            if (input == null || risiko.getLandArmee(attackingCountry)
                                                    - Integer.parseInt(input) < 1) {
                                                JOptionPane.showMessageDialog(null,
                                                        "Ungültige Eingabe, bitte nochmal versuchen",
                                                        "Invalid Input",
                                                        JOptionPane.ERROR_MESSAGE);
                                                break;
                                            }

                                            Exceptions.readIntAngreifen(input, 1,risiko.getMaxAttackNumber(attackingCountry)); // Eingabe prüffen
                                            armeeAnzahl = Integer.parseInt(input);
                                            JOptionPane.showMessageDialog(null, "Attack happened \n" + risiko
                                                    .angreifen(attackingCountry, defendingCountry, armeeAnzahl));
                                            updateTables(currentSpieler); // Update die Tabellen nach dem
                                                                          // Angriff
                                        }
                                    }
                                    isSelectingDefendingCountry = false;
                                    isSelectingAttackingCountry = false;
                                    updateTables(currentSpieler);
                                    // Wenn Gewonnen, also wenn defendingCountry keine Armee mehr hat
                                    if (risiko.landHatKeineArmee(defendingCountry)) {
                                        // Eine Einheitskarte an den Spieler Verteilen
                                        if (!hatEinheitskarteBekommen) {
                                            risiko.einheitskarteAusgabe();
                                            updateTables(currentSpieler);
                                            hatEinheitskarteBekommen = true;
                                        }
                                        if (risiko.getSpielerLaenderAnzahl() == 42) {
                                            // spieler hat alle laender erobert
                                        }

                                        // code fur gewonen, land besitzer saetzen usw

                                        risiko.neuerBesitzerSetzen(defendingCountry);
                                        risiko.einruecken(attackingCountry, defendingCountry);

                                        displayPlayerCountries(layeredPane);
                                        clearHighlightedCountry(layeredPane);
                                        updateTables(currentSpieler);

                                        // Uberprufung der Mission
                                        if (risiko.checkIfMissionErfuelt()) {
                                            JOptionPane.showMessageDialog(null, "Mission erfüllt, spieler +"
                                                    + currentSpieler.getSpielerName() + " Gewonnen");
                                            System.exit(0);

                                        }
                                        // Nachricht über die erfolgreiche Eroberung
                                        JOptionPane.showMessageDialog(null,
                                                "YOU WON YEEEEEEEEEEEEEE\n Du Hast das Land "
                                                        + risiko.getLandName(defendingCountry) + " erobert");
                                        result = JOptionPane.showConfirmDialog(null, "Moechtest du einruechen?",
                                                "Frage", JOptionPane.YES_NO_OPTION,
                                                JOptionPane.QUESTION_MESSAGE); // Möchte der Spieler Einrücken?

                                        if (result == JOptionPane.NO_OPTION) { // Wenn nicht dann überspringen
                                            break;
                                        } else {
                                            // code furs einruecken
                                            input = JOptionPane.showInputDialog(null, "Du kannst von "
                                                    + risiko.getLandName(attackingCountry) + " bis zu "
                                                    + (risiko.getLandArmee(attackingCountry) - 1)
                                                    + " Armeen auf "
                                                    + risiko.getLandName(defendingCountry) + " einruecken lassen.",
                                                    "Distribute Troops", JOptionPane.PLAIN_MESSAGE);
                                            int anzahl = Exceptions.readInt(input, 0,
                                                    (risiko.getLandArmee(attackingCountry) - 1));
                                            risiko.verschieben(attackingCountry, defendingCountry, anzahl);
                                            updateTables(currentSpieler);
                                            attackingCountry = 0;
                                            defendingCountry = 0;
                                        }
                                    }
                                } catch (NumberFormatException ex) {
                                    JOptionPane.showMessageDialog(null,
                                            "Invalid input. Please enter a valid number.", "Invalid Input",
                                            JOptionPane.ERROR_MESSAGE);
                                } catch (IllegalArgumentException ex) {
                                    // Display error for invalid number range
                                    Exceptions.showErrorDialog("Invalid input. " + ex.getMessage());
                                } catch (Exception ex) {
                                    // Handle any other unexpected exceptions
                                    Exceptions.showErrorDialog(
                                            "An unexpected error occurred: " + ex.getMessage());
                                }
                            }
                        }
                    }
                }
                break;
            case VERSCHIEBEN:
                hatEinheitskarteBekommen = false;

                // Wenn keine verschiebebereite Laender vorhanden sind, nächste phase +
                // naechster Spieler
                if (risiko.getVerschiebebereiteLaender().length == 0) {
                    JOptionPane.showMessageDialog(null,
                            "Du Hast keine Verschiebereite Laender. Wechselung zum naechsten Spieler und die Verteilephase");
                    clearHighlightedCountry(layeredPane);
                    risiko.nextPhase();
                    updatePhase();
                    updateCurrentPlayer();
                    risiko.addBonusArmee();// Addieren der Bonusarmee zu dem naechsten Spieler
                    updateTables(currentSpieler);
                }

                updateTables(currentSpieler);
                displayPlayerCountries(layeredPane);

                isSelectingVerschiebeVonCountry = true;

                // ABLAUF::
                // Auswaht und bestätigung der VerschiebeVonCountry, dannach auswahl und
                // bestätigung
                // der VerschiebeNachCountry, laender müssen benachbart sein.
                // Lätzändlich die Eingabe der Einheiten zur Verschiebung

                // Auswahl VerschiebeVonCountry
                if (isSelectingVerschiebeVonCountry && !isSelectingVerschiebeNachCountry) {
                    try {
                        if (risiko.getLand(ausgewaehltesLand) != null && risiko.istDeinLand(ausgewaehltesLand)
                                && risiko.getLandArmee(ausgewaehltesLand) > 1
                                && risiko.getAlleEigeneNachbars(ausgewaehltesLand).length != 0) {
                            int result = JOptionPane.showConfirmDialog(null,
                                    "Du Möchtest von " + risiko.getLandName(ausgewaehltesLand) + " Verschieben?",
                                    "Frage", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

                            if (result == JOptionPane.YES_OPTION) {
                                attackingCountry = ausgewaehltesLand;
                                isSelectingVerschiebeNachCountry = true;
                            }

                        } else {
                            JOptionPane.showMessageDialog(null, "Please select a valid country.", "Error",
                                    JOptionPane.ERROR_MESSAGE);
                        }
                    } catch (Exception ex) {
                        // Handle any other unexpected exceptions
                        Exceptions.showErrorDialog("An unexpected error occurred: " + ex.getMessage());
                    }

                    // Auswahl VerschiebeNachCountry
                } else if (isSelectingVerschiebeNachCountry) {
                    // Falls man ein generisches Land auswählt nach welchem man verschieben möchte
                    if (!risiko.istDeinLand(ausgewaehltesLand)) {
                        JOptionPane.showMessageDialog(null,
                                "Du kannst keine Armeen auf ein generisches Land Verschieben.",
                                "Fehler", JOptionPane.ERROR_MESSAGE);
                        break;
                    }
                    // Wenn das selektierte Land kein nachbar vom verschiebebereitem Land ist
                    else if (risiko.sindNachbar(attackingCountry, ausgewaehltesLand) == false) {
                        JOptionPane.showMessageDialog(null,
                                risiko.getLandName(ausgewaehltesLand) + " ist  kein Nachbar von "
                                        + risiko.getLandName(attackingCountry),
                                "Fehler", JOptionPane.ERROR_MESSAGE);
                        break;
                    } else {
                        int result = JOptionPane.showConfirmDialog(null,
                                "Du Möchtest auf das Land " + risiko.getLandName(ausgewaehltesLand)
                                        + " Verschieben?",
                                "Frage", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

                        if (result == JOptionPane.YES_OPTION) {

                            defendingCountry = ausgewaehltesLand;

                            input = JOptionPane.showInputDialog(this,
                                    "Gib die Anzal der zu Verschiebenen Armeen\n, du hast: "
                                            + risiko.getLandArmee(attackingCountry)
                                            + " Einheiten, du kannst bis zu "
                                            + (risiko.getLandArmee(attackingCountry) - 1) + " rüberbringen.",
                                    "Attack number", JOptionPane.PLAIN_MESSAGE);

                            if (input != null) {

                                try {
                                    Exceptions.readInt(input, 0, risiko.getLandArmee(attackingCountry) - 1);
                                    int armeeAnzahl = Integer.parseInt(input);
                                    JOptionPane.showMessageDialog(null,
                                            "Du hast Erfolgreich\n" + armeeAnzahl + " Einheiten auf das Land "
                                                    + risiko.getLandName(defendingCountry) + " rübergebracht.");

                                    risiko.verschieben(attackingCountry, defendingCountry, armeeAnzahl);

                                    isSelectingVerschiebeNachCountry = false;
                                    isSelectingVerschiebeVonCountry = false;

                                    updateTables(currentSpieler);

                                } catch (NumberFormatException ex) {
                                    JOptionPane.showMessageDialog(null,
                                            "Invalid input. Please enter a valid number.", "Invalid Input",
                                            JOptionPane.ERROR_MESSAGE);
                                } catch (IllegalArgumentException ex) {
                                    // Display error for invalid number range
                                    Exceptions.showErrorDialog("Invalid input. " + ex.getMessage());
                                } catch (Exception ex) {
                                    // Handle any other unexpected exceptions
                                    Exceptions.showErrorDialog("An unexpected error occurred: " + ex.getMessage());
                                }
                            }

                        } else {

                        }
                    }
                }
                break;
        }
    }
}
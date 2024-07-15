package risiko.local.ui;

import javax.swing.*;

import risiko.local.domain.Risiko;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class MainMenuWindow extends JFrame {

    private boolean playerInitializationComplete;
    
    public MainMenuWindow() { 
        setTitle("Main Menu");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        playerInitializationComplete = false;

        JPanel menuPanel = new JPanel(new GridLayout(3, 1, 10, 10));

        // Customize the buttons
        JButton newGameButton = new JButton("New Game");
        customizeButton(newGameButton);
        newGameButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                startPlayerInitialization();
            }
        });
        menuPanel.add(newGameButton);

        JButton loadGameButton = new JButton("Load Game");
        customizeButton(loadGameButton);
        loadGameButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                loadGame();
            }
        });
        menuPanel.add(loadGameButton);

        JButton quitButton = new JButton("Quit");
        customizeButton(quitButton);
        quitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        menuPanel.add(quitButton);

        add(menuPanel, BorderLayout.CENTER);

        setLocationRelativeTo(null); // Center the window on the screen
        setVisible(true);
    }

    private void customizeButton(JButton button) { // bersonalizirung des buttons
        button.setBackground(new Color(44, 62, 80));
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.PLAIN, 18));

        // Remove focus outline
        button.setFocusPainted(false);

        // Add hover effect
        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) {
                button.setBackground(new Color(52, 73, 94));
            }

            public void mouseExited(MouseEvent evt) {
                button.setBackground(new Color(44, 62, 80));
            }
        });
    }

    private void startPlayerInitialization() { // code für das starten der Spielerinitialisierung
        // Code to handle starting player initialization
        PlayerInitializationWindow playerInitWindow = new PlayerInitializationWindow();
        setVisible(false); // Hide the MainMenuWindow while player initialization is ongoing
    }

    public void playerInitializationComplete() { // completirung der Spielerinitialisierung
        playerInitializationComplete = true;
        setVisible(false); // Hide the MainMenuWindow after player initialization is complete
    }

    public boolean isPlayerInitializationComplete() { // prüfen ob die Spielerinitialisierung abgeschlossen ist
        return playerInitializationComplete;
    }

    private void loadGame() { // code für das laden eines Spiels
        try{
        // Create a new Risiko object
        Risiko risiko = new Risiko();
    
        // Load game data
        // risiko.loadGame(risiko);
        risiko.load(risiko);
        
    
        // Create an empty list of player names (adjust as needed)
        List<String> emptyList = new ArrayList<>();
    
        // Start the main game with the loaded data
        startMainGame(emptyList, risiko, true);
        }
        catch(Exception e){
            JOptionPane.showMessageDialog(null, "Fehler beim Laden des Spiels: " + e.getMessage());
        }
    }
    
    private void startMainGame(List<String> playerNames, Risiko risiko, boolean isLoaded) { // code für das starten des Hauptspiels
        // Instantiate the MainGame class with the provided parameters
        MainGame mainGame = new MainGame(playerNames, risiko, isLoaded);
    
        // Close the current window (this assumes you're in MainMenuWindow)
        dispose();
    }
    

    public static void main(String[] args) { // das ist die main-Methode
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new MainMenuWindow();
            }
        });
    }
}
package risiko.local.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

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

    private void customizeButton(JButton button) {
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

    private void startPlayerInitialization() {
        // Code to handle starting player initialization
        PlayerInitializationWindow playerInitWindow = new PlayerInitializationWindow();
        setVisible(false); // Hide the MainMenuWindow while player initialization is ongoing
    }

    public void playerInitializationComplete() {
        playerInitializationComplete = true;
        setVisible(false); // Hide the MainMenuWindow after player initialization is complete
    }

    public boolean isPlayerInitializationComplete() {
        return playerInitializationComplete;
    }

    private void loadGame() {
        // Code to handle loading a saved game
        // Implement this based on your requirements
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new MainMenuWindow();
            }
        });
    }
}
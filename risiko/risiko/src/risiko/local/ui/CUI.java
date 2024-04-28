package risiko.local.ui;

import java.util.Scanner;

import risiko.local.domain.Risiko;

public class CUI {
    public static void main(String[] args) {
        Risiko risiko = new Risiko();
        Scanner scanner = new Scanner(System.in);

        int auswahlHauptmenu = 0;
        while (auswahlHauptmenu != 3) {
            System.out.println("***MENU***");
            System.out.println("1. New Game");
            System.out.println("2. Load Game");
            System.out.println("3. Quit");
            
            auswahlHauptmenu = scanner.nextInt();
            switch (auswahlHauptmenu) {
                case 1:
                    System.out.println("You have chosen a new game!");
                    System.out.println("Please add a player!");

                    System.out.println("Select your name:");
                    String name = scanner.next();
                    risiko.spielerHinzufuegen(name);

                    boolean startGame = false;

                    while (!startGame) {
                       
                        

                        System.out.println("\n1. Would you like to start?");
                        System.out.println("2. Or would you like to add more players? (Current number of players: " + risiko.getAnzahlSpieler() + ")");
                        System.out.println("3. You could also look at the list of the current players!");
                        System.out.println("// note: You cannot start with less than 2 players! //");

                        int auswahlMenuSpielvorbereitung = scanner.nextInt();

                        switch (auswahlMenuSpielvorbereitung) {
                            case 1:
                            /*
                                if (risiko.getAnzahlSpieler() < 2) {
                                    System.out.println("You need at least 2 players to start the game!");
                                    break;
                                }
                                */
                                System.out.println("The Game Starts!");
                                startGame = true;
                                
                                break;
                            case 2:

                                System.out.println("Select your name:");
                                name = scanner.next();
                                risiko.spielerHinzufuegen(name);    

                                break;
                            case 3:
                             
                                System.out.println(risiko.getSpielerListe());
                                
                                break;
                            default:
                                System.out.println("Invalid choice!");
                                break;
                        }
                    }
                    break;
                case 2:
                   // risiko.loadGame();
                    // Spiel Laden, kommt spaeter
                    break;
                case 3:
                    // Spiel Verlassen
                    System.exit(0);
                    break;
                default:
                    System.out.println("Invalid choice!");
                    break;
            }
        } 
        scanner.close();
    }
}

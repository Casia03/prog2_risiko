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
                            
                                if (risiko.getAnzahlSpieler() < 2) {
                                    System.out.println("You need at least 2 players to start the game!");
                                    break;
                                }
                               
                                    
                                System.out.println("The Game Starts!");
                                startGame = true;
                                risiko.startGame(risiko);
                                switch (risiko.getPhase()) {
                                    case ERSTVERTEILEN: //Anfangsverteil
                                    //for(int i = 0;i <)
                                        int i = 0;

                                        while(risiko.currentSpielerHatZusatzarmee()){ // Solange dirnne bleiben bis der Jetzige Spieler alle armeen verteilt hat
                                            System.out.println(risiko.getJetzigerSpielerName() + ": es ist gerade die Verteilphase");
                                            System.out.println("Deine Länder: ");

                                                risiko.getCurrentSpielerLaender(); // muss noch geaendert werden

                                            System.out.println("zurzeit zusatz armen in besitzt: " + risiko.getJetzigerSpielerZusatzArmee());
                                            System.out.println("Du musst deine Zusatzarmeen verteilen. \nWaehle dazu ein Land auf dem du deine Zusatzarmeen verteilen möchtest.");

                                                int nachLand = scanner.nextInt();

                                            System.out.println("Wie viele Einheiten möchtest du auf das Land " + risiko.getLand(nachLand) + " platzieren? \n zu verfügung stehen dir noch " + risiko.getJetzigerSpielerZusatzArmee() + " Einheiten." );
                                                
                                                int anzahlArmee = scanner.nextInt();
                                                risiko.verteilen(nachLand, anzahlArmee);
                                        }


                                        i+=1;
                                        risiko.nextPlayer(); // Naechster spieler 
                                        if(i == risiko.getAnzahlSpieler()){ // wenn alle spieler zusatzarmee verteilt haben dann ab in die naechste phase 
                                            risiko.nextPhase();
                                        }
                                            
                                        break;
                                
                                    case VERTEILEN: //Verteilen
                                        while(risiko.currentSpielerHatZusatzarmee()){
                                            System.out.println("Spieler " + risiko.getJetzigerSpielerName()  + ", es ist gerade die Verteilephase \nBitte verteile deine Zusatzarmeen.");
                                            System.out.println("Deine Länder: ");

                                                risiko.getCurrentSpielerLaender();

                                            System.out.println("zurzeit zusatz armen in besitzt: " + risiko.getJetzigerSpielerZusatzArmee());
                                            System.out.println("Du musst deine Zusatzarmeen verteilen. \nWaehle dazu ein Land auf dem du deine Zusatzarmeen verteilen möchtest.");

                                                int nachLand = scanner.nextInt();

                                            System.out.println("Wie viele Einheiten möchtest du auf das Land " + risiko.getLand(nachLand) + " platzieren? \n zu verfügung stehen dir noch " + risiko.getJetzigerSpielerZusatzArmee() + " Einheiten." );
                                                
                                                int anzahlArmee = scanner.nextInt();
                                                risiko.verteilen(nachLand, anzahlArmee);
                                        }
                                                risiko.nextPhase();
                                        break;

                                    case ANGREIFFEN: //Angreifen
                                        int spielerWillAngreifen = 1;
                                        System.out.println("Spieler " + risiko.getJetzigerSpielerName() + ", gerade bist du in die Angreiffenphase ");
                                        System.out.println("Möchtest du die Angreiffephase übespringen? \nTippe: \n'1' für Nein\n'2' für Ja");
                                        
                                        spielerWillAngreifen = scanner.nextInt();

                                        while(spielerWillAngreifen == 1){
                                            
                                            System.out.println("Du hast dich fur den Angriff entschieden \nWälle ein land aus dem sie Angreiffen wollen");
                                            risiko.getAngriffbereiteLaender(); // Ausgabe der agnriffsbereiten laender 

                                            System.out.println("Bitte gib die Nummer des Angrifflandes.");

                                            int vonLand = scanner.nextInt(); // Eingabe der Angriffslandes 

                                            System.out.println("Von das Land: " + risiko.getLand(vonLand) + " können die Folgende gegnerische Länder angegriffen werden: ");

                                            risiko.getAngriffGegnerLaender(vonLand); // Ausgabe der Adjazierenden genger laender

                                            System.out.println("Bitte gib die Nummer des zum Angreiffendes landes ein.");

                                            int nachLand = scanner.nextInt(); // Eingabe der Verteidigungslandes 

                                            System.out.println("Du hasst " + risiko.getAngriffslandArmee(vonLand) + " Einheiten auf dein Land, und kannst somit mit höhstens " + risiko.getMaxAttackNumber(vonLand) + " Einheiten Angreifen. \n Geben sie jetzt an, wie viele Einheiten sollen für den Angriff benutzt werden sollen.");


                                            int armeeAnzahl = scanner.nextInt(); // Eingabe der Agriffs armee anzahl

                                            risiko.angreifen(vonLand,nachLand,armeeAnzahl);
                                            
                                            System.out.println("Resultat : " + risiko.getAngriffResult());
                                            
                                            System.out.println("Möchtest du nochmal Angreifen? \nTippe: \n'1' für Nein\n '2' für Ja");
                                             
                                            spielerWillAngreifen = scanner.nextInt();
                                            
                                        }
                                        
                                            risiko.nextPhase();
                                        break;
                                    
                                    case VERSCHIEBEN: //Verschieben
                                    System.out.println(risiko.getJetzigerSpielerName() + " es ist gerade die Verteilphase");
                                            //1risiko.verschieben();
                                            System.out.println("wollen sie einheiten verscheiben");
                                            
                                            System.out.println("wählen sie ein land");
                                            risiko.nextPhase();
                                        break;
                                }
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
                   //risiko.loadGame();
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

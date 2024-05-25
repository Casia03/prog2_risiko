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

            auswahlHauptmenu = risiko.readInt(scanner, 1, 3);

            switch (auswahlHauptmenu) {
                case 1:
                    System.out.println("You have chosen a new game!");
                    System.out.println("Please add a player!");

                    System.out.println("Select your name:");
                    String name = scanner.next();
                    risiko.spielerHinzufuegen(name);

                    //Kann man mit einer Methode austauschen in der Risiko klasse die prüft ob das spiel noch läuft
                    boolean startGame = false;

                    while (!startGame) {

                        System.out.println("\n1. Would you like to start?");
                        System.out.println("2. Or would you like to add more players? (Current number of players: " + risiko.getAnzahlSpieler() + ")");
                        System.out.println("3. You could also look at the list of the current players!");
                        System.out.println("// note: You cannot start with less than 2 players! //");

                        int auswahlMenuSpielvorbereitung = risiko.readInt(scanner, 1, 3);

                        switch (auswahlMenuSpielvorbereitung) {
                            case 1:

                                if (risiko.getAnzahlSpieler() < 2) {
                                    System.out.println("You need at least 2 players to start the game!");
                                    break;
                                }
                                int i = 0;
                                System.out.println("The Game Starts!");
                                startGame = true;
                                risiko.startGame(risiko);
                                // funktionier noch nicht System.out.println(risiko.getVerschiebebereiteLaender());
                                boolean spielende = false;
                                while (!spielende) {
                                

                                    switch (risiko.getPhase()) {
                                        case ERSTVERTEILEN: // Anfangsverteil
                                            while (risiko.jetzigerSpielerHatZusatzarmee()) { // Wird abgespielt bis
                                                                                            // der Jetzige Spieler alle
                                                                                            // armeen verteilt hat
                                                System.out.println("\n\nSpieler " + risiko.getJetzigerSpielerName() + ", es ist gerade die Verteilphase");
                                                System.out.println("Deine Länder: ");

                                                String[] resultArray = risiko.getJetzigerSpielerLaenderListe();
                                                for (String element : resultArray) {
                                                    System.out.println(element);
                                                }
                                                System.out.println("Dein Ziel:");
                                                System.out.println(risiko.getJetzigerSpieler().getMission());
                                                System.out.println("zurzeit zusatz armen in besitzt: " + risiko.getJetzigerSpielerZusatzArmee());
                                                System.out.println("Du musst deine Zusatzarmeen verteilen. \nWaehle dazu ein Land auf dem du deine Zusatzarmeen verteilen möchtest.");

                                                int nachLand = scanner.nextInt(); //risiko.readLandIndex(scanner, risiko.getJetzigerSpielerLaenderListe()); //BEGRENTUNG EINFUEGNE, LAENDER NUMMERN

                                                System.out.println("Wie viele Einheiten möchtest du auf das Land " + risiko.getLand(nachLand)+ " platzieren? \n zu verfügung stehen dir noch " + risiko.getJetzigerSpielerZusatzArmee() + " Einheiten.");

                                                int anzahlArmee = risiko.readInt(scanner, 1, risiko.getJetzigerSpielerZusatzArmee());
                                                risiko.verteilen(nachLand, anzahlArmee);
                                            }
                                            i = i + 1;
                                            risiko.nextPlayer(); // Naechster spieler
                                            if (i == risiko.getAnzahlSpieler()) { // wenn alle spieler zusatzarmee verteilt haben dann gehts in die naechste phase
                                                risiko.nextPhase();
                                            }
                                            break;

                                        case VERTEILEN: // Verteilen
                                            risiko.addZusatzarmee(risiko.getJetzigerSpieler(), risiko.berechneZusatzarmeen(risiko.getAnzahlSpielerLaender(risiko.getJetzigerSpieler())));
                                            while (risiko.jetzigerSpielerHatZusatzarmee()) {
                                                System.out.println("\n\nSpieler " + risiko.getJetzigerSpielerName() + ", es ist gerade die Verteilephase \nBitte verteile deine Zusatzarmeen.");
                                                System.out.println("Deine Länder: ");

                                                String[] resultArray = risiko.getJetzigerSpielerLaenderListe();
                                                for (String element : resultArray) {
                                                    System.out.println(element);
                                                }
                                                
                                                System.out.println("zurzeit zusatz armen in besitzt: " + risiko.getJetzigerSpielerZusatzArmee());
                                                System.out.println("Du musst deine Zusatzarmeen verteilen. \nGib die Nummer des Landes ein, auf dem du deine Zusatzarmeen verteilen möchtest.");

                                                int nachLand = scanner.nextInt(); // BEGRENZUNGEN EINFUEGEN, LAENDER NUMMERN

                                                System.out.println("Wie viele Einheiten möchtest du auf das Land "+ risiko.getLand(nachLand) + " platzieren? \n zu verfügung stehen dir noch " + risiko.getJetzigerSpielerZusatzArmee() + " Einheiten.");

                                                int anzahlArmee = risiko.readInt(scanner, 1, risiko.getJetzigerSpielerZusatzArmee());
                                                risiko.verteilen(nachLand, anzahlArmee);

                                                System.out.println("Die Einheiten wurden effolgreich verteilt.\n");
                                            }
                                            risiko.nextPhase();
                                            break;

                                        case ANGREIFFEN: // Angreifen
                                            boolean spielerWillAngreifen = true;
                                            boolean spielerWillEinruecken = false;
                                            System.out.println("\n\nSpieler " + risiko.getJetzigerSpielerName() + ", gerade bist du in die Angreiffenphase ");
                                            System.out.println("Möchtest du Angreiffen? \nTippe: \n'J' für Ja \n'N' für Nein");

                                            spielerWillAngreifen = scanner.next().trim().equalsIgnoreCase("j") ? true: false;

                                            while (spielerWillAngreifen) {

                                                System.out.println("Du hast dich fur den Angriff entschieden \nWälle ein land aus dem du Angreiffen moechtest");
                                                
                                                 // Ausgabe der agnriffsbereiten laender
                                                if(risiko.getAngreiffeBereiteLaender().length != 0){
                                                    String[] resultArray = risiko.getAngreiffeBereiteLaender();
                                                    for (String element : resultArray) {
                                                        System.out.println(element);
                                                    }

                                                    System.out.println("Bitte gib die Nummer des Landes an mit dem du angreifen moechtests.");

                                                    int vonLand = scanner.nextInt(); // Eingabe der Angriffslandes, Exception nicht vergessen // BEGRENZUNGEN EINFUEGEN, 

                                                    System.out.println("Von dem Land: " + risiko.getLand(vonLand)+ " können folgende gegnerische Länder angegriffen werden: ");

                                                    // Ausgabe der Adjazierenden genger laender

                                                    String[] resultArray2 = risiko.getAngriffGegnerLaender(vonLand);
                                                    for (String element : resultArray2) {
                                                        System.out.println(element);
                                                    }

                                                    System.out.println("Bitte gib die Nummer des Landes an, welches du angreifen möchtests.");

                                                    int nachLand = scanner.nextInt(); // Eingabe der Verteidigungslandes, Exception nicht vergessen // BEGRENZUNGEN EINFUEGEN, LAENDER NUMMERN

                                                    System.out.println("Du hast " + risiko.getLandArmee(vonLand) + " Einheiten auf dein Land, und kannst somit mit höhstens " + risiko.getMaxAttackNumber(vonLand) + " Einheiten Angreifen. \n Gib jetzt an, mit wie vielen Einheiten du angreifen möchtest (1, 2 oder 3 Einheiten).");

                                                    int armeeAnzahl = risiko.readInt(scanner, 1, risiko.getMaxAttackNumber(vonLand)); // Eingabe der Agriffs armee anzahl

                                                    System.out.println(risiko.angreifen(vonLand, nachLand, armeeAnzahl)); // Zeigt resultat von angriff

                                                    if(risiko.landHatKeineArmee(nachLand)){ // WENN GEWONNEN
                                                        risiko.neuerBesitzerSetzen(nachLand);
                                                        risiko.einruecken(vonLand, nachLand);
                                                        System.out.println("Du hast das Land " + risiko.getLandName(nachLand) + " Erobert.");
                                                        System.out.println("Möchtest du auf das eroberte Land mehr Einheiten einrücken?\nTippe: \n'J' für Ja \n'N' für Nein\"");

                                                        spielerWillEinruecken = scanner.next().trim().equalsIgnoreCase("j")? true: false;

                                                        if(spielerWillEinruecken){ // WENN EINRUECKEN MOECHTEN
                                                            System.out.println("Gib die Anzahl der Einheiten die du einruecken moechtest. \nDu hast " + (risiko.getLandArmee(vonLand)-1) + " Einheiten zur verfuegung."); 
                                                            int anzahlArmeeZumEinruecken = risiko.readInt(scanner, 1, (risiko.getLandArmee(vonLand)-1));
                                                            risiko.verschieben(vonLand, nachLand, anzahlArmeeZumEinruecken);
                                                            System.out.println("So sieht zurzeit die Liste deiner Angriffsbereite Laender: ");
                                                            String[] resultArray3 = risiko.getAngreiffeBereiteLaender();
                                                            for (String element : resultArray3) {
                                                                System.out.println(element);
                                                            }
                                                            spielerWillEinruecken = false;
                                                        }
                                                    }
                                                    
                                                    System.out.println("Möchtest du weiter angreifen? \nTippe: \n'J' für Ja \n'N' für Nein");

                                                    spielerWillAngreifen = scanner.next().trim().equalsIgnoreCase("j")? true: false;
                                                }else{
                                                    System.out.println("Du hast leider Keine möglichkeit anzugreiffen, du wirst an die Verschieben phase weitergeleitet");
                                                    spielerWillAngreifen = false;
                                                    risiko.nextPhase();
                                                    break;
                                                }
                                                
                                            }
                                            spielerWillAngreifen = false;
                                            risiko.nextPhase();
                                            break;

                                        case VERSCHIEBEN: // Verschieben
                                            boolean spielerWillVerschieben = true;
                                            System.out.println("\n\nSpieler " + risiko.getJetzigerSpielerName() + ", es ist gerade die Verschiebephase");

                                            System.out.println("Möchtest du Verschieben? \nTippe: \n'J' für Ja \n'N' für Nein");

                                            spielerWillVerschieben = scanner.next().trim().equalsIgnoreCase("j")? true: false;

                                            while (spielerWillVerschieben) {
                                                System.out.println("Vom welchem Land möchtest du Einheiten verschieben?");

                                                String[] resultArray = risiko.getVerschiebebereiteLaender();
                                                    for (String element : resultArray) {
                                                        System.out.println(element);
                                                    }

                                                System.out.println("Bitte gib die Nummer des Landes an von wo du die Einheiten aus verschieben möchtest.");

                                                int vonLand = scanner.nextInt(); // Eingabe der Landes von wo aus verschoben wird, Exception nicht vergessen // BEGRENZUNGEN EINFUEGEN, LAENDER NUMMERN
                                                
                                                System.out.println("Von dem Land: " + risiko.getLand(vonLand)+ " können nach folgenden Länder verschoben werden: ");

                                                //funktion für angrenzende länder
                                                
                                                String[] resultArray1 = risiko.getAlleEigeneNachbars(vonLand);
                                                for (String element : resultArray1) {
                                                    System.out.println(element);
                                                }

                                                System.out.println("Bitte gib die Nummer des Landes an, welches die Einheiten erhalten soll.");

                                                int nachLand = scanner.nextInt(); // Eingabe des Landes die die Einheiten bekommen soll, Exception nicht vergessen // BEGRENZUNGEN EINFUEGEN, LAENDER NUMMERN

                                                System.out.println("Du hast " + risiko.getLandArmee(vonLand) + " Einheiten auf dein Land, und kannst somit höhstens "+ (risiko.getLandArmee(vonLand)-1) + " Einheiten verschieben. \n" 
                                                + "Gib jetzt an, wie vielen Einheiten du verschieben möchtest.");

                                                int armeeAnzahl = risiko.readInt(scanner, 1, (risiko.getLandArmee(vonLand)-1)); // Eingabe der anzahl von verschobenen Einheiten

                                                risiko.verschieben(vonLand, nachLand, armeeAnzahl);

                                                System.out.println("Möchtest du weitere Einheiten verschieben? \nTippe: \n'J' für Ja\n'N' für Nein");

                                                spielerWillVerschieben = scanner.next().trim().equalsIgnoreCase("j")? true: false;
                                            }

                                            risiko.nextPhase();
                                            break;
                                    }
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
                    break;
            }
        }
        scanner.close();
    }
}
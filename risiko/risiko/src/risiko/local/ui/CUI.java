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

                    //Kann man mit einer Methode austauschen in der Risiko klasse die prüft ob das spiel noch läuft
                    boolean startGame = false;

                    while (!startGame) {

                        System.out.println("\n1. Would you like to start?");
                        System.out.println("2. Or would you like to add more players? (Current number of players: "
                                + risiko.getAnzahlSpieler() + ")");
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
                                boolean spielende = false;
                                while (!spielende) {

                                    switch (risiko.getPhase()) {
                                        case ERSTVERTEILEN: // Anfangsverteil
                                            // for(int i = 0;i <)
                                            int i = 0;

                                            while (risiko.jetzigerSpielerHatZusatzarmee()) { // Wird abgespielt bis
                                                                                            // der Jetzige Spieler alle
                                                                                            // armeen verteilt hat
                                                System.out.println(risiko.getJetzigerSpielerName()
                                                        + ": es ist gerade die Verteilphase");
                                                System.out.println("Deine Länder: ");

                                                risiko.getJetzigerSpielerLaenderListe(); // muss noch geaendert werden

                                                System.out.println("zurzeit zusatz armen in besitzt: "
                                                        + risiko.getJetzigerSpielerZusatzArmee());
                                                System.out.println(
                                                        "Du musst deine Zusatzarmeen verteilen. \nWaehle dazu ein Land auf dem du deine Zusatzarmeen verteilen möchtest.");

                                                int nachLand = scanner.nextInt();

                                                System.out.println("Wie viele Einheiten möchtest du auf das Land "
                                                        + risiko.getLand(nachLand)
                                                        + " platzieren? \n zu verfügung stehen dir noch "
                                                        + risiko.getJetzigerSpielerZusatzArmee() + " Einheiten.");

                                                int anzahlArmee = scanner.nextInt();
                                                risiko.verteilen(nachLand, anzahlArmee);
                                            }
                                            i += 1;
                                            risiko.nextPlayer(); // Naechster spieler
                                            if (i == risiko.getAnzahlSpieler()) { // wenn alle spieler zusatzarmee
                                                                                  // verteilt
                                                                                  // haben dann gehts in die naechste
                                                                                  // phase
                                                risiko.nextPhase();
                                            }

                                            break;

                                        case VERTEILEN: // Verteilen

                                            while (risiko.jetzigerSpielerHatZusatzarmee()) {
                                                System.out.println(risiko.getJetzigerSpielerName()
                                                        + ", es ist gerade die Verteilephase \nBitte verteile deine Zusatzarmeen.");
                                                System.out.println("Deine Länder: ");

                                                risiko.getJetzigerSpielerLaenderListe();

                                                System.out.println("zurzeit zusatz armen in besitzt: "
                                                        + risiko.getJetzigerSpielerZusatzArmee());
                                                System.out.println(
                                                        "Du musst deine Zusatzarmeen verteilen. \nGib die Nummer des Landes ein, auf dem du deine Zusatzarmeen verteilen möchtest.");

                                                int nachLand = scanner.nextInt();

                                                System.out.println("Wie viele Einheiten möchtest du auf das Land "
                                                        + risiko.getLand(nachLand)
                                                        + " platzieren? \n zu verfügung stehen dir noch "
                                                        + risiko.getJetzigerSpielerZusatzArmee() + " Einheiten.");

                                                int anzahlArmee = scanner.nextInt();
                                                risiko.verteilen(nachLand, anzahlArmee);

                                                System.out.println("Die Einheiten wurden effolgreich verteilt.\n");
                                            }
                                            risiko.nextPhase();
                                            break;

                                        case ANGREIFFEN: // Angreifen
                                            boolean spielerWillAngreifen = true;
                                            System.out.println("Spieler " + risiko.getJetzigerSpielerName() + ", gerade bist du in die Angreiffenphase ");
                                            System.out.println("Möchtest du die Angreiffephase übespringen? \nTippe: \n'J' für Ja \n'N' für Nein");

                                            spielerWillAngreifen = scanner.next().trim().equalsIgnoreCase("j") ? true: false;

                                            while (spielerWillAngreifen) {

                                                System.out.println("Du hast dich fur den Angriff entschieden \nWälle ein land aus dem sie Angreiffen wollen");
                                                risiko.getJetzigerSpielerAngriffBereiteLaender(); // Ausgabe der agnriffsbereiten laender

                                                System.out.println("Bitte gib die Nummer des Landes an mit dem du angreifen moechtests.");

                                                int vonLand = scanner.nextInt(); // Eingabe der Angriffslandes, Exception nicht vergessen

                                                System.out.println("Von dem Land: " + risiko.getLand(vonLand)+ " können folgende gegnerische Länder angegriffen werden: ");

                                                risiko.getAngriffGegnerLaender(vonLand); // Ausgabe der Adjazierenden genger laender

                                                System.out.println("Bitte gib die Nummer des Landes an, welches du angreifen möchtests.");

                                                int nachLand = scanner.nextInt(); // Eingabe der Verteidigungslandes, Exception nicht vergessen

                                                System.out.println("Du hast " + risiko.getLandArmee(vonLand)
                                                        + " Einheiten auf dein Land, und kannst somit mit höhstens "
                                                        + risiko.getMaxAttackNumber(vonLand)
                                                        + " Einheiten Angreifen. \n Gib jetzt an, mit wie vielen Einheiten du angreifen möchtest (1, 2 oder 3 Einheiten).");

                                                int armeeAnzahl = scanner.nextInt(); // Eingabe der Agriffs armee anzahl, Exception nicht vergessen

                                                risiko.angreifen(vonLand, nachLand, armeeAnzahl);

                                                System.out.println("Resultat : " + risiko.getAngriffResult());

                                                System.out.println("Möchtest du weiter angreifen? \nTippe: \n'J' für Ja \n'N' für Nein");

                                                spielerWillAngreifen = scanner.next().trim().equalsIgnoreCase("j")? true: false;
                                            }

                                            risiko.nextPhase();
                                            break;

                                        case VERSCHIEBEN: // Verschieben
                                            boolean spielerWillVerschieben = true;
                                            System.out.println(risiko.getJetzigerSpielerName() + " es ist gerade die Verschiebephase");

                                            System.out.println("Möchtest du die Verschiebephase übespringen? \nTippe: \n'J' für Ja \n'N' für Nein");

                                            spielerWillVerschieben = scanner.next().trim().equalsIgnoreCase("j")? true: false;

                                            while (spielerWillVerschieben) {
                                                System.out.println("Vom welchem Land möchtest du Einheiten verschieben?");
                                                risiko.getVerschiebebereiteLaender();

                                                System.out.println("Bitte gib die Nummer des Landes an von wo du die Einheiten aus verschieben möchtest.");

                                                int vonLand = scanner.nextInt(); // Eingabe der Landes von wo aus verschoben wird, Exception nicht vergessen
                                                
                                                System.out.println("Von dem Land: " + risiko.getLand(vonLand)+ " können nach folgenden Länder verschoben werden: ");

                                                //funktion für angrenzende länder

                                                System.out.println("Bitte gib die Nummer des Landes an, welches die Einheiten erhalten soll.");

                                                int nachLand = scanner.nextInt(); // Eingabe des Landes die die Einheiten bekommen soll, Exception nicht vergessen

                                                System.out.println("Du hast " + risiko.getLandArmee(vonLand)
                                                        + " Einheiten auf dein Land, und kannst somit höhstens "
                                                        + (risiko.getLandArmee(vonLand)-1)
                                                        + " Einheiten verschieben. \n"
                                                        + "Gib jetzt an, wie vielen Einheiten du verschieben möchtest (Mindestens eine Einheite muss bleiben).");

                                                int armeeAnzahl = scanner.nextInt(); // Eingabe der anzahl von verschobenen Einheiten, Exception nicht vergessen

                                                risiko.verschieben(vonLand, nachLand, armeeAnzahl);

                                                System.out.println("Möchtest du weitere Einheiten verschieben? \nTippe: \n'J' für Ja\n'N' für Nein");

                                                spielerWillVerschieben = scanner.next().trim().equalsIgnoreCase("j")? true: false;
                                            }

                                            risiko.nextPhase();
                                            break;
                                        // 1. Fragen ob man Verteilen möchte. Bei nein: Nächste Phase
                                        // 2. Ein Land wählen welches mehr als eine Einheit besitzt und dem Spieler
                                        // gehört
                                        // 3. Ein angrenzendes Land wählen, welches dem Spieler gehört und bestimmen wie
                                        // viele Einheiten übergehen sollen
                                        // 4. Fragen ob man weiter Verteilen möchte. Bei Ja: wieder von vorne anfangen,
                                        // Bei nein: Nächste Phase.
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

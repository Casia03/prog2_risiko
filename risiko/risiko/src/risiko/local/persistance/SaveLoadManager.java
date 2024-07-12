package risiko.local.persistance;

import java.io.*;
import java.util.List;

import risiko.local.domain.Risiko;
import risiko.local.entities.Land;
import risiko.local.entities.Spieler;
import risiko.local.entities.Turn;
import risiko.local.persistance.Exceptions;

public class SaveLoadManager {

    private static final String SAVE_FILE_PATH = "risiko/risiko/src/risiko/local/saveFiles/game.dat";
    Exceptions Exceptions = new Exceptions();

    public void saveGame(Risiko risiko) { // speichert Spieldaten in eine Datei
        GameData gameData = new GameData(risiko.getSpielerListe(), risiko.getLaender(), risiko.getTurn(), risiko.getJetzigerSpieler().getSpielerID(), risiko.getPhase(), risiko.getEingetauschteKarten());

        try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(SAVE_FILE_PATH))) {
            outputStream.writeObject(gameData);
            System.out.println("Game saved successfully.");
        } catch (IOException e) {
            // Exceptions.showErrorDialog("die spiel daten konnten nicht gespeichert werden");
            System.err.println("die spiel daten konnten nicht gespeichert werden: " + e);
        }
    }

    public Risiko loadGame(Risiko risiko) { // lädt Spieldaten aus einer Datei
        try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(SAVE_FILE_PATH))) {
            GameData gameData = (GameData) inputStream.readObject();


            risiko.loadSpielerListe(gameData.getSpielerListe());
            risiko.loadLaender(gameData.getLandList());

            risiko.loadInitializeTurn(risiko.getSpielerListe().size());
            
            risiko.loadTurn(gameData.getTurn());
            risiko.loadJetzigerSpieler(gameData.getSpielerId());
            risiko.loadPhase(gameData.getPhase());
            risiko.loadEingetauschteKarten(gameData.getEingetauschteKarten());

            // System.out.println("Game loaded successfully.");
            return risiko;

        } catch (IOException | ClassNotFoundException e) {
            // Exceptions.showErrorDialog("die spiel daten konnten nicht geladen werden");
            System.err.println("die spiel daten konnten nicht geladen werden: " + e);
            return null;
        }
    }

    public void printSavedGame() { // gibt gespeicherte Spieldaten aus
        try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(SAVE_FILE_PATH))) {
            GameData gameData = (GameData) inputStream.readObject();
/*            System.out.println("Spielerliste:");
            for (Spieler spieler : gameData.getSpielerListe()) {
                System.out.println(spieler);
            }

            System.out.println("Länder:");
            for (Land land : gameData.getLandList()) {
                System.out.println(land);
            }

            System.out.println("Turn:");
            System.out.println(gameData.getTurn());

            System.out.println("Phase:");
            System.out.println(gameData.getPhase());*/ 

            System.out.println("Eingetauschte Einheitenkarten:");
            System.out.println(gameData.getEingetauschteKarten());

        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Die gespeicherten Spieldaten konnten nicht geladen werden: " + e);
        }
    }
}

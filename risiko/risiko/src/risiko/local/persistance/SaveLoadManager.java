package risiko.local.persistance;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import risiko.local.domain.Risiko;
import risiko.local.entities.Land;
import risiko.local.entities.Spieler;
import risiko.local.entities.Turn;
import risiko.local.persistance.Exceptions;

public class SaveLoadManager {

    private static final String SAVE_FOLDER_PATH = "risiko/risiko/src/risiko/local/saveFiles/";
    Exceptions Exceptions = new Exceptions();
    
    private void saveSpieler(Spieler spieler) throws IOException {
        File saveFolder = new File(SAVE_FOLDER_PATH);
        String filePath = SAVE_FOLDER_PATH + spieler.getSpielerName() + ".dat";
        
        if (!saveFolder.exists()) {
            saveFolder.mkdirs();
        }

        try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(filePath))) {
            outputStream.writeObject(spieler);
        }
    }

    private void saveCountries(List<Land> landList) throws IOException {
        String filePath = SAVE_FOLDER_PATH + "countries.dat";
        try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(filePath))) {
            outputStream.writeObject(landList);
        }
    }

    private void saveTurn(int i, int j) throws IOException {
        String filePath = SAVE_FOLDER_PATH + "turn.dat";
        try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(filePath))) {
            outputStream.write(i);
            outputStream.write(j);
        }
    }

    private List<Spieler> loadSpieler() throws IOException, ClassNotFoundException {
        List<Spieler> spielerListe = new ArrayList<>();
        File folder = new File(SAVE_FOLDER_PATH);
        File[] files = folder.listFiles();

        for (File file : files) {
            if (file.isFile() && file.getName().endsWith(".dat") && !file.getName().equals("countries.dat") && !file.getName().equals("turn.dat")) {
                try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(file))) {
                    Spieler spieler = (Spieler) inputStream.readObject();
                    spielerListe.add(spieler);
                }
            }
        }
        return spielerListe;
    }

    private List<Land> loadCountries() throws IOException, ClassNotFoundException {
        String filePath = SAVE_FOLDER_PATH + "countries.dat";

        try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(filePath))) {
            List<Land> landList = (List<Land>) inputStream.readObject();
            return landList;
        }
    }

    private Turn loadTurn() throws IOException, ClassNotFoundException {
        String filePath = SAVE_FOLDER_PATH + "turn.dat";
        try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(filePath))) {
            Turn turn = (Turn) inputStream.readObject();
            return turn;
        }
    }

    public void saveGame(Risiko risiko) {
        try {
            
            
            for (Spieler spieler : risiko.getSpielerListe()) {
                saveSpieler(spieler);
            }
            
            saveCountries(risiko.getLaender());
            saveTurn(risiko.getTurn(), risiko.getPhaseNr());
             System.out.println("Game saved successfully.");

        } catch (IOException e) {
            //Exceptions.showErrorDialog("die spiel daten konnten nicht gespeichert werden");
            System.err.println("die spiel daten konnten nicht gespeichert werden" + e);
        }
    }

    public Risiko loadGame() {
        try {
            List<Spieler> spielerListe = loadSpieler();
            List<Land> landList = loadCountries();
            Turn turn = loadTurn();

            Risiko risiko = new Risiko();
            risiko.getSpielerListe().addAll(spielerListe);
            risiko.getLaender().addAll(landList);
            //risiko.setTurn(turn);

            // System.out.println("Game loaded successfully.");
            return risiko;

        } catch (IOException | ClassNotFoundException e) {
            //Exceptions.showErrorDialog("die spiel daten konnten nicht geladen werden");
            System.err.println("die spiel daten konnten nicht geladen werden");
            return null;
        }
    }

    
}
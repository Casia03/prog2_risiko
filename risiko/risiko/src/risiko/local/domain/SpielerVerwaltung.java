package risiko.local.domain;

import java.util.ArrayList;
import java.util.List;

import risiko.local.entities.Spieler;
import risiko.local.persistance.Exceptions;

public class SpielerVerwaltung {
    Exceptions Exceptions = new Exceptions();
    private List<Spieler> spielerListe = new ArrayList<>();;

    public SpielerVerwaltung() {
        
    }

    public void spielerHinzufuegen(String name) { // Hinzufuegen eines neuen Spielers
        try {
            for(Spieler s : spielerListe) {
                if(s.getSpielerName().equals(name)) {
                    throw new IllegalArgumentException("Ein Spieler mit dem Namen " + name + " existiert bereits.");
                }
            }
            
            Spieler spieler = new Spieler(name, getAnzahlSpieler(), 30, 0, new int[3]);
            spielerListe.add(spieler);
        } catch (IllegalArgumentException e) {
            // Hier könnten Sie eine Meldung ausgeben oder anderweitig darauf reagieren
            System.out.println("Fehler beim Hinzufügen des Spielers: " + e.getMessage());
        }
    }

    public int getAnzahlSpieler() { // Gibt die Anzahl der Spieler zurueck
        return spielerListe.size();
    }

    public Spieler returnSpieler(int spielerID) { // Gibt den Spieler anhand seiner ID zurueck
        return spielerListe.get(spielerID);
    }

    public List<Spieler> getSpielerListe() { // Gibt die Liste der Spieler zurueck
        return new ArrayList<>(spielerListe);
    }

    public Spieler getSpielerByID(int spielerID){ // Gibt den Spieler anhand seiner ID zurueck
        return spielerListe.get(spielerID);
    }

    public void loadSpieler(List<Spieler> loadedSpieler) { // Laden einer gespeicherten Spielerliste
        for(int i = 0; i < loadedSpieler.size(); i++){
            spielerListe.add(loadedSpieler.get(i));
        }
 
    }

}

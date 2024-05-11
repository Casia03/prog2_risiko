package risiko.local.domain;

import java.util.ArrayList;
import java.util.List;

import risiko.local.entities.Spieler;

public class SpielerVerwaltung {
    private List<Spieler> spielerListe = new ArrayList<>();

    public SpielerVerwaltung() {

    }

    public void spielerHinzufuegen(String name) {
        Spieler spieler = new Spieler(name, getAnzahlSpieler(), 30, 0);
        if (!spielerListe.contains(spieler)) {
            spielerListe.add(spieler);
        } else {
            // Spieler existiert schon exception
        }
    }

    public int getAnzahlSpieler() {
        return spielerListe.size();
    }

    public Spieler returnSpieler(int spielerID) {
        return spielerListe.get(spielerID);
    }

    public List<Spieler> getSpielerListe() {
        return new ArrayList<>(spielerListe);
    }

    public Spieler getSpielerByID(int spielerID){
        return spielerListe.get(spielerID);
    }

}

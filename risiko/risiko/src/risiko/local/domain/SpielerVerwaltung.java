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

<<<<<<< HEAD
    public Spieler getSpielerByID(int spielerID){
=======
    public Spieler getJetzigerSpieler(int spielerID) {
>>>>>>> be95d6b692f3cdcb4dc8b0946a0403df9a5cf535
        return spielerListe.get(spielerID);
    }

}

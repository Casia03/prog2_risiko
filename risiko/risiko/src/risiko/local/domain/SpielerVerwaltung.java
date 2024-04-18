package risiko.local.domain;

import java.util.ArrayList;
import java.util.List;

import risiko.local.entities.Spieler;

public class SpielerVerwaltung {
    private List<Spieler> spielerListe = new ArrayList<>();

    public void spielerHinzufuegen(String name) {
        Spieler spieler = new Spieler(name, getAnzahlSpieler(), 30, null);
        if (!spielerListe.contains(spieler)) {
            spielerListe.add(spieler);
        }
    }

    public int getAnzahlSpieler() {
        return spielerListe.size();
    }

    public Spieler returnSpieler(int i) {
        return spielerListe.get(i);
    }

    public List<Spieler> getSpieler() {
        return new ArrayList<>(spielerListe);
    }
}

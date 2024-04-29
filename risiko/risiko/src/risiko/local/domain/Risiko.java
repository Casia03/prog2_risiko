package risiko.local.domain;

import java.util.ArrayList;
import java.util.List;

import risiko.local.entities.Land;
import risiko.local.entities.Spieler;
//import risiko.local.persistance.SaveLoadManager;

public class Risiko {
    private Weltverwaltung weltVerwaltung;
    private SpielerVerwaltung spielerVerwaltung;
    private SpielLogik spielLogik;
  //  private SaveLoadManager saveloadmanager;

    public Risiko(){
        weltVerwaltung = new Weltverwaltung();
        spielerVerwaltung = new SpielerVerwaltung();
        // Spieler initialisieren

        weltVerwaltung.initialisiereWelt();
    //    SaveLoadGameManager = new SaveLoadGameManager();
        
    }

    public void game(Risiko risiko){
        
    }

    public int getAnzahlSpieler() {
        return spielerVerwaltung.getAnzahlSpieler();
    }

    public void spielerHinzufuegen(String name) {
        spielerVerwaltung.spielerHinzufuegen(name);
    }

    public List<Spieler> getSpielerListe(){
        return new ArrayList<>(spielerVerwaltung.getSpieler());
    }
    /* 
    public void loadGame() {
        SaveLoadManager.loadGame();
    }
    */
    public ArrayList<Land> getLaender() {
        return weltVerwaltung.getLaeder();
        
    }
}

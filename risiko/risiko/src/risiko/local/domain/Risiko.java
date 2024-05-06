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
        spielLogik = new SpielLogik();
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
    public void rewardsCheck(int spielerID){
        Spieler spieler = spielerVerwaltung.returnSpieler(spielerID);
        // konditionen zum zusatzermee erhalt checken, aber das alles in der logik
    }
    public void verteilen(int spielerID, int landID, int amree){
        Spieler spieler = spielerVerwaltung.returnSpieler(spielerID);
        Land land = weltVerwaltung.getLand(landID);
        spielLogik.verteilen(spieler, land, amree);
    }
    public void angreifen(int vonLandID, int nachLandID, int angreifeAnzahl, int verteidigerAnzahl){
        Land nachLand = weltVerwaltung.getLand(nachLandID);
        Land vonLand = weltVerwaltung.getLand(vonLandID);
        spielLogik.angreifen(vonLand, nachLand, angreifeAnzahl, verteidigerAnzahl); // Exceptions noetig
    } 
    public void verschieben(int vonLandID, int nachLandID, int anzahl){
        Land nachLand = weltVerwaltung.getLand(nachLandID);
        Land vonLand = weltVerwaltung.getLand(vonLandID);
        
        //adjazenz pruefen
        //verschiebe azahl pruefen und dann verschieben
    }
}

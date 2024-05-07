package risiko.local.domain;

import java.util.ArrayList;
import java.util.List;

import risiko.local.entities.Land;
import risiko.local.entities.Spieler;
//import risiko.local.persistance.SaveLoadManager;
import risiko.local.entities.Turn;

public class Risiko {
    private WeltVerwaltung weltVerwaltung;
    private SpielerVerwaltung spielerVerwaltung;
    private SpielLogik spielLogik;
    private Turn turn;
  //  private SaveLoadManager saveloadmanager;

    public Risiko(){
        weltVerwaltung = new WeltVerwaltung();
        spielerVerwaltung = new SpielerVerwaltung();
        spielLogik = new SpielLogik(weltVerwaltung);
        //Entweder hier muss das gemacht werden oer in Start game methode. 
    //    SaveLoadGameManager = new SaveLoadGameManager();
        
    }

    public int currentSpieler(){
        return turn.getSpieler();
    }
    public void startGame(Risiko risiko){
        List<Spieler> spielerListe = spielerVerwaltung.getSpieler();
        weltVerwaltung.initialisiereWelt();
        turn = new Turn(spielerListe);
        weltVerwaltung.verteileLaender(spielerListe);
        //werltverwaltung verteile missionen
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

    public void erstVerteilen(int spielerID, int landID, int armee, Turn turn){
        verteilen(spielerID,landID,armee);
        if(spielerVerwaltung.returnSpieler(spielerID).getZusatzArmee()!=0){
            turn.nextPlayer();
        }
        turn.nextPhase();
    }

    public void verteilen(int spielerID, int landID, int armee){
        Spieler spieler = spielerVerwaltung.returnSpieler(spielerID);
        Land land = weltVerwaltung.getLand(landID);
        spielLogik.verteilen(spieler, land, armee);
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

// StartGame
// --> Anfangsverteilephase soweit alle noch zusatzarmee haben
// ----> Angreifephase fur der ersten spieler, sollange er angreifen moeche/kann
// -------> Verschiebephase fur der Ersten spieler, wenn er moechte/kann
// ----------> Naechster spieler
// ------> Angreifephase fur spieler 2, wenn er moechte/...
// --------> ...
// ----> Wenn wieder Spieler 1, dann rewardscheck, zusatzarmee geben.
// ------> Verteilfephase Spieler 1.

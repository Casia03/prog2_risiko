package risiko.local.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import risiko.local.entities.AdjazenzMatrix;
import risiko.local.entities.Kontinent;
import risiko.local.entities.Land;
import risiko.local.entities.Mission;
import risiko.local.entities.Spieler;
import risiko.local.persistance.SaveLoadManager;
import risiko.local.persistance.Exceptions;
import risiko.local.entities.Turn;
import risiko.local.entities.Turn.Phase;

public class Risiko {
    private WeltVerwaltung weltVerwaltung;
    private SpielerVerwaltung spielerVerwaltung;
    private SpielLogik spielLogik;
    private Turn turn;
    private Spieler spieler;
    private AdjazenzMatrix adj;
    private Exceptions e;
    private Kontinent kontinente;
    private Mission mission;
    
    private SaveLoadManager slm;

    public Risiko(){
        weltVerwaltung = new WeltVerwaltung();
        spielerVerwaltung = new SpielerVerwaltung();
        spielLogik = new SpielLogik(weltVerwaltung);
        adj = new AdjazenzMatrix(weltVerwaltung);
        e = new Exceptions();
        slm = new SaveLoadManager();
        //Entweder hier muss das gemacht werden oer in Start game methode. 
        //SaveLoadGameManager = new SaveLoadGameManager();
        
    }

    public void startGame(Risiko risiko){
        List<Spieler> spielerListe = getSpielerListe(); // Spielerliste furs weiterleiten an Klassen die diese benoetigen
        weltVerwaltung.initialisiereWelt();             // laender objekte werden erstellt
        turn = new Turn(spielerListe);                  // Turn klasse wird Initialisiert, um das spielzyklus zu gestalten
        weltVerwaltung.verteileLaender(spielerListe);   // Laender werden an Spielern verteilt
        kontinente = new Kontinent(weltVerwaltung.getLaeder()); //Kontinente werden Initialisiert
        weltVerwaltung.missionenVerteilung(spielerListe);       // Missionen werden verteilt
        mission = new Mission(kontinente);                      // Missionen werde Initialisiert, um spaeter die uberpruefung des statuses jeder mission zur uberpruefen
    }

    public int getAnzahlSpieler() {
        return spielerVerwaltung.getAnzahlSpieler();
    }

    public void spielerHinzufuegen(String name) {
        spielerVerwaltung.spielerHinzufuegen(name);
    }

    public List<Spieler> getSpielerListe(){
        return new ArrayList<>(spielerVerwaltung.getSpielerListe());
    }
    
    public int getTurn(){
        return turn.getTurn();
    }


    public String getJetzigerSpielerMission(){
        spieler = getJetzigerSpieler();
        int missionNummer = spieler.getMissionId();

        return mission.getMissionBeschreibung(missionNummer);
    }

    public void nextPhase(){
        turn.nextPhase();
    }

    public void nextPlayer(){
        turn.nextPlayer();
    }

    public Phase getPhase(){
        return turn.getPhase();
    } 
 
    public void loadGame() {
        slm.loadGame();
    }

    public void saveGame(Risiko risiko){
        slm.saveGame(risiko);
    }

    public ArrayList<Land> getLaender() {
        return weltVerwaltung.getLaeder();
        
    }

    // public void rewardsCheck(int spielerID){
    //     Spieler spieler = spielerVerwaltung.returnSpieler(spielerID);
    //     // konditionen zum zusatzermee erhalt checken, aber das alles in der logik
    // }

    public void erstVerteilen(int landID, int armee){
        spieler = getJetzigerSpieler();
        verteilen(landID,armee);
        if(spieler.getZusatzArmee()!=0){
            turn.nextPlayer();
        }else{
            turn.nextPhase();
        }
        
    }

    public void verteilen(int landID, int armee){
        spieler = getJetzigerSpieler();
        Land land = weltVerwaltung.getLand(landID);
        spielLogik.verteilen(spieler, land, armee);

    }

    public String angreifen(int vonLandID, int nachLandID, int angreifeAnzahl){
        String resultat;
        Land nachLand = getLand(nachLandID);
        Land vonLand = getLand(vonLandID);
        resultat = spielLogik.angreifen(vonLand, nachLand, angreifeAnzahl); // Exceptions noetig
        return resultat;
    } 

    public void einruecken(int vonLandID, int nachLandID) {
        Land vonLand = getLand(vonLandID);
        Land nachLand = getLand(nachLandID);

        spielLogik.einruecken(vonLand, nachLand);
    }

    public void verschieben(int vonLandID, int nachLandID, int anzahl){
        Land nachLand = weltVerwaltung.getLand(nachLandID);
        Land vonLand = weltVerwaltung.getLand(vonLandID);

        spielLogik.verschieben(vonLand, nachLand, anzahl);

    }


    /*       SPIELER METHODEN          */
    
    public int jetzigerSpielerNummer(){ // Gibt die Nummer des jetzigen spielers zuruck
        return turn.getSpieler();
    }

    public Spieler getJetzigerSpieler(){ // Gibt das Object "jetziger spieler" zuruck
        spieler = spielerVerwaltung.getSpielerByID(turn.getSpieler());
        return spieler;
    }

    public String getJetzigerSpielerName(){ // Gibt den Namen des Jetzigen Spielers zuruck
        spieler = spielerVerwaltung.getSpielerByID(turn.getSpieler());
        return spieler.getSpielerName();
    }

    public int getJetzigerSpielerZusatzArmee() { // gib die Zusatzarmee dies Jetzigen Spielers zuruck
       Spieler spieler =  getJetzigerSpieler();
       return spieler.getZusatzArmee();
    }

    public boolean jetzigerSpielerHatZusatzarmee(){
        Spieler spieler = getJetzigerSpieler();
        if(spieler.getZusatzArmee() > 0){
            return true;
        }else{
            return false;
        }
    }

    public String[] getJetzigerSpielerLaenderListe(){  // Gibt die Laender des Jetzigen spielers zurcuk
        spieler = getJetzigerSpieler();
        List<Land> laender = weltVerwaltung.getSpielerLaender(spieler); // wird zur ne string ausgabe geaendert 
        return weltVerwaltung.getSpielerLaenderAusgabe(laender);
        //code zum anzeigen der laender, wahrscheinlich ein String return??

    }

    // public String[] getJetzigerSpielerAngriffBereiteLaender(){ // Gibt die Angriffsbereite laender die zu den Jetzigen spieler gehoren
    //     spieler = getJetzigerSpieler();
    //     List<Land> laender = weltVerwaltung.getSpielerLaender(spieler); // wird zur ne string ausgabe geaendert 
    //     String[] liste = adj.getAlleAngreifebereiteLaender(laender, spieler);

    //     return liste;

    // }
    
    public Spieler getSpielerByID(int spielerID){ // Gibt den spieler zuruck mit den eingegebenen spielerID
        return spielerVerwaltung.getSpielerByID(spielerID);
    }

    public int getZusatzArmeeBySpielerID ( int spielerID){
        return getSpielerByID(spielerID).getZusatzArmee();
    }

    public String getMissionBySpielerID ( int spielerID){
        return null;//getSpielerByID(spielerID).getMissionId();
    }

    public void getLaenderListeBySpielerID ( int spielerID){
        spieler = getSpielerByID(spielerID);
        List<Land> laender = weltVerwaltung.getSpielerLaender(spieler); // wird zur ne string ausgabe geaendert 

    }




    /*       LAND METHODEN          */
    public Land getLand(int landID) {
        Land land = weltVerwaltung.getLand(landID);
        return land;
    }

    public String getLandName(int landID){
        return getLand(landID).getName();
    }

    public String getLandSpielerName(int landID){ // Gibt den Namen des Spielers der das Ausgewaehlte Land besitzt
        String spielerName;
        Land land = getLand(landID);
        spielerName = getSpielerByID(land.getEingenommenVon()).getSpielerName();
        return spielerName;
    }

    public int getLandArmee(int landID) {
        int landArmee = getLand(landID).getArmee();
        return landArmee;
    }

    public String[] getAngriffGegnerLaender(int vonLand){ // Gibt eine liste von die gegnerische nachbar laender zuruck.
        spieler = getJetzigerSpieler();
        String[] list = adj.getAlleGegnerNachbar(vonLand - 1, spieler);
        return list;
    }

    public String[] getVerschiebebereiteLaender(){ // Gibt eine liste von den eigenen Laender, die genug Armee fur eine verschiebung besitzen
        String[] bereiteLaender;
        spieler = getJetzigerSpieler();
        List<Land> laender = weltVerwaltung.getSpielerLaender(spieler); // wird zur ne string ausgabe geaendert  
        
        bereiteLaender = adj.getAlleVerschiebeBereiteLaender(laender);
        if(bereiteLaender != null){
            return bereiteLaender;
        }

        throw new IllegalArgumentException("Du hast keine Verschiebebereite Laender");
    }

    public String[] getAngreiffeBereiteLaender(){
        String[] bereiteLaender;
        spieler = getJetzigerSpieler();
        List<Land> laender = weltVerwaltung.getSpielerLaender(spieler); // wird zur ne string ausgabe geaendert   
        bereiteLaender = adj.getAlleAngreifebereiteLaender(laender, spieler);
        if(bereiteLaender != null){
            return bereiteLaender;
        }

        throw new IllegalArgumentException("Du hast keine Angriffbereite Laender");
    }

    public void neuerBesitzerSetzen(int vonLand){
        spieler = getJetzigerSpieler();
        weltVerwaltung.setNeueBesitzer(vonLand, spieler);
    }
    
    public void waehleVerschiebeZiel(int vonLand){ //  hmm
        spieler = getJetzigerSpieler();
        adj.getAlleEigeneNachbars(vonLand, spieler);
    }
    
    /*      AUSWERTUNGS METHODEN        */

    public int getMaxAttackNumber(int vonLand) {
        int landArmee = getLand(vonLand).getArmee();
        int maxAttack = spielLogik.getMaxAttackNumber(landArmee);
        return maxAttack;
    }

    public boolean landHatKeineArmee(int nachLand) {
        Land land = getLand(nachLand);
        if(land.getArmee() <= 0){
            return true;
        }else{
            return false;
        }
    }

    public String[] getAlleEigeneNachbars(int vonLand) {
        spieler = getJetzigerSpieler();
        String[] liste = adj.getAlleEigeneNachbars(vonLand - 1, spieler);
        return liste;
    }

    public int berechneZusatzarmeen() {
        spieler = getJetzigerSpieler();
        int anzahlTerritorien = weltVerwaltung.getSpielerLaender(spieler).size();
        int zusatzarmeen = 0;

        // Berechnung basierend auf der Anzahl der Territorien
        zusatzarmeen += Math.max(anzahlTerritorien / 3, 3); // Mindestens 3 Armeen, wenn weniger als 9 Territorien

        // Bonusarmeen für vollständige Kontrolle von Kontinenten
        //if (kontrolliertAsien) zusatzarmeen += 7;
        //if (kontrolliertNordamerika) zusatzarmeen += 5;
        //if (kontrolliertEuropa) zusatzarmeen += 5;
        //if (kontrolliertAfrika) zusatzarmeen += 3;
        //if (kontrolliertSuedamerika) zusatzarmeen += 2;
        //if (kontrolliertAustralien) zusatzarmeen += 2;

        return zusatzarmeen;
    }

    public void addBonusArmee(){ // Addieren
        spieler = getJetzigerSpieler();
        int zusatzArmee = berechneZusatzarmeen();
        spieler.addZusatzarmee(zusatzArmee);
    }

    public int readInt(Scanner scanner,int min,int max){
        return e.readInt(scanner, min, max);
    }

    public int readJetzigerPlayerLandIndex(Scanner scanner){
        spieler = getJetzigerSpieler();
        List<Land> validIndices = weltVerwaltung.getSpielerLaender(spieler);
        return e.readLandIndex(scanner, validIndices);
    }

    public int readGegnerLandIndex(Scanner scanner, int vonLand){
        spieler = getJetzigerSpieler();
        List<Land> validIndices = adj.getAlleGegnerNachbarListe(vonLand - 1, spieler);
        return e.readLandIndex(scanner, validIndices);
    }


    public boolean checkIfMissionErfuelt(){
        spieler = getJetzigerSpieler();
        boolean ergebniss = mission.checkIfMissionErfuelt(weltVerwaltung.getSpielerLaender(spieler), spieler.getMissionId());
        return ergebniss;
    }

    public String missionStatus(){
        if(checkIfMissionErfuelt()){
            return "Spieler " + getJetzigerSpielerName() + " Du Hast deine Mission Eruellt, Kongratulations!!";
        } else{
            return "Spieler " + getJetzigerSpielerName() + ", deine Mission ist noch nicht erfuellt.";
        }
    }

    public int getSpielerMissionNummer(){
        spieler = getJetzigerSpieler();
        return spieler.getMissionId();
    }

}
package risiko.local.domain;

import java.security.cert.TrustAnchor;
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

    public void newGame(Risiko risiko){
        List<Spieler> spielerListe = getSpielerListe(); // Spielerliste furs weiterleiten an Klassen die diese benoetigen
        weltVerwaltung.initialisiereWelt();             // laender objekte werden erstellt 
        turn = new Turn(spielerListe.size());                  // Turn klasse wird Initialisiert, um das spielzyklus zu gestalten 
        weltVerwaltung.verteileLaender(spielerListe);   // Laender werden an Spielern verteilt
        weltVerwaltung.missionenVerteilung(spielerListe);   // Missionen werden verteilt
        kontinente = new Kontinent(weltVerwaltung.getLaeder()); //Kontinente werden Initialisiert   
        mission = new Mission(kontinente);                      // Missionen werde Initialisiert, um spaeter die uberpruefung des statuses jeder mission zur uberpruefen
    }

    public void loadGame(Risiko risiko){
        kontinente = new Kontinent(weltVerwaltung.getLaeder()); //Kontinente werden Initialisiert   
        mission = new Mission(kontinente);                      // Missionen werde Initialisiert, um spaeter die uberpruefung des statuses jeder mission zur uberpruefen
    }

    public int getAnzahlSpieler() {                     // gibt die größe der spieler Liste zurück
        return spielerVerwaltung.getAnzahlSpieler();
    }

    public void spielerHinzufuegen(String name) { // um neue Spieler hinzufügen
        spielerVerwaltung.spielerHinzufuegen(name);
    }
   
    public int getTurn(){ // gibt den Jetzigen turn zuruck
        return turn.getTurn();
    }

    public Phase getPhase(){ // gibt die Jetzige Phase zuruck
        return turn.getPhase();
    } 
   
    public void nextPhase(){ // aendert die Phase auf die naechste, wobei nach der Verschiebephase noch der Jetziger Spieler geaendert wird
        turn.nextPhase();
    }

    public void nextPlayer(){ // aendert den jetzigen spieler auf den naechsten, nur für die Erstverteilephase nötig, wird sonnst mit nextPhase() gemacht
        turn.nextPlayer();
    }
 
   

    // SPIELZYKLUS
    public void erstVerteilen(int landID, int armee){ // Die ErstverteilePhase, aendert sich nach dem durchgehen aller Spieler auf die Angreifephase
        spieler = getJetzigerSpieler();
        verteilen(landID,armee);
        if(spieler.getZusatzArmee()!=0){
            turn.nextPlayer();
        }else{
            turn.nextPhase();
        }
        
    }

    public void verteilen(int landID, int armee){ // Methode zur implementrierung der verteile phase 
        spieler = getJetzigerSpieler();
        Land land = weltVerwaltung.getLand(landID);
        spielLogik.verteilen(spieler, land, armee);

    }

    public String angreifen(int vonLandID, int nachLandID, int angreifeAnzahl){ // Methode zur implementrierung der angreiffe phase 
        String resultat;
        Land nachLand = getLand(nachLandID);
        Land vonLand = getLand(vonLandID);
        resultat = spielLogik.angreifen(vonLand, nachLand, angreifeAnzahl); // Exceptions noetig
        return resultat;
    } 

    public void einruecken(int vonLandID, int nachLandID) { // Methode zur implementrierung des einrueckens nach einer erfolgreichen eroberung eines Landes 
        Land vonLand = getLand(vonLandID);
        Land nachLand = getLand(nachLandID);

        spielLogik.einruecken(vonLand, nachLand);
    }

    public void verschieben(int vonLandID, int nachLandID, int anzahl){ //  Methode zur implementrierung der verschiebe phase 
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

    public boolean jetzigerSpielerHatZusatzarmee(){ //schaut nach ob der jetzige spieler zusatzarmmen hat
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

    public Spieler getSpielerByID(int spielerID){ // Gibt den spieler zuruck mit den eingegebenen spielerID
        return spielerVerwaltung.getSpielerByID(spielerID);
    }

    public int getZusatzArmeeBySpielerID ( int spielerID){ // Gibt die Zusatzarmee des Spielers zuruck mit der eingegeben
        return getSpielerByID(spielerID).getZusatzArmee();
    }

    public String getMissionBySpielerID ( int spielerID){ // Gibt die Mission des Spielers zuruck mit der eingegebenen spiel
        return null;//getSpielerByID(spielerID).getMissionId();
    }
    
    public int getSpielerID(){ // Gibt die ID des jetzigen Spielers zuruck
        return spieler.getSpielerID();
    }
    
    public String getSpielerName(){ // Gibt den Namen des jetzigen Spielers zuruck
        return spieler.getSpielerName();
    }
    
    public int getZusatzArmee(){ // Gibt die Zusatzarmee des jetzigen Spielers zuruck
        spieler = getJetzigerSpieler();
        return spieler.getZusatzArmee();
    }
    
    public int getSpielerMissionNummer(){ // Gibt die Nummer der Mission des jetzigen Spielers zuruck
            spieler = getJetzigerSpieler();
            return spieler.getMissionId();
    }
    
    public List<Spieler> getSpielerListe(){ // Gibt eine Liste aller Spieler zuruck
        return new ArrayList<>(spielerVerwaltung.getSpielerListe());
    }



    public String getJetzigerSpielerMission(){ // Gibt die Beschreibung der Mission des jetzigen Spielers zuruck
        spieler = getJetzigerSpieler();
        int missionNummer = spieler.getMissionId();

        return mission.getMissionBeschreibung(missionNummer);
    }

    /*       LAND METHODEN          */
    public ArrayList<Land> getLaender() { // Gibt eine Liste aller Landes zuruck
        return weltVerwaltung.getLaeder();
        
    }

 

    public Land getLand(int landID) { // Gibt das Land zuruck mit der eingegebenen landID
        Land land = weltVerwaltung.getLand(landID);
        return land;
    }

    public String getLandName(int landID){ // Gibt den Namen des Landes zuruck mit der eingegebenen landID
        return getLand(landID).getName();
    }

    public String getLandSpielerName(int landID){ // Gibt den Namen des Spielers der das Ausgewaehlte Land besitzt
        String spielerName;
        Land land = getLand(landID);
        spielerName = getSpielerByID(land.getEingenommenVon()).getSpielerName();
        return spielerName;
    }

    public int getLandArmee(int landID) { // Gibt die Anzahl der Armee des Landes zuruck 
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

    public String[] getAngreiffeBereiteLaender(){ // Gibt eine liste von den eigenen Laender, die genug Armee fur einen angriff besitzen
        String[] bereiteLaender;
        spieler = getJetzigerSpieler();
        List<Land> laender = weltVerwaltung.getSpielerLaender(spieler); // wird zur ne string ausgabe geaendert   
        bereiteLaender = adj.getAlleAngreifebereiteLaender(laender, spieler);
        if(bereiteLaender != null){
            return bereiteLaender;
        }

        throw new IllegalArgumentException("Du hast keine Angriffbereite Laender");
    }
    
    public void waehleVerschiebeZiel(int vonLand){ // Waehlt das Ziel fuer eine Verschiebung
        spieler = getJetzigerSpieler();
        adj.getAlleEigeneNachbars(vonLand, spieler);
    }
    
    public int getLandByColour(String colour) { // Gibt das Land zuruck mit der eingegebenen Farbe
        Land land = weltVerwaltung.getLandByColour(colour);
        return land.getTrueIndex();
    }
    
    public boolean sindNachbar(int vonLand, int nachLand){ // Gibt true zurueck, wenn vonLand und nachLand Nachbar sind
        if(adj.sindNachbar(vonLand-1,nachLand-1)){
            return true;
        }else{
            return false;
        }
    }
    /*      AUSWERTUNGS METHODEN        */
    public int getMaxAttackNumber(int vonLand) { // Gibt die höchste Anzahl der Armee, die ein Land besit
        int landArmee = getLand(vonLand).getArmee();
        int maxAttack = spielLogik.getMaxAttackNumber(landArmee);
        return maxAttack;
    }

    public boolean landHatKeineArmee(int nachLand) { // Gibt true zurueck, wenn ein Land keine Armee besitzt
        Land land = getLand(nachLand);
        if(land.getArmee() <= 0){
            return true;
        }else{
            return false;
        }
    }

    public String[] getAlleEigeneNachbars(int vonLand) { // Gibt eine Liste von den eigenen nachbar laender zuruck. 
        spieler = getJetzigerSpieler();
        String[] liste = adj.getAlleEigeneNachbars(vonLand - 1, spieler);
        return liste;
    }

    public int[] getAlleEigeneNachbarsIntArray(int vonLand) { // Gibt ein Array von den eigenen nachbar laender zuruck. 
        int[] gegner = new int[42];
        spieler = getJetzigerSpieler();
        List<Land> laender = adj.getAlleEigeneNachbarsListe(vonLand-1, spieler);
        for(int i = 0; i < laender.size(); i++){
            gegner[i] = laender.get(i).getTrueIndex();
        }
        return gegner;
    }

    public int berechneZusatzarmeen(Spieler spieler) { // Berechnet die Zusatzarmeen, die ein Spieler erhält
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

    public void addBonusArmee(){  // Fügt Zusatzarmee zu einem Spieler hinzu
        spieler = getJetzigerSpieler();
        int zusatzArmee = berechneZusatzarmeen(spieler);
        spieler.addZusatzarmee(zusatzArmee);
    }

    public void neuerBesitzerSetzen(int vonLand){ // Setzt den neuen Besitzer eines Landes
            spieler = getJetzigerSpieler();
            weltVerwaltung.setNeueBesitzer(vonLand, spieler);
        }
    
    /*      INFO AUSGABE                */
    public String missionStatus(){ // Gibt den Status der Mission zurueck
        if(checkIfMissionErfuelt()){
            return "Spieler " + getJetzigerSpielerName() + " Du Hast deine Mission Eruellt, Kongratulations!!";
        } else{
            return "Spieler " + getJetzigerSpielerName() + ", deine Mission ist noch nicht erfuellt.";
        }
    }
    
    /*      EXCEPTIONS  EINGABEN        */
    public int readInt(Scanner scanner,int min,int max){ // Liest eine Zahl vom Benutzer ein und prueft ob sie zwischen min und
        return e.readInt(scanner, min, max);
    }

    public int readJetzigerPlayerLandIndex(Scanner scanner){ // Liest den Index eines eigenen Landes vom Benutzer ein und prueft ob es
        spieler = getJetzigerSpieler();
        List<Land> validIndices = weltVerwaltung.getSpielerLaender(spieler);
        return e.readLandIndex(scanner, validIndices);
    }

    public int readGegnerLandIndex(Scanner scanner, int vonLand){ // Liest den Index eines gegnerischen Landes vom Benutzer ein und prueft
        spieler = getJetzigerSpieler();
        List<Land> validIndices = adj.getAlleGegnerNachbarListe(vonLand - 1, spieler);
        return e.readLandIndex(scanner, validIndices);
    }

    public boolean checkIfMissionErfuelt(){ // Prueft ob die Mission erfuellt ist
        spieler = getJetzigerSpieler();
        boolean ergebniss = mission.checkIfMissionErfuelt(weltVerwaltung.getSpielerLaender(spieler), spieler.getMissionId());
        return ergebniss;
    }

    public List<Land> getEigeneLaender() { // Gibt eine Liste der eigenen Landes zurueck
        spieler = getJetzigerSpieler();
        List<Land> laender = weltVerwaltung.getSpielerLaender(spieler);
        return laender;
    }

    public int[] getEigeneLaenderId(){ // Gibt ein Array der eigenen Landes zurueck
        spieler = getJetzigerSpieler();
        return weltVerwaltung.getSpielerLaenderId(spieler);
    }

    public int getSpielerLaenderAnzahl() { // Gibt die Anzahl der eigenen Landes zurueck
        spieler = getJetzigerSpieler();
        int anzahl = weltVerwaltung.getSpielerLaender(spieler).size();
        return anzahl;
    }

    public boolean istDeinLand(int ausgewaehltesLand) { // Prueft ob das ausgewaehlte Land ein eigenes Land ist
        spieler = getJetzigerSpieler();
        Land Land = getLand(ausgewaehltesLand);
        if(Land.getEingenommenVon() == spieler.getSpielerID()){
            return true;
        }else{
            return false;
        }
    }

     // SAVE LOAD COMMANDS
     public void load(Risiko risiko) { // Lädt das Spiel
        slm.loadGame(risiko);
    }

    public void save(Risiko risiko){ // Speichert das Spiel
        slm.saveGame(risiko);
    }

    public void printSavedGame(){ // anzeigen was gespeichert wurde
        slm.printSavedGame();
    }


    //      LOAD METHODEN

    public void loadLaender(List<Land> loadedLaender){ // gespeicherte Laender laden
        weltVerwaltung.loadLaender(loadedLaender);
    }

    public void loadSpielerListe(List<Spieler> loadedSpieler){ // gespeicherte SpielerListe laden
        spielerVerwaltung.loadSpieler(loadedSpieler);
    }

    public void loadTurn(int loadedTurn) { // gespeicherte Turn laden
        turn.loadTurn(loadedTurn);
    }

    public void loadPhase(Phase loadedPhase){ // gespeicherte phase laden
        turn.loadPhase(loadedPhase);
    }

    public void loadJetzigerSpieler(int spielerId) { // gespeicherten Jetzigen Spieler laden
        turn.loadSpieler(spielerId);
    }

    public void laodInitializeTurn(int spielerListe) { // gespeicherte 
        turn = new Turn(spielerListe);
    }

    public int[] getAlleGegnerLaenderIntArray() { // Gibt ein Array der gegnerischen Landes zurueck
        spieler = getJetzigerSpieler();
        List<Land> laender = weltVerwaltung.getAngriffsmoeglichelaender(spieler, getAnzahlSpieler());
        int[] gegner = new int[42];
        for(int i = 0; i <laender.size(); i++){
            gegner[i] = laender.get(i).getTrueIndex();
        }
        return gegner;
    }

    public int[] getAlleGegnerNachbarsIntArray(int ausgewaehltesLand) { // Gibt ein Array der gegnerischen Nachbars zurueck
        int[] gegner = new int[42];
        spieler = getJetzigerSpieler();
        List<Land> laender = adj.getAlleGegnerNachbarListe(ausgewaehltesLand-1, spieler);
        for(int i = 0; i < laender.size(); i++){
            gegner[i] = laender.get(i).getTrueIndex();
        }
        return gegner;
    }


}
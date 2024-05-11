package risiko.local.domain;

import java.util.ArrayList;
import java.util.List;

import risiko.local.entities.AdjazenzMatrix;
import risiko.local.entities.Land;
import risiko.local.entities.Spieler;
//import risiko.local.persistance.SaveLoadManager;
import risiko.local.entities.Turn;
import risiko.local.entities.Turn.Phase;

public class Risiko {
    private WeltVerwaltung weltVerwaltung;
    private SpielerVerwaltung spielerVerwaltung;
    private SpielLogik spielLogik;
    private Turn turn;
    private Spieler spieler;
    private AdjazenzMatrix adj;
  //  private SaveLoadManager saveloadmanager;

    public Risiko(){
        weltVerwaltung = new WeltVerwaltung();
        spielerVerwaltung = new SpielerVerwaltung();
        spielLogik = new SpielLogik(weltVerwaltung);
        adj = new AdjazenzMatrix(weltVerwaltung);
        //Entweder hier muss das gemacht werden oer in Start game methode. 
    //    SaveLoadGameManager = new SaveLoadGameManager();
        
    }

    public void startGame(Risiko risiko){
        List<Spieler> spielerListe = getSpielerListe();
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
        return new ArrayList<>(spielerVerwaltung.getSpielerListe());
    }
    
    public int getTurn(){
        return turn.getTurn();
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
 
    /* 
    public void loadGame() {
        SaveLoadManager.loadGame();
    }
    */

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

    public void angreifen(int vonLandID, int nachLandID, int angreifeAnzahl){
        Land nachLand = weltVerwaltung.getLand(nachLandID);
        Land vonLand = weltVerwaltung.getLand(vonLandID);
        spielLogik.angreifen(vonLand, nachLand, angreifeAnzahl); // Exceptions noetig
    } 

    public void verschieben(int vonLandID, int nachLandID, int anzahl){
        Land nachLand = weltVerwaltung.getLand(nachLandID);
        Land vonLand = weltVerwaltung.getLand(vonLandID);

        spielLogik.verschieben(vonLand, nachLand, anzahl);

        //String erfolg ausgabe
        //adjazenz pruefen
        //verschiebe azahl pruefen und dann verschieben
    }

    

    

    public void getAngriffGegnerLaender(int vonLand){ // Gibt eine liste von die gegnerische nachbar laender zuruck.
        spieler = getJetzigerSpieler();
        adj.getAlleGegnerNachbar(vonLand, spieler);
    }

    public void getVerschiebebereiteLaender(){ // Gibt eine liste von den eigenen Laender, die genug Armee fur eine verschiebung besitzen
        spieler = getJetzigerSpieler();
        List<Land> laender = weltVerwaltung.getSpielerAngriffsbereiteLaender(spieler); // wird zur ne string ausgabe geaendert 
    }

    public void waehleVerschiebeZiel(int vonLand){ //  hmm
        spieler = getJetzigerSpieler();
        adj.getAlleEigeneNachbars(vonLand, spieler);
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

    public String getJetzigerSpielerMission(){
        return null;
    }

    public void getJetzigerSpielerLaenderListe(){  // Gibt die Laender des Jetzigen spielers zurcuk
        spieler = getJetzigerSpieler();
        List<Land> laender = weltVerwaltung.getSpielerLaender(spieler); // wird zur ne string ausgabe geaendert 
        //code zum anzeigen der laender, wahrscheinlich ein String return??

    }

    public void getJetzigerSpielerAngriffBereiteLaender(){ // Gibt die Angriffsbereite laender die zu den Jetzigen spieler gehoren
        spieler = getJetzigerSpieler();
        List<Land> laender = weltVerwaltung.getSpielerAngriffsbereiteLaender(spieler); // wird zur ne string ausgabe geaendert 

    }
    
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
    public Land getLand(int nachLand) {
        Land land = weltVerwaltung.getLand(nachLand);
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









    
    /*      AUSWERTUNGS METHODEN        */

    public int getMaxAttackNumber(int vonLand) {
        int landArmee = getLand(vonLand).getArmee();
        int maxAttack = spielLogik.getMaxAttackNumber(landArmee);
        return maxAttack;
    }


    

    public String getAngriffResult() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getAngriffResult'");
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

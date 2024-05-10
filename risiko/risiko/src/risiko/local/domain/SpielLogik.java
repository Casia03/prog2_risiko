package risiko.local.domain;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import risiko.local.entities.Land;
import risiko.local.entities.Spieler;
import risiko.local.entities.AdjazenzMatrix;
public class SpielLogik {
    private AdjazenzMatrix adj;
    
    public SpielLogik(WeltVerwaltung wv){
        AdjazenzMatrix adj = new AdjazenzMatrix(wv);

    }
    
    public void verteilen(Spieler spieler, Land land, int anzahl){
        int armee = land.getArmee();
        int zusatzArmee = spieler.getZusatzArmee();
        
        land.setArmee(armee + anzahl); // Armee hinzufuegen auf das Land, da wir set benutzen, nehmen wir die alte zahl und daddieren die neue

        if(zusatzArmee < anzahl){ // mit exceptions ersetzen
            //verteilt mehr als er hat, zuruckschieken den idiot 
        }else{ // verteilte zusatzarmee substrahieren
            spieler.setZusatzArmee(zusatzArmee - anzahl);
        }
    }
    public void angreifen(Land vonLand, Land nachLand, int attackArmeeNumber, int defendArmeeNumber){ 
        if (angriffMoeglich(vonLand, nachLand) || adj.sindNachbar(vonLand.getTrueIndex(), nachLand.getTrueIndex())){ // prüfft 
            if(!attackNumberStimmt(vonLand,attackArmeeNumber) && !defendNumberStimmt(nachLand,defendArmeeNumber)){
                return; //Exception, neue Auswahl von Angaben, also !!nicht!! return 
            }else{
                int[] wurfelResultat = wurfel(attackArmeeNumber, defendArmeeNumber);
                //wurfel Anzeigen
                //angriffAuswertung(wurfelResultat);
                Arrays.sort(wurfelResultat,0,2);
                Arrays.sort(wurfelResultat,3,4);

                int highestAttack = wurfelResultat[2];
                int secondHighestAttack = wurfelResultat[1];

                int highestDefence = wurfelResultat[4];
                int secondHighestDefence = wurfelResultat[3];

                if(highestAttack <= highestDefence){ //wenn attackWurfel kleiner Gleich defendWurfel, dann gewinnt defend 
                    // AttackArmee - 1
                    vonLand.setArmee(vonLand.getArmee() - 1);

                }else{ //gewinnt  Angreifer
                    // Defend Armee - 1
                    nachLand.setArmee(nachLand.getArmee() - 1);

                }
                if(secondHighestDefence != 0){
                    if(secondHighestAttack <= secondHighestDefence){ //gewinnt defender
                        // AttackArmee - 1
                        vonLand.setArmee(vonLand.getArmee() - 1);

                    }else{ //gewinnt  Angreifer
                    // Defend Armee - 1
                        nachLand.setArmee(nachLand.getArmee() - 1);
                    }
                }
                
            }
            
            
        }else{
            //angriff nicht moeglich exception
        }
    }

    public void einruecken(Land vonLand, Land nachLand, int armee){ //Funktion zum einruecken von Armeen nach ne erfolgreiche eroberung eines Landes,
        nachLand.setArmee(1); // Eine Einheit automatisch rueberziehen
        vonLand.setArmee(vonLand.getArmee() - 1);

        if (vonLand.getArmee() <= armee || armee >= 0){ // wenn man alle armeen einruecken moechte fehler, oder negative zahlen
           //fehlermeldung
        }else{
            nachLand.setArmee(armee + 1);  //armee einruecken
            vonLand.setArmee(vonLand.getArmee() - armee); // eingerueckte armee entfernen
        }
    }

    public void verschieben(Land vonLand, Land nachLand, int armee){
        if(!adj.sindNachbar(vonLand.getTrueIndex(), nachLand.getTrueIndex())){
            //exception nicht nachbar kanns nicht verschieben
        }
        if(vonLand.getArmee() - armee < 1 || armee <= 0){
            // verschiebung kann nicht durchgefuhrt werden, Armee anzahl falsch\zu gross
        }else{
            //verschiebe Armee
            vonLand.setArmee(vonLand.getArmee() - armee);
            nachLand.setArmee(nachLand.getArmee() + armee);
        }

    }

    public boolean landErobert(Spieler angreifer, Land nachLand) {
        if(nachLand.getArmee() == 0){
            nachLand.setEigenommen(angreifer.getSpielerID());
            return true;
        }else{
            return false;
        }
    }

    private boolean defendNumberStimmt(Land nachLand, int defendArmeeNumber) {
        int landArmee = nachLand.getArmee();
        if (landArmee < defendArmeeNumber && defendArmeeNumber > 2 || defendArmeeNumber < 1){ // wenn ausgewehlte armee groesser ist als existierende, und auserhalb der geregelte anzahl liegt, fehler
            return false; // Exception mit erklaerung des Fehlers
        }
        else{
            return true;
        }

    }

    private boolean attackNumberStimmt(Land vonLand, int attackArmeeNumber) {
        int landArmee = vonLand.getArmee();
        if (landArmee < attackArmeeNumber && attackArmeeNumber > 3 || attackArmeeNumber < 1){ // wenn ausgewehlte armee grosser ist als existierendee, und auserhalb der mit Regele festgestellten bereiches liegt, fehler
            return false; // Exception mit erklaerung des Fehlers
        }
        else{

            return true;
        }
    }

    private int[] wurfel(int attackArmeeNumber, int defendArmeeNumber){ //Methode zur erzeugubg von zufaeliger wurfelnergebnissen
        int[] wurfel = new int[4]; //Array mit 5 Zellen, Zellen index 0 bis 2, also die Erste Drei sind fur die Wurfeln des Angreifers, zellen 4 und 5, also index 3-4 sind fur den Wurfeln des Verteidigers
        for (int i = 0; i < wurfel.length; i++){ // Wurfel mit Nullen Fullen, damit spaeter kein Null pointer exception kommt
            wurfel[i] = 0;
        }
        switch (attackArmeeNumber){  //Math random zur generierung einer Random wurfel zahl von 1 buis 6 fur entweder 1 2 oder 3 wurfeln
            case 1:
                wurfel[0] = (int)(Math.random() * 5 + 1);

                break;
            case 2:
                wurfel[0] = (int)(Math.random() * 5 + 1);
                wurfel[1] = (int)(Math.random() * 5 + 1);
                break;
            case 3:
                wurfel[0] = (int)(Math.random() * 5 + 1);
                wurfel[1] = (int)(Math.random() * 5 + 1);
                wurfel[3] = (int)(Math.random() * 5 + 1);
                break;
        }

        switch (defendArmeeNumber){
            case 1:
                wurfel[4] = (int)(Math.random() * 5 + 1);
                break;
            case 2:
                wurfel[4] = (int)(Math.random() * 5 + 1);
                wurfel[5] = (int)(Math.random() * 5 + 1);
                break;
        }
           
            return wurfel;
    }

        

    private boolean angriffMoeglich(Land vonLand, Land nachLand){ //Methode zum pruefen ob der Angreifer sein eigenes Land angreiff
        int angreifer = vonLand.getEingenommenVon();
        int verteidiger = nachLand.getEingenommenVon();

        if(angreifer != verteidiger){
            if(vonLand.getArmee() >= 2){ //Arme vonLAnd reicht zum Angreifen
                return true;
            }
            else{
                return false; //throw nicht genur armee
            }    
        }else{
            return false; //throw eigenes land
        }  
    }
    private boolean istAngreifer(Land angreiferLand, Spieler spieler){
        boolean istAngreifer = false;
        if(angreiferLand.getEingenommenVon() == spieler.getSpielerID()){
            istAngreifer = true;
        }
        return istAngreifer;
    }
    private boolean istVerteidiger(Land verteidigerLand, Spieler spieler){
        boolean istVerteidiger = false;
        if(verteidigerLand.getEingenommenVon() == spieler.getSpielerID()){
            istVerteidiger = true;
        }
        return istVerteidiger;
    }

    public int choosenAttackArmeeNumber(int attackArmeeNumber){
        // Soll Eingabe von attackArmeeNumber erlauben, durch scanner fur CUI. 
        attackArmeeNumber = attackArmeeNumber;


        return attackArmeeNumber;
    }

    public List<Land> getSpielerLaender(Spieler spieler){
        List<Land> spielerLaender = new ArrayList<Land>();
        for( int i = 0; i < lander.size(); i++){
            if(getLand(i).getEingenommenVon() == spieler.getSpielerID()){
                spielerLaender.add(getLand(i));
            }
        }
        return spielerLaender;
    }
    public List<Land> getSpielerAngriffsbereiteLaender(Spieler spieler) {
        List<Land> spielerLaender = new ArrayList<Land>();
        for( int i = 0; i < lander.size(); i++){
            if(getLand(i).getEingenommenVon() == spieler.getSpielerID() && getLand(i).getArmee() > 1){
                spielerLaender.add(getLand(i));
            }
        }
        return spielerLaender;
    }
    public List<Land> getAngriffsmoeglichelaender(Spieler spieler, int vonLand) {
        List<Land> spielerLaender = new ArrayList<Land>();
        for( int i = 0; i < lander.size(); i++){
            if(getLand(i).getEingenommenVon() == spieler.getSpielerID() && getLand(i).getArmee() > 1){
                spielerLaender.add(getLand(i));
            }
        }
        return spielerLaender;
    }
}


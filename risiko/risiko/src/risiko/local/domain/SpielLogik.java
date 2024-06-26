package risiko.local.domain;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import risiko.local.entities.Land;
import risiko.local.entities.Spieler;
import risiko.local.persistance.Exceptions;
import risiko.local.entities.AdjazenzMatrix;

public class SpielLogik {

    private AdjazenzMatrix adj;

    public SpielLogik(WeltVerwaltung wv){
       adj = new AdjazenzMatrix(wv);
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
    
    public String angreifen(Land vonLand, Land nachLand, int attackArmeeNumber){  // ist für das anfreifen zuständig
        StringBuilder resultatBuilder = new StringBuilder();
        //String[] resultat = new String[2];
        int defendArmeeNumber = 1;
        if(nachLand.getArmee() >= 2){
            defendArmeeNumber = 2;
        }else if (nachLand.getArmee() == 1){
            defendArmeeNumber = 1;
        }
        //System.out.println(defendArmeeNumber + " VERTEIDIGER WURFEL ANZAHL");
        if (angriffMoeglich(vonLand, nachLand) || adj.sindNachbar(vonLand.getTrueIndex(), nachLand.getTrueIndex())){ // prüfft Kommt wahrscheinlich weg, hmm gucken wir mal, ich mein die "kann man das uberhaupt auswaehlen" bedingungen werden meistens durch die adj matrix, weltverwaltung und dann auch von risiko verabeitet. Ich denke es wird fur die GUI spaeter notig haha, also bleibt es erstmal hier, stort doch bestimmt niemanden, hoffentlich
            if(!attackNumberStimmt(vonLand,attackArmeeNumber) && !defendNumberStimmt(nachLand,defendArmeeNumber)){
                throw new UnsupportedOperationException("Unimplemented method "); //Exception, neue Auswahl von Angaben, also !!nicht!! return 
            }else{
                int[] wurfelResultat = wurfel(attackArmeeNumber, defendArmeeNumber);
                //wurfel Anzeigen
                //angriffAuswertung(wurfelResultat);
                Arrays.sort(wurfelResultat,0,3);
                Arrays.sort(wurfelResultat,3,5);

                //System.out.println("WURFEL RESULTAT: " + wurfelResultat[0] + " " + wurfelResultat[1] + " " + wurfelResultat[2] + " " + wurfelResultat[3] + " " + wurfelResultat[4] + " " );

                int highestAttack = wurfelResultat[2];
                int secondHighestAttack = wurfelResultat[1];

                int highestDefence = wurfelResultat[4];
                int secondHighestDefence = wurfelResultat[3];

                String attackerLostOne = "Angreifer verloren Eine Einheit\n";
                String defenderLostOne = "Verteidiger verloren Eine Einheit\n";
                if(highestAttack <= highestDefence){ //wenn attackWurfel kleiner Gleich defendWurfel, dann gewinnt defend 
                    // AttackArmee - 1
                    vonLand.setArmee(vonLand.getArmee() - 1);
                    resultatBuilder.append(attackerLostOne);
                    //System.out.println("ANGREIFER VELOREN 1 PRINT AUS SPILLOGIK + vonlandarmee : " + vonLand.getArmee() );

                }else{ //gewinnt  Angreifer
                    // Defend Armee - 1
                    nachLand.setArmee(nachLand.getArmee() - 1);
                    resultatBuilder.append(defenderLostOne);
                    //System.out.println("VERTEIDIGER VELOREN 1 PRINT AUS SPILLOGIK + vonlandarmee : " + nachLand.getArmee() );

                }
                if(secondHighestDefence != 0 && secondHighestAttack != 0){
                    if(secondHighestAttack <= secondHighestDefence){ //gewinnt defender
                        // AttackArmee - 1
                        vonLand.setArmee(vonLand.getArmee() - 1);
                        // HIER STRING FOR ATTACKER LOST ONE
                        resultatBuilder.append(attackerLostOne);
                        //resultat[1] = attackerLostOne;
                        //System.out.println("ANGREIFER VELOREN 2 PRINT AUS SPILLOGIK + vonlandarmee : " + vonLand.getArmee() );

                    }else{ //gewinnt  Angreifer
                    // Defend Armee - 1
                        nachLand.setArmee(nachLand.getArmee() - 1);
                        // HIER STRING FOR DEFENDER LOST ONE
                        //resultat[1] = defenderLostOne;
                        resultatBuilder.append(defenderLostOne);
                        //System.out.println("VERTEIDIGER VELOREN 2 PRINT AUS SPILLOGIK + vonlandarmee : " + nachLand.getArmee() );
                    }
                }
                
            }
            
        }else{
            //angriff nicht moeglich exception
        }
        // CONJUCTION OF BOTH STRINGS UP IN THE IF STATEMENTS INTO THE resultat String[]

        return resultatBuilder.toString();
    }

    public void einruecken(Land vonLand, Land nachLand){ //Funktion zum einruecken von Armeen nach ne erfolgreiche eroberung eines Landes,
        nachLand.setArmee(1); // Eine Einheit automatisch rueberziehen
        vonLand.setArmee(vonLand.getArmee() - 1);
    }

    public void verschieben(Land vonLand, Land nachLand, int armee){ // Funktion zum verschieben von Armeen
        if(!adj.sindNachbar(vonLand.getTrueIndex()-1, nachLand.getTrueIndex()-1)){
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

    public boolean landErobert(Spieler angreifer, Land nachLand) { //Funktion zum eroben eines Landes
        if(nachLand.getArmee() == 0){
            nachLand.setEigenommen(angreifer.getSpielerID());
            return true;
        }else{
            return false;
        }
    }

    private boolean defendNumberStimmt(Land nachLand, int defendArmeeNumber) { //Funktion zum pruefen ob ausgewaehlte Angriffsarmeenummer stimmt oder nicht
        int landArmee = nachLand.getArmee();
        if (landArmee < defendArmeeNumber && defendArmeeNumber > 2 || defendArmeeNumber < 1){ // wenn ausgewehlte armee groesser ist als existierende, und auserhalb der geregelte anzahl liegt, fehler
            return false; // Exception mit erklaerung des Fehlers
        }
        else{
            return true;
        }

    }

    private boolean attackNumberStimmt(Land vonLand, int attackArmeeNumber) { //Funktion zum pruefen ob ausgewaehlte Angriffsarmeenummer
        int landArmee = vonLand.getArmee();
        if (landArmee < attackArmeeNumber && attackArmeeNumber > 3 || attackArmeeNumber < 1){ // wenn ausgewehlte armee grosser ist als existierendee, und auserhalb der mit Regele festgestellten bereiches liegt, fehler
            return false; // Exception mit erklaerung des Fehlers
        }
        else{

            return true;
        }
    }

    private int[] wurfel(int attackArmeeNumber, int defendArmeeNumber){ //Methode zur erzeugubg von zufaeliger wurfelnergebnissen
        
        int[] wurfel = new int[5]; //Array mit 5 Zellen, Zellen index 0 bis 2, also die Erste Drei sind fur die Wurfeln des Angreifers, zellen 4 und 5, also index 3-4 sind fur den Wurfeln des Verteidigers
        
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
                wurfel[2] = (int)(Math.random() * 5 + 1);
                break;
        }

        switch (defendArmeeNumber){
            case 1:
                wurfel[3] = (int)(Math.random() * 5 + 1);
                break;
            case 2:
                wurfel[3] = (int)(Math.random() * 5 + 1);
                wurfel[4] = (int)(Math.random() * 5 + 1);
                break;
        }
           
            return wurfel;
    }  

    private boolean angriffMoeglich(Land vonLand, Land nachLand){ //Methode zum pruefen ob der Angreifer sein eigenes Land angreiff moeglich
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
    
    private boolean istAngreifer(Land angreiferLand, Spieler spieler){ //schaut nach ober der spieler der angreifer ist
        boolean istAngreifer = false;
        if(angreiferLand.getEingenommenVon() == spieler.getSpielerID()){
            istAngreifer = true;
        }
        return istAngreifer;
    }
    
    private boolean istVerteidiger(Land verteidigerLand, Spieler spieler){//schaut nach ober der spieler der verteidiger ist
        boolean istVerteidiger = false;
        if(verteidigerLand.getEingenommenVon() == spieler.getSpielerID()){
            istVerteidiger = true;
        }
        return istVerteidiger;
    }

    public int getMaxAttackNumber(int armeeAnzahl) { //Methode zur Berechnung der maximalen Angriffsarmeenummer
        if(armeeAnzahl >= 4){
            return 3;
        }else if (armeeAnzahl == 3){
            return 2;
        }else if (armeeAnzahl == 2 ){
            return 1;
        }else{
            return 0;
        }
    }

}


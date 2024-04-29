package risiko.local.domain;
import java.util.Random;
import risiko.local.entities.Land;
import risiko.local.entities.Spieler;

public class SpielLogik {
    public SpielLogik(){

    }
    
    public void attack(Land vonLand, Land nachLand, int attackArmeeNumber, int defendArmeeNumber){ 
        if (angriffMoeglich(vonLand, nachLand)){ // prüfft 
            if(!attackNumberStimmt(vonLand,attackArmeeNumber) && !defendNumberStimmt(nachLand,defendArmeeNumber)){
                return; //Exception, neue Auswahl von Angaben, also !!nicht!! return 
            }else{
                int[] wurfelResultat = wurfel(attackArmeeNumber, defendArmeeNumber);
                //wurfel Anzeigen
                //angriffAuswertung(wurfelResultat);


                
            }
            
            
        }else{
            //angriff nicht moeglich exception
        }
    }

    private void angriffAuswertung(int[] wurfelResultat) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'wurfelAuswertung'");
    }

    private boolean defendNumberStimmt(Land nachLand, int defendArmeeNumber) {
        int landArmee = nachLand.getArmee();
        if (landArmee < defendArmeeNumber && defendArmeeNumber > 2 || defendArmeeNumber < 1){
            return false; // Exception mit erklaerung des Fehlers
        }
        else{
            return true;
        }

    }

    private boolean attackNumberStimmt(Land vonLand, int attackArmeeNumber) {
        int landArmee = vonLand.getArmee();
        if (landArmee < attackArmeeNumber && attackArmeeNumber > 3 || attackArmeeNumber < 1){
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
}


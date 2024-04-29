package risiko.local.domain;

import risiko.local.entities.Land;
import risiko.local.entities.Spieler;

public class SpielLogik {
    public SpielLogik(){

    }
    
    public void attack(Land vonLand, Land nachLand, int attackArmeeNumber, int defendArmeeNumber){ 
        if (angriffMoeglich(vonLand, nachLand)){
            int attackArmee;
            int defendArmee; 

            int[] resultat = wurfel(attackArmee, defendArmee);
            

            if(resultat.length != 0){

            }
        }else{
            //angriff nicht moeglich exception
        }
    }

    private int[] wurfel(int attackArmeeNumber, int defendArmeeNumber){ //Methode zur erzeugubg von zufaeliger wurfelnergebnissen
        int[] wurfel= new int[5]; // Wuerfel wird ein Array sein der aus 5 Zellen bestehen wird, die erste Drei fur den Angreifer und die letztee Zwei fur den Verteidiger

        switch (attackArmeeNumber){
            case 1:
                break;
            case 2:
                break;

        }
            // Auswahl von attack und defend armee anzahl, also mit wieviele mochtest du angreifen / verteidigen

            int a = 0;
           

            return wurfel;
    }

        

    private boolean angriffMoeglich(Land vonLand, Land nachLand){ //Methode zum pruefen ob der Angreifer sein eigenes Land angreift
        boolean angriffmoeglich = false;
        int angreifer = vonLand.getEingenommenVon();
        int verteidiger = nachLand.getEingenommenVon();

        if(angreifer != verteidiger){
            if(vonLand.getArmee() >= 2){
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

    public int choosenAttackArmeeNumber(){
        int attackArmeeNumber = 0;


        return attackArmeeNumber;
    }
}

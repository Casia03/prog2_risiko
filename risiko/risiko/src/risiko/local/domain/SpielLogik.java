package risiko.local.domain;

import risiko.local.entities.Land;
import risiko.local.entities.Spieler;

public class SpielLogik {
    public SpielLogik(){

    }
    
    public void attack(Land vonLand, Land nachLand, int[] wurfel){ 
        

        int[] resultat = wurfel(vonLand,nachLand);

        if(resultat.length != 0){

        }

    }

    private int[] wurfel(Land vonLand, Land nachLand){ //Methode zur erzeugubg von zufaeliger wurfelnergebnissen
        if (angriffMoeglich(vonLand, nachLand)){
            
            int[] wurfel= new int[5]; // Wuerfel wird ein Array sein der aus 5 Zellen bestehen wird, die erste Drei fur den Angreifer und die letztee Zwei fur den Verteidiger
            int maxAttackArmee = vonLand.getArmee(); // wieviele Armee stehen fur den Angriff zur verfuegung
            int maxDefendArmee = nachLand.getArmee(); // wieviele Armee steht fur die Verteidigung zur verfuegung

            //oder 

            //int attackWuerfel[x]; // wobei x und y die groesse des wurfel arrays darstellen wird, 
            //int defenceWuerfel[y];

            if(maxAttackArmee <= 1){
            //kann nicht von hier angreifen exception, weil zu wenig Armee
                return wurfel;   
            }

            if(maxAttackArmee >= 4){         //Angreifen mit 3 Armeen, 3 wurfel

            }else if (maxAttackArmee >= 3){  //Angreifen mit 2 Armeen, 2 wurfel

            }else if (maxAttackArmee == 2){  //Angreifen mit 1 Armee, 1 wurfel

            }

            if(maxDefendArmee >=2){

            }else{

            }
        
        }else{
            //kann nicht sein eigenes land angreifen exception
        }

        return wurfel;
    }

    private boolean angriffMoeglich(Land vonLand, Land nachLand){ //Methode zum pruefen ob der Angreifer sein eigenes Land angreift
        boolean angriffmoeglich = false;
        int angreifer = vonLand.getEingenommenVon();
        int verteidiger = nachLand.getEingenommenVon();

        if(angreifer != verteidiger){
            return true;
        }else{
            return false;
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
}

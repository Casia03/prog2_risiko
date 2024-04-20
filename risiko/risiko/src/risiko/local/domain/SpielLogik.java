package risiko.local.domain;

import risiko.local.entities.Land;

public class SpielLogik {
    public SpielLogik(){

    }
    
    public void attack(Land vonLand, Land nachLand, int[] wurfel){ 
        

        int[] resultat = wurfel(vonLand,nachLand);

    }

    private int[] wurfel(Land vonLand, Land nachLand){
        int[] wurfel= new int[5]; // Wuerfel wird ein Array sein der aus 5 Zellen bestehen wird, die erste Drei fur den Angreifer und die letztee Zwei fur den Verteidiger
        int maxAttackArmee = vonLand.getArmee(); // wieviele Armee stehen fur den Angriff zur verfuegung
        int maxDefendArmee = nachLand.getArmee(); // wieviele Armee steht fur die Verteidigung zur verfuegung

        //oder 

        //int attackWuerfel[x]; // wobei x und y die groesse des wurfel arrays darstellen wird, 
        //int defenceWuerfel[y];

        if(maxAttackArmee <= 1){
           //kann nicht von hier angreifen exception, weil zu wenig Armee
        }

        

        return wurfel;
    }
}

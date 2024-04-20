package risiko.local.domain;

import risiko.local.entities.Land;

public class SpielLogik {
    public SpielLogik(){

    }
    
    public void attack(Land vonLand, Land nachLand){ 
        
    }

    private int[] wurfel(Land vonLand, Land nachLand){
        int wurfel[];
        int maxAttackArmee = vonLand.getArmee(); // wieviele Armee stehen fur den Angriff zur verfuegung
        int maxDefendArmee = nachLand.getArmee(); // wieviele Armee steht fur die Verteidigung zur verfuegung

        if(maxAttackArmee <= 1){
           //kann nicht von heir angreifen exception
        }

        return wurfel;
    }
}

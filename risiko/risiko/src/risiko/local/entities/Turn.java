package risiko.local.entities;

import java.util.List;

public class Turn {

    // public enum Phase {
    //     NEUE_EINHEITEN_VERTEILEN,
    //     ANGRIFF,
    //     VERTEIDIGUNG,
    //     AUSWERTUNG_VON_KAEMPFEN,
    //     EINRUECKEN,
    //     VERSCHIEBEN_VON_EINHEITEN
    // }

    // Verteilen nach dem erhalt der Zusatzarmeen, 
    // Angreifen je nach wahl des Spielers,
    // Verschieben der auf dem Brett schon plazierter Armeen
    public enum Phase {
        ERSTVERTEILEN,
        VERTEILEN,
        ANGREIFFEN,
        VERSCHIEBEN
    }

    private Phase phase;
    private int turnNumber; // Track the current turn
    private List<Spieler> spielerListe;
    private int spielerId;

    public Turn(List<Spieler> spielerListe) {
        this.spielerListe = spielerListe;
        phase = Phase.VERTEILEN;
        turnNumber = 0; // Initialize the turn number to 0
        spielerId = 0; // Initialize the player index to 0
    }
    
    public Phase getPhase() {
        return phase;
    }

    public void setPlayerIndex(int newspielerId){
        spielerId = newspielerId;
    }


    public int getSpieler() {
        return spielerId;
    }

    public void nextPlayer() {
        spielerId++;

        if(spielerId >= spielerListe.size()){
            spielerId = 0;
        }
        
    }

    public int getTurn(){
        return turnNumber;
    }
    public void nextPhase() {
        switch (phase) {
            case ERSTVERTEILEN:
                phase = Phase.ANGREIFFEN;
                break;
            case VERTEILEN:
                phase = Phase.ANGREIFFEN;
                nextPlayer();
                break;
            case ANGREIFFEN:
                phase = Phase.VERSCHIEBEN;
            case VERSCHIEBEN:
                phase = Phase.VERTEILEN;
                nextPlayer(); // Move to the next player for the next turn
                // Check if we have completed a full cycle of players and increment the turn number
                if (spielerId == 0) {
                    turnNumber++;
                }
                break;
        }
    }

    public int getTurnNumber() {
        return turnNumber;
    }
}
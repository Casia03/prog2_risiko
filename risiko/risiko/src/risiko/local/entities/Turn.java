package risiko.local.entities;

import java.util.List;

public class Turn {

    public enum Phase {
        NEUE_EINHEITEN_VERTEILEN,
        ANGRIFF,
        VERTEIDIGUNG,
        AUSWERTUNG_VON_KAEMPFEN,
        EINRUECKEN,
        VERSCHIEBEN_VON_EINHEITEN
    }
    private Phase phase;
    private int turnNumber; // Track the current turn
    private List<Spieler> spielerListe;
    private int spielerIndex;

    public Turn(List<Spieler> spielerListe) {
        this.spielerListe = spielerListe;
        phase = Phase.NEUE_EINHEITEN_VERTEILEN;
        turnNumber = 0; // Initialize the turn number to 0
        spielerIndex = 0; // Initialize the player index to 0
    }
    
    public Phase getPhase() {
        return phase;
    }

    public void setPlayerIndex(int newSpielerIndex){
        spielerIndex = newSpielerIndex;
    }

    public int getPlayerIndex(){
        return spielerIndex;
    }

    public int getSpieler() {
        return spielerIndex;
    }

    public void nextPlayer() {
        spielerIndex++;

        if(spielerIndex >= spielerListe.size()){
            spielerIndex = 0;
        }
        
    }

    public int getTurn(){
        return turnNumber;
    }
    public void nextPhase() {
        switch (phase) {
            case NEUE_EINHEITEN_VERTEILEN:
                phase = Phase.ANGRIFF;
                nextPlayer();
                break;
            case ANGRIFF:
                phase = Phase.VERTEIDIGUNG;
                break;
            case VERTEIDIGUNG:
                phase = Phase.AUSWERTUNG_VON_KAEMPFEN;
                break;
            case AUSWERTUNG_VON_KAEMPFEN:
                phase = Phase.EINRUECKEN;
                break;
            case EINRUECKEN:
                phase = Phase.VERSCHIEBEN_VON_EINHEITEN;
                break;
            case VERSCHIEBEN_VON_EINHEITEN:
                phase = Phase.NEUE_EINHEITEN_VERTEILEN;
                nextPlayer(); // Move to the next player for the next turn

                // Check if we have completed a full cycle of players and increment the turn number
                if (spielerIndex == 0) {
                    turnNumber++;
                }
                break;
        }
    }

    public int getTurnNumber() {
        return turnNumber;
    }

    public Spieler getPlayer() {
        return spielerListe.get(spielerIndex);
    }
}
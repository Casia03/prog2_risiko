package risiko.local.entities;

public class Turn {

    // Verteilen nach dem erhalt der Zusatzarmeen, 
    // Angreifen je nach wahl des Spielers,
    // Verschieben der auf dem Brett schon plazierter Armeen
    public enum Phase { //setzt die Phasen
        ERSTVERTEILEN,
        VERTEILEN,
        ANGREIFFEN,
        VERSCHIEBEN;

        public String toString(){
            switch(this){
                case ERSTVERTEILEN:
                    return "Erstverteilen";
                case VERTEILEN:
                    return "Verteilen";
                case ANGREIFFEN:
                    return "Angreifen";
                case VERSCHIEBEN:
                    return "Verschieben";
            }
            return null;
        }
    }

    private Phase phase;
    private int turnNumber; // Track the current turn
    private int anzahlSpieler;
    private int spielerId;

    public Turn(int anzahlSpieler) { // inizializiert den turn mit der Anzahl der Spieler
        this.anzahlSpieler = anzahlSpieler;
        phase = Phase.ERSTVERTEILEN;
        turnNumber = 0; // Initialize the turn number to 0
        spielerId = 0; // Initialize the player index to 0
    }
    
    public Phase getPhase() { // gibt die aktuelle Phase zurueck
        return phase;
    }

    public void loadTurn(int a) { // Laden eines gespeicherten Turns
        turnNumber = a;
    }

    public void setPlayerIndex(int newspielerId){ // Setzt den Index des aktuellen Spielers
        spielerId = newspielerId;
    }

    public int getSpieler() { // gibt den Index des aktuellen Spielers zurueck
        return spielerId;
    }

    public void nextPlayer() { // Setzt den Index des naechsten Spielers
        spielerId++;
        spielerId = spielerId % anzahlSpieler;
        
    }

    public int getTurn(){ // gibt den aktuellen Turnnummer zurueck
        return turnNumber;
    }

    public void nextPhase() { // Wechselt zur naechsten Phase
        switch (phase) {
            case ERSTVERTEILEN:
                phase = Phase.ANGREIFFEN;
                break;
            case VERTEILEN:
                phase = Phase.ANGREIFFEN;
                break;
            case ANGREIFFEN:
                phase = Phase.VERSCHIEBEN;
                break;
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


    @Override
    public String toString() { // gibt die aktuelle Phase und den Index des aktuellen Spielers zurueck
        return super.toString();
    }

    public void loadPhase(Phase loadedPhase) { // Laden einer gespeicherten Phase
        switch (loadedPhase) {
            case ERSTVERTEILEN:
                phase = Phase.ERSTVERTEILEN;
                break;
            case VERTEILEN:
                phase = Phase.VERTEILEN;
                break;
            case ANGREIFFEN:
                phase = Phase.ANGREIFFEN;
                break;
            case VERSCHIEBEN:
                phase = Phase.VERSCHIEBEN;
        }
    }

    public void loadSpieler(int loadedSpieler){ // Laden des Index des aktuellen Spielers
        spielerId = loadedSpieler;
    }
}
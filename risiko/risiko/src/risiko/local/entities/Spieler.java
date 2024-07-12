
package risiko.local.entities;

import java.io.Serializable;

public class Spieler implements Serializable {
    private int spielerId;
    private String spielerName;
	private int zusatzArmee;
    private int missionId;
    private int[] einheitsKarten = new int[3];

    public Spieler(String spielerName, int spielerId, int zusatzArmee, int missionId, int[] einheitsKarten){
        this.spielerName = spielerName;
		this.spielerId = spielerId;
		this.zusatzArmee = zusatzArmee;
        this.missionId = missionId;
        this.einheitsKarten = einheitsKarten;
    }
    
    public void setSpielerID(int spielerId){ // SpielerID setzen
        this.spielerId = spielerId;
    }
    public int getSpielerID(){ // SpielerID abfragen
        return spielerId;
    }

    public void addEinheitskarte(int karte){ // Einheitenkarte hinzufuegen
        if (karte == 1){ // Infanterie karte addieren
            einheitsKarten[0] += 9;
        }
        else if (karte == 2){ // Kavallerie karte addieren
            einheitsKarten[1] += 9;
        }
        else if (karte == 3){ // Artillerie karte addieren
            einheitsKarten[2] += 9;
        }
    }

    public int[] getEinheitskarten (){
        return einheitsKarten;
    }
    
    public boolean darfTauschen(int[] einheitsKarten) {
        // Prüfen, ob mindestens drei Karten von irgendeinem Typ vorhanden sind
        boolean mindestensDreiVonEinemTyp = false;
        for (int karte : einheitsKarten) {
            if (karte >= 3) {
                mindestensDreiVonEinemTyp = true;
                break;
            }
        }
    
        // Prüfen, ob genau drei unterschiedliche Kartentypen vorhanden sind
        boolean dreiUnterschiedlicheTypen = true;
        for (int karte : einheitsKarten) {
            if (karte == 0) {
                dreiUnterschiedlicheTypen = false;
                break;
            }
        }
    
        return mindestensDreiVonEinemTyp || dreiUnterschiedlicheTypen;
    }

    public void setSpielerName(String spielerName){ // Spielername setzen
        this.spielerName = spielerName;
    }
    public String getSpielerName(){ // Spielername abfragen
        return spielerName;
    }
    
    public void setZusatzArmee(int zusatzArmee){ // Zusatzarmee setzen
        this.zusatzArmee = zusatzArmee;
    }
    public int getZusatzArmee(){ // Zusatzarmee abfragen
        return zusatzArmee;
    }

    public void setMissionId(int missionId){ // MissionId setzen
        this.missionId = missionId;
    }
    public int getMissionId(){ // MissionId abfragen
        return missionId;
    }
    
    public void addZusatzarmee(int zusatzArmee){ // Zusatzarmee hinzufuegen
        this.zusatzArmee += zusatzArmee;
    }

    @Override
    public String toString(){ // toString Methode
        return 
            "Spieler-Name= " + spielerName + '\'';
    }
}

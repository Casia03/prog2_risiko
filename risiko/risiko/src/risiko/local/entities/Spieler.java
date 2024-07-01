
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
            einheitsKarten[0] += 1;
        }
        else if (karte == 2){ // Kavallerie karte addieren
            einheitsKarten[1] += 1;
        }
        else if (karte == 3){ // Artillerie karte addieren
            einheitsKarten[2] += 1;
        }
    }

    public int[] getEinheitskarten (){
        return einheitsKarten;
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

package risiko.local.entities;

import risiko.local.entities.Mission.MissionType;

public class Spieler {
    private int spielerId;
    private String spielerName;
	private int zusatzArmee;
    private int missionId;
    private MissionType mission;

    public Spieler(String spielerName, int spielerId, int zusatzArmee, int missionId){
        this.spielerName = spielerName;
		this.spielerId = spielerId;
		this.zusatzArmee = zusatzArmee;
        this.missionId = missionId;
    }
    
    public void setSpielerID(int spielerId){
        this.spielerId = spielerId;
    }
    public int getSpielerID(){
        return spielerId;
    }
    
    public void setSpielerName(String spielerName){
        this.spielerName = spielerName;
    }
    public String getSpielerName(){
        return spielerName;
    }
    
    public void setZusatzArmee(int zusatzArmee){
        this.zusatzArmee = zusatzArmee;
    }
    public int getZusatzArmee(){
        return zusatzArmee;
    }

    public void setMissionId(int missionId){
        this.missionId = missionId;
    }
    public int getMissionId(){
        return missionId;
    }
    
    public MissionType getMission() {
		return mission;
	}

	public void setMission(MissionType missionTyp) {
		this.mission = missionTyp;
	}

    @Override
    public String toString(){
        return 
            "Spieler-Name= " + spielerName + '\'';
    }
}

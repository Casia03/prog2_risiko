package risiko.local.entities;

public class Spieler {
    private int spielerId;
    private String spielerName;
	private int zusatzArmee;
    private int missionId;

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
        return spielerId;
    }

    public void setMissionId(int missionId){
        this.missionId = missionId;
    }
    public int getMissionId(){
        return missionId;
    }
    
}

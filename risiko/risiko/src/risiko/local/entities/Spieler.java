package risiko.local.entities;

public class Spieler {
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
    
    public void addZusatzarmee(int zusatzArmee){
        this.zusatzArmee += zusatzArmee;
    }

    @Override
    public String toString(){
        return 
            "Spieler-Name= " + spielerName + '\'';
    }
}

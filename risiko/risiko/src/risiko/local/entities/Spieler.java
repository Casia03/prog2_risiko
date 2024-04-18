package risiko.local.entities;

public class Spieler {
    private int spielerId;
    private String spielerName;
	private int zusatzArmee;

    public Spieler(String name, int i, int j, Object object){
        this.spielerName = spielerName;
		this.spielerId = spielerId;
		this.zusatzArmee = zusatzArmee;
    }
    
    public int getSpielerID(){
        return spielerId;
    }
    public void setSpielerID(int spielerId){
        this.spielerId = spielerId;
    }

    public String spielerName(){
        return spielerName;
    }
    public void spielerName(String spielerName){
        this.spielerName = spielerName;
    }

    public int zusatzArmee(){
        return spielerId;
    }
    public void zusatzArmeeD(int zusatzArmee){
        this.zusatzArmee = zusatzArmee;
    }
}

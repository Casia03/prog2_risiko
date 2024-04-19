package risiko.local.entities;

public class Spieler {
    private int spielerId;
    private String spielerName;
	private int zusatzArmee;

    public Spieler(String name, int id, int armee, Object object){
        this.spielerName = name;
		this.spielerId = id;
		this.zusatzArmee = armee;
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
        return zusatzArmee;
    }
    public void zusatzArmeeD(int zusatzArmee){
        this.zusatzArmee = zusatzArmee;
    }
}

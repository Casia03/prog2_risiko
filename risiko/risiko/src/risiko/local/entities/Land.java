package risiko.local.entities;

public class Land {
    private int id;
    private int spielerId;
    private int armee;
    private String farbe;
    private String kontinent;
        
    public Land(){
        
    }

    public int getID(){
        return id;
    }
    public int getSpielerID(){
        return spielerId;
    }
    public int getArmee(){
        return armee;
    }
    public String getFarbe(){
        return farbe;
    }
    public String getKontinent(){
        return kontinent;
    }

    public void setID(int id){
        this.id = id;
    }
    public void setSpielerID(int spielerId){
        this.spielerId = spielerId;
    }
    public void setArmee(int armee){
        this.armee = armee;
    }
    public void setFarbe(String farbe){
        this.farbe = farbe;
    }
    public void setKontinent(String kontinent){
        this.kontinent = kontinent;
    }
}

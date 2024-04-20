package risiko.local.entities;

public class Land {
    private int id;
    private int spielerId;
    private int armee;
    private String farbe;
    private String kontinent;
        
    public Land(int id, int spielerId, int armee, String farbe, String kontinent){
        this.id = id;
        this.spielerId = spielerId;
        this.armee = armee;
        this.farbe = farbe;
        this.kontinent = kontinent;
    }

    public void setID(int id){
        this.id = id;
    }
    public int getID(){
        return id;
    }

    public void setSpielerID(int spielerId){
        this.spielerId = spielerId;
    }
    public int getSpielerID(){
        return spielerId;
    }

    public void setArmee(int armee){
        this.armee = armee;
    }
    public int getArmee(){
        return armee;
    }

    public void setFarbe(String farbe){
        this.farbe = farbe;
    }
    public String getFarbe(){
        return farbe;
    }

    public void setKontinent(String kontinent){
        this.kontinent = kontinent;
    }
    public String getKontinent(){
        return kontinent;
    }

    
    
    
    

}

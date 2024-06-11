package risiko.local.entities;

import java.io.Serializable;

public class Land implements Serializable{

    private String name;
    private int trueIndex;
    private String kuerzel;
    private int anzahlArmeen;
    private int eingenommen;
    private String kontinent;
    private String colour;
    public String getColour;

    // Constructor
    public Land(String name, int trueIndex, String kuerzel, String kontinent, int anzahlArmeen, int eingenommen, String colour) {
        this.name = name;
        this.trueIndex = trueIndex;
        this.kuerzel = kuerzel;
        this.anzahlArmeen = anzahlArmeen;
        this.eingenommen = eingenommen;
        this.kontinent = kontinent;
        this.colour = colour;
    }

    public String getName() {
        return name;
    }

    public int getTrueIndex() {
        return trueIndex;
    }

    public String getKuerzel() {
        return kuerzel;
    }

    public void addArmee(int a) {
        for (int i = 0; i < a; i++) {
            anzahlArmeen++;
        }
    }
    public void setArmee(int a){
        this.anzahlArmeen = a;
    }
    public int getArmee() {
        return anzahlArmeen;
    }
   
    public void setEigenommen(int n) {
        this.eingenommen = n;
    }
    public int getEingenommenVon() {
        return eingenommen;
    }

    public String getKontinent() {
        return kontinent;
    }

    public void setKontinent(String kontint) {
        kontinent = kontint;
    }

    @Override
    public String toString() {
        return "Nr." + trueIndex + " / Name: " + name + " / Armee: " + anzahlArmeen + " !! EINGENOMMEN VON:  " + eingenommen +" \n ";
    }

    public String getColour() {
        return colour;
    }
}

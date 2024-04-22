package risiko.local.entities;

public class Land {

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

    // Getter methods
    public String getName() {
        return name;
    }

    public int getTrueIndex() {
        return trueIndex;
    }

    public String getKuerzel() {
        return kuerzel;
    }

    public int getArmee() {
        return anzahlArmeen;
    }

    public int getEingenommenVon() {
        return eingenommen;
    }

    public String getKontinent() {
        return kontinent;
    }

    // Setter methods
    public void setEigenommen(int n) {
        this.eingenommen = n;
    }

    public void addArmee(int a) {
        for (int i = 0; i < a; i++) {
            anzahlArmeen++;
        }
    }

    public void setKontinent(String kontint) {
        kontinent = kontint;
    }

    public void setArmee(int i) {
        this.anzahlArmeen = i;
    }

    @Override
    public String toString() {
        return " Index: "+ trueIndex + " / Name: " + name + " / Armee: " + anzahlArmeen + " \n ";
    }

    public String getColour() {
        return colour;
    }
}

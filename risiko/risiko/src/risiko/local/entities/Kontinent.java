package risiko.local.entities;

import java.util.ArrayList;
import java.util.List;

public class Kontinent{
    private List<Land> Africa = new ArrayList<Land>();
    private List<Land> Australia = new ArrayList<Land>();
    private List<Land> Asia = new ArrayList<Land>();
    private List<Land> NorthAmerica = new ArrayList<Land>();
    private List<Land> SouthAmerica = new ArrayList<Land>();
    private List<Land> Europe = new ArrayList<Land>();

    public List<Land> returnAfrica(){ //gibt alle Länder aus Africa
        return Africa;
    }

    public List<Land> returnAsia (){ //gibt alle Länder aus Asia
        return Asia;
    }

    public List<Land> returnEurope (){ //gibt alle Länder aus Europe
        return Europe;
    }

    public List<Land> returnAustralia (){   //gibt alle Länder aus Australia
        return Australia;
    }

    public List<Land> returnNorthAmerica (){ //gibt alle Länder aus North America
        return NorthAmerica;
    }

    public List<Land> returnSouthAmerica (){ //gibt alle Länder aus South America
        return SouthAmerica;
    }

    public Kontinent(List<Land> laender){ //erstellt ein neues Objekt Kontinent und füllt es mit den Ländern

        for(Land land : laender){
            switch (land.getKontinent()) {
                case "Africa":
                    Africa.add(land);
                    break;
                case "Australia":
                    Australia.add(land);
                    break;
                case "Asia":
                    Asia.add(land);
                    break;
                case "Europe":
                    Europe.add(land);
                    break;
                case "North_America":
                    NorthAmerica.add(land);
                    break;
                case "South_America":
                    SouthAmerica.add(land);
                    break;
                default:
                    break;
            }
        }
    }  
}

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

    

    public List<Land> returnAfrica(){
        return Africa;
    }

    public List<Land> returnAsia (){
        return Asia;
    }

    public List<Land> returnEurope (){
        return Europe;
    }

    public List<Land> returnAustralia (){
        return Australia;
    }

    public List<Land> returnNorthAmerica (){
        return NorthAmerica;
    }

    public List<Land> returnSouthAmerica (){
        return SouthAmerica;
    }

    public Kontinent(List<Land> laender){

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

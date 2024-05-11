package risiko.local.domain;

import java.io.FileNotFoundException;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

import risiko.local.persistance.Exceptions;
import risiko.local.entities.AdjazenzMatrix;
import risiko.local.entities.Land;
import risiko.local.entities.Spieler;

public class WeltVerwaltung {
    Exceptions Exceptions = new Exceptions();

    private ArrayList<Land> lander = new ArrayList<Land>();

    public WeltVerwaltung(){

    }
    /*
     *
     * THis shit rocks!!
     * 
     * 
     */
    public void initialisiereWelt() {
        try {
            // Der Pfad wird an Scanner uebergeben
            Scanner s = new Scanner(new File("landtext.txt")); // C:\\Users\\Casia\\Desktop\\Semester2\\PROG_2\\Übungen\\risiko\\
            while (s.hasNextLine()) {
                // laenderNamen.add(s.nextLine());
                String landName = s.nextLine();
                int trueIndex = Integer.parseInt(s.nextLine());
                String kuerzel = s.nextLine();
                String kontinent = s.nextLine();
                String colour = s.nextLine();

                Land land = new Land(landName, trueIndex, kuerzel, kontinent, 1, 0, colour);
                lander.add(land);
                // System.out.println("Name: " + land.getName() + " "+ "Kuerzel: " +
                // land.getKuerzel()+ " " + "Kontinent: " + land.getKontinent() + " " + "Armee:
                // " + land.getArmee() + " " + "Spieler: " + land.getEingenommenVon());

            }
            // System.out.println(initialCountriesList.size() + "SO VIELE LAENDER GIBT ES");
            s.close();

        } catch (FileNotFoundException e) {
            // Handle the exception if the file is not found
            Exceptions.showErrorDialog("welt konnte nicht inizialisiert werden ");
        }
    }

    public void verteileLaender(List<Spieler> spieler) {

        List<Land> countries = new ArrayList<>(lander);
        Collections.shuffle(countries); // Kopie Vermischen

        int anzahlSpieler = spieler.size(); // anzahl spieler
        int anzahlLaender = countries.size(); // anzahl laender

        int currentSpielerIndex = 0;

        for (int i = 0; i < anzahlLaender; i++) {

            int currentCountry = countries.get(i).getTrueIndex() - 1;
                                                                           
            lander.get(currentCountry).setEigenommen(currentSpielerIndex); // Laender der Original Liste
                                                                           // an den spieler verteilen

            currentSpielerIndex = (currentSpielerIndex + 1) % anzahlSpieler;

            // System.out.println("Spieler: " + currentSpieler.getName() + " Land: " +
            // initialCountriesList.get(currentCountry));
     

        }
    }   

    public ArrayList<Land> getLaeder(){
        return lander;
    } 
                                       // aber in der li ste hat das erste emelent (Alaska) den Index 0 nicht 1, an sich
                                       // hat alaska index 1 ja aber nur als objekt aber nicht als listen element

    public Land getLand(int landID){
        return lander.get(landID - 1); // minus 1 weill spieler laender 1 bis 42 auswaehlen koennen und nicht 0 vis 41 aber in der liste hat das erste emelent (Alaska) den Index 0 nicht 1, an sich hat alaska index 1 ja aber nur als objekt aber nicht als listen element 
    }
  
    public List<Land> getSpielerLaender(Spieler spieler) {
        List<Land> spielerLaender = new ArrayList<Land>();
        for (int i = 0; i < lander.size(); i++) {
            if (getLand(i).getEingenommenVon() == spieler.getSpielerID()) {
                spielerLaender.add(getLand(i));
            }
        }
        return spielerLaender; 
    }  

    public List<Land> getSpielerAngriffsbereiteLaender(Spieler spieler) {
        List<Land> spielerLaender = new ArrayList<Land>();
        for (int i = 0; i < lander.size(); i++) {
            if(getLand(i).getEingenommenVon() == spieler.getSpielerID() && getLand(i).getArmee() > 1){

                spielerLaender.add(getLand(i));
            } 
        }
        return  spielerLaender; 
    }

    public List<Land> getAngriffsmoeglichelaender(Spieler spieler, int vonLand) {
        List<Land> spielerLaender = new ArrayList<Land>();
        for (int i = 0; i < lander.size(); i++) {
            if (getLand(i).getEingenommenVon() == spieler.getSpielerID() && getLand(i).getArmee() > 1) {
                spielerLaender.add(getLand(i));
            }
        }
        return spielerLaender;
    
    }
}


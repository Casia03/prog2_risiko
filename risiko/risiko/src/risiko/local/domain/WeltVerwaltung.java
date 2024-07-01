package risiko.local.domain;

import java.io.FileNotFoundException;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

import risiko.local.persistance.Exceptions;
import risiko.local.entities.Land;
import risiko.local.entities.Spieler;

public class WeltVerwaltung {
    Exceptions Exceptions = new Exceptions();

    private ArrayList<Land> lander = new ArrayList<Land>();
    private int landNr;

    public WeltVerwaltung(){

    }

    public void initialisiereWelt() { // Einlesen der Länder aus einer Textdatei
        try {
            // Der Pfad wird an Scanner uebergeben
            Scanner s = new Scanner(new File("landtext.txt")); // C:\\Users\\Casia\\Desktop\\PROG_2\\risiko\\risiko\\
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

    public void verteileLaender(List<Spieler> spieler) { // Verteilung die Laender auf die Spieler

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

    public ArrayList<Land> getLaeder() { // Gibt alle Länder zurueck
        return lander;
    }

    public Land getLand(int landID) { // Gibt ein Land anhand seiner ID zurueck
        return lander.get(landID - 1); 
    }

    public void setNeueBesitzer(int nachLand, Spieler spieler){ // Setzt den neuen Besitzer eines Lands
        getLand(nachLand).setEigenommen(spieler.getSpielerID());
    }

    private Land getLandForForLoops(int id){ 
        return lander.get(id); // minus 1 weill spieler laender 1 bis 42 auswaehlen koennen und nicht 0 vis 41
        // aber in der liste hat das erste emelent (Alaska) den Index 0 nicht 1, an sich
        // hat alaska index 1 ja aber nur als objekt aber nicht als listen element
    }

    public List<Land> getSpielerLaender(Spieler spieler) { // Gibt alle Länder eines Spielers zurueck
        List<Land> spielerLaender = new ArrayList<Land>();
        for (int i = 0; i < lander.size(); i++) {
            if (getLandForForLoops(i).getEingenommenVon() == spieler.getSpielerID()) {
                //System.out.println(getLandForForLoops(i));
                spielerLaender.add(getLandForForLoops(i));
            }
        }
        return spielerLaender;
    }

    public String[] getSpielerLaenderAusgabe(List<Land> listeLand){ // Gibt alle Länder eines Spielers zurueck
        List<String> liste = new ArrayList<>();
        for (int i = 0; i < listeLand.size(); i++){
            String nachbarInfo = String.format("%s", listeLand.get(i));
            liste.add(nachbarInfo);
        }
        String[] listeArray = new String[liste.size()];
        listeArray = liste.toArray(listeArray);
        return listeArray;
    }

    public List<Land> getSpielerAngriffsbereiteLaender(Spieler spieler) { // Gibt alle Länder eines Spielers zurueck, auf denen es moeg
        List<Land> spielerLaender = new ArrayList<Land>();
        for (int i = 0; i < lander.size(); i++) {
            if (getLandForForLoops(i).getEingenommenVon() == spieler.getSpielerID() && getLandForForLoops(i).getArmee() > 1) {
                spielerLaender.add(getLandForForLoops(i));
            }
        }
        return spielerLaender;
    }

    public List<Land> getAngriffsmoeglichelaender(Spieler spieler, int vonLand) { // Gibt alle Länder eines Spielers zurueck, auf denen es moeglich ist ein land anzugereifen
        List<Land> spielerLaender = new ArrayList<Land>();
        for (int i = 0; i < lander.size(); i++) {
            if (getLandForForLoops(i).getEingenommenVon() == spieler.getSpielerID() && getLandForForLoops(i).getArmee() > 1) {
                spielerLaender.add(getLandForForLoops(i));
            }
        }
        return spielerLaender;
    }
    private void shuffleArray(int[] initialArray){ // Fisher-Yates shuffle algorükmus zum randomisen
        Random rnd = new Random();
        for (int i = initialArray.length - 1; i > 0; i--){
            int index = rnd.nextInt(i + 1);
            // Simple swap
            int a = initialArray[index];
            initialArray[index] = initialArray[i];
            initialArray[i] = a;
        }
    }
    public void missionenVerteilung(List<Spieler> spielerList) { // Verteilung der Missionstypen auf die Spieler
        // Create a list of mission types from the enumerator
        int[] mission = new int[7];
        for (int i = 0; i < 7; i++){
            mission[i] = i;
        }   
        shuffleArray(mission);
        
        // Assign each player a mission type
        for (int i = 0; i < spielerList.size(); i++) {
            Spieler spieler = spielerList.get(i);
            spieler.setMissionId(mission[i]);
        }
    }

    public Land getLandByColour(String colour) { // vergleicht die länder farbe mit die ausgewählte farbe und schmeisst das passende zurück
        for(Land land: lander){
            if(land.getColour().equals(colour)){
                landNr = land.getTrueIndex();
            }
        }
        return getLand(landNr);
    }

    public void loadLaender(List<Land> loadedLaender) { // fügt die geladenen Länder hinzu
        lander.addAll(loadedLaender);
    }

    public int[] getSpielerLaenderId(Spieler spieler) { // Gibt die IDs der Länder eines Spielers zurueck
        int[] listLandId = new int[42];
        for (int i = 0; i < lander.size(); i++) {
            if (getLandForForLoops(i).getEingenommenVon() == spieler.getSpielerID()) {
                //System.out.println(getLandForForLoops(i));
                // System.out.println(i); 
                listLandId[i] = getLandForForLoops(i).getTrueIndex();
                // System.out.println(listLandId[i]);
            }
        }
        return listLandId;
    }
}

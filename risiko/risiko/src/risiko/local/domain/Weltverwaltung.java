package risiko.local.domain;

import java.io.FileNotFoundException;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Scanner;

import risiko.local.persistance.Exceptions;
import risiko.local.entities.Land;
import risiko.local.entities.Spieler;

public class Weltverwaltung {
    Exceptions Exceptions = new Exceptions();
    private ArrayList<Land> lander = new ArrayList<Land>();
    public Weltverwaltung(){

    }

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

    public void verteileLander(List<Spieler> spielerListe){

        List<Land> laenderShuffel = new ArrayList<>(lander);
        Collection.shuffle(laenderShuffel);
    }
    
}

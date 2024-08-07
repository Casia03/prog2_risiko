package risiko.local.entities;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import risiko.local.domain.WeltVerwaltung;

public class AdjazenzMatrix {
   
    WeltVerwaltung wv;
    Spieler spieler;
    int n = 42;
    List<Land> countries;
    int[][] adjazenzmatrix = new int[n][n];

    public AdjazenzMatrix(WeltVerwaltung wv) {
        this.wv = wv;   
        countries = wv.getLaeder();
        //    adjazenzMatrix matrix = new adjazenzMatrix();
        
        

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                adjazenzmatrix[i][j] = 0;
            }
        }
   
        // Alaska
        adjazenzmatrix[0][1] = 1;
        adjazenzmatrix[0][5] = 1;
        adjazenzmatrix[0][31] = 1;

        // Alberta
        adjazenzmatrix[1][0] = 1;
        adjazenzmatrix[1][5] = 1;
        adjazenzmatrix[1][6] = 1;
        adjazenzmatrix[1][8] = 1;

        // Central America
        adjazenzmatrix[2][11] = 1;
        adjazenzmatrix[2][8] = 1;
        adjazenzmatrix[2][3] = 1;

        // Eastern United States
        adjazenzmatrix[3][2] = 1;
        adjazenzmatrix[3][8] = 1;
        adjazenzmatrix[3][6] = 1;
        adjazenzmatrix[3][7] = 1;

        // Greenland
        adjazenzmatrix[4][7] = 1;
        adjazenzmatrix[4][6] = 1;
        adjazenzmatrix[4][5] = 1;
        adjazenzmatrix[4][14] = 1;

        // Northwest
        adjazenzmatrix[5][0] = 1;
        adjazenzmatrix[5][1] = 1;
        adjazenzmatrix[5][4] = 1;
        adjazenzmatrix[5][6] = 1;

        // Ontario
        adjazenzmatrix[6][1] = 1;
        adjazenzmatrix[6][5] = 1;
        adjazenzmatrix[6][4] = 1;
        adjazenzmatrix[6][8] = 1;
        adjazenzmatrix[6][3] = 1;
        adjazenzmatrix[6][7] = 1;

        // Quebec
        adjazenzmatrix[7][4] = 1;
        adjazenzmatrix[7][3] = 1;
        adjazenzmatrix[7][6] = 1;

        // Western United States
        adjazenzmatrix[8][1] = 1;
        adjazenzmatrix[8][6] = 1;
        adjazenzmatrix[8][3] = 1;
        adjazenzmatrix[8][2] = 1;

        // Argentina
        adjazenzmatrix[9][12] = 1;
        adjazenzmatrix[9][10] = 1;

        // Brazil
        adjazenzmatrix[10][9] = 1;
        adjazenzmatrix[10][11] = 1;
        adjazenzmatrix[10][12] = 1;
        adjazenzmatrix[10][24] = 1;

        // Venezuela
        adjazenzmatrix[11][2] = 1;
        adjazenzmatrix[11][10] = 1;
        adjazenzmatrix[11][12] = 1;

        // Peru
        adjazenzmatrix[12][9] = 1;
        adjazenzmatrix[12][10] = 1;
        adjazenzmatrix[12][11] = 1;

        // Great Britain
        adjazenzmatrix[13][14] = 1;
        adjazenzmatrix[13][15] = 1;
        adjazenzmatrix[13][16] = 1;
        adjazenzmatrix[13][19] = 1;

        // Iceland
        adjazenzmatrix[14][16] = 1;
        adjazenzmatrix[14][13] = 1;
        adjazenzmatrix[14][4] = 1;

        // Northern Europe
        adjazenzmatrix[15][13] = 1;
        adjazenzmatrix[15][19] = 1;
        adjazenzmatrix[15][16] = 1;
        adjazenzmatrix[15][17] = 1;
        adjazenzmatrix[15][18] = 1;

        // Scandinavia
        adjazenzmatrix[16][14] = 1;
        adjazenzmatrix[16][13] = 1;
        adjazenzmatrix[16][15] = 1;
        adjazenzmatrix[16][18] = 1;

        // Southern Europe
        adjazenzmatrix[17][15] = 1;
        adjazenzmatrix[17][22] = 1;
        adjazenzmatrix[17][18] = 1;
        adjazenzmatrix[17][19] = 1;
        adjazenzmatrix[17][24] = 1;
        adjazenzmatrix[17][32] = 1;

        // Russia
        adjazenzmatrix[18][15] = 1;
        adjazenzmatrix[18][16] = 1;
        adjazenzmatrix[18][17] = 1;
        adjazenzmatrix[18][26] = 1;
        adjazenzmatrix[18][36] = 1;
        adjazenzmatrix[18][32] = 1;

        // Western Europe
        adjazenzmatrix[19][13] = 1;
        adjazenzmatrix[19][15] = 1;
        adjazenzmatrix[19][17] = 1;
        adjazenzmatrix[19][24] = 1;

        // Congo
        adjazenzmatrix[20][24] = 1;
        adjazenzmatrix[20][21] = 1;
        adjazenzmatrix[20][25] = 1;

        // East Africa
        adjazenzmatrix[21][20] = 1;
        adjazenzmatrix[21][24] = 1;
        adjazenzmatrix[21][25] = 1;
        adjazenzmatrix[21][22] = 1;
        adjazenzmatrix[21][23] = 1;

        // Egypt
        adjazenzmatrix[22][17] = 1;
        adjazenzmatrix[22][24] = 1;
        adjazenzmatrix[22][21] = 1;
        adjazenzmatrix[22][32] = 1;

        // Madagascar
        adjazenzmatrix[23][21] = 1;
        adjazenzmatrix[23][25] = 1;

        // North Africa
        adjazenzmatrix[24][19] = 1;
        adjazenzmatrix[24][10] = 1;
        adjazenzmatrix[24][20] = 1;
        adjazenzmatrix[24][22] = 1;
        adjazenzmatrix[24][17] = 1;
        adjazenzmatrix[24][21] = 1;

        // South Africa
        adjazenzmatrix[25][20] = 1;
        adjazenzmatrix[25][21] = 1;
        adjazenzmatrix[25][23] = 1;

        // Afghanistan
        adjazenzmatrix[26][18] = 1;
        adjazenzmatrix[26][32] = 1;
        adjazenzmatrix[26][36] = 1;
        adjazenzmatrix[26][28] = 1;
        adjazenzmatrix[26][27] = 1;

        // China
        adjazenzmatrix[27][26] = 1;
        adjazenzmatrix[27][35] = 1;
        adjazenzmatrix[27][33] = 1;
        adjazenzmatrix[27][28] = 1;
        adjazenzmatrix[27][34] = 1;
        adjazenzmatrix[27][36] = 1;

        // India
        adjazenzmatrix[28][27] = 1;
        adjazenzmatrix[28][26] = 1;
        adjazenzmatrix[28][34] = 1;
        adjazenzmatrix[28][32] = 1;

        // Irkutsk
        adjazenzmatrix[29][35] = 1;
        adjazenzmatrix[29][33] = 1;
        adjazenzmatrix[29][31] = 1;
        adjazenzmatrix[29][37] = 1;

        // Japan
        adjazenzmatrix[30][31] = 1;
        adjazenzmatrix[30][33] = 1;


        // Kamchatka
        adjazenzmatrix[31][30] = 1;
        adjazenzmatrix[31][33] = 1;
        adjazenzmatrix[31][29] = 1;
        adjazenzmatrix[31][37] = 1;
        adjazenzmatrix[31][0] = 1;

        // Middle East
        adjazenzmatrix[32][28] = 1;
        adjazenzmatrix[32][26] = 1;
        adjazenzmatrix[32][18] = 1;
        adjazenzmatrix[32][17] = 1;
        adjazenzmatrix[32][22] = 1;

        // Mongolia
        adjazenzmatrix[33][27] = 1;
        adjazenzmatrix[33][35] = 1;
        adjazenzmatrix[33][29] = 1;
        adjazenzmatrix[33][31] = 1;
        adjazenzmatrix[33][30] = 1;

        // Siam
        adjazenzmatrix[34][27] = 1;
        adjazenzmatrix[34][28] = 1;
        adjazenzmatrix[34][39] = 1;

        // Siberia
        adjazenzmatrix[35][37] = 1;
        adjazenzmatrix[35][29] = 1;
        adjazenzmatrix[35][33] = 1;
        adjazenzmatrix[35][27] = 1;
        adjazenzmatrix[35][36] = 1;

        // Ural
        adjazenzmatrix[36][35] = 1;
        adjazenzmatrix[36][18] = 1;
        adjazenzmatrix[36][26] = 1;
        adjazenzmatrix[36][27] = 1;

        // Yakutsk
        adjazenzmatrix[37][35] = 1;
        adjazenzmatrix[37][29] = 1;
        adjazenzmatrix[37][31] = 1;


        // Eastern Australia
        adjazenzmatrix[38][40] = 1;
        adjazenzmatrix[38][41] = 1;


        // Indonesia 
        adjazenzmatrix[39][40] = 1;
        adjazenzmatrix[39][41] = 1;
        adjazenzmatrix[39][34] = 1;

        // New Guinea
        adjazenzmatrix[40][39] = 1;
        adjazenzmatrix[40][41] = 1;
        adjazenzmatrix[40][38] = 1;

        // Western Australia
        adjazenzmatrix[41][39] = 1;
        adjazenzmatrix[41][40] = 1;
        adjazenzmatrix[41][38] = 1;

    }
   
    public int[][] getAdjazenzmatrix() {
        return adjazenzmatrix;
    }

    public String[] getAlleVerschiebeBereiteLaender(List<Land> spielerLaender){ // Zeigt nur die laender an, die einen genug armee fur eine verschiebung besitzen UND eigene nachbarn haben
        List<String> bereiteLaender = new ArrayList<>();
        Set<String> uniqueLaender = new HashSet<>();

        for (Land land : spielerLaender) {
            for (Land country : countries) {
                if (sindNachbar(land.getTrueIndex() - 1, country.getTrueIndex() - 1) && land.getArmee() >= 2){
                    String nachbarInfo = land.toString();
                            if (!uniqueLaender.contains(nachbarInfo)) {
                                bereiteLaender.add(nachbarInfo);
                                uniqueLaender.add(nachbarInfo);
                            }
                }
            }
        }    
        return bereiteLaender.toArray(new String[0]);
    }
    
    public String[] getAlleAngreifebereiteLaender(List<Land> spielerLaender, Spieler spieler){ // Zeigt nur die laender an, die einen genug armee fur ein angriff besitzen UND eigene nachbarn haben
            List<String> bereiteLaender = new ArrayList<>();
            Set<String> uniqueLaender = new HashSet<>();
        
            for (Land land : spielerLaender) {
                for (Land country : countries) {
                    if (sindNachbar(land.getTrueIndex() - 1, country.getTrueIndex() - 1) &&
                        country.getEingenommenVon() != spieler.getSpielerID() &&
                        land.getArmee() >= 2) {
                            String nachbarInfo = land.toString();
                            if (!uniqueLaender.contains(nachbarInfo)) {
                                bereiteLaender.add(nachbarInfo);
                                uniqueLaender.add(nachbarInfo);
                            }
                    }
                }
            }
        
            return bereiteLaender.toArray(new String[0]);
        }
    
    public String[] getAlleEigeneNachbars(int verteilungsLand, Spieler spieler) {
        if (verteilungsLand < 0 || verteilungsLand > n) {
            throw new IllegalArgumentException("Ungültiger Länderindex");
        }
    
        List<String> nachbarnListe = new ArrayList<>();
    
        for (int i = 0; i < n; i++) {
            if (sindNachbar(verteilungsLand, i) && countries.get(i).getEingenommenVon() == spieler.getSpielerID()) {
                String nachbarInfo = String.format("%s", countries.get(i));
                nachbarnListe.add(nachbarInfo);
                //System.out.println("INDEX: " + (i+1) + " Name : " + countries.get(i).getName() + " Armee : " + countries.get(i).getArmee());
            }
        }
    
        String[] nachbarnArray = new String[nachbarnListe.size()];
        nachbarnArray = nachbarnListe.toArray(nachbarnArray);
    
        return nachbarnArray;
    }

    public String[] getAlleGegnerNachbar(int angreifeLand, Spieler spieler) { // gibt eine String[] zuruck mit allen gegner nachbarn die von das angreifeLand zugreifbar sind.
                                                                                // Fur TextAusgabe
        if (angreifeLand < 0 || angreifeLand > n ) {
            throw new IllegalArgumentException("Ungültiger Länderindex");
        }

        List<String> nachbarn = new ArrayList<>();

           // nachbarn.add("Nachbarn von " + countries.get(angreifeLand) + ":");
            for (int i = 0; i < n; i++) {
                if (sindNachbar(angreifeLand, i) && (countries.get(i).getEingenommenVon() != spieler.getSpielerID())) {
                    String nachbarInfo = String.format("%s", countries.get(i));
                    nachbarn.add(nachbarInfo);
                    //nachbarn.add(i + " " +  countries.get(i));
                    //System.out.println("TRUEIndex: "+ countries.get(i).getTrueIndex() + " Name : " + countries.get(i).getName() + " Armee : " + countries.get(i).getArmee());
                }
            }

        String[] nachbarnArray = new String[nachbarn.size()];
        nachbarnArray = nachbarn.toArray(nachbarnArray);
        return nachbarnArray;
    }

    public List<Land> getAlleGegnerNachbarListe(int angreifeLand, Spieler spieler){ // Gibt eine List<Land> Liste zuruck mit allen gegner nachbarn die von das angreifeland zugreifbar sind.
        if (angreifeLand < 0 || angreifeLand > n ) {
            throw new IllegalArgumentException("Ungültiger Länderindex");
        }

        List<Land> liste = new ArrayList<>();
            for (int i = 0; i < n; i++) {
                if (sindNachbar(angreifeLand, i) && (countries.get(i).getEingenommenVon() != spieler.getSpielerID())) {
                    liste.add(countries.get(i));

                }
            }

        return liste;
    }

    public boolean sindNachbar(int angreifeLand, int verteidigerLand) {
        if (angreifeLand < 0 || angreifeLand > n || verteidigerLand < 0 || verteidigerLand > n) {
            throw new IllegalArgumentException("Invalid country index");
        }

        return adjazenzmatrix[angreifeLand][verteidigerLand] == 1;
    }

    public List<Land> getAlleEigeneNachbarsListe(int vonLand, Spieler spieler) {
        if (vonLand < 0 || vonLand > n ) {
            throw new IllegalArgumentException("Ungültiger Länderindex");
        }

        List<Land> liste = new ArrayList<>();
            for (int i = 0; i < n; i++) {
                if (sindNachbar(vonLand, i) && (countries.get(i).getEingenommenVon() == spieler.getSpielerID())) {
                    liste.add(countries.get(i));

                }
            }

        return liste;
    }
}

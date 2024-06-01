package risiko.local.entities;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Mission {
	private int enthoelltekarten = 0;
	private int anzahlDerZusatzarmeen = 4;

	private String[] beschreibung = new String[7];
	private List<Land> africaTerritories = new ArrayList<Land>();
	private List<Land> asiaTerritories = new ArrayList<Land>();
	private List<Land> australiaTerritories = new ArrayList<Land>();
	private List<Land> europeTerritories = new ArrayList<Land>();
	private List<Land> southAmericaTerritories = new ArrayList<Land>();
	private List<Land> northAmericaTerritories = new ArrayList<Land>();

    public enum MissionType {
        CONQUER_NORTH_AMERICA_AFRICA,
        CONQUER_NORTH_AMERICA_AUSTRALIA,
        CONQUER_24_COUNTRIES,
        CONQUER_18_COUNTRIES_WITH_MIN_ARMIES,
        CONQUER_EUROPE_SOUTH_AMERICA_AND_THIRD,
        CONQUER_ASIA_SOUTH_AMERICA,
        CONQUER_AFRICA_ASIA,
    }

	public enum EinheitsKarten{
		INFANTERIE,
		KAVALLERIE,
		ARTILLERIE
	}

	public Mission(Kontinent kontinente){
		africaTerritories = kontinente.returnAfrica();
		asiaTerritories = kontinente.returnAsia();
		australiaTerritories = kontinente.returnAustralia();
		europeTerritories = kontinente.returnEurope();
		southAmericaTerritories = kontinente.returnSouthAmerica();
		northAmericaTerritories = kontinente.returnNorthAmerica();

		beschreibung[0] = "CONQUER_NORTH_AMERICA_AFRICA";
		beschreibung[1] = "CONQUER_NORTH_AMERICA_AUSTRALIA";
		beschreibung[2] = "CONQUER_24_COUNTRIES";
		beschreibung[3] = "CONQUER_18_COUNTRIES_WITH_MIN_ARMIES";
		beschreibung[4] = "CONQUER_EUROPE_SOUTH_AMERICA_AND_THIRD";
		beschreibung[5] = "CONQUER_ASIA_SOUTH_AMERICA";
		beschreibung[6] = "CONQUER_AFRICA_ASIA";
	}

	public int generiereRandomEinheitskarte(){
		int karte = (int)(Math.random() * 3);
		return karte;
	}

	public void anzahlEnthoellteKartenErhoehen(){
		enthoelltekarten++;
	}

	public int anzahlVonEinheitenNachEnthoellen(){
		if(enthoelltekarten < 5){
			anzahlDerZusatzarmeen += 2;
			anzahlEnthoellteKartenErhoehen();
		}else if(enthoelltekarten == 5){
			anzahlDerZusatzarmeen = 15;
			anzahlEnthoellteKartenErhoehen();
		}else{
			anzahlDerZusatzarmeen += 5;
			anzahlEnthoellteKartenErhoehen();
		}
		return anzahlDerZusatzarmeen;
	}
    
	public String getMissionBeschreibung(int mission){
        return beschreibung[mission];
    }
	
	// Methods for checking player's conquests and conditions
	public boolean hasConqueredAfrica(List<Land> playerTerritories) {
		return playerTerritories.containsAll(africaTerritories);
	}

	public boolean hasConqueredAsia(List<Land> playerTerritories) {
		return playerTerritories.containsAll(asiaTerritories);
	}

	public boolean hasConqueredAustralia(List<Land> playerTerritories) {
		return playerTerritories.containsAll(australiaTerritories);
	}

	public boolean hasConqueredEurope(List<Land> playerTerritories) {
		return playerTerritories.containsAll(europeTerritories);
	}

	public boolean hasConqueredSouthAmerica(List<Land> playerTerritories) {
		return playerTerritories.containsAll(southAmericaTerritories);
	}

	public boolean hasConqueredNorthAmerica(List<Land> playerTerritories) {
		return playerTerritories.containsAll(northAmericaTerritories);
	}

	public boolean hasFreedAllCountries(List<Land> playerTerritories) {
		return playerTerritories.isEmpty();
	}

	public boolean hasConquered18CountriesWithMinArmies(List<Land> playerTerritories) {
		int i = 0;
		for (Land land : playerTerritories) {
			if (land.getArmee() >= 2) {
				i++;
			}
		}
		return i == 18;
	}

	public boolean hasConqueredThirdContinent(List<Land> playerTerritories) {
		int africa = 0;
		int asia = 0;
		int australia = 0;
		int europe = 0;
		int southAmerica = 0;
		int northAmerica = 0;

		for (Land land : playerTerritories) {
			String kontinent = land.getKontinent();
			switch (kontinent) {
				case "Africa":
					africa++;
					break;
				case "Asia":
					asia++;
					break;
				case "Australia":
					australia++;
					break;
				case "Europe":
					europe++;
					break;
				case "South_America":
					southAmerica++;
					break;
				case "North_America":
					northAmerica++;
					break;
			}
		}

		return (africa == 6) || (asia == 12) || (australia == 4) || (europe == 7) || (southAmerica == 4)
				|| (northAmerica == 9);
	}

	public int getConqueredTerritoryCount(List<Land> playerTerritories) {
		return playerTerritories.size();
	}

    
}

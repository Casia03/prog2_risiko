package risiko.local.entities;

import java.util.List;

public class Mission {
    private String beschreibung;


    public enum MissionType {
        CONQUER_NORTH_AMERICA_AFRICA,
        CONQUER_NORTH_AMERICA_AUSTRALIA,
        CONQUER_24_COUNTRIES,
        CONQUER_18_COUNTRIES_WITH_MIN_ARMIES,
        CONQUER_EUROPE_SOUTH_AMERICA_AND_THIRD,
        CONQUER_ASIA_SOUTH_AMERICA,
        CONQUER_AFRICA_ASIA,
    }
     
    public String getMission(){
        return beschreibung;
    }
    // Methods for checking player's conquests and conditions
	public boolean hasConqueredAfrica(List<Land> playerTerritories, List<Land> africaTerritories) {
		return playerTerritories.containsAll(africaTerritories);
	}

	public boolean hasConqueredAsia(List<Land> playerTerritories, List<Land> asiaTerritories) {
		return playerTerritories.containsAll(asiaTerritories);
	}

	public boolean hasConqueredAustralia(List<Land> playerTerritories, List<Land> australiaTerritories) {
		return playerTerritories.containsAll(australiaTerritories);
	}

	public boolean hasConqueredEurope(List<Land> playerTerritories, List<Land> europeTerritories) {
		return playerTerritories.containsAll(europeTerritories);
	}

	public boolean hasConqueredSouthAmerica(List<Land> playerTerritories, List<Land> southAmericaTerritories) {
		return playerTerritories.containsAll(southAmericaTerritories);
	}

	public boolean hasConqueredNorthAmerica(List<Land> playerTerritories, List<Land> northAmericaTerritories) {
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

	public boolean isFirstPlayer() {
		return false;
	}

	public boolean isSecondPlayer() {
		return false;
	}
        

    
}

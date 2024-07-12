package risiko.local.entities;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Mission {
	private int eingetauschteKarten = 0;

	private String[] beschreibung = new String[7];
	private List<Land> africaTerritories = new ArrayList<Land>();
	private List<Land> asiaTerritories = new ArrayList<Land>();
	private List<Land> australiaTerritories = new ArrayList<Land>();
	private List<Land> europeTerritories = new ArrayList<Land>();
	private List<Land> southAmericaTerritories = new ArrayList<Land>();
	private List<Land> northAmericaTerritories = new ArrayList<Land>();

	public enum MissionType { // fügt missionen hinzu
		CONQUER_NORTH_AMERICA_AFRICA,
		CONQUER_NORTH_AMERICA_AUSTRALIA,
		CONQUER_24_COUNTRIES,
		CONQUER_18_COUNTRIES_WITH_MIN_ARMIES,
		CONQUER_EUROPE_SOUTH_AMERICA_AND_THIRD,
		CONQUER_ASIA_SOUTH_AMERICA,
		CONQUER_AFRICA_ASIA,
	}

	public enum EinheitsKarten { // fügt Einheitskarten hinzu
		INFANTERIE,
		KAVALLERIE,
		ARTILLERIE;

		void add(List<EinheitsKarten> einheitsKartenList, EinheitsKarten karte) {
			einheitsKartenList.add(karte);
		}
	}

	public Mission(Kontinent kontinente) { // erstellt eine Mission
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

	public int generiereRandomEinheitskarte() { // generiert eine zufällige Einheitskarte
		int karte = (int) (Math.random() * 3);
		return karte;
	}

	// Austausch 3 gleicher Einheitenkarten
	public int tauscheDreiGleicheKartenEin(List<EinheitsKarten> einheitsKartenList, int select) {
		EinheitsKarten selectedType = null;
		switch (select) {
			case 1:
				selectedType = EinheitsKarten.INFANTERIE;
				break;
			case 2:
				selectedType = EinheitsKarten.KAVALLERIE;
				break;
			case 3:
				selectedType = EinheitsKarten.ARTILLERIE;
				break;
			default:
				return -1; // Wenn keine gültige Auswahl = ungültig
		}

		List<EinheitsKarten> AusgewaehlteKarten = new ArrayList<>();
		for (EinheitsKarten karte : einheitsKartenList) {
			if (karte == selectedType) {
				selectedType.add(einheitsKartenList, karte);
			}
		}

		int[] einheitsKartenCount = countEinheitsKarten(AusgewaehlteKarten);
		if (hatDreiGleicheKarten(einheitsKartenCount)) {
			int zusatzArmee = zusatzArmeenBerechnen();
			entferneDreiGleicheKarten(einheitsKartenList, selectedType, 3);
			eingetauschteKarten++;
			return zusatzArmee;
		} else {
			return -1; // Wenn bedingung nicht erfüllt = ungültig
		}
	}

	// Austausch 3 unterschiedliche Einheitenkarten
	public int tauscheDreiGUnterschiedlicheKartenEin(List<EinheitsKarten> einheitsKartenList) {
		int[] einheitsKartenCount = countEinheitsKarten(einheitsKartenList);
		if (hatDreiUnterschielicheKarten(einheitsKartenCount)) {
			int zusatzArmee = zusatzArmeenBerechnen();
			entferneDreiUnterschiedlicheKarten(einheitsKartenList);
			eingetauschteKarten++;
			return zusatzArmee;
		} else {
			return -1; // Wenn bedingung nicht erfüllt = ungültig
		}
	}

	private boolean hatDreiGleicheKarten(int[] einheitsKarten) {
		return einheitsKarten[0] >= 3 || einheitsKarten[1] >= 3 || einheitsKarten[2] >= 3;
	}

	private boolean hatDreiUnterschielicheKarten(int[] einheitsKartenCount) {
		return einheitsKartenCount[0] > 0 && einheitsKartenCount[1] > 0 && einheitsKartenCount[2] > 0;
	}

	private int[] countEinheitsKarten(List<EinheitsKarten> einheitsKartenList) {
		int[] count = new int[3]; // INFANTERIE, KAVALLERIE, ARTILLERIE
		for (EinheitsKarten karte : einheitsKartenList) {
			switch (karte) {
				case INFANTERIE:
					count[0]++;
					break;
				case KAVALLERIE:
					count[1]++;
					break;
				case ARTILLERIE:
					count[2]++;
					break;
			}
		}
		return count;
	}

	private int zusatzArmeenBerechnen() { // zählt die Einheiten nach Enthoellen
		if (eingetauschteKarten == 0) {
			return 4;
		} else if (eingetauschteKarten == 1) {
			return 6;
		} else if (eingetauschteKarten == 2) {
			return 8;
		} else if (eingetauschteKarten == 3) {
			return 10;
		} else {
			return 15 + (eingetauschteKarten - 3) * 5;
		}
	}

	private void entferneDreiGleicheKarten(List<EinheitsKarten> einheitsKartenList, EinheitsKarten type,
			int initialCount) {
		int[] countToRemove = { initialCount };
		einheitsKartenList.removeIf(karte -> {
			if (karte == type && countToRemove[0] > 0) {
				countToRemove[0]--;
				return true;
			}
			return false;
		});
	}

	private void entferneDreiUnterschiedlicheKarten(List<EinheitsKarten> einheitsKartenList) {
		einheitsKartenList.removeIf(karte -> {
			switch (karte) {
				case INFANTERIE:
				case KAVALLERIE:
				case ARTILLERIE:
					return true;
				default:
					return false;
			}
		});
	}

	public String getMissionBeschreibung(int mission) { // gibt die Beschreibung einer Mission zurück
		return beschreibung[mission];
	}

	// Methods for checking player's conquests and conditions
	public boolean hasConqueredAfrica(List<Land> playerTerritories) { // schaut ob der spieler africa erobert hat
		return playerTerritories.containsAll(africaTerritories);
	}

	public boolean hasConqueredAsia(List<Land> playerTerritories) { // schaut ob der spieler asia erobert hat
		return playerTerritories.containsAll(asiaTerritories);
	}

	public boolean hasConqueredAustralia(List<Land> playerTerritories) { // schaut ob der spieler australien erobert hat
		return playerTerritories.containsAll(australiaTerritories);
	}

	public boolean hasConqueredEurope(List<Land> playerTerritories) { // schaut ob der spieler europe erobert hat
		return playerTerritories.containsAll(europeTerritories);
	}

	public boolean hasConqueredSouthAmerica(List<Land> playerTerritories) { // schaut ob der spieler südamerika erobert
																			// hat
		return playerTerritories.containsAll(southAmericaTerritories);
	}

	public boolean hasConqueredNorthAmerica(List<Land> playerTerritories) { // schaut ob der spieler nordamerika erobert
																			// hat
		return playerTerritories.containsAll(northAmericaTerritories);
	}

	public boolean hasFreedAllCountries(List<Land> playerTerritories) { // schaut ob der spieler alle kontinente frei
																		// gegeben hat
		return playerTerritories.isEmpty();
	}

	public boolean hasConquered18CountriesWithMinArmies(List<Land> playerTerritories) { // schaut ob der spieler 18
																						// kontinente mit mindestens 2
		int i = 0;
		for (Land land : playerTerritories) {
			if (land.getArmee() >= 2) {
				i++;
			}
		}
		return i >= 18;
	}

	public boolean hasConqueredThirdContinent(List<Land> playerTerritories) { // schaut ob der spieler den dritten
																				// kontinent erobert hat
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

	public int getConqueredTerritoryCount(List<Land> playerTerritories) { // gibt die Anzahl der eroberten Territorien
																			// zurück
		return playerTerritories.size();
	}

	public boolean hasConquered24Countries(List<Land> playerTerritories) { // schaut ob der spieler 24 kontinente
																			// erobert hat
		if (playerTerritories.size() >= 24) {
			return true;
		} else {
			return false;
		}
	}

	public boolean checkIfMissionErfuelt(List<Land> spielerTerritorien, int spielerMission) { // prüft ob die Mission
																								// erfüllt ist
		switch (spielerMission + 1) {
			case 1:

				if (hasConqueredNorthAmerica(spielerTerritorien) && hasConqueredAfrica(spielerTerritorien)) { // CONQUER_NORTH_AMERICA_AFRICA
					System.out.println("MISSION 1 !!!");
					return true;
				}
				return false;

			case 2:
				System.out.println("MISSION 2 !!!");
				if (hasConqueredNorthAmerica(spielerTerritorien) && hasConqueredAustralia(spielerTerritorien)) { // CONQUER_NORTH_AMERICA_AUSTRALIA,
					return true;
				}
				return false;

			case 3:
				System.out.println("MISSION 3 !!!");
				if (hasConquered24Countries(spielerTerritorien)) { // CONQUER_24_COUNTRIES FEHLT

					return true;
				}
				return false;

			case 4:
				System.out.println("MISSION 4 !!!");
				if (hasConquered18CountriesWithMinArmies(spielerTerritorien)) { // CONQUER_18_COUNTRIES_WITH_MIN_ARMIES
					return true;
				}
				return false;

			case 5:
				System.out.println("MISSION 5 !!!");
				if (hasConqueredEurope(spielerTerritorien) && hasConqueredSouthAmerica(spielerTerritorien)
						&& hasConqueredThirdContinent(spielerTerritorien)) { // CONQUER_EUROPE_SOUTH_AMERICA_AND_THIRD
					return true;
				}
				return false;

			case 6:

				if (hasConqueredAsia(spielerTerritorien) && hasConqueredSouthAmerica(spielerTerritorien)) { // CONQUER_ASIA_SOUTH_AMERICA
					return true;
				}
				return false;

			case 7:

				if (hasConqueredAfrica(spielerTerritorien) && hasConqueredAsia(spielerTerritorien)) { // CONQUER_AFRICA_ASIA
					return true;
				}
				return false;

			default:
				return false;

		}

	}

}

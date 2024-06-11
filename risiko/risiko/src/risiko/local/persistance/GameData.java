package risiko.local.persistance;

import java.io.Serializable;
import java.util.List;

import risiko.local.entities.Land;
import risiko.local.entities.Spieler;
import risiko.local.entities.Turn;

public class GameData implements Serializable {
    private static final long serialVersionUID = 1L;
    private List<Spieler> spielerListe;
    private List<Land> landList;
    private int turn;
    private int phase;

    public GameData(List<Spieler> spielerListe, List<Land> landList, int turn, int phase) {
        this.spielerListe = spielerListe;
        this.landList = landList;
        this.turn = turn;
        this.phase = phase;
    }

    public List<Spieler> getSpielerListe() {
        return spielerListe;
    }

    public List<Land> getLandList() {
        return landList;
    }

    public int getTurn() {
        return turn;
    }

    public int getPhase() {
        return phase;
    }
}

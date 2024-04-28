package risiko.local.entities;

public class Turn {

    public enum Phase {
        NEUE_EINHEITEN_VERTEILEN,
        ANGRIFF,
        VERTEIDIGUNG,
        AUSWERTUNG_VON_KAEMPFEN,
        EINRUECKEN,
        VERSCHIEBEN_VON_EINHEITEN
    }
}
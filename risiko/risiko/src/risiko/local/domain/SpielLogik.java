package risiko.local.domain;

public class SpielLogik {
    
    void neueEinheitenVerteilen(){
        /*
         * Der Spieler erhält zu Beginn jeder Runde neue Einheiten basierend auf den von ihm besetzten Ländern und Kontinenten.
         * Diese Einheiten müssen vom Spieler auf seine Länder verteilt werden.
         */
        
    }  

    void angreifen(){
        /*
         * Der Spieler gibt ein, mit welcher Teilarmee von welchem eigenen Land aus welches andere Land angegriffen wird.
         * Der Spieler kann mit maximal drei Einheiten zurzeit angreifen.
         * Mindestens eine Einheit muss im eigenen Land verbleiben und darf nicht für den Angriff genutzt werden.
         */
    }

    void verteidigen(){
        /*
         * Der Spieler gibt ein, mit welcher Teilarmee das angegriffene Land verteidigt wird.
         * Der Spieler kann sein Land mit maximal zwei Einheiten zurzeit verteidigen.
         */
    }

    void auswertungVonKaempfen(){
        /*
         * Der Angreifer würfelt mit der Anzahl von Würfeln, die der Anzahl seiner Angriffseinheiten entspricht (max. drei).
         * Der Verteidiger würfelt mit der Anzahl von Würfeln, die der Anzahl seiner Verteidigungseinheiten entspricht (max. zwei).
         * Die Würfel von Angreifer und Verteidiger werden verglichen, und die entsprechenden Einheiten werden basierend auf den Ergebnissen entfernt.
         */
    }

    void einruecken(){
        /*
         * Wenn die Verteidigungsarmee vernichtet ist, rückt der Angreifer mit den Einheiten, die er beim letzten Angriff eingesetzt und nicht im Kampf verloren hat, 
         * in das eroberte Land ein.
         * Weitere Einheiten können nachgezogen werden, aber mindestens eine Einheit muss im Land des Angreifers verbleiben.
         */
    }

    void einheitenVerschieben(){
        /*
         * Der Spieler gibt ein, wie viele bisher unbeteiligte Einheiten von welchem eigenen Land in welches eigene Nachbarland verschoben werden sollen.
         * Dabei muss immer mindestens eine Einheit in dem Land zurückbleiben.
         * Eine Einheit darf nur einmal pro Runde verschoben werden.
         */
    }

}

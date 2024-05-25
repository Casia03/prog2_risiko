package risiko.local.persistance;

import risiko.local.entities.Land;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class Exceptions {
    // nicht genug soldaten exception fur angriff mit 1 soldat
    public Exceptions() {

    }
    // nicht genug spieler fur den spielstart exception fur spielbeginn mit <2
    // spieler

    public void showErrorDialog(String string) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'showErrorDialog'");
    }

    public int readInt(Scanner scanner, int min, int max) {
        int value = -1;
        boolean valid = false;
        while (!valid) {
            try {
                value = scanner.nextInt();
                if (value < min || value > max) {
                    throw new IllegalArgumentException("Bitte geben Sie eine gültige Zahl zwischen " + min + " und " + max + " ein.");
                }
                valid = true;
            } catch (InputMismatchException e) {
                System.out.println("Du musst eine Nummer eingeben.");
                scanner.next(); // Clear invalid input
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
        return value;
    }

    public int readLandIndex(Scanner scanner, List<Land> validIndices) {
        int value = -1;
        boolean valid = false;
        while (!valid) {
            try {
                value = scanner.nextInt();
                if (!isValidLandIndex(validIndices, value)) {
                    throw new IllegalArgumentException(
                            "Bitte geben Sie einen gültigen Index eines Landes ein, das Sie besitzen.");
                }
                valid = true;
            } catch (InputMismatchException e) {
                System.out.println("Du musst eine Nummer eingeben.");
                scanner.next();
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
        return value;
    }

    public boolean isValidLandIndex(List<Land> landList, int inputIndex) {
    for (Land land : landList) {
        if (land.getTrueIndex() == inputIndex) {
            return true;
        }
    }
    return false;
    }
}

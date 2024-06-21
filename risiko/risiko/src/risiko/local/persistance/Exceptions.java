package risiko.local.persistance;

import risiko.local.entities.Land;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import javax.swing.JOptionPane;

public class Exceptions {
    // nicht genug soldaten exception fur angriff mit 1 soldat
    public Exceptions() {}

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
                            "Bitte geben Sie einen gültigen Index eines Landes ein.");
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

    public String getErrorMessage(String message) {
        return message;
    }

    public int readInt(String input, int min, int max) throws NumberFormatException {
        int value = Integer.parseInt(input);
        if (value < min || value > max) {
            throw new IllegalArgumentException("Please enter a valid number between " + min + " and " + max + ".");
        }
        return value;
    }

    public int readLandIndexNoScanner(String input, List<Land> validIndices) throws NumberFormatException {
        int value = Integer.parseInt(input);
        if (!isValidLandIndex(validIndices, value)) {
            throw new IllegalArgumentException("Please enter a valid index of a land.");
        }
        return value;
    }

    public void showErrorDialog(String message) {
        JOptionPane.showMessageDialog(null, message, "Error", JOptionPane.ERROR_MESSAGE);
    
    }

    public void readIntAngreifen(String input, int i, int armee) {
        int value = Integer.parseInt(input);
        if (value < i || value > armee) {
            if (armee == 2){
                throw new IllegalArgumentException("Please enter the only possible number: " + i + ".");
            }
            throw new IllegalArgumentException("Please enter a valid number between " + i + " and " + armee + ".");
        }
    }
}

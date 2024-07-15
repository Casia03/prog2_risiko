package risiko.local.server;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class SpielHandler implements Runnable {
    private Socket clientSocket;

    public SpielHandler(Socket socket) {
        this.clientSocket = socket;
    }

    @Override
    public void run() {
        try (
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)
        ) {
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                // Hier die Logik deines Spiels verarbeiten
                System.out.println("Nachricht vom Client: " + inputLine);
                out.println("Antwort vom Server: " + inputLine);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

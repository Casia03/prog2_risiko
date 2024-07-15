package risiko.local.server;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private static final int PORT = 12345;

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Risiko Server läuft auf Port " + PORT);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Neuer Client verbunden: " + clientSocket.getInetAddress());

                // Starte einen neuen Thread für jede Client-Verbindung
                new Thread(new SpielHandler(clientSocket)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

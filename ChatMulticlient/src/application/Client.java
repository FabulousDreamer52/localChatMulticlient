package application;
import java.io.*;
import java.net.*;

public class Client {
    private Socket socket; // Connessione al server
    private BufferedReader in; // Per leggere i messaggi dal server
    private PrintWriter out; // Per inviare i messaggi al server
    private MessageChat chat; // Interfaccia per gestire i messaggi ricevuti

    // Costruttore: inizializza la connessione al server
    public Client(String serverAddress, int port) throws IOException {
        socket = new Socket(serverAddress, port); // Crea una connessione al server (serverAddress) e alla porta (port)
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);
    }

    // Metodo per inviare messaggi al server
    public void invio(String message) {
        out.println(message); // Invia il messaggio al server
    }

    // Metodo per chiudere la connessione
    public void close() throws IOException {
        socket.close(); // Chiude la connessione al server
    }

    // Metodo per impostare un listener per i messaggi ricevuti
    public void setChatLog(MessageChat chat) {
        this.chat = chat; 
        }

    // Metodo per ascoltare i messaggi provenienti dal server
    public void ascolto() {
        new Thread(() -> { // Crea un nuovo thread per evitare di bloccare il programma principale
            try {
                String msg;
                while ((msg = in.readLine()) != null) { // Continua a leggere i messaggi dal server finché sono disponibili
                    if (chat != null) {
                        chat.ricevuto(msg); 
                    }
                }
            } catch (IOException e) {
                System.out.println("Connessione chiusa."); 
            }
        }).start(); // Avvia il thread
    }

    // Interfaccia per gestire i messaggi ricevuti
    public interface MessageChat {
        void ricevuto(String message); // Metodo che verrà chiamato quando un messaggio viene ricevuto dal server
    }
}

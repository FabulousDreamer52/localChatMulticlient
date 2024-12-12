package application;
import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class Server {
    private static final int PORT = 12345; // La porta su cui il server ascolterà le connessioni
    private static Set<ClientHandler> clients = Collections.synchronizedSet(new HashSet<>()); // Collezione che contiene tutti i client connessi
    private static AtomicInteger contaClient = new AtomicInteger(1); // Contatore per assegnare nomi univoci ai client numerandoli

    public static void main(String[] args) {
        System.out.println("Server avviato.");
        try (ServerSocket serverSocket = new ServerSocket(PORT)) { // Crea un server che ascolta sulla porta PORT (12345)
            while (true) {
                Socket clientSocket = serverSocket.accept(); // Aspetta che un client si connetta
                String nomeClient = "Client" + contaClient.getAndIncrement(); // Assegna un nome univoco al client connesso
                System.out.println(nomeClient + " connesso."); // Stampa un messaggio sul terminale del server
                new ClientHandler(clientSocket, nomeClient).start(); // Crea e avvia un nuovo thread per gestire la connessione del client
            }
        } catch (IOException e) {
            System.out.println("Errore server: " + e.getMessage()); // Gestisce eventuali errori durante l'esecuzione del server
        }
    }

    // Questa classe gestisce ciascun client in un thread separato
    private static class ClientHandler extends Thread {
        private Socket socket; // Connessione al client
        private PrintWriter out; // Invia messaggi al client
        private String nomeClient; // Nome del client

        public ClientHandler(Socket socket, String nomeClient) {
            this.socket = socket;
            this.nomeClient = nomeClient;
        }

        @Override
        public void run() {
            try (
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream())); // Legge messaggi inviati dal client
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true); // Invia messaggi al client
            ) {
                this.out = out;
                synchronized (clients) {
                    clients.add(this); // Aggiunge il client alla lista dei client connessi
                }

                invia(nomeClient + " si è unito alla chat."); // Informa i client che un nuovo client si è unito

                String message;
                while ((message = in.readLine()) != null) { // Legge i messaggi inviati dal client
                    invia(nomeClient + ": " + message); // Invia il messaggio a tutti i client connessi
                }
            } catch (IOException e) {
            	e.printStackTrace();
            } finally {	
            	System.out.println(nomeClient + " disconnesso."); // Gestisce la disconnessione del client

            	if (out != null) {
                    synchronized (clients) {
                        clients.remove(this); // Rimuove il client dalla lista dei client connessi
                    }
                }
                invia(nomeClient + " ha lasciato la chat."); // Informa tutti i client che un client ha abbandonato la chat o7
                try {
                    socket.close(); // Chiude la connessione del client
                } catch (IOException e) {
                    System.out.println("Errore chiusura socket: " + e.getMessage()); 
                }
            }
        }

        // Metodo per inviare un messaggio a tutti i client connessi
        private void invia(String message) {
            synchronized (clients) { // Blocca l'accesso alla lista dei client per evitare un overlappping dei client
                for (ClientHandler client : clients) {
                    client.out.println(message);
                }
            }
        }
    }
}

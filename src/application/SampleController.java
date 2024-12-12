package application;

import java.io.IOException;
import java.net.URL;
import java.util.Random;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

public class SampleController {
    private Client client;
	
    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextArea txtAreaChat;

    @FXML
    private TextField txtScrittura;

    @FXML
    private Button btnLeave;

    @FXML
    private Button btnInvio;

    @FXML
    void leave(MouseEvent event) {
	    new Thread(()-> { // Avvia un nuovo thread per non bloccare la GUI
	    	try {
	    		txtAreaChat.appendText("Connessione chiusa.\n"); // Messaggio di conferma
	    		client.close(); // Chiude la connessione al server
	            int timer = 6; // Durata del conto alla rovescia in secondi
		        txtAreaChat.appendText("L'app si chiuderà in...\n");
		        for (timer=6; timer >=0; timer--) {
		            int fine = timer;
		            try {
		                Thread.sleep(1000); // Aspetta 1 secondo
		            } catch (InterruptedException e) {
		                Platform.runLater(() -> txtAreaChat.appendText("Errore nel conto alla rovescia.\n"));
		                return; // Interrompe il conto alla rovescia in caso di errore
		            }
		            if (timer == 1) {
		                txtAreaChat.appendText("Ciao ciao!!! :wave:"); // Messaggio finale
		            } else {
		                txtAreaChat.appendText(fine - 1 + "\n"); // Mostra il tempo rimanente
		            }
	            } 
	    	}catch (IOException e) {
	            txtAreaChat.appendText("Errore durante la chiusura dell'app.\n"); // Messaggio in caso di errore
	        }
            System.exit(0); // Chiude completamente l'applicazione
	       
		}).start();
    }
    

    @FXML
    void send(MouseEvent event) {
        String message = txtScrittura.getText();
        Random numRan = new Random(); // Generazione numero randomico
        if (!message.isEmpty()) {
        	if (message.equalsIgnoreCase("/test")) { // Test
        		if (numRan.nextInt(2) == 0) {client.invio("abacus");} else {client.invio("Sus");}
	        } else if (message.equalsIgnoreCase("/help")) { // Comando /help
	        	txtAreaChat.appendText("Questi sono i comandi disponibili: /test, /gastaldelli, /cirioni, /savoldi, /zapponi\n");
	        } else if (message.equalsIgnoreCase("/gastaldelli")) {
	        	int n = numRan.nextInt(3);
        		if (n == 0) {client.invio("Buongiorno studenti!");} else if (n == 1) {client.invio("GALLI!!!");} else {client.invio("No Galli, non buttarti nella mia ora!");}
	        } else if (message.equalsIgnoreCase("/cirioni")) {
	        	int n = numRan.nextInt(3);
        		if (n == 0) {client.invio("IL RUUUUUTER!!!");} else if (n == 1) {client.invio("Ragazzi, il router si collega al cavo? DI ULTIMO MIGLIO!!!");} else {client.invio("CONTRO VENTO!");}
	        } else if (message.equalsIgnoreCase("/savoldi")) {
	        	int n = numRan.nextInt(3);
        		if (n == 0) {client.invio("EHHH ALORA!");} else if (n == 1) {client.invio("IO SONO STUFA DI VOI!");} else {client.invio("Galli, sai che queste tue sub culture sono illegali... vero?");}
	        } else if (message.equalsIgnoreCase("/zapponi")) {
	        	int n = numRan.nextInt(4);
        		if (n == 0) {client.invio("DAIEMOH");} else if (n == 1) {client.invio("Ma abbiamo già visto Colpo Vincente?");} else if (n == 2) {client.invio("KUKU!!!");} else {client.invio("Ma abbiamo già guardato Gran Torino?");}
	        } else {
	            client.invio(message); // Invia il messaggio al server
	        }
            txtScrittura.clear();
        }
    }

    private void mostra(String message) {
        Platform.runLater(() -> txtAreaChat.appendText(message + "\n")); // Aggiorno la GUI da un thread secondario
    }
    @FXML
    void initialize() {
    	try {
            client = new Client("localhost", 12345); // Connessione al server locale sulla porta 12345
            client.setChatLog(this::mostra); // Imposta il metodo per gestire i messaggi ricevuti
            client.ascolto(); // Avvia l'ascolto dei messaggi dal server
            txtAreaChat.appendText("Benvenuto alla chat!\nDigita ''/help'' per conoscere i vari comandi.\n");
        } catch (IOException e) {
            txtAreaChat.appendText("Errore di connessione al server.\n"); 
        }
        assert txtAreaChat != null : "fx:id=\"txtAreaChat\" was not injected: check your FXML file 'Sample.fxml'.";
        assert txtScrittura != null : "fx:id=\"txtScrittura\" was not injected: check your FXML file 'Sample.fxml'.";
        assert btnLeave != null : "fx:id=\"btnLeave\" was not injected: check your FXML file 'Sample.fxml'.";
        assert btnInvio != null : "fx:id=\"btnInvio\" was not injected: check your FXML file 'Sample.fxml'.";

    }
}

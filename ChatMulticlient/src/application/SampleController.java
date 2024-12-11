package application;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

public class SampleController {
	public static Boolean special = false;
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
	            client.close(); // Chiude la connessione al server
	            txtAreaChat.appendText("Connessione chiusa.\n"); // Messaggio di conferma
	        } catch (IOException e) {
	            txtAreaChat.appendText("Errore durante la chiusura.\n"); // Messaggio in caso di errore
	        }
	        int timer = 6; // Durata del conto alla rovescia in secondi
	        txtAreaChat.appendText("L'app si chiuderà in...\n");
	        while (timer >= 0) {
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
	            timer--;
	        }
	        System.exit(0); // Chiude completamente l'applicazione
		}).start();
    }
    

    @FXML
    void send(MouseEvent event) {
        String msg = txtScrittura.getText();
        int mam; // random numero per le frasi DA COMPLETARE
        if (!msg.isEmpty()) {
        	 if(msg.equalsIgnoreCase("/test")){
 	        	client.invio("abacus"); // Test invio messaggio
 	        }else if (msg.equalsIgnoreCase("/gastaldelli")) {
	            client.invio(""); // Tipica frade del mitico Gasta
	        } else if(msg.equalsIgnoreCase("/cirioni")){
	        	client.invio(""); // Tipica frase del Prof Cirioni
	        } else if(msg.equalsIgnoreCase("/savoldi")){
	        	client.invio(""); // Tipica frase della Ex Prof Savoldi
	        } else if(msg.equalsIgnoreCase("/zapponi")){
	        	client.invio("Daiemoh!!!"); // Tipica frase del mitico Zapponi
	        } else {
	            client.invio(msg); // Invia il messaggio al server
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
        } catch (IOException e) {
            txtAreaChat.appendText("Errore connessione al server.\n"); 
        }
        assert txtAreaChat != null : "fx:id=\"txtAreaChat\" was not injected: check your FXML file 'Sample.fxml'.";
        assert txtScrittura != null : "fx:id=\"txtScrittura\" was not injected: check your FXML file 'Sample.fxml'.";
        assert btnLeave != null : "fx:id=\"btnLeave\" was not injected: check your FXML file 'Sample.fxml'.";
        assert btnInvio != null : "fx:id=\"btnInvio\" was not injected: check your FXML file 'Sample.fxml'.";

    }
}

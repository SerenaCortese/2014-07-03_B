package it.polito.tdp.meteo;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.meteo.bean.Citta;
import it.polito.tdp.meteo.bean.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;


public class MeteoController {
	
	private Model model;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private ChoiceBox<Integer> boxMese;

    @FXML
    private Button btnCalcola;

    @FXML
    private Button btnUmidita;

    @FXML
    private TextArea txtResult;

    @FXML
    void getUmiditaMedie(ActionEvent event) {
    	txtResult.clear();
    	Integer mese = boxMese.getValue();
    	if(mese == null) {
    		txtResult.setText("Selezionare un mese.");
    		return;
    	}
    	List<Citta> citta = model.getUmiditaMediePerMese(mese);
    	for(Citta c : citta) {
    		txtResult.appendText(c.toString()+"\n");
    	}
    }

    
    @FXML
    void doCalcolaSequenza(ActionEvent event) {
    	txtResult.clear();
    	Integer mese = boxMese.getValue();
    	if(mese == null) {
    		txtResult.setText("Selezionare un mese.");
    		return;
    	}
    	List<Citta> cittaSequenza = model.getSequenzaMinimoCosto(mese);
    	for(Citta c : cittaSequenza) {
    		txtResult.appendText(c.getCitta().toString()+"\n");
    	}
    }

    
    @FXML
    void initialize() {
        assert boxMese != null : "fx:id=\"boxMese\" was not injected: check your FXML file 'Meteo.fxml'.";
        assert btnCalcola != null : "fx:id=\"btnCalcola\" was not injected: check your FXML file 'Meteo.fxml'.";
        assert btnUmidita != null : "fx:id=\"btnUmidita\" was not injected: check your FXML file 'Meteo.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Meteo.fxml'.";


    }


	public void setModel(Model model) {
		this.model = model;
		for(int i=1; i<13; i++) {
			boxMese.getItems().add(i);
		}
	}

}

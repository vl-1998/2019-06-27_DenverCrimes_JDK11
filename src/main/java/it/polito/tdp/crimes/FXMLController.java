package it.polito.tdp.crimes;

import java.net.URL;
import java.util.ResourceBundle;

import it.polito.tdp.crimes.db.EventsDAO;
import it.polito.tdp.crimes.model.Adiacenza;
import it.polito.tdp.crimes.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;

//controller turno A --> switchare al branch master_turnoB o master_turnoC per turno B o C

public class FXMLController {
	
	private Model model;
	private EventsDAO dao;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private ComboBox<String> boxCategoria;

    @FXML
    private ComboBox<Integer> boxAnno;

    @FXML
    private Button btnAnalisi;

    @FXML
    private ComboBox<Adiacenza> boxArco;

    @FXML
    private Button btnPercorso;

    @FXML
    private TextArea txtResult;

    @FXML
    void doCalcolaPercorso(ActionEvent event) {
    	txtResult.clear();
    	
    	Adiacenza arco = this.boxArco.getValue();
    	
    	if(arco == null) {
    		txtResult.appendText("Scegliere un arco!");
    	}
    	
    	txtResult.appendText(this.model.percorsoMinimo(arco)+"\n");

    }

    @FXML
    void doCreaGrafo(ActionEvent event) {
    	txtResult.clear();
    	
    	String categoria = this.boxCategoria.getValue();
    	Integer anno = this.boxAnno.getValue();
    	
    	if (anno == null && categoria == null) {
    		txtResult.appendText("Scegliere un anno e una categoria!");
    		return;
    	}
    	
    	if(categoria == null) {
    		txtResult.appendText("Inserire una categoria!");
    		return;
    	}
    	if (anno == null) {
    		txtResult.appendText("Scegliere un anno!");
    		return;
    	}
    	
    	this.model.creaGrafo(categoria, anno);
    	this.boxArco.getItems().clear();
    	this.boxArco.getItems().addAll(this.model.getAdiacenze());
    	txtResult.appendText("Grafo creato!\n#Vertici = "+this.model.getVertex().size()+" #Archi="+this.model.getEdge().size());
    	
    	txtResult.appendText("\nArchi di peso massimo :\n"+this.model.getPesoMassimo()+"\n");
    	

    }

    @FXML
    void initialize() {
        assert boxCategoria != null : "fx:id=\"boxCategoria\" was not injected: check your FXML file 'Crimes.fxml'.";
        assert boxAnno != null : "fx:id=\"boxAnno\" was not injected: check your FXML file 'Crimes.fxml'.";
        assert btnAnalisi != null : "fx:id=\"btnAnalisi\" was not injected: check your FXML file 'Crimes.fxml'.";
        assert boxArco != null : "fx:id=\"boxArco\" was not injected: check your FXML file 'Crimes.fxml'.";
        assert btnPercorso != null : "fx:id=\"btnPercorso\" was not injected: check your FXML file 'Crimes.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Crimes.fxml'.";

    }

	public void setModel(Model model, EventsDAO dao) {
		this.model = model;
		this.dao = dao;
		this.boxCategoria.getItems().addAll(this.dao.getCategory());
		this.boxAnno.getItems().addAll(this.dao.getYear());
	}
}

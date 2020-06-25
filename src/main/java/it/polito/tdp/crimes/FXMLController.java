package it.polito.tdp.crimes;

import java.net.URL;
import java.util.ResourceBundle;

import it.polito.tdp.crimes.model.Model;
import it.polito.tdp.crimes.model.Arco;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;

//controller turno C --> switchare al branch master_turnoA o master_turnoB per turno A o B

public class FXMLController {
	
	private Model model;

	@FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private ComboBox<String> boxCategoria;

    @FXML
    private ComboBox<Integer> boxGiorno;

    @FXML
    private Button btnAnalisi;

    @FXML
    private ComboBox<Arco> boxArco;

    @FXML
    private Button btnPercorso;

    @FXML
    private TextArea txtResult;

    @FXML
    void doCalcolaPercorso(ActionEvent event) {
    	txtResult.clear();
    	Arco a = boxArco.getValue();
    	
    	if (a==null) {
    		txtResult.appendText("Scegliere un arco!");
    	}
    	
    	for (String s : this.model.getCammino(a)) {
    		txtResult.appendText(s+"\n");
    	}
    	
    	txtResult.appendText("Peso massimo = "+this.model.getPesoMassimo());

    }

    @FXML
    void doCreaGrafo(ActionEvent event) {
    	txtResult.clear();
    	String categoria = boxCategoria.getValue();
    	Integer giorno = boxGiorno.getValue();
    	
    	if (categoria==null || giorno==null) {
    		txtResult.appendText("Scegliere sia la categoria sia il giorno!");
    	}
    	
    	this.model.creaGrafo(categoria, giorno);
    	txtResult.appendText("Grafo creato! #vertici = "+this.model.getVertex().size()+ " #archi= "+this.model.getArchi().size()+"\n\n");
    	for (Arco a : this.model.getPesoInferiore(categoria, giorno)) {
    		txtResult.appendText(a.toString()+"\n");
    	}
    	boxArco.getItems().clear();
    	boxArco.getItems().addAll(this.model.getPesoInferiore(categoria, giorno));

    }

    @FXML
    void initialize() {
        assert boxCategoria != null : "fx:id=\"boxCategoria\" was not injected: check your FXML file 'Crimes.fxml'.";
        assert boxGiorno != null : "fx:id=\"boxGiorno\" was not injected: check your FXML file 'Crimes.fxml'.";
        assert btnAnalisi != null : "fx:id=\"btnAnalisi\" was not injected: check your FXML file 'Crimes.fxml'.";
        assert boxArco != null : "fx:id=\"boxArco\" was not injected: check your FXML file 'Crimes.fxml'.";
        assert btnPercorso != null : "fx:id=\"btnPercorso\" was not injected: check your FXML file 'Crimes.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Crimes.fxml'.";

    }

	public void setModel(Model model) {
		this.model = model;
		this.boxCategoria.getItems().addAll(this.model.getOffense());
		this.boxGiorno.getItems().addAll(this.model.getDay());
	}
}

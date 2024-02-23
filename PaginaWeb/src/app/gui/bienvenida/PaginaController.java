package app.gui.bienvenida;

import app.gui.appController.AppController;
import app.gui.modelo.PaginaWeb;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

public class PaginaController extends AppController{


    @FXML
    private Button btnVolver;

    @FXML
    private TextField txtUrl;


    @FXML
    private WebView webView;
    
    @FXML
    public void initialize() {
        
        WebEngine webEngine = webView.getEngine();
        PaginaWeb seleccionada= (PaginaWeb) getUserDataObject("paginaWebSeleccionada");
        
        String urlGuardada = seleccionada.getUrl(); 
      
        
        webEngine.load(urlGuardada);
    }
    
    @FXML
    void irAInicio(ActionEvent event) {

    	
    	 cambiarVista(FXML_BIENVENIDA);
    	
    	
    	
    }

    
   

}

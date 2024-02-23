package app.gui.bienvenida;




import app.gui.appController.AppController;
import app.gui.modelo.Usuario;
import javafx.fxml.FXML;
import javafx.scene.control.Label;


public class PerfilController extends AppController{

    @FXML
    private Label lblCorreo;

    @FXML
    private Label lblNombre;

    Usuario yo= (Usuario) getUserDataObject("usuario");
    @FXML
    public void initialize() {
    	lblCorreo.setText(yo.getCorreo());
    	lblNombre.setText(yo.getNombre());
    }
   
    
}

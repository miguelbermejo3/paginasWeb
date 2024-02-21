package app.gui.bienvenida;

import java.math.BigDecimal;
import java.math.RoundingMode;

import app.gui.appController.AppController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;

public class SliderController extends AppController{
    @FXML
    private Button btnLeer;

    @FXML
    private TextField resultado;

    @FXML
    private Slider slider;

    @FXML
    void leer(ActionEvent event) {
    	Double valor=slider.getValue();
    	BigDecimal valorFinal=new BigDecimal(valor);
    	valorFinal=valorFinal.setScale(2,RoundingMode.HALF_DOWN);
    	resultado.setText(valorFinal.toString());
    }
    
   
    
}

package app.gui.bienvenida;

import java.util.Optional;

import app.gui.appController.AppController;
import app.gui.service.PaginaService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.BorderPane;

public class BienvenidaController extends AppController {

	@FXML
	private BorderPane borderPane;

	@FXML
	void cerrarSesion(ActionEvent event) {

		cambiarVista(FXML_LOGIN);
	}

	@FXML
	void irAAñadir(ActionEvent event) {
		
		Parent vista=cargarVista(FXML_ALTA);
    	borderPane.setCenter(vista);
		
		
		
	}

	@FXML
	void irAConsulta(ActionEvent event) {
		Parent vista=cargarVista(FXML_CONSULTA);
		borderPane.setCenter(vista);
	}

	@FXML
	void irAModificar(ActionEvent event) {
		
		Parent vista=cargarVista(FXML_MODIFICAR);
		borderPane.setCenter(vista);
	}

	@FXML
	void salir(ActionEvent event) {
		Alert pregunta = new Alert(AlertType.CONFIRMATION);
		pregunta.setContentText("¿Estás seguro de querer cerrar?");
		pregunta.setTitle("Confirmación");
		pregunta.setHeaderText(null);
		Optional<ButtonType> respuesta = pregunta.showAndWait();
		if (respuesta.get() == ButtonType.OK) {
			System.exit(0);
		}
	}
    @FXML
    void irAPerfil(ActionEvent event) {
    	Parent vista=cargarVista(FXML_PERFIL);
		borderPane.setCenter(vista);
    }
	
}

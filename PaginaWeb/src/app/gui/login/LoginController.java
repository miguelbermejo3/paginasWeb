package app.gui.login;

import java.util.Optional;

import app.gui.appController.AppController;
import app.gui.modelo.Usuario;
import app.gui.service.PaginaService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;

public class LoginController extends AppController {

	@FXML
	private Button btnEntrar;

	@FXML
	private Button btnRegistro;

	@FXML
	private CheckBox chckTerminos;

	@FXML
	private TextField txtNombre;

	@FXML
	private PasswordField txtPass;
	@FXML
	private Button btnSalir;

	@FXML
	void habilitarUsuario(ActionEvent event) {
		if (!chckTerminos.isSelected()) {
			btnEntrar.setDisable(true);
			btnRegistro.setDisable(true);
		} else {
			btnEntrar.setDisable(false);
			btnRegistro.setDisable(false);
		}
	}

	@FXML
	void irABienvenida(ActionEvent event) {
		PaginaService us = new PaginaService();

		String passCodificada = us.codificarShar256(txtPass.getText());
		Usuario u = us.consultarUsuario(passCodificada);

		if (txtNombre.getText().isBlank()) {
			mostrarError("El usuario o la contraseña no pueden estar vacios");
		}

		else if (u != null) {

			putUserDataObject("usuario", u);

			cambiarVista(FXML_BIENVENIDA);
		} else {
			mostrarError("El usuario o contraseña es incorrecto");
		}

	}

	@FXML
	void irAPantallaRegistro(ActionEvent event) {
		cambiarVista(FXML_REGISTRO);
	}

	@FXML
	public void initialize() {
		btnEntrar.setDisable(true);
		btnRegistro.setDisable(true);
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

}

package app.gui.login;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import app.gui.appController.AppController;
import app.gui.modelo.Usuario;
import app.gui.service.PaginaService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

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
	void habilitarUsuario(ActionEvent event) {

	}

	@FXML
	void irABienvenida(ActionEvent event) {
		PaginaService us = new PaginaService();

		String passCodificada = us.codificarShar256(txtPass.getText());
		Usuario u = us.consultarUsuario(passCodificada);

		if (u != null) {
			
			
			putUserDataObject("usuario",u);
			System.out.println(u);
			cambiarVista(FXML_BIENVENIDA);
		} else {
			mostrarError("El usuario o contraseña es incorrecto");
		}

	}

	@FXML
	void irAPantallaRegistro(ActionEvent event) {
		cambiarVista(FXML_REGISTRO);
	}

}

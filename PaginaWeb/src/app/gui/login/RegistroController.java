package app.gui.login;

import app.gui.appController.AppController;
import app.gui.modelo.Usuario;
import app.gui.service.PaginaService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class RegistroController extends AppController {

	@FXML
	private TextField txtCorreo;

	@FXML
	private TextField txtNombre;

	@FXML
	private PasswordField txtPassword;

	@FXML
	private PasswordField txtPass;

	@FXML
	private Button btnRegistro;

	@FXML
	private Button btnSalir;

	@FXML
	void irAPantallaBienvenida(ActionEvent event) {
		PaginaService service = new PaginaService();
		Usuario u = new Usuario();
		String clave = txtPassword.getText();

		if (service.consultarUsuarioPorCorreo(txtCorreo.getText()) == null) {
			u.setCorreo(txtCorreo.getText());
			u.setContraseña(service.codificarShar256(txtPass.getText()));
			u.setNombre(txtNombre.getText());

			//String idUsuario= 
					service.insertarUsuario(u);
			//u.setIdUsuario(idUsuario);
			//System.out.println(idUsuario);
			
			if(getUserDataObject("usuario")==null) {
				putUserDataObject("usuario",u);
			}
			
			
			
			
			cambiarVista(FXML_LOGIN);
		} else if (clave == null || !clave.equals(txtPass.getText())) {
			mostrarError("Las contraseñas deben ser exactamente iguales");
		}

		else {
			mostrarError("El Correo ya tiene una cuenta asociada");
		}

	}

	@FXML
	void volverALogin(ActionEvent event) {
		cambiarVista(FXML_LOGIN);
	}

}

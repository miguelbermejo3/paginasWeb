package app.gui.bienvenida;

import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import app.gui.appController.AppController;
import app.gui.modelo.PaginaWeb;
import app.gui.modelo.Usuario;
import app.gui.service.PaginaService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

public class anhadirController extends AppController {

	private PaginaService service = null;
	private List<String> usuarios = null;

	@FXML
	private Button btnCategoria;

	@FXML
	private Button btnAnhadir;

	@FXML
	private ComboBox<String> cboxUsuarios;

	@FXML
	private ComboBox<String> cmboxSeccion;

	@FXML
	private TextField txtNombre;

	@FXML
	private TextField txtUrl;
	@FXML
	private TextField txtCategoria;

	@FXML
	void anhadir(ActionEvent event) {
		anhadirPaginaWeb();
		cargarVista(FXML_BIENVENIDA);
	}

	@FXML
	public void initialize() {
		service = new PaginaService();
		usuarios = service.consultarNombreDeUsuarios();
		for (String string : usuarios) {
			cboxUsuarios.getItems().add(string);
		}

		Set<String> categorias = service.obtenerCategorias();
		for (String string : categorias) {

			cmboxSeccion.getItems().add(string);
		}
		

	}

	public void anhadirPaginaWeb() {

		Usuario u = service.consultarUsuarioPorNombre(cboxUsuarios.getValue());
		System.out.println(u);
		PaginaWeb web = new PaginaWeb();
		web.setCategoria(cmboxSeccion.getValue());
		web.setNombrePagina(txtNombre.getText());
		web.setUrl(txtUrl.getText());
		web.setFechaActualizacion(Date.valueOf(LocalDate.now()));

		service.actualizarPaginaUsuario(u, web);

	}

	public void anhadirCat(String nombre) {
		cmboxSeccion.getItems().add(nombre);
		txtCategoria.setText("");
	}

	@FXML
	void anhadirCategoria(ActionEvent event) {
		anhadirCat(txtCategoria.getText());
	}

}

package app.gui.bienvenida;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

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
	private DateTimeFormatter formatter;

	private PaginaService service = null;
	private List<String> usuarios = null;
	private List<PaginaWeb> paginas = null;
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
		paginas = new ArrayList<>();

		cmboxSeccion.getItems().add("prueba");

	}

	public void anhadirPaginaWeb() {

		Usuario u = (Usuario) getUserDataObject("usuario");
		System.out.println(u);
		PaginaWeb web = new PaginaWeb();
		web.setCategoria(cmboxSeccion.getValue());
		web.setNombrePagina(txtNombre.getText());
		web.setUrl(txtUrl.getText());
		//web.setUsuario(u);
		paginas.add(web);

		// u.getPaginas().add(web);

		service.actualizarPaginaUsuario(u, web);

	}

}

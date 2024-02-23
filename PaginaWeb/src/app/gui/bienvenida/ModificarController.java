package app.gui.bienvenida;

import java.util.List;
import java.util.Set;

import app.gui.appController.AppController;
import app.gui.modelo.PaginaWeb;
import app.gui.modelo.Usuario;
import app.gui.service.PaginaService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.effect.BoxBlur;
import javafx.scene.input.MouseEvent;

public class ModificarController extends AppController {

	@FXML
	private ComboBox<String> cboxUsuario;

	@FXML
	private TableColumn<PaginaWeb, String> columNombre;

	@FXML
	private TableColumn<PaginaWeb, String> columnSeccion;

	@FXML
	private TableColumn<PaginaWeb, String> columnUrl;

	@FXML
	private TableView<PaginaWeb> tabla;

	@FXML
	private TextField txtNombre;

	@FXML
	private ComboBox<String> cboxSeccion;

	private ObservableList<PaginaWeb> datos;

	PaginaService service;

	@FXML
	private Button btnEliminar;

	@FXML
	private Button btnModificar;

	private PaginaWeb web;

	private Usuario u;

	private Usuario root;

	private Usuario actual;

	@FXML
	public void initialize() {

		PropertyValueFactory<PaginaWeb, String> factoryValueNombre = new PropertyValueFactory<>("nombrePagina");
		PropertyValueFactory<PaginaWeb, String> factoryValueCategoria = new PropertyValueFactory<>("categoria");
		PropertyValueFactory<PaginaWeb, String> factoryValueLink = new PropertyValueFactory<>("Url");

		columnSeccion.setCellValueFactory(factoryValueCategoria);
		columnUrl.setCellValueFactory(factoryValueLink);
		columNombre.setCellValueFactory(factoryValueNombre);

		datos = FXCollections.observableArrayList();
		tabla.setItems(datos);

		service = new PaginaService();
		u = new Usuario();
		root = new Usuario();
		actual = new Usuario();
		List<String> correosUsuarios = service.consultarCorreoUsuarios();
		for (String string : correosUsuarios) {
			cboxUsuario.getItems().add(string);
		}

		web = new PaginaWeb();

		Set<String> categorias = service.obtenerCategorias();
		for (String string : categorias) {

			cboxSeccion.getItems().add(string);
		}
		txtNombre.setDisable(true);
		cboxSeccion.setDisable(true);

		List<String> nombresUsuarios = service.consultarNombreDeUsuarios();
		for (String string : nombresUsuarios) {
			cboxUsuario.getItems().add(string);
		}
		limpiarTodo();
		

	}

	@FXML
	void consultar(ActionEvent event) {

		String correo = cboxUsuario.getValue();

		consultar(correo);
	}

	public void consultar(String correo) {

		Usuario u = service.consultarUsuarioPorCorreo(correo);

		tabla.setEffect(new BoxBlur());

		Task<Void> task = new Task<Void>() {

			List<PaginaWeb> paginas;

			@Override
			protected Void call() throws Exception {

				paginas = service.consultarWebsPorUsuario(u);

				return null;
			}

			@Override
			protected void succeeded() {
				super.succeeded();
				tabla.setEffect(null);
				datos.setAll(paginas);
				limpiarTodo();
				updateProgress(100, 100);
			}

			@Override
			protected void failed() {
				super.failed();
				tabla.setEffect(null);
				datos.clear();
				mostrarError("No hay registros en la bbdd con ese filtro");
				updateProgress(100, 100);
			}

		};

		new Thread(task).start();
	}

	@FXML
	void eliminar(ActionEvent event) {

		u = service.consultarUsuarioPorCorreo(cboxUsuario.getValue());

		root = service.consultarUsuarioPorCorreo("miguelbermejo1@gmail.com");

		actual = (Usuario) getUserDataObject("usuario");

		if (actual.equals(u) || actual.equals(root)) {
			
			if(mostrarAlerta("¿Seguro que desea borrar la página web seleccionada?")) {
			
			web = tabla.getSelectionModel().getSelectedItem();
			Usuario u = service.consultarUsuarioPorCorreo(cboxUsuario.getValue());
			List<PaginaWeb> paginas = u.getPaginas();
			if (paginas.contains(web)) {
				paginas.remove(web);
			}
			
			service.actualizarUsuario(u);
			datos.setAll(service.consultarWebsPorUsuario(u));
		}
			}
		else {
			mostrarError("Lo siento, debes ser admin. para poder borrar páginas de otra cuenta");
		}

//		service.borrarPaginaWeb(web, u);
//		System.out.println("página eliminada: "+web);

	}

	@FXML
	void modificar(ActionEvent event) {
		web.setCategoria(cboxSeccion.getValue());
		web.setNombrePagina(txtNombre.getText());

		u = service.consultarUsuarioPorCorreo(cboxUsuario.getValue());

		root = service.consultarUsuarioPorCorreo("miguelbermejo1@gmail.com");

		actual = (Usuario) getUserDataObject("usuario");

		if (actual.equals(u) || actual.equals(root)) {

			service.modificarPaginaWeb(u, web);
			datos.setAll(service.consultarWebsPorUsuario(u));

		} else {
			mostrarError("Lo siento, debes ser administrador para poder modificar las demas páginas web");
		}

	}

	@FXML
	void seleccionar(MouseEvent event) {

		web = tabla.getSelectionModel().getSelectedItem();

		if (web != null) {
			txtNombre.setDisable(false);
			cboxSeccion.setDisable(false);
		}
		txtNombre.setText(web.getNombrePagina());

		cboxSeccion.setValue(web.getCategoria());

		if (web == null) {
			mostrarError("Debes seleccionar una Página Web");
		}

	}

	public void limpiarTodo() {

		txtNombre.setText("");
		cboxSeccion.setValue("");
	}

}

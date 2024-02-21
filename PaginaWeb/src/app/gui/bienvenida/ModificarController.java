package app.gui.bienvenida;

import java.util.List;



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
	private TextField txtSección;

	@FXML
	private TextField txtUrl;

	private ObservableList<PaginaWeb> datos;

	PaginaService service;

	@FXML
	private Button btnEliminar;

	@FXML
	private Button btnModificar;
	
	private PaginaWeb web;

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

		List<String> correosUsuarios = service.consultarCorreoUsuarios();
		for (String string : correosUsuarios) {
			cboxUsuario.getItems().add(string);
		}
		
		web=new PaginaWeb();

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
				// lo que tengo que hacer en otro hilo

				paginas = service.consultarWebsPorUsuario(u);

				return null;
			}

			@Override
			protected void succeeded() {
				super.succeeded();
				tabla.setEffect(null);
				datos.setAll(paginas); // cuando va bien
				updateProgress(100, 100);
			}

			@Override
			protected void failed() {
				super.failed();
				tabla.setEffect(null);
				datos.clear(); // cuando falla
				mostrarError("No hay registros en la bbdd con ese filtro");
				updateProgress(100, 100);
			}

		};

		new Thread(task).start();
	}

	@FXML
	void eliminar(ActionEvent event) {

	}

	@FXML
	void modificar(ActionEvent event) {
		web.setCategoria(txtSección.getText());
		web.setNombrePagina(txtNombre.getText());
		web.setUrl(txtUrl.getText());
		
		Usuario u=service.consultarUsuarioPorCorreo(cboxUsuario.getValue());
		
		
		service.modificarPaginaWeb(u,web);
		cargarVista(FXML_CONSULTA);
	}

	@FXML
	void seleccionar(MouseEvent event) {

		 web = tabla.getSelectionModel().getSelectedItem();
		 txtSección.setText(web.getCategoria());
		 txtNombre.setText(web.getNombrePagina());
		 txtUrl.setText(web.getUrl());
		if (web == null) {
			mostrarError("Debes seleccionar una Página Web");
		}
		else {
			
			
		}

	}
	
	
	
	
	

}

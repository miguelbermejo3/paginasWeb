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
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.effect.BoxBlur;
import javafx.scene.input.MouseEvent;

public class ConsultaController extends AppController {

	@FXML
	private TableColumn<PaginaWeb, String> columnCategoria;

	@FXML
	private TableColumn<PaginaWeb, String> columnUrl;

	@FXML
	private TableColumn<PaginaWeb, String> columnNombrePagina;

	@FXML
	private TableView<PaginaWeb> tabla;

	@FXML
	private Button btnBuscar;

	@FXML
	private TextField txtFiltro;

	private ObservableList<PaginaWeb> datos;
	@FXML
	private ProgressBar progressbar;
	@FXML
	private ComboBox<String> cboxUsuario;

	private PaginaService service;

	@FXML
	public void initialize() {

		PropertyValueFactory<PaginaWeb, String> factoryValueNombre = new PropertyValueFactory<>("nombrePagina");
		PropertyValueFactory<PaginaWeb, String> factoryValueCategoria = new PropertyValueFactory<>("categoria");
		PropertyValueFactory<PaginaWeb, String> factoryValueLink = new PropertyValueFactory<>("Url");

		columnCategoria.setCellValueFactory(factoryValueCategoria);
		columnUrl.setCellValueFactory(factoryValueLink);
		columnNombrePagina.setCellValueFactory(factoryValueNombre);

		datos = FXCollections.observableArrayList();
		tabla.setItems(datos);
		service = new PaginaService();

		List<String> nombresUsuarios = service.consultarNombreDeUsuarios();
		for (String string : nombresUsuarios) {
			cboxUsuario.getItems().add(string);
		}

		btnBuscar.setDisable(true);

	}

	@FXML
	void Consultar(ActionEvent event) {
		consultarPorUsuario(cboxUsuario.getValue());

	}

	public void consultarPorUsuario(String nombre) {

		Usuario u = service.consultarUsuarioPorNombre(nombre);

		tabla.setEffect(new BoxBlur());

		Task<Void> task = new Task<Void>() {

			List<PaginaWeb> paginas;

			@Override
			protected Void call() throws Exception {
				paginas = service.consultarWebsPorUsuarioFiltro(u, txtFiltro.getText());

				return null;
			}

			@Override
			protected void succeeded() {
				super.succeeded();
				tabla.setEffect(null);
				btnBuscar.setDisable(false);
				datos.setAll(paginas);
				updateProgress(100, 100);
				txtFiltro.setText("");
			}

			@Override
			protected void failed() {
				super.failed();
				tabla.setEffect(null);
				datos.clear();
				mostrarError("No hay registros en la bbdd con ese filtro, por favor seleccione usuario");
				updateProgress(100, 100);
			}

		};
		progressbar.progressProperty().bind(task.progressProperty());

		new Thread(task).start();
	}

	@FXML
	void abrirUrl(MouseEvent event) {

		if (event.getClickCount() == 2) {
			PaginaWeb paginaSeleccionada = tabla.getSelectionModel().getSelectedItem();
			if (paginaSeleccionada != null) {
				putUserDataObject("paginaWebSeleccionada", paginaSeleccionada);
				cambiarVista(FXML_PAGINA);

				
			}
		}

	}

}

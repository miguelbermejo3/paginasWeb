package app.gui.appController;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import app.gui.App;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class AppController {
	public static final String FXML_LOGIN = "/app/gui/login/login.fxml";
	public static final String FXML_BIENVENIDA = "/app/gui/bienvenida/bienvenida.fxml";
	public static final String FXML_REGISTRO = "/app/gui/login/registro.fxml";
	public static final String FXML_ALTA = "/app/gui/bienvenida/añadir.fxml";
	public static final String FXML_MODIFICAR = "/app/gui/bienvenida/modificar.fxml";
	public static final String FXML_CONSULTA = "/app/gui/bienvenida/consulta.fxml";
	public static final String FXML_PERFIL = "/app/gui/bienvenida/perfil.fxml";
	public static final String FXML_PAGINA = "/app/gui/bienvenida/pagina.fxml";

	private static Stage primaryStage;
	
	@FXML
	private AnchorPane panel;
	public AppController() {
	}

	public AppController(Stage stage) {
		primaryStage = stage;
		stage.setResizable(false);
		stage.initStyle(StageStyle.UNDECORATED);
		stage.setUserData(new HashMap<String, Object>());
	}

	public Object getUserDataObject(String key) {
		@SuppressWarnings("unchecked")
		Map<String, Object> map = (Map<String, Object>) primaryStage.getUserData();
		return map.get(key);
	}

	public void putUserDataObject(String key, Object data) {
		@SuppressWarnings("unchecked")
		Map<String, Object> map = (Map<String, Object>) primaryStage.getUserData();
		map.put(key, data);
	}

	public AppController cambiarVista(String fxml) {
		try {
			// 1. Cargar el FXML que quiero mostrar
			FXMLLoader loader = new FXMLLoader(App.class.getResource(fxml));
			// 2. Creamos la escena
			Scene scene = new Scene(loader.load());
			// 3. Cargar la escena en el escenario
			primaryStage.setScene(scene);
			// 4. Devolvemos el controller
			return loader.getController();
		} catch (IOException e) {
			System.err.println("No se ha podido cambiar de vista");
			System.err.println("Vista destino: " + fxml);
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	public Parent cargarVista(String fxml) {
		try {
			URL url = App.class.getResource(fxml);
			FXMLLoader loader = new FXMLLoader(url);
			return loader.load();
		} catch (IOException e) {
			System.err.println("No se ha podido cambiar de vista");
			System.err.println("Vista destino: " + fxml);
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	public void mostrarError(String mensaje) {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setHeaderText(null);
		alert.setTitle("Error");
		alert.setContentText(mensaje);
		alert.showAndWait();
	}
	public Boolean mostrarAlerta(String mensaje) {
		Alert alert = new Alert(AlertType.CONFIRMATION);
	    alert.setHeaderText(null);
	    alert.setTitle("Confirmar acción");
	    alert.setContentText(mensaje);

	   
	    ButtonType buttonTypeAceptar = new ButtonType("Aceptar");
	    ButtonType buttonTypeCancelar = new ButtonType("Cancelar");
	    alert.getButtonTypes().setAll(buttonTypeAceptar, buttonTypeCancelar);

	    
	    Optional<ButtonType> resultado = alert.showAndWait();
	    
	    
	    return resultado.isPresent() && resultado.get() == buttonTypeAceptar;
	}
	

}

package app.gui.service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.model.Updates;
import com.mongodb.client.result.InsertOneResult;
import com.mongodb.client.result.UpdateResult;

import app.gui.modelo.PaginaWeb;
import app.gui.modelo.Usuario;
import app.gui.mongo.MongoSession;

public class PaginaService {

	public String insertarUsuario(Usuario usuario) {
		MongoDatabase db = MongoSession.getDatabase();
		MongoCollection<Usuario> c = db.getCollection("Usuario", Usuario.class);
		InsertOneResult result = c.insertOne(usuario);
		return result.getInsertedId().toString();
	}

	public Usuario consultarUsuario(String pass) {
		MongoDatabase db = MongoSession.getDatabase();
		MongoCollection<Usuario> c = db.getCollection("Usuario", Usuario.class);
		Bson filter = Filters.eq("contraseña", pass);
		FindIterable<Usuario> result = c.find(filter);
		return result.first();
	}

	public Usuario consultarUsuarioPorCorreo(String correo) {
		MongoDatabase db = MongoSession.getDatabase();
		MongoCollection<Usuario> c = db.getCollection("Usuario", Usuario.class);
		Bson filter = Filters.eq("correo", correo);
		FindIterable<Usuario> result = c.find(filter);
		return result.first();
	}

	public String codificarShar256(String pass) {
		StringBuilder sb = null;

		try {
			// Obtener una instancia de MessageDigest para SHA-256
			MessageDigest md = MessageDigest.getInstance("SHA-256");

			// Calcular el hash SHA-256 de la cadena
			byte[] hashBytes = md.digest(pass.getBytes());

			// Convertir los bytes del hash a una cadena en formato hexadecimal
			sb = new StringBuilder();
			for (byte b : hashBytes) {
				sb.append(String.format("%02x", b));
			}

			// Mostrar la cadena cifrada
			System.out.println("Resultado del hash SHA-256: " + sb.toString());

		} catch (NoSuchAlgorithmException e) {
			System.err.println("El algoritmo SHA-256 no está disponible.");
		}

		return sb.toString();

	}

	public List<String> consultarNombreDeUsuarios() {
		List<String> nombres = new ArrayList<>();
		MongoDatabase db = MongoSession.getDatabase();
		MongoCollection<Usuario> c = db.getCollection("Usuario", Usuario.class);
		FindIterable<Usuario> result = c.find();
		try (MongoCursor<Usuario> cursor = result.iterator()) {
			while (cursor.hasNext()) {
				Usuario usuario = cursor.next();
				nombres.add(usuario.getNombre());
			}
		}
		return nombres;
	}

	public Usuario consultarUsuarioPorNombre(String nombre) {
		MongoDatabase db = MongoSession.getDatabase();
		MongoCollection<Usuario> c = db.getCollection("Usuario", Usuario.class);
		Bson filter = Filters.eq("nombre", nombre);
		FindIterable<Usuario> result = c.find(filter);
		return result.first();
	}

	public List<PaginaWeb> consultarWebsPorUsuario(Usuario usuario) {

		List<PaginaWeb> paginas = usuario.getPaginas();
		MongoDatabase db = MongoSession.getDatabase();
		MongoCollection<PaginaWeb> c = db.getCollection("PagWeb", PaginaWeb.class);
		FindIterable<PaginaWeb> result = c.find();
		try (MongoCursor<PaginaWeb> cursor = result.iterator()) {
			while (cursor.hasNext()) {
				for (PaginaWeb paginaWeb : result) {
					paginas.add(paginaWeb);
				}
			}
		}
		return paginas;
	}

	public void actualizarUsuario(Usuario usuario, PaginaWeb web) {

		// Obtener la colección de usuarios de la base de datos
		MongoDatabase db = MongoSession.getDatabase();
		MongoCollection<Usuario> c = db.getCollection("Usuario", Usuario.class);

		// Crear un filtro para encontrar el usuario por su correo
		Bson filtro = Filters.eq("correo", usuario.getCorreo());

		usuario.getPaginas().add(web);

		// Realizar la actualización
		UpdateResult result = c.replaceOne(filtro, usuario);

		// Manejar el resultado si es necesario
		if (result.getModifiedCount() != 1) {
			// La actualización no se realizó correctamente
			System.out.println("Error al actualizar el usuario.");
		} else {
			// La actualización se realizó con éxito
			System.out.println("Usuario actualizado con éxito.");
		}
	}

//	public void insertarPaginaAUsuario(Usuario u,PaginaWeb pagina) {
//		MongoDatabase db = MongoSession.getDatabase();
//		MongoCollection<Usuario> c = db.getCollection("Usuario", Usuario.class);
//		
//		
//		
//		
//		
//		
//		
//		
//		
//		InsertOneResult result = c.insertOne(pagina);
//		 result.getInsertedId().toString();
//	}

	// Actualizo la lista del usuario con el correo indicado, añadiéndole una nueva
	public Long actualizarPaginaUsuario(Usuario u, PaginaWeb pagina) {
		MongoDatabase db = MongoSession.getDatabase();
		MongoCollection<Usuario> c = db.getCollection("Usuario", Usuario.class);
		String correo = u.getCorreo();
		Bson filter = Filters.regex("correo", correo);
		Bson updates = Updates.addToSet("paginas", pagina);
		UpdateResult result = c.updateMany(filter, updates);

		return result.getModifiedCount();
	}

	public List<String> consultarCorreoUsuarios() {
		List<String> correos = new ArrayList<>();
		MongoDatabase db = MongoSession.getDatabase();
		MongoCollection<Usuario> c = db.getCollection("Usuario", Usuario.class);
		FindIterable<Usuario> result = c.find();
		try (MongoCursor<Usuario> cursor = result.iterator()) {
			while (cursor.hasNext()) {
				Usuario usuario = cursor.next();
				correos.add(usuario.getCorreo());
			}
		}
		return correos;
	}

	public void modificarPaginaWeb(Usuario u, PaginaWeb pagina) {

		MongoDatabase db = MongoSession.getDatabase();
		MongoCollection<Usuario> c = db.getCollection("Usuario", Usuario.class);
		String correo = u.getCorreo();
		Bson filter = Filters.eq("correo", correo);

		List<PaginaWeb> paginas = u.getPaginas();
		for (PaginaWeb paginaWeb : paginas) {
			if (pagina.getUrl().equals(paginaWeb.getUrl())) {
				paginaWeb.setNombrePagina(pagina.getNombrePagina()); // Actualizamos el nombre de la página
				paginaWeb.setCategoria(pagina.getCategoria()); // Actualizamos la categoría de la página
				
				UpdateResult result = c.updateOne(filter, Updates.set("paginas", paginas)); // Reemplazamos el documento completo del usuario en
																// MongoDB
				System.out.println("Documentos modificados: " + result.getModifiedCount());
				return;
			}
		}
		System.out.println("La página web no fue encontrada para ser modificada.");

	}

//	public List<PaginaWeb> consultarWebsPorUsuarioFiltro(Usuario usuario, String nombre) {
//
//		List<PaginaWeb> paginasUsuario = usuario.getPaginas();
//		List<PaginaWeb> paginasFiltradas = new ArrayList<>();
//		MongoDatabase db = MongoSession.getDatabase();
//		MongoCollection<PaginaWeb> c = db.getCollection("PagWeb", PaginaWeb.class);
//		FindIterable<PaginaWeb> result = c.find();
//		try (MongoCursor<PaginaWeb> cursor = result.iterator()) {
//			while (cursor.hasNext()) {
//				for (PaginaWeb paginaWeb : result) {
//					if (nombre == null || nombre.isEmpty()) {
//						paginasUsuario.add(paginaWeb);
//						return paginasUsuario;
//
//					}
//					for (PaginaWeb paginaWebs : paginasUsuario) {
//						if (paginaWebs.getNombrePagina().equalsIgnoreCase(nombre)) {
//							paginasFiltradas.add(paginaWebs);
//						}
//					}
//				}
//			}
//		}
//		return paginasFiltradas;
//	}

	public List<Usuario> consultarTodosUsuarios() {

		List<Usuario> usuarios = new ArrayList<Usuario>();
		MongoDatabase db = MongoSession.getDatabase();
		MongoCollection<Usuario> c = db.getCollection("Usuario", Usuario.class);
		FindIterable<Usuario> result = c.find();
		MongoCursor<Usuario> cursor = result.cursor();
		while (cursor.hasNext()) {
			usuarios.add(cursor.next());
		}
		return usuarios;

	}

	public List<PaginaWeb> consultarTodasPaginas() {
		List<PaginaWeb> paginasWeb = new ArrayList<>();
		List<Usuario> usuarios = consultarTodosUsuarios();

		for (Usuario usuario : usuarios) {
			List<PaginaWeb> pag = usuario.getPaginas();
			for (PaginaWeb paginaUsuario : pag) {
				paginasWeb.add(paginaUsuario);
			}
		}
		return paginasWeb;

	}

	public List<PaginaWeb> consultarWebsPorUsuarioFiltro(Usuario usuario, String nombre) {
		List<PaginaWeb> paginasFiltradas = new ArrayList<>();
		List<PaginaWeb> paginasUsuario = usuario.getPaginas();

		// Si no hay filtro de nombre, retornar todas las páginas del usuario
		if (nombre == null || nombre.isEmpty()) {
			return paginasUsuario;
		}

		// Iterar sobre las páginas del usuario para encontrar aquellas que coincidan
		// con el nombre
		for (PaginaWeb paginaWeb : paginasUsuario) {
			if (paginaWeb.getNombrePagina().equals(nombre)) {
				paginasFiltradas.add(paginaWeb);
			}

		}

		return paginasFiltradas;
	}

	

	public Set<String> obtenerCategorias() {
		List<Usuario> usuarios = consultarTodosUsuarios();

		// Conjunto para almacenar los nombres de las páginas web únicos
		Set<String> categoriasPaginas = new HashSet<>();

		// Iterar sobre todos los usuarios y sus páginas web
		for (Usuario usuario : usuarios) {
			for (PaginaWeb pagina : usuario.getPaginas()) {
				categoriasPaginas.add(pagina.getCategoria().toString());
			}
		}

		return categoriasPaginas;

	}
	
	public void insertarCategoriaNueva(String categoria) {
		
		MongoDatabase db = MongoSession.getDatabase();
		MongoCollection<PaginaWeb> c = db.getCollection("pagWeb", PaginaWeb.class);
		
		Bson filter = Filters.regex("nombrePagina", "general");
		Bson updates = Updates.addToSet("categorias", categoria);
		UpdateResult result = c.updateMany(filter, updates);

		 result.getModifiedCount();
	
		
		
	}
	
	public String insertarPagWeb(PaginaWeb pagina) {
		MongoDatabase db = MongoSession.getDatabase();
		MongoCollection<PaginaWeb> c = db.getCollection("pagWeb", PaginaWeb.class);
		InsertOneResult result = c.insertOne(pagina);
		return result.getInsertedId().toString();
	}
	
	

}

package app.gui.service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.bson.conversions.Bson;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;

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

			MessageDigest md = MessageDigest.getInstance("SHA-256");

			byte[] hashBytes = md.digest(pass.getBytes());

			sb = new StringBuilder();
			for (byte b : hashBytes) {
				sb.append(String.format("%02x", b));
			}

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

		MongoDatabase db = MongoSession.getDatabase();
		MongoCollection<Usuario> c = db.getCollection("Usuario", Usuario.class);

		Bson filtro = Filters.eq("correo", usuario.getCorreo());

		usuario.getPaginas().add(web);

		UpdateResult result = c.replaceOne(filtro, usuario);

		if (result.getModifiedCount() != 1) {

			System.out.println("Error al actualizar el usuario.");
		} else {

			System.out.println("Usuario actualizado con éxito.");
		}
	}

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
				paginaWeb.setNombrePagina(pagina.getNombrePagina());
				paginaWeb.setCategoria(pagina.getCategoria());
				paginaWeb.setFechaActualizacion(LocalDate.now());

				UpdateResult result = c.updateOne(filter, Updates.set("paginas", paginas));
				System.out.println("Documentos modificados: " + result.getModifiedCount());
				return;
			}
		}
		System.out.println("La página web no fue encontrada para ser modificada.");

	}

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

		if (nombre == null || nombre.isEmpty()) {
			return paginasUsuario;
		}

		for (PaginaWeb paginaWeb : paginasUsuario) {
			if (paginaWeb.getNombrePagina().equals(nombre)) {
				paginasFiltradas.add(paginaWeb);
			}

		}

		return paginasFiltradas;
	}

	public Set<String> obtenerCategorias() {
		List<Usuario> usuarios = consultarTodosUsuarios();

		Set<String> categoriasPaginas = new HashSet<>();

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

	public void actualizarUsuario(Usuario u) {
		MongoDatabase db = MongoSession.getDatabase();
		MongoCollection<Usuario> c = db.getCollection("Usuario", Usuario.class);

		// Actualizar el usuario en la base de datos
		c.replaceOne(Filters.eq("correo", u.getCorreo()), u);
	}

}

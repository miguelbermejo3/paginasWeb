package app.gui.modelo;

import java.util.ArrayList;
import java.util.List;

public class Usuario {

private String nombre;
private String contraseña;
private String correo;
private String idUsuario;
private String descripción;

private List<PaginaWeb>paginas;

public Usuario() {
	paginas= new ArrayList<PaginaWeb>();
}

public String getNombre() {
	return nombre;
}
public void setNombre(String nombre) {
	this.nombre = nombre;
}
public String getContraseña() {
	return contraseña;
}
public void setContraseña(String contraseña) {
	this.contraseña = contraseña;
}
public String getCorreo() {
	return correo;
}
public void setCorreo(String correo) {
	this.correo = correo;
}
public String getIdUsuario() {
	return idUsuario;
}
public void setIdUsuario(String idUsuario) {
	this.idUsuario = idUsuario;
}
public String getDescripción() {
	return descripción;
}
public void setDescripción(String descripción) {
	this.descripción = descripción;
}

@Override
public String toString() {
	return "Usuario [nombre=" + nombre + ", contraseña=" + contraseña + ", correo=" + correo + ", idUsuario="
			+ idUsuario + ", descripción=" + descripción + ", paginas=" + paginas + "]";
}

public List<PaginaWeb> getPaginas() {
	return paginas;
}

public void setPaginas(List<PaginaWeb> paginas) {
	this.paginas = paginas;
}




}

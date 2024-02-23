package app.gui.modelo;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Usuario {

private String nombre;
private String contraseña;
private String correo;
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

public String getDescripción() {
	return descripción;
}
public void setDescripción(String descripción) {
	this.descripción = descripción;
}

@Override
public String toString() {
	return "Usuario [nombre=" + nombre + ", contraseña=" + contraseña + ", correo=" + correo + ", idUsuario="+
			", descripción=" + descripción + ", paginas=" + paginas + "]";
}

public List<PaginaWeb> getPaginas() {
	return paginas;
}

public void setPaginas(List<PaginaWeb> paginas) {
	this.paginas = paginas;
}

@Override
public int hashCode() {
	return Objects.hash(contraseña, correo, descripción, nombre, paginas);
}

@Override
public boolean equals(Object obj) {
	if (this == obj)
		return true;
	if (obj == null)
		return false;
	if (getClass() != obj.getClass())
		return false;
	Usuario other = (Usuario) obj;
	return  Objects.equals(correo, other.correo);
}




}

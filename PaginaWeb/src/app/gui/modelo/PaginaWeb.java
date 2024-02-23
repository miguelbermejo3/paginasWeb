package app.gui.modelo;

import java.util.List;
import java.util.Objects;

public class PaginaWeb {

	private String url;
	private String categoria;
	private String nombrePagina;
	private List<String>categorias;

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getCategoria() {
		return categoria;
	}

	public void setCategoria(String categoria) {
		this.categoria = categoria;
	}

	public String getNombrePagina() {
		return nombrePagina;
	}

	public void setNombrePagina(String nombrePagina) {
		this.nombrePagina = nombrePagina;
	}

	

	@Override
	public String toString() {
		return "PaginaWeb [url=" + url + ", categoria=" + categoria + ", nombrePagina=" + nombrePagina + ", categorias="
				+ categorias + "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(categoria, nombrePagina, url);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PaginaWeb other = (PaginaWeb) obj;
		return Objects.equals(categoria, other.categoria) && Objects.equals(nombrePagina, other.nombrePagina)
				&& Objects.equals(url, other.url);
	}

	public List<String> getCategorias() {
		return categorias;
	}

	public void setCategorias(List<String> categorias) {
		this.categorias = categorias;
	}
	

}

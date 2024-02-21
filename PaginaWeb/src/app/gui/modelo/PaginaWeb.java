package app.gui.modelo;

public class PaginaWeb {

	private String url;
	private String categoria;
	private String nombrePagina;

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
		return "PaginaWeb [url=" + url + ", categoria=" + categoria + ", nombrePagina=" + nombrePagina + "]";
	}

}

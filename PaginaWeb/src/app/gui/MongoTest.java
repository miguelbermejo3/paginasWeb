package app.gui;


import app.gui.modelo.PaginaWeb;

import app.gui.service.PaginaService;

public class MongoTest {

	public static void main(String[] args) {
		PaginaService service= new PaginaService();
		System.out.println(service.consultarUsuario("miguel"));
	
		
		
	
	}

}

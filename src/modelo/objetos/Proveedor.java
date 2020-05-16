package modelo.objetos;

public class Proveedor {
	
	private String codigo,nombre,domicilio,telefono,email,listaprecios;
	
	public Proveedor(String codigo, String nombre, String domicilio, String telefono, String email) {
		this.codigo = codigo;
		this.nombre = nombre;
		this.domicilio = domicilio;
		this.telefono = telefono;
		this.email = email;
	}
	
	public String getCodigo() {
		return codigo;
	}
	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getDomicilio() {
		return domicilio;
	}
	public void setDomicilio(String domicilio) {
		this.domicilio = domicilio;
	}
	public String getTelefono() {
		return telefono;
	}
	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getListaprecios() {
		return listaprecios;
	}
	public void setListaprecios(String listaprecios) {
		this.listaprecios = listaprecios;
	}	
}

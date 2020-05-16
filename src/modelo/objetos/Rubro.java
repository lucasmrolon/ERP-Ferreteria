package modelo.objetos;

public class Rubro {

	private int codigoRubro;
	private String nombre,subRubro;
	
	public Rubro(int codigoRubro, String nombre, String subRubro) {
		this.codigoRubro = codigoRubro;
		this.nombre = nombre;
		this.subRubro = subRubro;
	}
	
	public int getCodigoRubro() {
		return codigoRubro;
	}
	public void setCodigoRubro(int codigoRubro) {
		this.codigoRubro = codigoRubro;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getSubRubro() {
		return subRubro;
	}
	public void setSubRubro(String subRubro) {
		this.subRubro = subRubro;
	}
}

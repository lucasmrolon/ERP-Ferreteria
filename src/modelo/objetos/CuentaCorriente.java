package modelo.objetos;

public class CuentaCorriente {

	private int codigo_cuenta;
	private double estado;
	private String dni;
	
	public CuentaCorriente(double estado, String dni) {
		this.estado = estado;
		this.dni = dni;
	}
	
	public int getCodigo_cuenta() {
		return codigo_cuenta;
	}
	public void setCodigo_cuenta(int codigo_cuenta) {
		this.codigo_cuenta = codigo_cuenta;
	}
	public double getEstado() {
		return estado;
	}
	public void setEstado(double estado) {
		this.estado = estado;
	}
	public String getDni() {
		return dni;
	}
	public void setDni(String dni) {
		this.dni = dni;
	}
}

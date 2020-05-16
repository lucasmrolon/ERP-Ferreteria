package modelo.objetos;

public class FormaDePago {

	private int codigo;
	private String tipo;
	private double recargo,descuento;
	
	public FormaDePago(int codigo, String tipo, double recargo, double descuento) {
		super();
		this.codigo = codigo;
		this.tipo = tipo;
		this.recargo = recargo;
		this.descuento = descuento;
	}
	
	public String toString(){
		return tipo;
	}
	
	public int getCodigo() {
		return codigo;
	}
	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}
	public String getTipo() {
		return tipo;
	}
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	public double getRecargo() {
		return recargo;
	}
	public void setRecargo(double recargo) {
		this.recargo = recargo;
	}
	public double getDescuento() {
		return descuento;
	}
	public void setDescuento(double descuento) {
		this.descuento = descuento;
	}	
}

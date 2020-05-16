package modelo.objetos;

public class LineaVenta {

	private int codigo;
	private double cantidad,precio_u,subtotal;
	private boolean reparte;
	private int codProducto,codVenta;
	
	public LineaVenta(int codVenta, int codProducto, double precio_u, double cantidad,  double subtotal, boolean reparte) {
		this.cantidad = cantidad;
		this.subtotal = subtotal;
		this.precio_u = precio_u;
		this.reparte = reparte;
		this.codProducto = codProducto;
		this.codVenta = codVenta;
	}
	
	public String toString(){
		String reparto;
		if(reparte) reparto="SI";
		else reparto="NO";
		return "(" + codigo + ") || " + String.format("%05d", codProducto) +  " || " + cantidad + " || " + subtotal + " || Reparte: " + reparto;
	}

	public double getPrecio_u() {
		return precio_u;
	}

	public void setPrecio_u(double precio_u) {
		this.precio_u = precio_u;
	}

	public int getCodigo() {
		return codigo;
	}

	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}

	public double getCantidad() {
		return cantidad;
	}

	public void setCantidad(double cantidad) {
		this.cantidad = cantidad;
	}

	public double getSubtotal() {
		return subtotal;
	}

	public void setSubtotal(double subtotal) {
		this.subtotal = subtotal;
	}

	public boolean isReparte() {
		return reparte;
	}

	public void setReparte(boolean reparte) {
		this.reparte = reparte;
	}

	public int getCodProducto() {
		return codProducto;
	}

	public void setCodProducto(int codProducto) {
		this.codProducto = codProducto;
	}

	public int getCodVenta() {
		return codVenta;
	}

	public void setCodVenta(int codVenta) {
		this.codVenta = codVenta;
	}
}

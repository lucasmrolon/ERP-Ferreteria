package modelo.objetos;

public class LineaPedido {
	
	int codigo_pedido,codigo_linea,codigo_producto;
	double precio_u,cantidad,subtotal;
	
	public LineaPedido(int codigo_pedido, int codigo_linea,int codigo_producto, double precio_u, double cantidad, double subtotal) {
		this.codigo_pedido = codigo_pedido;
		this.codigo_linea = codigo_linea;
		this.codigo_producto = codigo_producto;
		this.precio_u = precio_u;
		this.cantidad = cantidad;
		this.subtotal = subtotal;
	}

	public int getCodigo_pedido() {
		return codigo_pedido;
	}

	public void setCodigo_pedido(int codigo_pedido) {
		this.codigo_pedido = codigo_pedido;
	}

	public int getCodigo_linea() {
		return codigo_linea;
	}

	public void setCodigo_linea(int codigo_linea) {
		this.codigo_linea = codigo_linea;
	}

	public int getCodigo_producto() {
		return codigo_producto;
	}

	public void setCodigo_producto(int codigo_producto) {
		this.codigo_producto = codigo_producto;
	}

	public double getPrecio_u() {
		return precio_u;
	}

	public void setPrecio_u(double precio_u) {
		this.precio_u = precio_u;
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

	
	
}

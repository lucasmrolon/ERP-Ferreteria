package modelo.objetos;

public class Producto {

	private int codigo,codrubro;
	private String descripcion,unidad,estado,comentarios,codproveedor;
	private double precio,cantidad,cant_min;
	
	public Producto(int codigo, String codproveedor, int codrubro, String descripcion, String unidad, String estado,
			String comentarios, double precio, double cantidad, double cant_min) {
		super();
		this.codigo = codigo;
		this.codproveedor = codproveedor;
		this.codrubro = codrubro;
		this.descripcion = descripcion;
		this.unidad = unidad;
		this.estado = estado;
		this.comentarios = comentarios;
		this.precio = precio;
		this.cantidad = cantidad;
		this.cant_min = cant_min;
	}
	
	public int getCodigo() {
		return codigo;
	}
	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}
	public String getCodproveedor() {
		return codproveedor;
	}
	public void setCodproveedor(String codproveedor) {
		this.codproveedor = codproveedor;
	}
	public int getCodrubro() {
		return codrubro;
	}
	public void setCodrubro(int codrubro) {
		this.codrubro = codrubro;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public String getUnidad() {
		return unidad;
	}
	public void setUnidad(String unidad) {
		this.unidad = unidad;
	}
	public String getEstado() {
		return estado;
	}
	public void setEstado(String estado) {
		this.estado = estado;
	}
	public String getComentarios() {
		return comentarios;
	}
	public void setComentarios(String comentarios) {
		this.comentarios = comentarios;
	}
	public double getPrecio() {
		return precio;
	}
	public void setPrecio(double precio) {
		this.precio = precio;
	}
	public double getCantidad() {
		return cantidad;
	}
	public void setCantidad(double cantidad) {
		this.cantidad = cantidad;
	}
	public double getCant_min() {
		return cant_min;
	}
	public void setCant_min(double cant_min) {
		this.cant_min = cant_min;
	}
}
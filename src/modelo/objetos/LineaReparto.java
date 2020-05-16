package modelo.objetos;

public class LineaReparto {
	
	private int codigo_linea;
	private String nombre,direccion,turno,observaciones;
	private int cod_linea_venta, cod_reparto;
	private boolean pendiente;

	public LineaReparto(String nombre, String direccion, String turno, String observaciones,int cod_linea_venta,boolean reparte) {
		this.nombre = nombre;
		this.direccion = direccion;
		this.turno = turno;
		this.observaciones = observaciones;
		this.cod_linea_venta = cod_linea_venta;
		pendiente = reparte;
	}

	public int getCodigo_linea() {
		return codigo_linea;
	}

	public void setCodigo_linea(int codigo_linea) {
		this.codigo_linea = codigo_linea;
	}
	
	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getDireccion() {
		return direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public String getTurno() {
		return turno;
	}

	public void setTurno(String turno) {
		this.turno = turno;
	}

	public String getObservaciones() {
		return observaciones;
	}

	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}

	public int getCod_linea_venta() {
		return cod_linea_venta;
	}

	public void setCod_linea_venta(int cod_linea_venta) {
		this.cod_linea_venta = cod_linea_venta;
	}

	public int getCod_reparto() {
		return cod_reparto;
	}

	public void setCod_reparto(int cod_reparto) {
		this.cod_reparto = cod_reparto;
	}

	public boolean isPendiente() {
		return pendiente;
	}

	public void setPendiente(boolean pendiente) {
		this.pendiente = pendiente;
	}
}

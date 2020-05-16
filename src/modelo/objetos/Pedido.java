package modelo.objetos;

import java.util.Date;

public class Pedido {
	
	int codigo_pedido;
	Date emision,recepcion;
	String cod_proveedor;
	int id_empleado;
	double monto;
	
	public Pedido(int codigo_pedido, Date emision, String cod_proveedor) {
		this.codigo_pedido = codigo_pedido;
		this.emision = emision;
		this.cod_proveedor = cod_proveedor;
	}
	
	public double getMonto() {
		return monto;
	}

	public void setMonto(double monto) {
		this.monto = monto;
	}

	public int getCodigo_pedido() {
		return codigo_pedido;
	}

	public void setCodigo_pedido(int codigo_pedido) {
		this.codigo_pedido = codigo_pedido;
	}

	public Date getEmision() {
		return emision;
	}

	public void setEmision(Date emision) {
		this.emision = emision;
	}

	public Date getRecepcion() {
		return recepcion;
	}

	public void setRecepcion(Date recepcion) {
		this.recepcion = recepcion;
	}

	public String getCod_proveedor() {
		return cod_proveedor;
	}

	public void setCod_proveedor(String cod_proveedor) {
		this.cod_proveedor = cod_proveedor;
	}

	public int getId_empleado() {
		return id_empleado;
	}

	public void setId_empleado(int id_empleado) {
		this.id_empleado = id_empleado;
	}

	
}

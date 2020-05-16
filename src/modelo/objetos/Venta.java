package modelo.objetos;

import java.util.Date;

public class Venta {
	
	private int codigoventa;
	private Date fecha;
	private double monto_total,recdesc;
	private int id_empleado;
	private int cta_cte;
	private int forma_pago;
	
	public Venta(Date fecha, double monto_total, double recdesc, int id_empleado, int cta_cte, int forma_pago) {
		this.fecha = fecha;
		this.monto_total = monto_total;
		this.recdesc = recdesc;
		this.id_empleado = id_empleado;
		this.cta_cte = cta_cte;
		this.forma_pago = forma_pago;
	}
	
	public int getCodigoventa() {
		return codigoventa;
	}
	public double getRecdesc() {
		return recdesc;
	}
	public void setRecdesc(double recdesc) {
		this.recdesc = recdesc;
	}
	public void setCodigoventa(int codigoventa) {
		this.codigoventa = codigoventa;
	}
	public Date getFecha() {
		return fecha;
	}
	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}
	public Double getMonto_total() {
		return monto_total;
	}
	public void setMonto_total(Double monto_total) {
		this.monto_total = monto_total;
	}
	public int getId_empleado() {
		return id_empleado;
	}
	public void setId_empleado(int id_empleado) {
		this.id_empleado = id_empleado;
	}
	public int getCta_cte() {
		return cta_cte;
	}
	public void setCta_cte(int cta_cte) {
		this.cta_cte = cta_cte;
	}
	public int getForma_pago() {
		return forma_pago;
	}
	public void setForma_pago(int forma_pago) {
		this.forma_pago = forma_pago;
	}
}

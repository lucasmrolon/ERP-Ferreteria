package modelo.objetos;

import java.util.Date;

public class Reparto {

	int codigo,cod_empleado;
	Date creado,confirmado;

	public Reparto(int codigo,Date creado){
		this.codigo=codigo;
		this.creado=creado;
	}

	public Reparto(int codigo,Date creado,int id_empleado, Date confirmado){
		this.codigo=codigo;
		this.creado=creado;
		this.cod_empleado=id_empleado;
		this.confirmado=confirmado;
	}

	public Date getCreado() {
		return creado;
	}

	public void setCreado(Date creado) {
		this.creado = creado;
	}

	public Date getConfirmado() {
		return confirmado;
	}

	public void setConfirmado(Date confirmado) {
		this.confirmado = confirmado;
	}

	public int getCodigo() {
		return codigo;
	}

	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}

	public int getCod_empleado() {
		return cod_empleado;
	}

	public void setCod_empleado(int cod_empleado) {
		this.cod_empleado = cod_empleado;
	}	
}

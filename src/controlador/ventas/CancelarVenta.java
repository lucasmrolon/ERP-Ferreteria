package controlador.ventas;

import java.util.Date;

import modelo.consultas.EjecutaConsultasVenta;
import vista.ventas.PanelFacturacion;

//CLASE QUE PERMITE CANCELAR UNA VENTA ANTES DE FACTURAR
public class CancelarVenta{
	
	public CancelarVenta(int cod_venta,PanelFacturacion panel,int cod_empleado){
		
		EjecutaConsultasVenta paraCancelar = new EjecutaConsultasVenta();
		
		Date fecha = new Date();
		
		boolean ok = paraCancelar.registrar_venta_cancelada(cod_empleado, fecha, cod_venta);
		
		if(ok){
			panel.actualizarModelo();
		}
		
		
	}

}

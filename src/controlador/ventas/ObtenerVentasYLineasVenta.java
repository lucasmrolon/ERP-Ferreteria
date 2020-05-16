package controlador.ventas;

import java.util.ArrayList;
import java.util.Date;

import modelo.consultas.EjecutaConsultasVenta;
import modelo.objetos.LineaVenta;
import modelo.objetos.Venta;

//CLASE QUE PERMITE OBTENER VENTAS Y LINEAS DE VENTA ASOCIADAS
public class ObtenerVentasYLineasVenta{

	public ArrayList<Venta> obtenerVentas(Date despues_de, Date antes_de){
		
		EjecutaConsultasVenta consulta = new EjecutaConsultasVenta();
		ArrayList<Venta> ventas = consulta.obtenerVentas(despues_de,antes_de);
		
		return ventas;
	}
	
	public ArrayList<LineaVenta> obtenerLineas(int codigo_venta){
		
		EjecutaConsultasVenta consulta = new EjecutaConsultasVenta();
		ArrayList<LineaVenta> lineas = consulta.obtenerLineasDeVenta(codigo_venta);
		
		return lineas;
	}
}

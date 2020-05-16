package controlador.stock;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import modelo.consultas.EjecutaConsultas;
import vista.stock.VentanaActualizarStock;

//CLASE QUE PERMITE ACTUALIZAR EL STOCK DE UN PRODUCTO
public class ActualizarStock implements ActionListener {

	public ActualizarStock(VentanaActualizarStock ventana, JTable tablaProductos, int codigo,JSpinner paraCantidad,double cant_actual){
		this.codigo=codigo;
		this.paraCantidad=paraCantidad;
		this.ventana = ventana;
		tabla = tablaProductos;
		this.cant_actual = cant_actual;
	}
	@Override
	public void actionPerformed(ActionEvent arg0) {
		
		double cantidad_a_sumar = (double)paraCantidad.getValue();
		
		EjecutaConsultas nueva_consulta = new EjecutaConsultas();
		
		nueva_consulta.actualizarStockProducto(codigo, cantidad_a_sumar);
		
		
		
		tabla.setValueAt(cant_actual + cantidad_a_sumar, tabla.getSelectedRow(), 4);
		
		ventana.dispose();
	}

	double cant_actual;
	JTable tabla;
	int codigo;
	JSpinner paraCantidad;
	VentanaActualizarStock ventana;
}

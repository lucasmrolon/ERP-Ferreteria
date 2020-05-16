package controlador.compras;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import modelo.consultas.EjecutaConsultas;
import vista.compras.EditarRecibido;

//CLASE QUE PERMITE REGISTRAR EL RETRASO DE UN PRODUCTO NO RECIBIDO 
public class RegistrarProductoRetrasado implements ActionListener{

	public RegistrarProductoRetrasado(EditarRecibido ventana_padre,JTable tabla,int fila_seleccionada,JTable productoRecibido){
		
		this.ventana_padre=ventana_padre;
		cod = Integer.parseInt((String)productoRecibido.getValueAt(0, 1));
		precio = (Double)productoRecibido.getValueAt(0, 3);
		cantidad = (Double)productoRecibido.getValueAt(0, 4);
		subtotal = (Double)productoRecibido.getValueAt(0, 5);
		tablaPendientes = tabla;
		seleccionado = fila_seleccionada;
		
	}
	@Override
	public void actionPerformed(ActionEvent arg0) {
		
		EjecutaConsultas paraRegistrarRecepcion = new EjecutaConsultas();
		paraRegistrarRecepcion.registrarRecepcionProducto(cod,precio,cantidad,subtotal);
		DefaultTableModel modelo =(DefaultTableModel)tablaPendientes.getModel();
		modelo.removeRow(seleccionado);
		ventana_padre.dispose();
		
	}

	EditarRecibido ventana_padre;
	JTable tablaPendientes;
	int cod,seleccionado;
	double precio,cantidad,subtotal;
}
